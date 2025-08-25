package com.example.pedido.infrastructure.adapter.out;

import com.example.pedido.application.port.out.PedidoNotifierPort;
import com.example.pedido.domain.evento.PedidoCriadoEvent;
import org.springframework.stereotype.Component;

/**
 * Adaptador para notificação via Slack
 */
@Component
public class NotificadorSlackAdapter implements PedidoNotifierPort {

    @Override
    public void notificarPedidoCriado(PedidoCriadoEvent evento) {
        System.out.println("[Slack] Enviando notificação no canal #pedidos: Novo pedido " + 
                evento.getPedido().getId() + " criado com sucesso!");
    }
}
