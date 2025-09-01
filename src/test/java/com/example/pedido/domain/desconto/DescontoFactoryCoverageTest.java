package com.example.pedido.domain.desconto;

import com.example.pedido.domain.model.TipoCliente;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Testes unitários para garantir cobertura completa do DescontoFactory.
 * Foco específico no switch statement do método getStrategy.
 */
public class DescontoFactoryCoverageTest {

    @Test
    void deveRetornarDescontoClienteVIP() {
        // Arrange
        TipoCliente tipoCliente = TipoCliente.VIP;
        
        // Act
        DescontoStrategy strategy = DescontoFactory.getStrategy(tipoCliente);
        
        // Assert
        assertNotNull(strategy);
        assertEquals(DescontoClienteVIP.class, strategy.getClass());
    }
    
    @Test
    void deveRetornarDescontoClientePadrao() {
        // Arrange
        TipoCliente tipoCliente = TipoCliente.PADRAO;
        
        // Act
        DescontoStrategy strategy = DescontoFactory.getStrategy(tipoCliente);
        
        // Assert
        assertNotNull(strategy);
        assertEquals(DescontoClientePadrao.class, strategy.getClass());
    }
    
    /**
     * Teste parametrizado que verifica todos os valores do enum TipoCliente.
     * Garante que o switch statement lida com todos os casos possíveis.
     */
    @ParameterizedTest
    @EnumSource(TipoCliente.class)
    void deveRetornarEstrategiaParaTodosTiposCliente(TipoCliente tipoCliente) {
        // Act
        DescontoStrategy strategy = DescontoFactory.getStrategy(tipoCliente);
        
        // Assert
        assertNotNull(strategy);
        
        // Verifica se a estratégia retornada é a correta para o tipo de cliente
        switch (tipoCliente) {
            case VIP -> assertEquals(DescontoClienteVIP.class, strategy.getClass());
            case PADRAO -> assertEquals(DescontoClientePadrao.class, strategy.getClass());
        }
    }
}