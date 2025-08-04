package com.example.pedido.domain.event;

import com.example.pedido.domain.model.Pedido;

/**
 * Evento de domínio que representa a criação de um pedido
 */
public class PedidoCriadoEvent {
    private final Pedido pedido;

    public PedidoCriadoEvent(Pedido pedido) {
        this.pedido = pedido;
    }

    public Pedido getPedido() {
        return pedido;
    }
}
