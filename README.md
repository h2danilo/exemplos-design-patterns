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
  - **Service**: Implementações dos casos de uso (PedidoService)

- **Infraestrutura**:
  - **Adapter/In**: Adaptadores de entrada (REST Controllers)
  - **Adapter/Out**: Adaptadores de saída (NotificadorEmailAdapter, AtualizadorEstoqueAdapter)
  - **Config**: Configurações da aplicação

---

## 📦 Tecnologias e Ferramentas

- **Java 17** – Linguagem principal usada no projeto.
- **Spring Boot 3.1.5** – Framework para desenvolvimento de aplicações web e APIs.
- **Maven** – Gerenciador de dependências e build.
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

## 📄 Documentação JavaDoc

A documentação JavaDoc é publicada automaticamente em:  
🔗 https://h2danilo.github.io/exemplos-design-patterns/

Para gerar manualmente:

```bash
mvn javadoc:javadoc
```

---

## 🚀 Como Executar

```bash
# Clonar o projeto
git clone https://github.com/h2danilo/exemplos-design-patterns.git

# Entrar na pasta do projeto
cd exemplos-design-patterns

# Executar a aplicação
./mvnw spring-boot:run
```

---

## 🐳 Deployment

Para instruções detalhadas sobre como fazer o deployment desta aplicação usando Docker, Docker Compose, ou em provedores de nuvem, consulte nosso [Guia de Deployment](docs/deployment-guide.md).

O guia inclui:
- Criação de Dockerfile
- Configuração do Docker Compose
- Opções de deployment em AWS, Google Cloud e Azure
- Deployment avançado com Kubernetes

### Kubernetes

Os arquivos de configuração para Kubernetes estão disponíveis na pasta `k8s` e incluem:
- `deployment.yaml` - Configuração do deployment da aplicação
- `service.yaml` - Configuração do serviço para expor a aplicação
- `configmap.yaml` - Configurações da aplicação
- `ingress.yaml` - Configuração de ingress para acesso externo

Para recomendações sobre como aprimorar a implementação de Docker e Kubernetes para demonstração prática, consulte nossas [Recomendações de Aprimoramento](docs/docker-kubernetes-enhancements.md).
