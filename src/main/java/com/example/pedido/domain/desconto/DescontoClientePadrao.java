
package com.example.pedido.domain.desconto;

import java.math.BigDecimal;

public class DescontoClientePadrao implements DescontoStrategy {
    public BigDecimal aplicarDesconto(BigDecimal valor) {
        return valor;
    }
}
