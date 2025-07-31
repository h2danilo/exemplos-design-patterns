package com.example.pedido.domain.desconto;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testes unitários para a estratégia de desconto de clientes VIP.
 */
public class DescontoClienteVIPTest {

    @Test
    void deveAplicarDescontoDeDezPorCento() {
        DescontoClienteVIP desconto = new DescontoClienteVIP();
        
        BigDecimal valorOriginal = new BigDecimal("100.00");
        BigDecimal valorComDesconto = desconto.aplicarDesconto(valorOriginal);
        
        assertEquals(new BigDecimal("90.00").setScale(2), valorComDesconto.setScale(2));
    }
    
    @Test
    void deveAplicarDescontoEmValorZero() {
        DescontoClienteVIP desconto = new DescontoClienteVIP();
        
        BigDecimal valorOriginal = BigDecimal.ZERO;
        BigDecimal valorComDesconto = desconto.aplicarDesconto(valorOriginal);
        
        assertEquals(BigDecimal.ZERO.setScale(2), valorComDesconto.setScale(2));
    }
    
    @Test
    void deveAplicarDescontoEmValorNegativo() {
        DescontoClienteVIP desconto = new DescontoClienteVIP();
        
        BigDecimal valorOriginal = new BigDecimal("-100.00");
        BigDecimal valorComDesconto = desconto.aplicarDesconto(valorOriginal);
        
        assertEquals(new BigDecimal("-90.00").setScale(2), valorComDesconto.setScale(2));
    }
}