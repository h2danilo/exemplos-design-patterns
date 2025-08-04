package com.example.pedido.infrastructure.adapter.out;

import com.example.pedido.application.port.out.EstoqueUpdaterPort;
import com.example.pedido.domain.model.Pedido;
import org.springframework.stereotype.Component;

/**
 * Adaptador para atualização de estoque
 */
@Component
public class AtualizadorEstoqueAdapter implements EstoqueUpdaterPort {

    @Override
    public void atualizarEstoque(Pedido pedido) {
        System.out.println("[Estoque] Atualizando estoque para o pedido " + pedido.getId());
    }
}
