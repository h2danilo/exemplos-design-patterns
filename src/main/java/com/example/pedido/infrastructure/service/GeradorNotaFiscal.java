
package com.example.pedido.infrastructure.service;

import com.example.pedido.domain.Pedido;
import com.example.pedido.domain.evento.PedidoObserver;
import org.springframework.stereotype.Component;

@Component
public class GeradorNotaFiscal implements PedidoObserver {
    @Override
    public void notificar(Pedido pedido) {
        System.out.println("[NF] Emitindo nota fiscal para o pedido " + pedido.getId());
    }
}
