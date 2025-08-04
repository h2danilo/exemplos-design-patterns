package com.example.pedido.infrastructure.adapter.out;

import com.example.pedido.application.port.out.PedidoNotifierPort;
import com.example.pedido.domain.model.Pedido;
import org.springframework.stereotype.Component;

/**
 * Adaptador para notificação por email
 */
@Component
public class NotificadorEmailAdapter implements PedidoNotifierPort {

    @Override
    public void notificar(Pedido pedido) {
        System.out.println("[Email] Enviando email de confirmação para o pedido " + pedido.getId());
    }
}
