package com.example.pedido.domain.relatorio;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testes unitários para a classe RelatorioComRodape.
 */
public class RelatorioComRodapeTest {

    @Test
    void deveAdicionarRodapeAoRelatorio() {
        // Arrange
        RelatorioBase relatorioBase = new RelatorioBase();
        RelatorioComRodape relatorioComRodape = new RelatorioComRodape(relatorioBase);
        
        // Act
        String resultado = relatorioComRodape.gerar();
        
        // Assert
        assertTrue(resultado.contains("Relatório base do pedido."));
        assertTrue(resultado.contains("Rodapé: confidencial"));
        assertEquals("Relatório base do pedido.\nRodapé: confidencial", resultado);
    }
    
    @Test
    void deveDecorarQualquerImplementacaoDeRelatorio() {
        // Arrange
        Relatorio relatorioCustomizado = new Relatorio() {
            @Override
            public String gerar() {
                return "Relatório customizado";
            }
        };
        
        RelatorioComRodape relatorioComRodape = new RelatorioComRodape(relatorioCustomizado);
        
        // Act
        String resultado = relatorioComRodape.gerar();
        
        // Assert
        assertTrue(resultado.contains("Relatório customizado"));
        assertTrue(resultado.contains("Rodapé: confidencial"));
        assertEquals("Relatório customizado\nRodapé: confidencial", resultado);
    }
}