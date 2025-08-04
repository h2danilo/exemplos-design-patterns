package com.example.pedido.infrastructure.adapter.out;

import com.example.pedido.domain.model.Pedido;
import com.example.pedido.domain.model.TipoCliente;
import com.example.pedido.infrastructure.config.AppProperties;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testes unitários para o adaptador de notificação por email.
 */
public class NotificadorEmailAdapterTest {

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
        
        NotificadorEmailAdapter notificador = new NotificadorEmailAdapter();
        Pedido pedido = new Pedido(1L, new BigDecimal("100.00"), TipoCliente.VIP);
        
        // Act
        notificador.notificar(pedido);
        
        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("[Email] Enviando email de confirmação para o pedido 1"));
        
        // Restaura System.out
        System.setOut(System.out);
    }
    
    @Test
    void deveNotificarPedidoComValorZero() {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        NotificadorEmailAdapter notificador = new NotificadorEmailAdapter();
        Pedido pedido = new Pedido(2L, BigDecimal.ZERO, TipoCliente.PADRAO);
        
        // Act
        notificador.notificar(pedido);
        
        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("[Email] Enviando email de confirmação para o pedido 2"));
        
        // Restaura System.out
        System.setOut(System.out);
    }
}