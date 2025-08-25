package com.example.pedido.infrastructure.adapter.out;

import com.example.pedido.application.port.out.PedidoNotifierPort;
import com.example.pedido.domain.evento.PedidoCriadoEvent;
import org.springframework.stereotype.Component;

/**
 * Adaptador para notificação por email
 */
@Component
public class NotificadorEmailAdapter implements PedidoNotifierPort {

    @Override
    public void notificarPedidoCriado(PedidoCriadoEvent evento) {
        System.out.println("[Email] Enviando email de confirmação para o pedido " + evento.getPedido().getId());
    }
}
