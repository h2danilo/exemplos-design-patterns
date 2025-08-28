package com.example.pedido.infrastructure.config;

import com.example.pedido.application.port.in.PedidoUseCase;
import com.example.pedido.application.port.out.EstoqueUpdaterPort;
import com.example.pedido.application.port.out.MetricsPort;
import com.example.pedido.application.port.out.NotaFiscalGeneratorPort;
import com.example.pedido.application.port.out.PedidoNotifierPort;
import com.example.pedido.domain.model.Pedido;
import com.example.pedido.domain.model.TipoCliente;
import com.example.pedido.infrastructure.adapter.out.AtualizadorEstoqueAdapter;
import com.example.pedido.infrastructure.adapter.out.GeradorNotaFiscalAdapter;
import com.example.pedido.infrastructure.adapter.out.NotificadorEmailAdapter;
import com.example.pedido.infrastructure.adapter.out.NotificadorSlackAdapter;
import com.example.pedido.infrastructure.adapter.out.StubMetricsAdapter;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testes unitários para a classe ApplicationConfig.
 * Verifica se a configuração da aplicação está correta de acordo com a arquitetura hexagonal.
 */
public class ApplicationConfigTest {

    @Test
    void deveCriarPedidoUseCaseComTodasPortas() {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        // Criando adaptadores para as portas de saída
        List<PedidoNotifierPort> notificadores = new ArrayList<>();
        notificadores.add(new NotificadorEmailAdapter());
        notificadores.add(new NotificadorSlackAdapter());

        EstoqueUpdaterPort estoqueUpdater = new AtualizadorEstoqueAdapter();
        NotaFiscalGeneratorPort notaFiscalGenerator = new GeradorNotaFiscalAdapter();
        MetricsPort metricsPort = new StubMetricsAdapter();

        ApplicationConfig config = new ApplicationConfig();

        // Act
        PedidoUseCase pedidoUseCase = config.pedidoUseCase(
                notificadores,
                estoqueUpdater,
                notaFiscalGenerator,
                metricsPort
        );

        // Assert
        assertNotNull(pedidoUseCase);

        // Verificar se todas as portas são chamadas corretamente
        Pedido pedido = new Pedido(1L, new BigDecimal("100.00"), TipoCliente.VIP);
        pedidoUseCase.criarPedido(pedido);

        // Restaura System.out
        System.setOut(originalOut);

        String output = outputStream.toString();
        assertTrue(output.contains("[Email] Enviando email de confirmação para o pedido 1"));
        assertTrue(output.contains("[Slack] Enviando notificação no canal #pedidos"));
        assertTrue(output.contains("[Estoque] Atualizando estoque para o pedido 1"));
        assertTrue(output.contains("[Nota Fiscal] Gerando nota fiscal para o pedido 1"));
    }
}
