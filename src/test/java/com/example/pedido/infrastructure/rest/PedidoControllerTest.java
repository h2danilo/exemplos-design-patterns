package com.example.pedido.infrastructure.rest;

import com.example.pedido.domain.Pedido;
import com.example.pedido.domain.TipoCliente;
import com.example.pedido.domain.evento.PedidoNotifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Testes unitários para o controlador REST de pedidos.
 */
public class PedidoControllerTest {

    private PedidoController controller;
    private PedidoNotifier notifier;

    @BeforeEach
    void setUp() {
        notifier = new PedidoNotifier();
        controller = new PedidoController(notifier);
    }

    @Test
    void deveCriarPedidoViaEndpoint() {
        // Arrange
        Pedido pedidoRequest = new Pedido(1L, new BigDecimal("100.00"), TipoCliente.VIP);
        
        // Act
        ResponseEntity<Pedido> response = controller.criarPedido(pedidoRequest);
        
        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        
        Pedido pedidoCriado = response.getBody();
        assertNotNull(pedidoCriado);
        assertEquals(1L, pedidoCriado.getId());
        assertEquals(new BigDecimal("90.00").setScale(2), pedidoCriado.getValor().setScale(2));
        assertEquals(TipoCliente.VIP, pedidoCriado.getTipoCliente());
    }
    
    @Test
    void deveCriarPedidoExemplo() {
        // Act
        ResponseEntity<Pedido> response = controller.exemploCriacaoPedido();
        
        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        
        Pedido pedidoCriado = response.getBody();
        assertNotNull(pedidoCriado);
        assertEquals(99L, pedidoCriado.getId());
        assertEquals(new BigDecimal("180.00").setScale(2), pedidoCriado.getValor().setScale(2));
        assertEquals(TipoCliente.VIP, pedidoCriado.getTipoCliente());
    }
    
    @Test
    void deveCriarPedidoClientePadrao() {
        // Arrange
        Pedido pedidoRequest = new Pedido(2L, new BigDecimal("100.00"), TipoCliente.PADRAO);
        
        // Act
        ResponseEntity<Pedido> response = controller.criarPedido(pedidoRequest);
        
        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        
        Pedido pedidoCriado = response.getBody();
        assertNotNull(pedidoCriado);
        assertEquals(2L, pedidoCriado.getId());
        assertEquals(new BigDecimal("100.00").setScale(2), pedidoCriado.getValor().setScale(2));
        assertEquals(TipoCliente.PADRAO, pedidoCriado.getTipoCliente());
    }
}