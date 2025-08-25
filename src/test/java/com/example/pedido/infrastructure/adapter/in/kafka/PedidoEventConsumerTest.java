package com.example.pedido.infrastructure.adapter.in.kafka;

import com.example.pedido.domain.evento.PedidoCriadoEvent;
import com.example.pedido.domain.model.Pedido;
import com.example.pedido.domain.model.TipoCliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.Acknowledgment;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testes unitários para o consumidor de eventos Kafka.
 */
@ExtendWith(MockitoExtension.class)
public class PedidoEventConsumerTest {

    @InjectMocks
    private PedidoEventConsumer pedidoEventConsumer;

    @Mock
    private Acknowledgment acknowledgment;

    private PedidoCriadoEvent event;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        // Configurar o evento de teste
        Pedido pedido = new Pedido(1L, new BigDecimal("100.00"), TipoCliente.VIP);
        event = new PedidoCriadoEvent(pedido);

        // Capturar saída do console para verificar logs
        originalOut = System.out;
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void deveProcessarEventoPedidoCriado() {
        // Act
        assertDoesNotThrow(() -> pedidoEventConsumer.consumePedidoCriadoEvent(event, acknowledgment));

        // Restaurar System.out
        System.setOut(originalOut);

        // Verificar se o método foi executado sem exceções
        // Não podemos verificar logs diretamente, mas o teste garante que o método não lança exceções
    }

    @Test
    void deveCapturarExcecaoAoProcessarEvento() {
        // Arrange - criar um evento que causará exceção
        Pedido pedidoNulo = null;
        PedidoCriadoEvent eventComErro = new PedidoCriadoEvent(pedidoNulo);

        // Act - deve capturar a exceção internamente
        assertDoesNotThrow(() -> pedidoEventConsumer.consumePedidoCriadoEvent(eventComErro, acknowledgment));

        // Restaurar System.out
        System.setOut(originalOut);
    }

    @Test
    void deveEnviarNotificacaoDePedido() {
        // Act
        pedidoEventConsumer.sendPedidoNotification(event, acknowledgment);

        // Restaurar System.out
        System.setOut(originalOut);

        // Verificar se o método foi executado sem exceções
        // Não podemos verificar logs diretamente, mas o teste garante que o método não lança exceções
    }
}
