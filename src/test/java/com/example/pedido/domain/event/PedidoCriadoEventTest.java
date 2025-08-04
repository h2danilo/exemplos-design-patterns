package com.example.pedido.domain.event;

import com.example.pedido.domain.model.Pedido;
import com.example.pedido.domain.model.TipoCliente;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Testes unitários para o evento de criação de pedido.
 */
public class PedidoCriadoEventTest {

    @Test
    void deveArmazenarPedidoNoEvento() {
        // Arrange
        Pedido pedido = new Pedido(1L, new BigDecimal("100.00"), TipoCliente.VIP);
        
        // Act
        PedidoCriadoEvent evento = new PedidoCriadoEvent(pedido);
        
        // Assert
        assertNotNull(evento.getPedido());
        assertEquals(pedido, evento.getPedido());
        assertEquals(1L, evento.getPedido().getId());
        assertEquals(new BigDecimal("100.00"), evento.getPedido().getValor());
        assertEquals(TipoCliente.VIP, evento.getPedido().getTipoCliente());
    }
    
    @Test
    void deveManterReferenciaDoPedido() {
        // Arrange
        Pedido pedido = new Pedido(1L, new BigDecimal("100.00"), TipoCliente.VIP);
        PedidoCriadoEvent evento = new PedidoCriadoEvent(pedido);
        
        // Act
        pedido.setValor(new BigDecimal("200.00"));
        
        // Assert - verifica que a alteração no pedido original reflete no evento
        assertEquals(new BigDecimal("200.00"), evento.getPedido().getValor());
    }
}