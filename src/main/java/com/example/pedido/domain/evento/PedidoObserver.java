
package com.example.pedido.domain.evento;

import com.example.pedido.domain.Pedido;

public interface PedidoObserver {
    void notificar(Pedido pedido);
}
