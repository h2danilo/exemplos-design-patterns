package com.example.pedido.infrastructure.rest;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller para fornecer informações sobre métricas disponíveis
 * Complementa o endpoint /actuator/prometheus que expõe as métricas no formato Prometheus
 */
@RestController
@RequestMapping("/api/metrics")
public class MetricsController {

    private final MeterRegistry meterRegistry;

    @Autowired
    public MetricsController(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    /**
     * Fornece informações sobre as métricas disponíveis e como acessá-las
     * @return Mapa com informações sobre métricas
     */
    @GetMapping("/info")
    public Map<String, Object> getMetricsInfo() {
        Map<String, Object> info = new HashMap<>();

        info.put("metricsEndpoint", "/actuator/prometheus");
        info.put("description", "Endpoint que expõe métricas no formato Prometheus");

        Map<String, String> availableMetrics = new HashMap<>();
        availableMetrics.put("pedidos.criados", "Contador do total de pedidos criados");
        availableMetrics.put("pedidos.criacao.tempo", "Tempo para criar um pedido");
        availableMetrics.put("pedidos.processamento.tempo", "Tempo de processamento de pedidos");
        availableMetrics.put("pedidos.valor", "Distribuição dos valores dos pedidos");
        availableMetrics.put("pedidos.por.tipo.cliente", "Contador de pedidos por tipo de cliente");
        availableMetrics.put("pedidos.evento.processamento", "Tempo para processar evento de pedido");
        availableMetrics.put("pedidos.estoque.atualizacao", "Tempo para atualizar o estoque");
        availableMetrics.put("pedidos.notafiscal.geracao", "Tempo para gerar nota fiscal");
        availableMetrics.put("pedidos.notificacao", "Tempo para enviar notificações (por tipo)");
        availableMetrics.put("pedidos.notificacao.total", "Tempo total para enviar todas as notificações");
        availableMetrics.put("pedidos.eventos.processados", "Contador de eventos de pedido processados");

        info.put("availableMetrics", availableMetrics);

        return info;
    }
}
