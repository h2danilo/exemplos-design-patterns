package com.example.pedido.application.useCase;

import com.example.pedido.application.port.in.PedidoUseCase;
import com.example.pedido.application.port.out.EstoqueUpdaterPort;
import com.example.pedido.application.port.out.NotaFiscalGeneratorPort;
import com.example.pedido.application.port.out.PedidoNotifierPort;
import com.example.pedido.domain.model.Pedido;
import com.example.pedido.domain.desconto.DescontoFactory;
import com.example.pedido.domain.evento.PedidoCriadoEvent;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Camada de aplicação responsável pela orquestração de regras de negócio
 * Aplica o padrão Strategy via Factory e dispara eventos de domínio
 */
@Service
public class PedidoUseCaseImpl implements PedidoUseCase {

    private final List<PedidoNotifierPort> notificadores;
    private final EstoqueUpdaterPort estoqueUpdater;
    private final NotaFiscalGeneratorPort notaFiscalGenerator;

    public PedidoUseCaseImpl(
            List<PedidoNotifierPort> notificadores,
            EstoqueUpdaterPort estoqueUpdater,
            NotaFiscalGeneratorPort notaFiscalGenerator) {
        this.notificadores = notificadores;
        this.estoqueUpdater = estoqueUpdater;
        this.notaFiscalGenerator = notaFiscalGenerator;
    }

    /**
     * Cria um novo pedido aplicando o desconto correto e notificando os serviços
     * @param pedido Pedido a ser processado
     * @return Pedido com valor atualizado
     */
    @Override
    public Pedido criarPedido(Pedido pedido) {
        // Obtem a estratégia de desconto baseada no tipo de cliente
        var strategy = DescontoFactory.getStrategy(pedido.getTipoCliente());

        // Aplica o desconto ao valor
        pedido.setValor(strategy.aplicarDesconto(pedido.getValor()));

        // Cria e processa o evento de domínio
        PedidoCriadoEvent evento = new PedidoCriadoEvent(pedido);
        processarEvento(evento);

        return pedido;
    }

    private void processarEvento(PedidoCriadoEvent evento) {
        Pedido pedido = evento.getPedido();

        // Atualiza o estoque
        estoqueUpdater.atualizarEstoque(pedido);

        // Gera a nota fiscal
        notaFiscalGenerator.gerarNota(pedido);

        // Notifica por diferentes canais
        notificadores.forEach(notificador -> notificador.notificarPedidoCriado(evento));
    }
}