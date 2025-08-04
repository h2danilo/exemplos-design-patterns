package com.example.pedido.application.port.out;

import com.example.pedido.domain.model.Pedido;

/**
 * Porta de saída para notificação de pedido
 */
public interface PedidoNotifierPort {
    void notificar(Pedido pedido);
}
