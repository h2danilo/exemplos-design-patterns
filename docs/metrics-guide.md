# Guia de Métricas - Sistema de Pedidos

Este documento fornece informações detalhadas sobre as métricas disponíveis no sistema de pedidos, como acessá-las, interpretá-las e utilizá-las para monitoramento em tempo real.

## Índice

1. [Visão Geral](#visão-geral)
2. [Métricas Disponíveis](#métricas-disponíveis)
3. [Como Acessar as Métricas](#como-acessar-as-métricas)
4. [Configuração do Prometheus](#configuração-do-prometheus)
5. [Visualização com Grafana](#visualização-com-grafana)
6. [Casos de Uso Comuns](#casos-de-uso-comuns)
7. [Troubleshooting](#troubleshooting)

## Visão Geral

O sistema de pedidos utiliza o Micrometer com Prometheus para coletar e expor métricas em tempo real. Isso permite monitorar o desempenho da aplicação, identificar gargalos e acompanhar o comportamento do sistema em produção.

As métricas são coletadas em vários pontos do fluxo de processamento de pedidos, desde a criação até a notificação, permitindo uma visão detalhada de cada etapa do processo.

## Métricas Disponíveis

O sistema expõe as seguintes métricas:

| Nome da Métrica | Tipo | Descrição |
|-----------------|------|-----------|
| `pedidos.criados` | Contador | Total de pedidos criados no sistema |
| `pedidos.criacao.tempo` | Timer | Tempo necessário para criar um pedido completo |
| `pedidos.processamento.tempo` | Timer | Tempo de processamento de pedidos |
| `pedidos.valor` | Distribuição | Distribuição estatística dos valores dos pedidos |
| `pedidos.por.tipo.cliente` | Contador | Contador de pedidos segmentado por tipo de cliente |
| `pedidos.evento.processamento` | Timer | Tempo para processar eventos de pedido |
| `pedidos.estoque.atualizacao` | Timer | Tempo para atualizar o estoque |
| `pedidos.notafiscal.geracao` | Timer | Tempo para gerar nota fiscal |
| `pedidos.notificacao` | Timer | Tempo para enviar notificações (por tipo) |
| `pedidos.notificacao.total` | Timer | Tempo total para enviar todas as notificações |
| `pedidos.eventos.processados` | Contador | Total de eventos de pedido processados |

### Detalhes dos Tipos de Métricas

- **Contador**: Incrementa cada vez que um evento ocorre. Útil para medir frequência de operações.
- **Timer**: Mede o tempo de execução de operações. Fornece contagem, tempo total e distribuição estatística.
- **Distribuição**: Registra a distribuição de valores, fornecendo percentis, média, máximo, etc.

## Como Acessar as Métricas

### Endpoint de Informações

O sistema fornece um endpoint REST para obter informações sobre as métricas disponíveis:

```
GET /api/metrics/info
```

Este endpoint retorna um JSON com detalhes sobre as métricas disponíveis e como acessá-las.

### Endpoint Prometheus

As métricas são expostas no formato Prometheus através do endpoint:

```
GET /actuator/prometheus
```

Este endpoint retorna todas as métricas no formato que o Prometheus pode consumir.

### Exemplo de Resposta do Prometheus

```
# HELP pedidos_criados_total Total de pedidos criados
# TYPE pedidos_criados_total counter
pedidos_criados_total 42.0

# HELP pedidos_criacao_tempo_seconds Tempo para criar um pedido
# TYPE pedidos_criacao_tempo_seconds summary
pedidos_criacao_tempo_seconds_count 42.0
pedidos_criacao_tempo_seconds_sum 3.12
```

## Configuração do Prometheus

Para configurar o Prometheus para coletar métricas da aplicação, adicione o seguinte trecho ao seu arquivo `prometheus.yml`:

```yaml
scrape_configs:
  - job_name: 'pedidos-app'
    metrics_path: '/actuator/prometheus'
    scrape_interval: 5s
    static_configs:
      - targets: ['localhost:8080']
```

Substitua `localhost:8080` pelo endereço e porta onde sua aplicação está rodando.

### Docker Compose

O projeto já utiliza Docker Compose para o ambiente de desenvolvimento. Para adicionar Prometheus e Grafana ao ambiente existente, você pode estender o arquivo `docker-compose.yml` com as seguintes configurações:

```yaml
services:
  # Serviços existentes (app, kafka, zookeeper, kafka-ui)
  # ...

  # Adicione o Prometheus para coletar métricas
  prometheus:
    image: prom/prometheus:latest
    container_name: prometheus
    ports:
      - "9090:9090"
    volumes:
      - ./prometheus.yml:/etc/prometheus/prometheus.yml
    networks:
      - pedido-network
    restart: unless-stopped

  # Adicione o Grafana para visualização de métricas
  grafana:
    image: grafana/grafana:latest
    container_name: grafana
    ports:
      - "3000:3000"
    environment:
      - GF_SECURITY_ADMIN_USER=admin
      - GF_SECURITY_ADMIN_PASSWORD=admin
      - GF_USERS_ALLOW_SIGN_UP=false
    volumes:
      - grafana-data:/var/lib/grafana
    networks:
      - pedido-network
    restart: unless-stopped
    depends_on:
      - prometheus

volumes:
  grafana-data:
```

Um arquivo `prometheus.yml` já está disponível na raiz do projeto, configurado para coletar métricas da aplicação no ambiente Docker Compose. O arquivo contém:

```yaml
global:
  scrape_interval: 15s
  evaluation_interval: 15s

scrape_configs:
  - job_name: 'pedidos-app'
    metrics_path: '/actuator/prometheus'
    scrape_interval: 5s
    # Configuração para prod
    # Descomente as linhas abaixo e comente a linha config para dev local (fora do Docker)
    static_configs:
      - targets: ['app:8080']  # Nome do serviço no Docker Compose

    # Configuração para desenvolvimento local (fora do Docker)
    # Descomente as linhas abaixo e comente a linha acima se estiver executando localmente
    # static_configs:
    #   - targets: ['host.docker.internal:8080']
```

Observe que usamos `app:8080` como alvo, que é o nome do serviço da aplicação no Docker Compose. Se você estiver executando a aplicação localmente (fora do Docker), você precisará modificar o arquivo conforme indicado nos comentários.

## Visualização com Grafana

O Grafana é uma ferramenta poderosa para visualizar métricas do Prometheus. Se você seguiu as instruções do Docker Compose acima, o Grafana já estará disponível em http://localhost:3000 (usuário e senha: admin).

### Configuração do Grafana

1. Acesse o Grafana em http://localhost:3000 e faça login
2. Vá para Configurações > Data Sources > Add data source
3. Selecione Prometheus
4. No campo URL, digite `http://prometheus:9090` (nome do serviço no Docker Compose)
5. Clique em "Save & Test" para verificar a conexão

### Dashboard para o Sistema de Pedidos

Você pode criar um dashboard específico para o sistema de pedidos com os seguintes painéis:

#### Painel de Visão Geral
- **Taxa de Pedidos**: `rate(pedidos_criados_total[5m])`
- **Total de Pedidos**: `sum(pedidos_criados_total)`
- **Tempo Médio de Criação**: `rate(pedidos_criacao_tempo_seconds_sum[5m]) / rate(pedidos_criacao_tempo_seconds_count[5m])`

#### Painel de Performance
- **Tempo de Processamento (95º percentil)**: `histogram_quantile(0.95, sum(rate(pedidos_processamento_tempo_seconds_bucket[5m])) by (le))`
- **Tempo de Atualização de Estoque**: `rate(pedidos_estoque_atualizacao_seconds_sum[5m]) / rate(pedidos_estoque_atualizacao_seconds_count[5m])`
- **Tempo de Geração de Nota Fiscal**: `rate(pedidos_notafiscal_geracao_seconds_sum[5m]) / rate(pedidos_notafiscal_geracao_seconds_count[5m])`
- **Tempo de Envio de Notificações**: `rate(pedidos_notificacao_total_seconds_sum[5m]) / rate(pedidos_notificacao_total_seconds_count[5m])`

#### Painel de Negócios
- **Pedidos por Tipo de Cliente**: `sum(pedidos_por_tipo_cliente) by (tipoCliente)`
- **Valor Médio dos Pedidos**: `avg(pedidos_valor_sum / pedidos_valor_count)`
- **Distribuição de Valores (Histograma)**: Visualização dos buckets de `pedidos_valor_bucket`

### Modelo de Dashboard JSON

Para facilitar, você pode importar um modelo básico de dashboard no Grafana. Vá para "Create > Import" e cole o seguinte JSON:

```json
{
  "annotations": {
    "list": [
      {
        "builtIn": 1,
        "datasource": "-- Grafana --",
        "enable": true,
        "hide": true,
        "iconColor": "rgba(0, 211, 255, 1)",
        "name": "Annotations & Alerts",
        "type": "dashboard"
      }
    ]
  },
  "editable": true,
  "gnetId": null,
  "graphTooltip": 0,
  "id": 1,
  "links": [],
  "panels": [
    {
      "aliasColors": {},
      "bars": false,
      "dashLength": 10,
      "dashes": false,
      "datasource": "colocar nome do Data Source criado no grafana", 
      "fieldConfig": {
        "defaults": {
          "custom": {}
        },
        "overrides": []
      },
      "fill": 1,
      "fillGradient": 0,
      "gridPos": {
        "h": 8,
        "w": 12,
        "x": 0,
        "y": 0
      },
      "hiddenSeries": false,
      "id": 2,
      "legend": {
        "avg": false,
        "current": false,
        "max": false,
        "min": false,
        "show": true,
        "total": false,
        "values": false
      },
      "lines": true,
      "linewidth": 1,
      "nullPointMode": "null",
      "options": {
        "alertThreshold": true
      },
      "percentage": false,
      "pluginVersion": "7.3.7",
      "pointradius": 2,
      "points": false,
      "renderer": "flot",
      "seriesOverrides": [],
      "spaceLength": 10,
      "stack": false,
      "steppedLine": false,
      "targets": [
        {
          "expr": "rate(pedidos_criados_total[5m])",
          "interval": "",
          "legendFormat": "Taxa de Pedidos",
          "refId": "A"
        }
      ],
      "thresholds": [],
      "timeFrom": null,
      "timeRegions": [],
      "timeShift": null,
      "title": "Taxa de Pedidos",
      "tooltip": {
        "shared": true,
        "sort": 0,
        "value_type": "individual"
      },
      "type": "graph",
      "xaxis": {
        "buckets": null,
        "mode": "time",
        "name": null,
        "show": true,
        "values": []
      },
      "yaxes": [
        {
          "format": "short",
          "label": null,
          "logBase": 1,
          "max": null,
          "min": null,
          "show": true
        },
        {
          "format": "short",
          "label": null,
          "logBase": 1,
          "max": null,
          "min": null,
          "show": true
        }
      ],
      "yaxis": {
        "align": false,
        "alignLevel": null
      }
    }
  ],
  "refresh": "5s",
  "schemaVersion": 26,
  "style": "dark",
  "tags": [],
  "templating": {
    "list": []
  },
  "time": {
    "from": "now-6h",
    "to": "now"
  },
  "timepicker": {},
  "timezone": "",
  "title": "Sistema de Pedidos",
  "uid": "pedidos",
  "version": 1
}
```

Este é apenas um modelo inicial com um gráfico de taxa de pedidos. Você pode expandir adicionando mais painéis conforme necessário.

## Casos de Uso Comuns

### Monitoramento de Performance

Para monitorar a performance geral do sistema, foque nas métricas:
- `pedidos.criacao.tempo`
- `pedidos.processamento.tempo`
- `pedidos.estoque.atualizacao`
- `pedidos.notafiscal.geracao`

Estas métricas ajudam a identificar gargalos no processamento.

### Análise de Negócio

Para análise de negócios, utilize:
- `pedidos.criados`
- `pedidos.valor`
- `pedidos.por.tipo.cliente`

Estas métricas fornecem insights sobre o volume de negócios e perfil de clientes.

### Alertas

Configure alertas no Prometheus/Grafana para ser notificado quando:
- O tempo de processamento exceder um limite
- A taxa de pedidos cair abaixo do esperado
- Ocorrerem erros no processamento

Exemplo de regra de alerta no Prometheus:

```yaml
groups:
- name: pedidos
  rules:
  - alert: TempoProcessamentoAlto
    expr: histogram_quantile(0.95, sum(rate(pedidos_criacao_tempo_seconds_bucket[5m])) by (le)) > 2
    for: 5m
    labels:
      severity: warning
    annotations:
      summary: "Tempo de processamento de pedidos alto"
      description: "95% dos pedidos estão levando mais de 2 segundos para serem processados"
```

## Adicionando Métricas Personalizadas

O sistema de métricas foi projetado seguindo os princípios da arquitetura hexagonal, o que facilita a adição de novas métricas sem modificar o núcleo da aplicação.

### Adicionando Métricas na Camada de Aplicação

Para adicionar novas métricas na camada de aplicação:

1. Adicione novos métodos na interface `MetricsPort`:

```java
public interface MetricsPort {
    // Métodos existentes...

    // Novo método para uma métrica personalizada
    void registrarOperacaoPersonalizada(String tipoOperacao, double valor);
}
```

2. Implemente o método na classe `MicrometerMetricsAdapter`:

```java
@Component
public class MicrometerMetricsAdapter implements MetricsPort {
    // Implementações existentes...

    @Override
    public void registrarOperacaoPersonalizada(String tipoOperacao, double valor) {
        meterRegistry.counter("pedidos.operacao.personalizada", "tipo", tipoOperacao).increment();
        meterRegistry.gauge("pedidos.operacao.valor", Tags.of("tipo", tipoOperacao), valor);
    }
}
```

3. Atualize a implementação de stub para testes:

```java
public class StubMetricsAdapter implements MetricsPort {
    // Implementações existentes...

    @Override
    public void registrarOperacaoPersonalizada(String tipoOperacao, double valor) {
        // Não faz nada em testes
    }
}
```

### Adicionando Métricas em Novos Adaptadores

Se você estiver criando um novo adaptador (por exemplo, um novo notificador), você pode adicionar métricas específicas:

1. Injete o `MeterRegistry` no seu adaptador:

```java
@Component
public class NovoNotificadorAdapter implements PedidoNotifierPort {

    private final MeterRegistry meterRegistry;

    public NovoNotificadorAdapter(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Registre contadores, timers ou gauges específicos para este adaptador
        Counter.builder("pedidos.notificacao.novo")
               .description("Contador de notificações pelo novo canal")
               .register(meterRegistry);
    }

    @Override
    public void notificarPedidoCriado(PedidoCriadoEvent evento) {
        Timer.Sample sample = Timer.start(meterRegistry);
        try {
            // Lógica de notificação
            // ...

            // Incrementa o contador
            meterRegistry.counter("pedidos.notificacao.novo").increment();
        } finally {
            // Registra o tempo de execução
            sample.stop(meterRegistry.timer("pedidos.notificacao", "tipo", "NovoNotificador"));
        }
    }
}
```

### Boas Práticas para Métricas Personalizadas

1. **Nomenclatura consistente**: Mantenha um padrão de nomenclatura para suas métricas (ex: `dominio.entidade.operacao`)
2. **Use tags/labels**: Adicione tags para permitir filtragem e agrupamento (ex: `tipo`, `status`, `ambiente`)
3. **Documente suas métricas**: Atualize o endpoint `/api/metrics/info` para incluir suas novas métricas
4. **Evite cardinalidade alta**: Cuidado com tags que podem ter muitos valores diferentes
5. **Teste suas métricas**: Verifique se as métricas estão sendo registradas corretamente em testes

## Troubleshooting

### Métricas não aparecem no Prometheus

Verifique:
1. Se o endpoint `/actuator/prometheus` está acessível
2. Se a configuração do Prometheus está correta
3. Se não há problemas de rede entre o Prometheus e a aplicação

### Valores de métricas inesperados

Se você observar valores inesperados:
1. Verifique se há erros nos logs da aplicação
2. Confirme se a lógica de negócios está funcionando corretamente
3. Verifique se há problemas de concorrência que possam afetar as métricas

### Reinicialização de métricas

As métricas são reiniciadas quando a aplicação é reiniciada. Para análises de longo prazo, certifique-se de que o Prometheus está configurado para reter dados por tempo suficiente.
