# Guia de Implementação do Kafka no Sistema de Pedidos

Este documento descreve como implementar e utilizar o Apache Kafka no Sistema de Pedidos, fornecendo uma fonte de estudo complementar e facilitando o uso do Kafka em ambiente de desenvolvimento.

## Índice

1. [Conceitos Básicos do Kafka](#conceitos-básicos-do-kafka)
2. [Kafka na Arquitetura Hexagonal](#kafka-na-arquitetura-hexagonal)
3. [Implementação no Projeto](#implementação-no-projeto)
4. [Configuração do Ambiente de Desenvolvimento](#configuração-do-ambiente-de-desenvolvimento)
5. [Exemplos de Uso](#exemplos-de-uso)
6. [Boas Práticas](#boas-práticas)
7. [Troubleshooting](#troubleshooting)

## Conceitos Básicos do Kafka

### O que é o Apache Kafka?

O Apache Kafka é uma plataforma distribuída de streaming de eventos que permite publicar, subscrever, armazenar e processar fluxos de eventos em tempo real. Foi originalmente desenvolvido pelo LinkedIn e posteriormente doado para a Apache Software Foundation.

### Componentes Principais

- **Broker**: Servidor Kafka que armazena os dados e serve os produtores e consumidores.
- **Topic**: Categoria ou feed de mensagens onde os eventos são publicados.
- **Partition**: Divisão de um tópico que permite paralelismo.
- **Producer**: Aplicação que publica mensagens em tópicos.
- **Consumer**: Aplicação que se inscreve em tópicos e processa as mensagens.
- **Consumer Group**: Grupo de consumidores que trabalham juntos para processar mensagens.
- **Zookeeper**: Serviço de coordenação usado pelo Kafka (opcional nas versões mais recentes).

### Vantagens do Kafka

- **Alta Throughput**: Capaz de lidar com milhões de mensagens por segundo.
- **Escalabilidade**: Facilmente escalável horizontalmente.
- **Durabilidade**: Armazena mensagens de forma persistente e replicada.
- **Tolerância a Falhas**: Projetado para operar em clusters distribuídos.
- **Baixa Latência**: Processamento de mensagens em tempo real.

## Kafka na Arquitetura Hexagonal

A implementação do Kafka neste projeto segue os princípios da Arquitetura Hexagonal (também conhecida como Ports and Adapters), garantindo a separação de responsabilidades e o desacoplamento entre o domínio da aplicação e a infraestrutura externa.

### Princípios da Arquitetura Hexagonal

A Arquitetura Hexagonal organiza o código em três camadas principais:

1. **Domínio (Core)**: Contém as regras de negócio, entidades e eventos de domínio
2. **Aplicação**: Define portas (interfaces) para comunicação com o mundo externo
3. **Infraestrutura**: Implementa adaptadores que conectam as portas às tecnologias específicas

### Como o Kafka se Encaixa na Arquitetura Hexagonal

No contexto do Kafka, a implementação segue este padrão:

#### Camada de Domínio
- **Eventos de Domínio**: `PedidoCriadoEvent` representa um evento de negócio que ocorre quando um pedido é criado
- Os eventos são objetos puros de domínio, sem dependências de infraestrutura ou Kafka

#### Camada de Aplicação
- **Portas de Saída (Output Ports)**: `PedidoNotifierPort` define uma interface para notificar sobre eventos de pedido
- A aplicação depende apenas da abstração (porta), não da implementação específica (Kafka)

#### Camada de Infraestrutura
- **Adaptadores de Saída (Output Adapters)**: `KafkaPedidoNotifierAdapter` implementa a porta de saída usando Kafka
- **Adaptadores de Entrada (Input Adapters)**: `PedidoEventConsumer` consome eventos do Kafka e aciona a lógica de aplicação
- **Configuração**: `KafkaConfig` configura a infraestrutura Kafka sem afetar o domínio ou a aplicação

### Benefícios desta Abordagem

1. **Testabilidade**: O domínio e a aplicação podem ser testados sem dependências do Kafka
2. **Flexibilidade**: Podemos trocar o Kafka por outra tecnologia de mensageria sem alterar o domínio
3. **Manutenibilidade**: Separação clara de responsabilidades facilita a manutenção
4. **Evolução Independente**: As camadas podem evoluir independentemente

### Oportunidades de Melhoria

Para aderir ainda mais estritamente à Arquitetura Hexagonal:

1. **Consumidores com Portas de Entrada**: Idealmente, os consumidores Kafka deveriam implementar portas de entrada (interfaces) definidas na camada de aplicação
2. **Eventos como Comandos**: Considerar a transformação de eventos em comandos que são processados por handlers específicos
3. **Injeção de Dependência**: Garantir que todas as dependências fluam de fora para dentro, respeitando a regra de dependência da arquitetura hexagonal

## Implementação no Projeto

### Dependências Maven

Adicione as seguintes dependências ao arquivo `pom.xml`:

```xml
<!-- Spring Kafka -->
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>

<!-- Para testes -->
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka-test</artifactId>
    <scope>test</scope>
</dependency>
```

### Configuração do Kafka

Crie uma classe de configuração para o Kafka:

```java
package com.example.pedido.infrastructure.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${app.kafka.topic.pedido-criado}")
    private String pedidoCriadoTopic;

    // Configuração do Producer
    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    // Configuração do Consumer
    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "pedido-group");
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "com.example.pedido.domain.evento");
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = 
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    // Criação de tópicos
    @Bean
    public NewTopic pedidoCriadoTopic() {
        return new NewTopic(pedidoCriadoTopic, 3, (short) 1);
    }
}
```

### Configuração no application.properties/yml

Adicione as seguintes propriedades ao arquivo `application.properties` ou `application.yml`:

```properties
# Configuração do Kafka
spring.kafka.bootstrap-servers=localhost:9092

# Tópicos da aplicação
app.kafka.topic.pedido-criado=pedido-criado
app.kafka.topic.pedido-atualizado=pedido-atualizado
```

### Adaptador de Saída para Kafka

Crie um adaptador para enviar eventos para o Kafka:

```java
package com.example.pedido.infrastructure.adapter.out.kafka;

import com.example.pedido.application.port.out.PedidoNotifierPort;
import com.example.pedido.domain.evento.PedidoCriadoEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaPedidoNotifierAdapter implements PedidoNotifierPort {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String pedidoCriadoTopic;

    public KafkaPedidoNotifierAdapter(
            KafkaTemplate<String, Object> kafkaTemplate,
            @Value("${app.kafka.topic.pedido-criado}") String pedidoCriadoTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.pedidoCriadoTopic = pedidoCriadoTopic;
    }

    @Override
    public void notificarPedidoCriado(PedidoCriadoEvent evento) {
        kafkaTemplate.send(pedidoCriadoTopic, evento.getPedido().getId().toString(), evento);
    }
}
```

### Consumidor Kafka

Crie um consumidor para processar eventos do Kafka:

```java
package com.example.pedido.infrastructure.adapter.in.kafka;

import com.example.pedido.domain.evento.PedidoCriadoEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PedidoEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(PedidoEventConsumer.class);

    @KafkaListener(topics = "${app.kafka.topic.pedido-criado}", groupId = "pedido-group")
    public void consumePedidoCriadoEvent(PedidoCriadoEvent event) {
        logger.info("Recebido evento de pedido criado: {}", event);
        // Lógica para processar o evento
    }
}
```

## Configuração do Ambiente de Desenvolvimento

### Docker Compose para Kafka

O arquivo `docker-compose.yml` foi atualizado para incluir serviços Kafka para desenvolvimento:

```yaml
version: '3.8'

services:
  app:
    build: .
    container_name: sistema-pedidos
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=dev
      - SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092
    restart: unless-stopped
    depends_on:
      - kafka
    networks:
      - pedido-network

  zookeeper:
    image: confluentinc/cp-zookeeper:7.3.2
    container_name: zookeeper
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
    ports:
      - "2181:2181"
    networks:
      - pedido-network

  kafka:
    image: confluentinc/cp-kafka:7.3.2
    container_name: kafka
    depends_on:
      - zookeeper
    ports:
      - "9092:9092"
      - "29092:29092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:9092,PLAINTEXT_HOST://localhost:29092
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
      KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
    networks:
      - pedido-network

  kafka-ui:
    image: provectuslabs/kafka-ui:latest
    container_name: kafka-ui
    depends_on:
      - kafka
    ports:
      - "8090:8080"
    environment:
      KAFKA_CLUSTERS_0_NAME: local
      KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS: kafka:9092
      KAFKA_CLUSTERS_0_ZOOKEEPER: zookeeper:2181
    networks:
      - pedido-network

networks:
  pedido-network:
    driver: bridge
```

### Perfil de Desenvolvimento

Crie um arquivo `application-dev.properties` ou `application-dev.yml` com configurações específicas para desenvolvimento:

```properties
# Configuração do Kafka para desenvolvimento
spring.kafka.bootstrap-servers=localhost:29092
spring.kafka.consumer.auto-offset-reset=earliest
```

## Exemplos de Uso

### Publicando Eventos

```java
@Service
public class PedidoService implements PedidoUseCase {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String pedidoCriadoTopic;

    public PedidoService(
            KafkaTemplate<String, Object> kafkaTemplate,
            @Value("${app.kafka.topic.pedido-criado}") String pedidoCriadoTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.pedidoCriadoTopic = pedidoCriadoTopic;
    }

    @Override
    public Pedido criarPedido(Pedido pedido) {
        // Lógica para criar o pedido

        // Publicar evento no Kafka
        PedidoCriadoEvent evento = new PedidoCriadoEvent(pedido);
        kafkaTemplate.send(pedidoCriadoTopic, pedido.getId().toString(), evento);

        return pedido;
    }
}
```

### Consumindo Eventos

```java
@Component
public class EstoqueUpdater {

    private static final Logger logger = LoggerFactory.getLogger(EstoqueUpdater.class);

    @KafkaListener(topics = "${app.kafka.topic.pedido-criado}", groupId = "estoque-group")
    public void atualizarEstoque(PedidoCriadoEvent event) {
        logger.info("Atualizando estoque para o pedido: {}", event.getPedido().getId());
        // Lógica para atualizar o estoque
    }
}
```

## Boas Práticas

1. **Idempotência**: Garanta que os consumidores sejam idempotentes para evitar processamento duplicado.
2. **Particionamento**: Use chaves de partição consistentes para garantir a ordem das mensagens relacionadas.
3. **Monitoramento**: Configure métricas e alertas para monitorar a saúde do Kafka.
4. **Tratamento de Erros**: Implemente estratégias robustas de tratamento de erros e retentativas.
5. **Testes**: Utilize `EmbeddedKafkaBroker` para testes de integração.

## Troubleshooting

### Problemas Comuns

1. **Conexão Recusada**:
   - Verifique se o Kafka está em execução
   - Confirme se as configurações de bootstrap-servers estão corretas

2. **Deserialização Falhou**:
   - Verifique se o formato da mensagem corresponde à classe esperada
   - Confirme se o pacote está na lista de pacotes confiáveis

3. **Tópico Não Existe**:
   - Verifique se o tópico foi criado corretamente
   - Use a interface Kafka UI para verificar os tópicos existentes

### Verificando Logs do Kafka

```bash
docker logs kafka
```

### Acessando a Interface Kafka UI

Acesse http://localhost:8090 para visualizar tópicos, consumidores e mensagens através da interface Kafka UI.
