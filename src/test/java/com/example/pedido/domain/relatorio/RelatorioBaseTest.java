package com.example.pedido.domain.relatorio;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testes unitários para a classe RelatorioBase.
 */
public class RelatorioBaseTest {

    @Test
    void deveGerarRelatorioBase() {
        // Arrange
        RelatorioBase relatorio = new RelatorioBase();
        
        // Act
        String resultado = relatorio.gerar();
        
        // Assert
        assertEquals("Relatório base do pedido.", resultado);
    }
}