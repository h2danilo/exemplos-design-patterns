package com.example.pedido.domain.desconto;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testes unitários para a estratégia de desconto de clientes padrão.
 */
public class DescontoClientePadraoTest {

    @Test
    void deveManterValorOriginalSemDesconto() {
        DescontoClientePadrao desconto = new DescontoClientePadrao();
        
        BigDecimal valorOriginal = new BigDecimal("100.00");
        BigDecimal valorComDesconto = desconto.aplicarDesconto(valorOriginal);
        
        assertEquals(valorOriginal, valorComDesconto);
    }
    
    @Test
    void deveManterValorZero() {
        DescontoClientePadrao desconto = new DescontoClientePadrao();
        
        BigDecimal valorOriginal = BigDecimal.ZERO;
        BigDecimal valorComDesconto = desconto.aplicarDesconto(valorOriginal);
        
        assertEquals(BigDecimal.ZERO, valorComDesconto);
    }
    
    @Test
    void deveManterValorNegativo() {
        DescontoClientePadrao desconto = new DescontoClientePadrao();
        
        BigDecimal valorOriginal = new BigDecimal("-100.00");
        BigDecimal valorComDesconto = desconto.aplicarDesconto(valorOriginal);
        
        assertEquals(valorOriginal, valorComDesconto);
    }
}