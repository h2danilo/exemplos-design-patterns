package com.example.pedido.domain.desconto;

import com.example.pedido.domain.TipoCliente;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testes unitários para a fábrica de estratégias de desconto.
 */
public class DescontoFactoryTest {

    @Test
    void deveRetornarEstrategiaVIPParaClienteVIP() {
        DescontoStrategy strategy = DescontoFactory.getStrategy(TipoCliente.VIP);
        
        assertTrue(strategy instanceof DescontoClienteVIP);
        
        BigDecimal valorOriginal = new BigDecimal("100.00");
        BigDecimal valorComDesconto = strategy.aplicarDesconto(valorOriginal);
        
        assertEquals(new BigDecimal("90.00").setScale(2), valorComDesconto.setScale(2));
    }
    
    @Test
    void deveRetornarEstrategiaPadraoParaClientePadrao() {
        DescontoStrategy strategy = DescontoFactory.getStrategy(TipoCliente.PADRAO);
        
        assertTrue(strategy instanceof DescontoClientePadrao);
        
        BigDecimal valorOriginal = new BigDecimal("100.00");
        BigDecimal valorComDesconto = strategy.aplicarDesconto(valorOriginal);
        
        assertEquals(valorOriginal, valorComDesconto);
    }
}