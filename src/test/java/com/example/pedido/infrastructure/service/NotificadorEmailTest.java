package com.example.pedido.infrastructure.service;

import com.example.pedido.domain.Pedido;
import com.example.pedido.domain.TipoCliente;
import com.example.pedido.infrastructure.config.AppProperties;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testes unitários para o notificador de email.
 */
public class NotificadorEmailTest {

    @Test
    void deveEnviarEmailEmAmbienteProducao() {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        AppProperties propertiesProducao = new AppProperties() {
            @Override
            public boolean isProducao() {
                return true;
            }
            
            @Override
            public String getAmbiente() {
                return "producao";
            }
        };
        
        NotificadorEmail notificador = new NotificadorEmail(propertiesProducao);
        Pedido pedido = new Pedido(1L, new BigDecimal("100.00"), TipoCliente.VIP);
        
        // Act
        notificador.notificar(pedido);
        
        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("[Email] Enviando email para pedido 1"));
        
        // Restaura System.out
        System.setOut(System.out);
    }
    
    @Test
    void deveSimularEnvioEmailEmAmbienteNaoProducao() {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        AppProperties propertiesDesenvolvimento = new AppProperties() {
            @Override
            public boolean isProducao() {
                return false;
            }
            
            @Override
            public String getAmbiente() {
                return "desenvolvimento";
            }
        };
        
        NotificadorEmail notificador = new NotificadorEmail(propertiesDesenvolvimento);
        Pedido pedido = new Pedido(2L, new BigDecimal("200.00"), TipoCliente.PADRAO);
        
        // Act
        notificador.notificar(pedido);
        
        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("[Sandbox] Simulando envio de e-mail para pedido 2"));
        
        // Restaura System.out
        System.setOut(System.out);
    }
}