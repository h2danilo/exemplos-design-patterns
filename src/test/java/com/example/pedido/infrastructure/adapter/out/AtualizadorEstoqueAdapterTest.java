package com.example.pedido.infrastructure.adapter.out;

import com.example.pedido.domain.model.Pedido;
import com.example.pedido.domain.model.TipoCliente;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testes unitários para o adaptador de atualização de estoque.
 */
public class AtualizadorEstoqueAdapterTest {

    @Test
    void deveAtualizarEstoqueAoExecutar() {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        AtualizadorEstoqueAdapter atualizador = new AtualizadorEstoqueAdapter();
        Pedido pedido = new Pedido(1L, new BigDecimal("100.00"), TipoCliente.VIP);
        
        // Act
        atualizador.atualizarEstoque(pedido);
        
        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("[Estoque] Atualizando estoque para o pedido 1"));
        
        // Restaura System.out
        System.setOut(System.out);
    }
    
    @Test
    void deveAtualizarEstoqueParaPedidoComValorZero() {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        AtualizadorEstoqueAdapter atualizador = new AtualizadorEstoqueAdapter();
        Pedido pedido = new Pedido(2L, BigDecimal.ZERO, TipoCliente.PADRAO);
        
        // Act
        atualizador.atualizarEstoque(pedido);
        
        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("[Estoque] Atualizando estoque para o pedido 2"));
        
        // Restaura System.out
        System.setOut(System.out);
    }
}