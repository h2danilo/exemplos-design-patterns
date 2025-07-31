
# 🧠 Projeto: Sistema de Pedidos com Design Patterns e Arquitetura Hexagonal

Este projeto demonstra a aplicação dos principais **Design Patterns cobrados em entrevistas técnicas** usando **Spring Boot** com **arquitetura hexagonal**, foco em **clean code** e **boas práticas**.

---

## ✅ Padrões de Projeto Aplicados

| Padrão         | Finalidade                                                                 |
|----------------|----------------------------------------------------------------------------|
| **Singleton**  | Gerenciamento global de configurações com `enum` seguro (`Configuracoes`) |
| **Strategy**   | Cálculo de desconto baseado no tipo de cliente                             |
| **Factory**    | Instancia a estratégia correta de desconto                                 |
| **Observer**   | Notifica automaticamente ações após criação de pedido                      |
| **Decorator**  | Adiciona funcionalidades ao relatório sem alterar a classe base           |

---

## 🧱 Arquitetura Hexagonal

- **domain**: entidades, enums e interfaces (core de negócio)
- **application**: orquestração dos casos de uso
- **infrastructure**: implementação de serviços externos (e-mail, Slack)
- **adapters**: interfaces REST, listeners de eventos etc.

---

## 🚀 Como Rodar

```bash
git clone https://github.com/seu-usuario/pedido-design-patterns.git
cd pedido-design-patterns
./mvnw spring-boot:run
```

---

## 🧪 Exemplo de Requisição

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

### Gerar Relatório Decorado

```http
GET /pedidos/relatorio
```

---

## ✨ Diferenciais

- Padrões prontos para responder em entrevistas
- Código limpo e desacoplado
- Estrutura pronta para expandir para testes, banco e REST completo

---

## 📁 Estrutura de Pacotes

```
pedido
├── domain
│   ├── Pedido, TipoCliente, Configuracoes
│   ├── desconto (Strategy + Factory)
│   └── evento (Observer)
├── application
│   └── PedidoService
├── infrastructure
│   └── (Notificadores, Estoque, NF - futuros)
├── adapters
│   └── PedidoController
└── PedidoApplication.java
```

---

## 🧠 Ideal para

- Portfólio técnico
- Provas de conceito
- Estudo de arquitetura e boas práticas
- Preparação para entrevistas backend

---

**Desenvolvido por [Seu Nome] - Back-end Java Developer**
