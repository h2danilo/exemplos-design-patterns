
package com.example.pedido.domain.desconto;

import java.math.BigDecimal;

public interface DescontoStrategy {
    BigDecimal aplicarDesconto(BigDecimal valor);
}
