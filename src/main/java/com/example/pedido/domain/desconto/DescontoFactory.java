
package com.example.pedido.domain.desconto;

import com.example.pedido.domain.model.TipoCliente;

public class DescontoFactory {
    public static DescontoStrategy getStrategy(TipoCliente tipo) {
        return switch (tipo) {
            case VIP -> new DescontoClienteVIP();
            case PADRAO -> new DescontoClientePadrao();
        };
    }
}
