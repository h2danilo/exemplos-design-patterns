package com.example.pedido.infrastructure.config;

import com.example.pedido.domain.Pedido;
import com.example.pedido.domain.TipoCliente;
import com.example.pedido.domain.evento.PedidoNotifier;
import com.example.pedido.domain.evento.PedidoObserver;
import com.example.pedido.infrastructure.service.AtualizadorEstoque;
import com.example.pedido.infrastructure.service.GeradorNotaFiscal;
import com.example.pedido.infrastructure.service.NotificadorEmail;
import com.example.pedido.infrastructure.service.NotificadorSlack;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testes unitários para a classe PedidoNotifierConfig.
 * Não usa Mockito para garantir compatibilidade com Java 24+.
 */
public class PedidoNotifierConfigTest {

    @Test
    void deveCriarPedidoNotifierComTodosObservadores() {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        AppProperties props = new AppProperties() {
            @Override
            public boolean isProducao() {
                return true;
            }

            @Override
            public String getAmbiente() {
                return "producao";
            }
        };

        NotificadorEmail notificadorEmail = new NotificadorEmail(props);
        NotificadorSlack notificadorSlack = new NotificadorSlack(props);
        AtualizadorEstoque atualizadorEstoque = new AtualizadorEstoque();
        GeradorNotaFiscal geradorNotaFiscal = new GeradorNotaFiscal();

        PedidoNotifierConfig config = new PedidoNotifierConfig();

        // Act
        PedidoNotifier notifier = config.pedidoNotifier(
                notificadorEmail,
                notificadorSlack,
                atualizadorEstoque,
                geradorNotaFiscal
        );

        // Assert
        assertNotNull(notifier);

        // Verificar se todos os observadores são notificados
        Pedido pedido = new Pedido(1L, new BigDecimal("100.00"), TipoCliente.VIP);
        notifier.notificarTodos(pedido);

        // Restaura System.out
        System.setOut(originalOut);

        String output = outputStream.toString();
        assertTrue(output.contains("[Email] Enviando email para pedido 1"));
        assertTrue(output.contains("[Slack] Enviando alerta real para pedido 1"));
        assertTrue(output.contains("[Estoque] Atualizando estoque para o pedido 1"));
        assertTrue(output.contains("[NF] Emitindo nota fiscal para o pedido 1"));
    }
}
