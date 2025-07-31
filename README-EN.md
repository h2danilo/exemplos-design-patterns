
# 🧠 Project: Order Management System with Design Patterns and Hexagonal Architecture

[![JavaDoc](https://img.shields.io/badge/docs-javadoc-blue.svg)](https://h2danilo.github.io/exemplos-design-patterns/)
[![Java CI with Maven](https://github.com/h2danilo/exemplos-design-patterns/actions/workflows/build.yml/badge.svg)](https://github.com/h2danilo/exemplos-design-patterns/actions/workflows/build.yml)
[![Coverage](https://codecov.io/gh/h2danilo/exemplos-design-patterns/branch/main/graph/badge.svg?token=TOKEN_AQUI)](https://codecov.io/gh/h2danilo/exemplos-design-patterns)

This is a hands-on study project demonstrating the use of key **Design Patterns** in a real-world context using **Spring Boot** and **Hexagonal Architecture**. It was built to serve as a reference for developers looking to understand and apply clean code principles, decoupled design, and professional practices in Java backend development.

---

## ✅ Design Patterns Applied

| Pattern            | Purpose & Usage                                                                 |
|--------------------|----------------------------------------------------------------------------------|
| **Strategy**        | Calculates discounts based on customer type (`DescontoStrategy`)               |
| **Factory**         | Instantiates the correct discount strategy dynamically (`DescontoFactory`)     |
| **Observer**        | Triggers services (email, stock, invoice) after order creation (`PedidoNotifier`) |
| **Decorator**       | Adds behavior to reports without modifying the base class (`Relatorio`)         |
| **External Config** | Uses `application.properties` with `@Value` for environment configuration       |

---

## 🧱 Hexagonal Architecture

- **domain**: business entities, enums, interfaces
- **application**: orchestration logic (use cases)
- **infrastructure**: external services and configurations
- **rest**: REST controllers (HTTP interface)

---

## ⚙️ Configuration

Environment is set via `application.properties` using `@ConfigurationProperties`.

```properties
app.notificacao.ambiente=producao
```

---
## 🚀 How to Run

```bash
./mvnw spring-boot:run        # Linux/macOS
.\mvnw.cmd spring-boot:run    # Windows
```

---

## 🧪 Run Tests

```bash
./mvnw test
```

Unit tests are written with JUnit 5. No mocking frameworks are used — dependencies are manually instantiated to ensure compatibility with Java 24+ and maintain clear test coverage.

---

## 📁 Project Structure

```
pedido-design-patterns
├── domain/
│   ├── Pedido.java, TipoCliente.java
│   ├── desconto/
│   ├── evento/
│   └── relatorio/
├── application/
│   └── PedidoService.java
├── infrastructure/
│   ├── config/
│   ├── service/
│   └── rest/
├── resources/
│   └── application.properties
└── test/
    └── PedidoServiceTest.java
```

---

## 📊 Sample HTTP Requests

### Create Order

```http
POST /pedidos
Content-Type: application/json

{
  "id": 1,
  "valor": 100.00,
  "tipoCliente": "VIP"
}
```

### Get Decorated Report

```http
GET /pedidos/relatorio
```

---

## 📌 About this Project

This project was designed for **educational and demonstration purposes**. It is a clean example of how to apply foundational design patterns within a structured and maintainable Java backend system.

It’s ideal for:
- Understanding how to structure Spring Boot projects using Hexagonal Architecture
- Learning how to apply behavioral and creational patterns in real services
- Using external configuration to decouple environments from business logic

---

## 🧠 Author & Credits

Developed by **Danilo Valim** – Backend Java Developer  
Feel free to use, fork or adapt this project as part of your own learning journey or portfolio.

