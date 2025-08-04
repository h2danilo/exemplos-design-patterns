package com.example.pedido.application.port.out;

import com.example.pedido.domain.model.Pedido;

/**
 * Porta de saída para atualização de estoque
 */
public interface EstoqueUpdaterPort {
    void atualizarEstoque(Pedido pedido);
}
