package com.example.pedido.infrastructure.adapter.out;

import com.example.pedido.application.port.out.PedidoNotifierPort;
import com.example.pedido.domain.model.Pedido;
import org.springframework.stereotype.Component;

/**
 * Adaptador para notificação via Slack
 */
@Component
public class NotificadorSlackAdapter implements PedidoNotifierPort {

    @Override
    public void notificar(Pedido pedido) {
        System.out.println("[Slack] Enviando notificação no canal #pedidos: Novo pedido " + 
                pedido.getId() + " criado com sucesso!");
    }
}
