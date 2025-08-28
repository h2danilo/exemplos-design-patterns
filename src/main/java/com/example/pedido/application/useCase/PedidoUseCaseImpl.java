package com.example.pedido.application.useCase;

import com.example.pedido.application.port.in.PedidoUseCase;
import com.example.pedido.application.port.out.EstoqueUpdaterPort;
import com.example.pedido.application.port.out.MetricsPort;
import com.example.pedido.application.port.out.NotaFiscalGeneratorPort;
import com.example.pedido.application.port.out.PedidoNotifierPort;
import com.example.pedido.domain.model.Pedido;
import com.example.pedido.domain.desconto.DescontoFactory;
import com.example.pedido.domain.evento.PedidoCriadoEvent;

import java.util.List;

/**
 * Camada de aplicação responsável pela orquestração de regras de negócio
 * Aplica o padrão Strategy via Factory e dispara eventos de domínio
 */
public class PedidoUseCaseImpl implements PedidoUseCase {

    private final List<PedidoNotifierPort> notificadores;
    private final EstoqueUpdaterPort estoqueUpdater;
    private final NotaFiscalGeneratorPort notaFiscalGenerator;
    private final MetricsPort metricsPort;

    public PedidoUseCaseImpl(
            List<PedidoNotifierPort> notificadores,
            EstoqueUpdaterPort estoqueUpdater,
            NotaFiscalGeneratorPort notaFiscalGenerator,
            MetricsPort metricsPort) {
        this.notificadores = notificadores;
        this.estoqueUpdater = estoqueUpdater;
        this.notaFiscalGenerator = notaFiscalGenerator;
        this.metricsPort = metricsPort;
    }

    /**
     * Cria um novo pedido aplicando o desconto correto e notificando os serviços
     * @param pedido Pedido a ser processado
     * @return Pedido com valor atualizado
     */
    @Override
    public Pedido criarPedido(Pedido pedido) {
        return metricsPort.medirTempoCriacaoPedido(() -> {
            // Registra métricas do pedido
            metricsPort.registrarPedidoCriado(pedido);

            // Obtem a estratégia de desconto baseada no tipo de cliente
            var strategy = DescontoFactory.getStrategy(pedido.getTipoCliente());

            // Aplica o desconto ao valor
            pedido.setValor(strategy.aplicarDesconto(pedido.getValor()));

            // Cria e processa o evento de domínio
            PedidoCriadoEvent evento = new PedidoCriadoEvent(pedido);

            // Processa o evento (já com medição de tempo interna)
            processarEvento(evento);

            return pedido;
        });
    }

    private void processarEvento(PedidoCriadoEvent evento) {
        metricsPort.medirTempoProcessamentoEvento(() -> {
            Pedido pedido = evento.getPedido();

            // Atualiza o estoque
            Object estoqueTimer = metricsPort.iniciarAtualizacaoEstoque();
            estoqueUpdater.atualizarEstoque(pedido);
            metricsPort.finalizarAtualizacaoEstoque(estoqueTimer);

            // Gera a nota fiscal
            Object notaFiscalTimer = metricsPort.iniciarGeracaoNotaFiscal();
            notaFiscalGenerator.gerarNota(pedido);
            metricsPort.finalizarGeracaoNotaFiscal(notaFiscalTimer);

            // Notifica por diferentes canais
            Object notificacaoTimer = metricsPort.iniciarEnvioNotificacoes();
            notificadores.forEach(notificador -> {
                Object notificadorTimer = metricsPort.iniciarEnvioNotificacao(notificador.getClass().getSimpleName());
                notificador.notificarPedidoCriado(evento);
                metricsPort.finalizarEnvioNotificacao(notificadorTimer, notificador.getClass().getSimpleName());
            });
            metricsPort.finalizarEnvioNotificacoes(notificacaoTimer);

            // Incrementa contador de eventos processados
            metricsPort.registrarEventoProcessado();

            return null; // Void equivalent for the functional interface
        });
    }
}
