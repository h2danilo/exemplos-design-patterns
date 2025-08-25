package com.example.pedido.domain.evento;

import com.example.pedido.domain.model.Pedido;
import com.example.pedido.domain.model.TipoCliente;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para o evento de domínio PedidoCriadoEvent.
 */
public class PedidoCriadoEventTest {

    @Test
    void deveCriarEventoComPedido() {
        // Arrange
        Pedido pedido = new Pedido(1L, new BigDecimal("100.00"), TipoCliente.VIP);
        
        // Act
        PedidoCriadoEvent evento = new PedidoCriadoEvent(pedido);
        
        // Assert
        assertNotNull(evento);
        assertEquals(pedido, evento.getPedido());
        assertNotNull(evento.getTimestamp());
        
        // Verifica se o timestamp está próximo do tempo atual (dentro de 1 segundo)
        LocalDateTime agora = LocalDateTime.now();
        long diferencaSegundos = ChronoUnit.SECONDS.between(evento.getTimestamp(), agora);
        assertTrue(Math.abs(diferencaSegundos) < 1, "O timestamp deve ser próximo ao tempo atual");
    }
    
    @Test
    void deveCriarEventoComConstrutorPadrao() {
        // Act
        PedidoCriadoEvent evento = new PedidoCriadoEvent();
        
        // Assert
        assertNotNull(evento);
        assertNull(evento.getPedido());
        assertNotNull(evento.getTimestamp());
        
        // Verifica se o timestamp está próximo do tempo atual (dentro de 1 segundo)
        LocalDateTime agora = LocalDateTime.now();
        long diferencaSegundos = ChronoUnit.SECONDS.between(evento.getTimestamp(), agora);
        assertTrue(Math.abs(diferencaSegundos) < 1, "O timestamp deve ser próximo ao tempo atual");
    }
    
    @Test
    void deveRetornarPedidoAssociadoAoEvento() {
        // Arrange
        Pedido pedido = new Pedido(1L, new BigDecimal("100.00"), TipoCliente.VIP);
        PedidoCriadoEvent evento = new PedidoCriadoEvent(pedido);
        
        // Act
        Pedido resultado = evento.getPedido();
        
        // Assert
        assertEquals(pedido, resultado);
    }
    
    @Test
    void deveRetornarTimestampDoEvento() {
        // Arrange
        PedidoCriadoEvent evento = new PedidoCriadoEvent();
        
        // Act
        LocalDateTime timestamp = evento.getTimestamp();
        
        // Assert
        assertNotNull(timestamp);
    }
    
    @Test
    void deveGerarToStringComInformacoesDoPedido() {
        // Arrange
        Pedido pedido = new Pedido(1L, new BigDecimal("100.00"), TipoCliente.VIP);
        PedidoCriadoEvent evento = new PedidoCriadoEvent(pedido);
        
        // Act
        String resultado = evento.toString();
        
        // Assert
        assertTrue(resultado.contains("pedido=" + pedido));
        assertTrue(resultado.contains("timestamp="));
    }
}