
package com.example.pedido.infrastructure.service;

import com.example.pedido.domain.Pedido;
import com.example.pedido.domain.evento.PedidoObserver;
import com.example.pedido.infrastructure.config.AppProperties;
import org.springframework.stereotype.Component;

@Component
public class NotificadorEmail implements PedidoObserver {

    private final AppProperties properties;

    public NotificadorEmail(AppProperties properties) {
        this.properties = properties;
    }

    @Override
    public void notificar(Pedido pedido) {
        if (properties.isProducao()) {
            System.out.println("[Email] Enviando email para pedido " + pedido.getId());
        } else {
            System.out.println("[Sandbox] Simulando envio de e-mail para pedido " + pedido.getId());
        }
    }
}
