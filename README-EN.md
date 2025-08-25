# 🧠 Project: Order System with Design Patterns and Hexagonal Architecture

[![JavaDoc](https://img.shields.io/badge/docs-javadoc-blue.svg)](https://h2danilo.github.io/exemplos-design-patterns/)
![Java CI with Maven](https://github.com/h2danilo/exemplos-design-patterns/actions/workflows/build.yml/badge.svg)
[![codecov](https://codecov.io/gh/h2danilo/exemplos-design-patterns/branch/main/graph/badge.svg)](https://codecov.io/gh/h2danilo/exemplos-design-patterns)

This project demonstrates the practical application of **Design Patterns** using **Spring Boot**, focusing on clean code, best practices, and hexagonal architecture. It's a study and reference project for those who want to understand how to apply these concepts in a real system.

---

## ✅ Design Patterns Used

| Pattern     | Application                                                                 |
|------------|---------------------------------------------------------------------------|
| Strategy   | Discount calculation based on customer type (`DescontoStrategy`)      |
| Factory    | Dynamically instantiates the correct strategy (`DescontoFactory`)         |
| Observer   | Event notifications like email sending or stock update    |
| Decorator  | Dynamically adds behavior to the notifier (`NotificadorDecorator`) |

---

## 🧱 Hexagonal Architecture

The hexagonal architecture (also known as Ports and Adapters) organizes the project into:

- **Domain (Core)**: 
  - **Model**: Entities and value objects (Pedido, TipoCliente)
  - **Event**: Domain events (PedidoCriadoEvent)
  - **Desconto**: Discount strategies (Strategy Pattern)

- **Application**:
  - **Port/In**: Input ports (interfaces like PedidoUseCase)
  - **Port/Out**: Output ports (interfaces like PedidoNotifierPort, EstoqueUpdaterPort)
  - **useCase**: Use case implementations (PedidoUseCaseImpl)

- **Infrastructure**:
  - **Adapter/In**: Input adapters (REST Controllers)
  - **Adapter/Out**: Output adapters (NotificadorEmailAdapter, AtualizadorEstoqueAdapter)
  - **Config**: Application configurations

---

## 📦 Technologies and Tools

- **Java 21** – Main language used in the project.
- **Spring Boot 3.3.2** – Framework for developing web applications and APIs.
- **Maven** – Dependency and build manager.
- **Docker** – Application containerization with multi-stage builds for optimization.
- **Kubernetes** – Container orchestration for production environments.
- **GitHub Container Registry (GHCR)** – Docker image registry integrated with GitHub.
- **Apache Kafka** – Event streaming platform for asynchronous communication.
- **Spring Kafka** – Spring integration with Apache Kafka.
- **Spring Boot Actuator** – Application monitoring and metrics.
- **Micrometer Prometheus** – Metrics exposure for monitoring.
- **JUnit & Mockito** – Testing frameworks to ensure code quality and behavior.
- **JaCoCo** – Tool for test coverage analysis.
- **Codecov** – Service for analyzing and visualizing code coverage (via GitHub Actions).
- **GitHub Actions (CI)** – Continuous integration for running tests and builds automatically.
- **Hexagonal Architecture** – Project organization based on ports and adapters, promoting decoupling and testability.
- **JavaDoc** – Documentation automatically generated and published via GitHub Pages.

---

## 🧪 Tests and Coverage

Tests are automated and covered by CI on GitHub Actions. Test coverage is reported via [Codecov](https://codecov.io/gh/h2danilo/exemplos-design-patterns).

```bash
# Run tests and generate coverage report:
mvn clean verify
```

---

## 📄 JavaDoc Documentation

The JavaDoc documentation is automatically published at:  
🔗 https://h2danilo.github.io/exemplos-design-patterns/

To generate it manually:

```bash
mvn javadoc:javadoc
```

---

## 🚀 How to Run

### Using Maven

```bash
# Clone the project
git clone https://github.com/h2danilo/exemplos-design-patterns.git

# Enter the project folder
cd exemplos-design-patterns

# Run the application
./mvnw spring-boot:run
```

### Using Docker Compose (with Kafka)

```bash
# Clone the project
git clone https://github.com/h2danilo/exemplos-design-patterns.git

# Enter the project folder
cd exemplos-design-patterns

# Start the application with Docker Compose (includes Kafka)
docker-compose up -d

# Check running services
docker-compose ps

# Access the Kafka UI interface
# Open http://localhost:8090 in your browser
```

---

## 🐳 Deployment

For detailed instructions on how to deploy this application using Docker, Docker Compose, or on cloud providers, check our [Deployment Guide](./docs/deployment-guide.md).

### Current Implementation

The application is configured for deployment with:

- **Docker**: Uses a multi-stage Dockerfile with Java 21
  - First stage: Maven with JDK 21 for building
  - Second stage: JRE 21 for runtime (optimized image)
  - Exposes port 8080

- **Docker Compose**: Simple configuration for local execution
  - Defines a service for the application
  - Configures Spring profile as "dev"
  - Includes Kafka services (broker, zookeeper, and management interface)
  - Prepares infrastructure for future integrations (such as databases)

- **Kubernetes**: Complete configuration for production environments
  - `deployment.yaml` - Deployment with 2 replicas, RollingUpdate strategy, health probes, resource limits, and security configurations
  - `service.yaml` - ClusterIP service with annotations for Prometheus monitoring
  - `configmap.yaml` - Application configurations with variables for different environments
  - `ingress.yaml` - NGINX ingress configuration for external access via "design-patterns.example.com"
