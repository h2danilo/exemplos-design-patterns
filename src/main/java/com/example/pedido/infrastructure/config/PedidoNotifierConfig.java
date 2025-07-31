package com.example.pedido.infrastructure.config;

import com.example.pedido.domain.evento.PedidoNotifier;
import com.example.pedido.infrastructure.service.AtualizadorEstoque;
import com.example.pedido.infrastructure.service.GeradorNotaFiscal;
import com.example.pedido.infrastructure.service.NotificadorEmail;
import com.example.pedido.infrastructure.service.NotificadorSlack;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PedidoNotifierConfig {

    @Bean
    public PedidoNotifier pedidoNotifier(
            NotificadorEmail notificadorEmail,
            NotificadorSlack notificadorSlack,
            AtualizadorEstoque atualizadorEstoque,
            GeradorNotaFiscal geradorNotaFiscal
    ) {
        PedidoNotifier notifier = new PedidoNotifier();
        notifier.adicionar(notificadorEmail);
        notifier.adicionar(notificadorSlack);
        notifier.adicionar(atualizadorEstoque);
        notifier.adicionar(geradorNotaFiscal);
        return notifier;
    }
}