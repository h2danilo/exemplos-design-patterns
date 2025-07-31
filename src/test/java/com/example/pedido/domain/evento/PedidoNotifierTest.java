package com.example.pedido.domain.evento;

import com.example.pedido.domain.Pedido;
import com.example.pedido.domain.TipoCliente;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testes unitários para o notificador de pedidos.
 */
public class PedidoNotifierTest {

    @Test
    void deveNotificarTodosObservadores() {
        // Arrange
        PedidoNotifier notifier = new PedidoNotifier();
        ContadorNotificacoes contador = new ContadorNotificacoes();
        
        // Adiciona o mesmo observador duas vezes para verificar múltiplas notificações
        notifier.adicionar(contador);
        notifier.adicionar(contador);
        
        Pedido pedido = new Pedido(1L, new BigDecimal("100.00"), TipoCliente.VIP);
        
        // Act
        notifier.notificarTodos(pedido);
        
        // Assert
        assertEquals(2, contador.getContador());
    }
    
    @Test
    void naoDeveGerarErroQuandoListaObservadoresVazia() {
        // Arrange
        PedidoNotifier notifier = new PedidoNotifier();
        Pedido pedido = new Pedido(1L, new BigDecimal("100.00"), TipoCliente.VIP);
        
        // Act & Assert - não deve lançar exceção
        notifier.notificarTodos(pedido);
    }
    
    // Classe auxiliar para contar notificações
    private static class ContadorNotificacoes implements PedidoObserver {
        private int contador = 0;
        
        @Override
        public void notificar(Pedido pedido) {
            contador++;
        }
        
        public int getContador() {
            return contador;
        }
    }
}