
# 🧠 Projeto: Sistema de Pedidos com Design Patterns e Arquitetura Hexagonal

[![JavaDoc](https://img.shields.io/badge/docs-javadoc-blue.svg)](https://h2danilo.github.io/exemplos-design-patterns/)
[![Java CI with Maven](https://github.com/h2danilo/exemplos-design-patterns/actions/workflows/build.yml/badge.svg)](https://github.com/h2danilo/exemplos-design-patterns/actions/workflows/build.yml)
[![codecov](https://codecov.io/gh/h2danilo/exemplos-design-patterns/branch/main/graph/badge.svg)](https://codecov.io/gh/h2danilo/exemplos-design-patterns)

Este projeto demonstra a aplicação prática de **padrões de projeto (Design Patterns)** usando **Spring Boot**, com foco em código limpo, boas práticas e arquitetura hexagonal. É um projeto de estudo e referência para quem deseja entender como aplicar esses conceitos em um sistema real.

---

## ✅ Padrões de Projeto Aplicados

| Padrão             | Onde foi aplicado                                                        |
|--------------------|---------------------------------------------------------------------------|
| **Strategy**        | Cálculo de desconto baseado no tipo de cliente (`DescontoStrategy`)      |
| **Factory**         | Instancia a estratégia correta dinamicamente (`DescontoFactory`)         |
| **Observer**        | Ações automáticas após a criação de pedido (`PedidoNotifier`)            |
| **Decorator**       | Composição dinâmica de funcionalidades em relatórios (`Relatorio`)       |
| **Injeção de Configuração** | Leitura do ambiente via `application.properties` com `@Value`         |

---

## 🧱 Arquitetura Hexagonal

- **domain**: entidades, enums e interfaces (core de negócio)
- **application**: orquestração dos casos de uso
- **infrastructure**: implementação de serviços externos e configuração
- **rest**: interface HTTP (controladores REST)

---

## 🚀 Como Rodar

```bash
./mvnw spring-boot:run        # Linux/macOS
.\mvnw.cmd spring-boot:run    # Windows
```

---

## 🧪 Executar Testes

```bash
./mvnw test
```

Os testes utilizam `JUnit 5`, sem uso de Mockito, com instância manual das dependências para maior clareza e compatibilidade com Java 24+.

---

## 📁 Estrutura do Projeto

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

## 📊 Exemplo de Requisição

### Criar Pedido

```http
POST /pedidos
Content-Type: application/json

{
  "id": 1,
  "valor": 100.00,
  "tipoCliente": "VIP"
}
```

---

### Gerar Relatório Decorado

```http
GET /pedidos/relatorio
```

---

## 🧠 Finalidade

Este projeto tem finalidade exclusivamente didática, voltado para estudos e referência sobre boas práticas de arquitetura e aplicação de padrões de projeto no ecossistema Java com Spring Boot.

