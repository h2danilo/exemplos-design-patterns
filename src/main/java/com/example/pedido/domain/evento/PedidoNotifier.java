
package com.example.pedido.domain.evento;

import com.example.pedido.domain.Pedido;
import java.util.ArrayList;
import java.util.List;

public class PedidoNotifier {
    private final List<PedidoObserver> observers = new ArrayList<>();

    public void adicionar(PedidoObserver observer) {
        observers.add(observer);
    }

    public void notificarTodos(Pedido pedido) {
        observers.forEach(obs -> obs.notificar(pedido));
    }
}
