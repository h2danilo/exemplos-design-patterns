package com.example.pedido.infrastructure.rest;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para o MetricsController.
 */
public class MetricsControllerTest {

    private SimpleMeterRegistry meterRegistry;
    private MetricsController metricsController;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        metricsController = new MetricsController(meterRegistry);
    }

    @Test
    void deveRetornarInformacoesMetricas() {
        // Act
        Map<String, Object> result = metricsController.getMetricsInfo();

        // Assert
        assertNotNull(result);
        assertEquals("/actuator/prometheus", result.get("metricsEndpoint"));
        assertEquals("Endpoint que expõe métricas no formato Prometheus", result.get("description"));

        // Verifica se o mapa de métricas disponíveis existe e contém as entradas esperadas
        @SuppressWarnings("unchecked")
        Map<String, String> availableMetrics = (Map<String, String>) result.get("availableMetrics");
        assertNotNull(availableMetrics);
        assertEquals(11, availableMetrics.size());

        // Verifica algumas métricas específicas
        assertEquals("Contador do total de pedidos criados", availableMetrics.get("pedidos.criados"));
        assertEquals("Tempo para criar um pedido", availableMetrics.get("pedidos.criacao.tempo"));
        assertEquals("Tempo de processamento de pedidos", availableMetrics.get("pedidos.processamento.tempo"));
        assertEquals("Distribuição dos valores dos pedidos", availableMetrics.get("pedidos.valor"));
        assertEquals("Contador de pedidos por tipo de cliente", availableMetrics.get("pedidos.por.tipo.cliente"));
    }
}
