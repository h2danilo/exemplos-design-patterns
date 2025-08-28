package com.example.pedido.infrastructure.adapter.out;

import com.example.pedido.application.port.out.MeasurableOperation;
import com.example.pedido.application.port.out.MetricsPort;
import com.example.pedido.domain.evento.PedidoCriadoEvent;
import com.example.pedido.domain.model.Pedido;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

/**
 * Adaptador para coleta de métricas usando Micrometer
 */
@Component
public class MicrometerMetricsAdapter implements MetricsPort {

    private final MeterRegistry meterRegistry;
    private final Counter pedidosCriadosCounter;
    private final Timer pedidoProcessamentoTimer;
    private final DistributionSummary pedidoValorSummary;

    public MicrometerMetricsAdapter(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Inicialização das métricas
        this.pedidosCriadosCounter = Counter.builder("pedidos.criados")
                .description("Total de pedidos criados")
                .register(meterRegistry);

        this.pedidoProcessamentoTimer = Timer.builder("pedidos.processamento.tempo")
                .description("Tempo de processamento de pedidos")
                .register(meterRegistry);

        this.pedidoValorSummary = DistributionSummary.builder("pedidos.valor")
                .description("Distribuição dos valores dos pedidos")
                .baseUnit("BRL")
                .register(meterRegistry);
    }

    @Override
    public void registrarPedidoCriado(Pedido pedido) {
        // Registra a métrica de pedido criado
        pedidosCriadosCounter.increment();

        // Registra o valor do pedido
        pedidoValorSummary.record(pedido.getValor().doubleValue());

        // Registra o tipo de cliente como tag para análise detalhada
        meterRegistry.counter("pedidos.por.tipo.cliente", "tipoCliente", pedido.getTipoCliente().name())
                .increment();
    }

    @Override
    public Object iniciarProcessamentoEvento(PedidoCriadoEvent evento) {
        return Timer.start(meterRegistry);
    }

    @Override
    public void finalizarProcessamentoEvento(Object timerContext) {
        if (timerContext instanceof Timer.Sample) {
            ((Timer.Sample) timerContext).stop(pedidoProcessamentoTimer);
        }
    }

    @Override
    public Object iniciarAtualizacaoEstoque() {
        return Timer.start(meterRegistry);
    }

    @Override
    public void finalizarAtualizacaoEstoque(Object timerContext) {
        if (timerContext instanceof Timer.Sample) {
            ((Timer.Sample) timerContext).stop(meterRegistry.timer("pedidos.estoque.atualizacao"));
        }
    }

    @Override
    public Object iniciarGeracaoNotaFiscal() {
        return Timer.start(meterRegistry);
    }

    @Override
    public void finalizarGeracaoNotaFiscal(Object timerContext) {
        if (timerContext instanceof Timer.Sample) {
            ((Timer.Sample) timerContext).stop(meterRegistry.timer("pedidos.notafiscal.geracao"));
        }
    }

    @Override
    public Object iniciarEnvioNotificacoes() {
        return Timer.start(meterRegistry);
    }

    @Override
    public void finalizarEnvioNotificacoes(Object timerContext) {
        if (timerContext instanceof Timer.Sample) {
            ((Timer.Sample) timerContext).stop(meterRegistry.timer("pedidos.notificacao.total"));
        }
    }

    @Override
    public Object iniciarEnvioNotificacao(String tipoNotificador) {
        return Timer.start(meterRegistry);
    }

    @Override
    public void finalizarEnvioNotificacao(Object timerContext, String tipoNotificador) {
        if (timerContext instanceof Timer.Sample) {
            ((Timer.Sample) timerContext).stop(meterRegistry.timer("pedidos.notificacao", 
                "tipo", tipoNotificador));
        }
    }

    @Override
    public void registrarEventoProcessado() {
        meterRegistry.counter("pedidos.eventos.processados").increment();
    }

    @Override
    @Timed(value = "pedidos.criacao.tempo", description = "Tempo para criar um pedido")
    public <T> T medirTempoCriacaoPedido(MeasurableOperation<T> codigo) {
        Timer.Sample sample = Timer.start(meterRegistry);
        try {
            return codigo.execute();
        } finally {
            sample.stop(meterRegistry.timer("pedidos.criacao.tempo"));
        }
    }

    @Override
    @Timed(value = "pedidos.evento.processamento", description = "Tempo para processar evento de pedido")
    public <T> T medirTempoProcessamentoEvento(MeasurableOperation<T> codigo) {
        Timer.Sample sample = Timer.start(meterRegistry);
        try {
            return codigo.execute();
        } finally {
            sample.stop(meterRegistry.timer("pedidos.evento.processamento"));
        }
    }
}
