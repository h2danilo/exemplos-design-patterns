
package com.example.pedido.infrastructure.service;

import com.example.pedido.domain.Pedido;
import com.example.pedido.domain.evento.PedidoObserver;
import com.example.pedido.infrastructure.config.AppProperties;
import org.springframework.stereotype.Component;

@Component
public class NotificadorSlack implements PedidoObserver {
    private final AppProperties properties;

    public NotificadorSlack(AppProperties properties) {
        this.properties = properties;
    }

    @Override
    public void notificar(Pedido pedido) {
        if (properties.isProducao()) {
            System.out.println("[Slack] Enviando alerta real para pedido " + pedido.getId());
        } else {
            System.out.println("[Sandbox] Simulando alerta Slack para pedido " + pedido.getId());
        }
    }
}
