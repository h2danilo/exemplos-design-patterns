# Guia de Deployment para o Sistema de Pedidos

Este documento descreve os passos necessários para utilizar as imagens Docker do Sistema de Pedidos disponíveis no GitHub Container Registry (GHCR).

## Índice

1. [Utilizando Imagens do GitHub Container Registry](#utilizando-imagens-do-github-container-registry)

## Utilizando Imagens do GitHub Container Registry

O GitHub Container Registry (GHCR) é um serviço de registro de containers que permite armazenar e gerenciar imagens Docker diretamente no GitHub. Este projeto utiliza o GHCR para armazenar suas imagens oficiais.

### Autenticação no GitHub Container Registry

Para baixar imagens do GHCR, primeiro é necessário se autenticar:

1. Crie um Personal Access Token (PAT) no GitHub:
   - Acesse Settings > Developer settings > Personal access tokens > Tokens (classic)
   - Clique em "Generate new token"
   - Dê um nome ao token (ex: docker-login) e marque a caixa "read:packages"
   - Clique em "Generate token" e copie o token gerado

2. Faça login no GHCR usando o Docker CLI:
   ```bash
   docker login ghcr.io -u SEU_USUARIO_GITHUB
   ```
   Quando solicitada a senha, cole o token que você acabou de criar.

### Baixando a Imagem do GHCR

Após a autenticação, você pode baixar a imagem mais recente do projeto:

```bash
docker pull ghcr.io/h2danilo/exemplos-design-patterns:latest
```

### Executando o Container

Com a imagem baixada, você pode executar a aplicação em um container:

```bash
docker run -d -p 8080:8080 --name design-patterns-app ghcr.io/h2danilo/exemplos-design-patterns:latest
```

Parâmetros:
- `-d`: Executa o container em modo "detached" (em segundo plano)
- `-p 8080:8080`: Mapeia a porta 8080 do host para a porta 8080 do container
- `--name design-patterns-app`: Define um nome para o container

### Verificando o Status da Aplicação

Verifique os logs para confirmar que a aplicação iniciou corretamente:

```bash
docker logs design-patterns-app
```

Acesse os endpoints da aplicação:
- Endpoint de saúde do Actuator: http://localhost:8080/actuator/health
- Outros endpoints disponíveis na raiz: http://localhost:8080/

### Parando e Removendo o Container

Quando terminar de usar a aplicação, você pode parar e remover o container:

```bash
# Para o container
docker stop design-patterns-app

# Remove o container
docker rm design-patterns-app
```
