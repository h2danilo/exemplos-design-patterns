package com.example.pedido.application.port.out;

import com.example.pedido.domain.evento.PedidoCriadoEvent;

/**
 * Porta de saída para notificação de eventos relacionados a pedidos.
 * Esta interface define o contrato para adaptadores que enviam notificações
 * quando ocorrem eventos relacionados a pedidos.
 */
public interface PedidoNotifierPort {
    
    /**
     * Notifica que um pedido foi criado.
     *
     * @param evento O evento de pedido criado
     */
    void notificarPedidoCriado(PedidoCriadoEvent evento);
}