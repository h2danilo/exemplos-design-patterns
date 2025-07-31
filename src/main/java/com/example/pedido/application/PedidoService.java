
package com.example.pedido.application;

import com.example.pedido.domain.Pedido;
import com.example.pedido.domain.desconto.DescontoFactory;
import com.example.pedido.domain.evento.PedidoNotifier;

/**
 * Camada de aplicação responsável pela orquestração de regras de negócio
 * Aplica o padrão Strategy via Factory e dispara notificações via Observer
 */
public class PedidoService {

    private final PedidoNotifier notifier;

    public PedidoService(PedidoNotifier notifier) {
        this.notifier = notifier;
    }

    /**
     * Cria um novo pedido aplicando o desconto correto e notificando os serviços
     * @param pedido Pedido a ser processado
     * @return Pedido com valor atualizado
     */
    public Pedido criarPedido(Pedido pedido) {
        // Obtem a estratégia de desconto baseada no tipo de cliente
        var strategy = DescontoFactory.getStrategy(pedido.getTipoCliente());

        // Aplica o desconto ao valor
        pedido.setValor(strategy.aplicarDesconto(pedido.getValor()));

        // Notifica os observadores (e-mail, estoque, nota fiscal, etc)
        notifier.notificarTodos(pedido);

        return pedido;
    }
}
