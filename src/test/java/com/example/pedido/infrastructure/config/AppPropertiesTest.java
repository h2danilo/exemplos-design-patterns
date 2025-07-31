package com.example.pedido.infrastructure.config;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testes unitários para a classe AppProperties.
 */
public class AppPropertiesTest {

    @Test
    void deveIdentificarAmbienteProducao() {
        // Arrange
        AppProperties properties = new AppProperties();
        ReflectionTestUtils.setField(properties, "ambiente", "producao");
        
        // Act & Assert
        assertTrue(properties.isProducao());
        assertEquals("producao", properties.getAmbiente());
    }
    
    @Test
    void deveIdentificarAmbienteNaoProducao() {
        // Arrange
        AppProperties properties = new AppProperties();
        ReflectionTestUtils.setField(properties, "ambiente", "desenvolvimento");
        
        // Act & Assert
        assertFalse(properties.isProducao());
        assertEquals("desenvolvimento", properties.getAmbiente());
    }
    
    @Test
    void deveIdentificarAmbienteProducaoIndependenteDeCaixa() {
        // Arrange
        AppProperties properties = new AppProperties();
        ReflectionTestUtils.setField(properties, "ambiente", "PRODUCAO");
        
        // Act & Assert
        assertTrue(properties.isProducao());
        assertEquals("PRODUCAO", properties.getAmbiente());
    }
}