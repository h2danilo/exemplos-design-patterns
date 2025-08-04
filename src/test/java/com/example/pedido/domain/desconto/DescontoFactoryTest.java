package com.example.pedido.domain.desconto;

import com.example.pedido.domain.model.TipoCliente;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testes unitários para a fábrica de estratégias de desconto.
 */
public class DescontoFactoryTest {

    @Test
    void deveRetornarDescontoClienteVIP() {
        // Arrange
        TipoCliente tipoCliente = TipoCliente.VIP;
        
        // Act
        DescontoStrategy strategy = DescontoFactory.getStrategy(tipoCliente);
        
        // Assert
        assertEquals(DescontoClienteVIP.class, strategy.getClass());
        
        // Verifica se o desconto é aplicado corretamente
        BigDecimal valorOriginal = new BigDecimal("100.00");
        BigDecimal valorComDesconto = strategy.aplicarDesconto(valorOriginal);
        assertEquals(new BigDecimal("90.00").setScale(2), valorComDesconto.setScale(2));
    }
    
    @Test
    void deveRetornarDescontoClientePadrao() {
        // Arrange
        TipoCliente tipoCliente = TipoCliente.PADRAO;
        
        // Act
        DescontoStrategy strategy = DescontoFactory.getStrategy(tipoCliente);
        
        // Assert
        assertEquals(DescontoClientePadrao.class, strategy.getClass());
        
        // Verifica se o desconto é aplicado corretamente (nenhum desconto)
        BigDecimal valorOriginal = new BigDecimal("100.00");
        BigDecimal valorComDesconto = strategy.aplicarDesconto(valorOriginal);
        assertEquals(new BigDecimal("100.00").setScale(2), valorComDesconto.setScale(2));
    }
}