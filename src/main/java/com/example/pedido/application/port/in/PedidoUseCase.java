package com.example.pedido.application.port.in;

import com.example.pedido.domain.model.Pedido;

/**
 * Porta de entrada para o caso de uso de criação de pedido
 */
public interface PedidoUseCase {

    /**
     * Cria um novo pedido aplicando o desconto correto e notificando os serviços
     * @param pedido Pedido a ser processado
     * @return Pedido com valor atualizado
     */
    Pedido criarPedido(Pedido pedido);
}
