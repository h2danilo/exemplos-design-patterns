
package com.example.pedido.domain.desconto;

import java.math.BigDecimal;

public class DescontoClienteVIP implements DescontoStrategy {
    public BigDecimal aplicarDesconto(BigDecimal valor) {
        return valor.multiply(BigDecimal.valueOf(0.9));
    }
}
