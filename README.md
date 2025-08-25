# 🧠 Projeto: Sistema de Pedidos com Design Patterns e Arquitetura Hexagonal

[![JavaDoc](https://img.shields.io/badge/docs-javadoc-blue.svg)](https://h2danilo.github.io/exemplos-design-patterns/)
![Java CI with Maven](https://github.com/h2danilo/exemplos-design-patterns/actions/workflows/build.yml/badge.svg)
[![codecov](https://codecov.io/gh/h2danilo/exemplos-design-patterns/branch/main/graph/badge.svg)](https://codecov.io/gh/h2danilo/exemplos-design-patterns)

Este projeto demonstra a aplicação prática de **padrões de projeto (Design Patterns)** usando **Spring Boot**, com foco em código limpo, boas práticas e arquitetura hexagonal. É um projeto de estudo e referência para quem deseja entender como aplicar esses conceitos em um sistema real.

---

## ✅ Padrões de Projeto Utilizados

| Padrão     | Aplicação                                                                 |
|------------|---------------------------------------------------------------------------|
| Strategy   | Cálculo de desconto baseado no tipo de cliente (`DescontoStrategy`)      |
| Factory    | Instancia a estratégia correta dinamicamente (`DescontoFactory`)         |
| Observer   | Notificação de eventos como envio de e-mail ou atualização de estoque    |
| Decorator  | Adição dinâmica de comportamento ao notificador (`NotificadorDecorator`) |

---

## 🧱 Arquitetura Hexagonal

A arquitetura hexagonal (também conhecida como Ports and Adapters) organiza o projeto em:

- **Domínio (Core)**: 
  - **Model**: Entidades e objetos de valor (Pedido, TipoCliente)
  - **Event**: Eventos de domínio (PedidoCriadoEvent)
  - **Desconto**: Estratégias de desconto (Strategy Pattern)

- **Aplicação**:
  - **Port/In**: Portas de entrada (interfaces como PedidoUseCase)
  - **Port/Out**: Portas de saída (interfaces como PedidoNotifierPort, EstoqueUpdaterPort)
  - **useCase**: Implementações dos casos de uso (PedidoUseCaseImpl)

- **Infraestrutura**:
  - **Adapter/In**: Adaptadores de entrada (REST Controllers)
  - **Adapter/Out**: Adaptadores de saída (NotificadorEmailAdapter, AtualizadorEstoqueAdapter)
  - **Config**: Configurações da aplicação

---

## 📦 Tecnologias e Ferramentas

- **Java 21** – Linguagem principal usada no projeto.
- **Spring Boot 3.3.2** – Framework para desenvolvimento de aplicações web e APIs.
- **Maven** – Gerenciador de dependências e build.
- **Docker** – Containerização da aplicação com multi-estágio para otimização.
- **Kubernetes** – Orquestração de containers para ambientes de produção.
- **GitHub Container Registry (GHCR)** – Registro de imagens Docker integrado ao GitHub.
- **Apache Kafka** – Plataforma de streaming de eventos para comunicação assíncrona.
- **Spring Kafka** – Integração do Spring com o Apache Kafka.
- **Spring Boot Actuator** – Monitoramento e métricas da aplicação.
- **Micrometer Prometheus** – Exposição de métricas para monitoramento.
- **JUnit & Mockito** – Frameworks de teste para garantir a qualidade e o comportamento do código.
- **JaCoCo** – Ferramenta para análise de cobertura de testes.
- **Codecov** – Serviço para análise e visualização da cobertura de código (via GitHub Actions).
- **GitHub Actions (CI)** – Integração contínua para execução de testes e builds automáticos.
- **Arquitetura Hexagonal** – Organização do projeto baseada em portas e adaptadores, promovendo desacoplamento e testabilidade.
- **JavaDoc** – Documentação gerada automaticamente com publicação via GitHub Pages.

---

## 🧪 Testes e Cobertura

Os testes são automatizados e cobertos por CI no GitHub Actions. A cobertura de testes é reportada via [Codecov](https://codecov.io/gh/h2danilo/exemplos-design-patterns).

```bash
# Executar testes e gerar relatório de cobertura:
mvn clean verify
```

---

## 📄 Documentação

### JavaDoc

A documentação JavaDoc é publicada automaticamente em:  
🔗 https://h2danilo.github.io/exemplos-design-patterns/

Para gerar manualmente:

```bash
mvn javadoc:javadoc
```

### Kafka

Para informações sobre como implementar e utilizar o Apache Kafka no projeto, consulte o [Guia de Kafka](./docs/kafka-guide.md). Este guia inclui:

- Conceitos básicos do Kafka
- Implementação no projeto
- Configuração do ambiente de desenvolvimento
- Exemplos de uso
- Boas práticas
- Troubleshooting

---

## 🚀 Como Executar

### Usando Maven

```bash
# Clonar o projeto
git clone https://github.com/h2danilo/exemplos-design-patterns.git

# Entrar na pasta do projeto
cd exemplos-design-patterns

# Executar a aplicação
./mvnw spring-boot:run
```

### Usando Docker Compose (com Kafka)

```bash
# Clonar o projeto
git clone https://github.com/h2danilo/exemplos-design-patterns.git

# Entrar na pasta do projeto
cd exemplos-design-patterns

# Iniciar a aplicação com Docker Compose (inclui Kafka)
docker-compose up -d

# Verificar os serviços em execução
docker-compose ps

# Acessar a interface do Kafka UI
# Abra http://localhost:8090 no navegador
```

---

## 🐳 Deployment

Para instruções detalhadas sobre como fazer o deployment desta aplicação usando Docker, Docker Compose, ou em provedores de nuvem, consulte nosso [Guia de Deployment](./docs/deployment-guide.md).

### Implementação Atual

A aplicação está configurada para deployment com:

- **Docker**: Utiliza um Dockerfile multi-estágio com Java 21
  - Primeiro estágio: Maven com JDK 21 para build
  - Segundo estágio: JRE 21 para runtime (imagem otimizada)
  - Expõe a porta 8080

- **Docker Compose**: Configuração simples para execução local
  - Define um serviço para a aplicação
  - Configura o perfil Spring como "dev"
  - Inclui serviços Kafka (broker, zookeeper e interface de gerenciamento)
  - Prepara a infraestrutura para futuras integrações (como banco de dados)

- **Kubernetes**: Configuração completa para ambientes de produção
  - `deployment.yaml` - Deployment com 2 réplicas, estratégia RollingUpdate, probes de saúde, limites de recursos e configurações de segurança
  - `service.yaml` - Serviço ClusterIP com anotações para monitoramento Prometheus
  - `configmap.yaml` - Configurações da aplicação com variáveis para diferentes ambientes
  - `ingress.yaml` - Configuração de ingress com NGINX para acesso externo via "design-patterns.example.com"
