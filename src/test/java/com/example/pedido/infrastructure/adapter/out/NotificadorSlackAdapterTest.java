package com.example.pedido.infrastructure.adapter.out;

import com.example.pedido.domain.evento.PedidoCriadoEvent;
import com.example.pedido.domain.model.Pedido;
import com.example.pedido.domain.model.TipoCliente;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testes unitários para o adaptador de notificação via Slack.
 */
public class NotificadorSlackAdapterTest {

    @Test
    void deveEnviarNotificacaoSlack() {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        NotificadorSlackAdapter notificador = new NotificadorSlackAdapter();
        Pedido pedido = new Pedido(1L, new BigDecimal("100.00"), TipoCliente.VIP);
        PedidoCriadoEvent evento = new PedidoCriadoEvent(pedido);

        // Act
        notificador.notificarPedidoCriado(evento);

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("[Slack] Enviando notificação no canal #pedidos"));
        assertTrue(output.contains("Novo pedido 1 criado com sucesso"));

        // Restaura System.out
        System.setOut(System.out);
    }

    @Test
    void deveNotificarPedidoComValorZero() {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        NotificadorSlackAdapter notificador = new NotificadorSlackAdapter();
        Pedido pedido = new Pedido(2L, BigDecimal.ZERO, TipoCliente.PADRAO);
        PedidoCriadoEvent evento = new PedidoCriadoEvent(pedido);

        // Act
        notificador.notificarPedidoCriado(evento);

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("[Slack] Enviando notificação no canal #pedidos"));
        assertTrue(output.contains("Novo pedido 2 criado com sucesso"));

        // Restaura System.out
        System.setOut(System.out);
    }
}
