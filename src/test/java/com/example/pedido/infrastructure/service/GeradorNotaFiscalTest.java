package com.example.pedido.infrastructure.service;

import com.example.pedido.domain.Pedido;
import com.example.pedido.domain.TipoCliente;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testes unitários para o gerador de nota fiscal.
 */
public class GeradorNotaFiscalTest {

    @Test
    void deveGerarNotaFiscalAoNotificar() {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        GeradorNotaFiscal gerador = new GeradorNotaFiscal();
        Pedido pedido = new Pedido(1L, new BigDecimal("100.00"), TipoCliente.VIP);
        
        // Act
        gerador.notificar(pedido);
        
        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("[NF] Emitindo nota fiscal para o pedido 1"));
        
        // Restaura System.out
        System.setOut(System.out);
    }
    
    @Test
    void deveGerarNotaFiscalParaPedidoComValorZero() {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        GeradorNotaFiscal gerador = new GeradorNotaFiscal();
        Pedido pedido = new Pedido(2L, BigDecimal.ZERO, TipoCliente.PADRAO);
        
        // Act
        gerador.notificar(pedido);
        
        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("[NF] Emitindo nota fiscal para o pedido 2"));
        
        // Restaura System.out
        System.setOut(System.out);
    }
}