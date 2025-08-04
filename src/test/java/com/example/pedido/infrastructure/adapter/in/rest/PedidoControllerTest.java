package com.example.pedido.infrastructure.adapter.in.rest;

import com.example.pedido.application.port.in.PedidoUseCase;
import com.example.pedido.domain.model.Pedido;
import com.example.pedido.domain.model.TipoCliente;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Testes unitários para o adaptador de entrada REST.
 */
public class PedidoControllerTest {

    @Test
    void deveCriarPedidoViaRest() {
        // Arrange
        PedidoUseCase pedidoUseCase = mock(PedidoUseCase.class);
        Pedido pedidoMock = new Pedido(1L, new BigDecimal("90.00"), TipoCliente.VIP);
        when(pedidoUseCase.criarPedido(any(Pedido.class))).thenReturn(pedidoMock);

        PedidoController controller = new PedidoController(pedidoUseCase);
        Pedido pedidoRequest = new Pedido(1L, new BigDecimal("100.00"), TipoCliente.VIP);

        // Act
        ResponseEntity<Pedido> response = controller.criarPedido(pedidoRequest);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(pedidoMock, response.getBody());
        assertEquals(new BigDecimal("90.00"), response.getBody().getValor());
    }

    @Test
    void deveCriarPedidoExemplo() {
        // Arrange
        PedidoUseCase pedidoUseCase = mock(PedidoUseCase.class);
        Pedido pedidoMock = new Pedido(99L, new BigDecimal("180.00"), TipoCliente.VIP);
        when(pedidoUseCase.criarPedido(any(Pedido.class))).thenReturn(pedidoMock);

        PedidoController controller = new PedidoController(pedidoUseCase);

        // Act
        ResponseEntity<Pedido> response = controller.exemploCriacaoPedido();

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(pedidoMock, response.getBody());
        assertEquals(new BigDecimal("180.00"), response.getBody().getValor());
    }
}