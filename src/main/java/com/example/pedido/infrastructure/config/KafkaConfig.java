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
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuração do Kafka para o sistema de pedidos.
 * Esta classe configura os beans necessários para produzir e consumir mensagens Kafka.
 */
@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;

    @Value("${app.kafka.topic.pedido-criado:pedido-criado}")
    private String pedidoCriadoTopic;

    /**
     * Configura a fábrica de produtores Kafka.
     *
     * @return ProducerFactory configurado para serializar chaves como String e valores como JSON
     */
    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    /**
     * Cria um template Kafka para enviar mensagens.
     *
     * @return KafkaTemplate configurado com a fábrica de produtores
     */
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    /**
     * Configura a fábrica de consumidores Kafka.
     *
     * @return ConsumerFactory configurado para deserializar chaves como String e valores como JSON
     */
    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "pedido-group");

        // Configurar ErrorHandlingDeserializer para chaves
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        configProps.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);

        // Configurar ErrorHandlingDeserializer para valores
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        configProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);

        // Configurações adicionais para o JsonDeserializer
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "com.example.pedido.domain.evento");
        configProps.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.example.pedido.domain.evento.PedidoCriadoEvent");

        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    /**
     * Cria uma fábrica de containers para listeners Kafka.
     *
     * @return ConcurrentKafkaListenerContainerFactory configurada com a fábrica de consumidores
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = 
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        // Configurar para usar acknowledgment manual (AckMode.MANUAL_IMMEDIATE)
        factory.getContainerProperties().setAckMode(org.springframework.kafka.listener.ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }

    /**
     * Cria o tópico para eventos de pedido criado.
     *
     * @return NewTopic configurado com o nome, número de partições e fator de replicação
     */
    @Bean
    public NewTopic pedidoCriadoTopic() {
        // 3 partições e fator de replicação 1 (adequado para desenvolvimento)
        return new NewTopic(pedidoCriadoTopic, 3, (short) 1);
    }
}
