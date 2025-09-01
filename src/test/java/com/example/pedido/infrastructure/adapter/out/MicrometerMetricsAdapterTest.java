package com.example.pedido.infrastructure.adapter.out;

import com.example.pedido.application.port.out.MeasurableOperation;
import com.example.pedido.domain.evento.PedidoCriadoEvent;
import com.example.pedido.domain.model.Pedido;
import com.example.pedido.domain.model.TipoCliente;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testes unitários para o MicrometerMetricsAdapter.
 */
public class MicrometerMetricsAdapterTest {

    private SimpleMeterRegistry meterRegistry;
    private MicrometerMetricsAdapter metricsAdapter;
    private Pedido pedido;
    private PedidoCriadoEvent evento;

    @BeforeEach
    void setUp() {
        // Criar um SimpleMeterRegistry para testes
        meterRegistry = new SimpleMeterRegistry();

        // Criar o adaptador de métricas com o registry real
        metricsAdapter = new MicrometerMetricsAdapter(meterRegistry);

        // Criar pedido e evento para teste
        pedido = new Pedido(1L, new BigDecimal("100.00"), TipoCliente.VIP);
        evento = new PedidoCriadoEvent(pedido);
    }

    @Test
    void deveRegistrarPedidoCriado() {
        // Act
        metricsAdapter.registrarPedidoCriado(pedido);

        // Assert
        Counter counter = meterRegistry.find("pedidos.criados").counter();
        assertNotNull(counter);
        assertEquals(1.0, counter.count());

        DistributionSummary summary = meterRegistry.find("pedidos.valor").summary();
        assertNotNull(summary);
        assertEquals(1, summary.count());
        assertEquals(100.0, summary.totalAmount());

        Counter tipoClienteCounter = meterRegistry.find("pedidos.por.tipo.cliente")
                .tag("tipoCliente", "VIP").counter();
        assertNotNull(tipoClienteCounter);
        assertEquals(1.0, tipoClienteCounter.count());
    }

    @Test
    void deveIniciarEFinalizarProcessamentoEvento() {
        // Act
        Object context = metricsAdapter.iniciarProcessamentoEvento(evento);
        metricsAdapter.finalizarProcessamentoEvento(context);

        // Assert
        Timer timer = meterRegistry.find("pedidos.processamento.tempo").timer();
        assertNotNull(timer);
        assertEquals(1, timer.count());
    }

    @Test
    void deveIniciarEFinalizarAtualizacaoEstoque() {
        // Act
        Object context = metricsAdapter.iniciarAtualizacaoEstoque();
        metricsAdapter.finalizarAtualizacaoEstoque(context);

        // Assert
        Timer timer = meterRegistry.find("pedidos.estoque.atualizacao").timer();
        assertNotNull(timer);
        assertEquals(1, timer.count());
    }

    @Test
    void deveIniciarEFinalizarGeracaoNotaFiscal() {
        // Act
        Object context = metricsAdapter.iniciarGeracaoNotaFiscal();
        metricsAdapter.finalizarGeracaoNotaFiscal(context);

        // Assert
        Timer timer = meterRegistry.find("pedidos.notafiscal.geracao").timer();
        assertNotNull(timer);
        assertEquals(1, timer.count());
    }

    @Test
    void deveIniciarEFinalizarEnvioNotificacoes() {
        // Act
        Object context = metricsAdapter.iniciarEnvioNotificacoes();
        metricsAdapter.finalizarEnvioNotificacoes(context);

        // Assert
        Timer timer = meterRegistry.find("pedidos.notificacao.total").timer();
        assertNotNull(timer);
        assertEquals(1, timer.count());
    }

    @Test
    void deveIniciarEFinalizarEnvioNotificacao() {
        // Arrange
        String tipoNotificador = "email";

        // Act
        Object context = metricsAdapter.iniciarEnvioNotificacao(tipoNotificador);
        metricsAdapter.finalizarEnvioNotificacao(context, tipoNotificador);

        // Assert
        Timer timer = meterRegistry.find("pedidos.notificacao")
                .tag("tipo", tipoNotificador).timer();
        assertNotNull(timer);
        assertEquals(1, timer.count());
    }

    @Test
    void deveRegistrarEventoProcessado() {
        // Act
        metricsAdapter.registrarEventoProcessado();

        // Assert
        Counter counter = meterRegistry.find("pedidos.eventos.processados").counter();
        assertNotNull(counter);
        assertEquals(1.0, counter.count());
    }

    @Test
    void deveMedirTempoCriacaoPedido() {
        // Arrange
        String resultado = "Pedido criado";
        MeasurableOperation<String> operation = () -> resultado;

        // Act
        String result = metricsAdapter.medirTempoCriacaoPedido(operation);

        // Assert
        assertEquals(resultado, result);
        Timer timer = meterRegistry.find("pedidos.criacao.tempo").timer();
        assertNotNull(timer);
        assertEquals(1, timer.count());
    }

    @Test
    void deveMedirTempoProcessamentoEvento() {
        // Arrange
        String resultado = "Evento processado";
        MeasurableOperation<String> operation = () -> resultado;

        // Act
        String result = metricsAdapter.medirTempoProcessamentoEvento(operation);

        // Assert
        assertEquals(resultado, result);
        Timer timer = meterRegistry.find("pedidos.evento.processamento").timer();
        assertNotNull(timer);
        assertEquals(1, timer.count());
    }

    @Test
    void deveTratarExcecaoAoMedirTempoCriacaoPedido() {
        // Arrange
        MeasurableOperation<String> operation = () -> {
            throw new RuntimeException("Erro simulado");
        };

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            metricsAdapter.medirTempoCriacaoPedido(operation);
        });

        // Verifica que o timer foi registrado mesmo com a exceção
        Timer timer = meterRegistry.find("pedidos.criacao.tempo").timer();
        assertNotNull(timer);
        assertEquals(1, timer.count());
    }

    @Test
    void deveTratarExcecaoAoMedirTempoProcessamentoEvento() {
        // Arrange
        MeasurableOperation<String> operation = () -> {
            throw new RuntimeException("Erro simulado");
        };

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            metricsAdapter.medirTempoProcessamentoEvento(operation);
        });

        // Verifica que o timer foi registrado mesmo com a exceção
        Timer timer = meterRegistry.find("pedidos.evento.processamento").timer();
        assertNotNull(timer);
        assertEquals(1, timer.count());
    }
}
