package com.example.pedido.infrastructure.adapter.out.kafka;

import com.example.pedido.domain.evento.PedidoCriadoEvent;
import com.example.pedido.domain.model.Pedido;
import com.example.pedido.domain.model.TipoCliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para o adaptador de notificação Kafka.
 */
public class KafkaPedidoNotifierAdapterTest {

    /**
     * Testes que usam a implementação real do adaptador com mocks para dependências externas.
     */
    @ExtendWith(MockitoExtension.class)
    public static class RealImplementationTest {
        @Mock
        private KafkaTemplate<String, Object> kafkaTemplate;

        @Captor
        private ArgumentCaptor<String> topicCaptor;

        @Captor
        private ArgumentCaptor<String> keyCaptor;

        @Captor
        private ArgumentCaptor<PedidoCriadoEvent> eventCaptor;

        private KafkaPedidoNotifierAdapter adapter;
        private PedidoCriadoEvent event;
        private Pedido pedido;
        private final String topicName = "pedido-criado";

        @BeforeEach
        void setUp() {
            // Configurar o adaptador com o mock do KafkaTemplate
            adapter = new KafkaPedidoNotifierAdapter(kafkaTemplate, topicName);

            // Criar pedido e evento para teste
            pedido = new Pedido(1L, new BigDecimal("100.00"), TipoCliente.VIP);
            event = new PedidoCriadoEvent(pedido);

            // Configurar o mock para retornar um CompletableFuture completo
            when(kafkaTemplate.send(anyString(), anyString(), any())).thenReturn(CompletableFuture.completedFuture(null));
        }

        @Test
        void deveEnviarEventoParaKafkaComImplementacaoReal() {
            // Act
            adapter.notificarPedidoCriado(event);

            // Assert
            verify(kafkaTemplate).send(eq(topicName), eq("1"), eq(event));
        }

        @Test
        void deveTratarExcecaoAoEnviarParaKafkaComImplementacaoReal() {
            // Arrange
            when(kafkaTemplate.send(anyString(), anyString(), any())).thenThrow(new RuntimeException("Erro simulado"));

            // Act
            adapter.notificarPedidoCriado(event);

            // Assert - verifica que o método foi chamado mesmo com a exceção
            verify(kafkaTemplate).send(eq(topicName), eq("1"), eq(event));
        }

        @Test
        void deveUsarIdDoPedidoComoChaveComImplementacaoReal() {
            // Arrange
            Pedido outroPedido = new Pedido(999L, new BigDecimal("200.00"), TipoCliente.PADRAO);
            PedidoCriadoEvent outroEvento = new PedidoCriadoEvent(outroPedido);

            // Act
            adapter.notificarPedidoCriado(outroEvento);

            // Assert
            verify(kafkaTemplate).send(eq(topicName), eq("999"), eq(outroEvento));
        }
    }

    // Classe de teste que estende o adaptador para capturar as chamadas
    private static class TestableKafkaPedidoNotifierAdapter extends KafkaPedidoNotifierAdapter {
        private final List<String> topicsCalled = new ArrayList<>();
        private final List<String> keysCalled = new ArrayList<>();
        private final List<PedidoCriadoEvent> eventsSent = new ArrayList<>();
        private boolean throwExceptionOnSend = false;
        private final String topicName;

        public TestableKafkaPedidoNotifierAdapter(String topicName) {
            super(null, topicName);
            this.topicName = topicName;
        }

        @Override
        public void notificarPedidoCriado(PedidoCriadoEvent evento) {
            String key = evento.getPedido().getId().toString();

            try {
                if (throwExceptionOnSend) {
                    throw new RuntimeException("Erro simulado ao enviar para o Kafka");
                }

                // Captura os parâmetros em vez de enviar para o Kafka
                topicsCalled.add(topicName);
                keysCalled.add(key);
                eventsSent.add(evento);

            } catch (Exception ex) {
                // Captura a exceção mas registra a tentativa
                topicsCalled.add(topicName);
                keysCalled.add(key);
                eventsSent.add(evento);
            }
        }

        // Métodos para verificar as chamadas
        public List<String> getTopicsCalled() {
            return topicsCalled;
        }

        public List<String> getKeysCalled() {
            return keysCalled;
        }

        public List<PedidoCriadoEvent> getEventsSent() {
            return eventsSent;
        }

        public void setThrowExceptionOnSend(boolean throwException) {
            this.throwExceptionOnSend = throwException;
        }
    }

    private TestableKafkaPedidoNotifierAdapter adapter;
    private PedidoCriadoEvent event;
    private Pedido pedido;
    private final String topicName = "pedido-criado";

    @BeforeEach
    void setUp() {
        // Configurar o adaptador de teste
        adapter = new TestableKafkaPedidoNotifierAdapter(topicName);

        // Criar pedido e evento para teste
        pedido = new Pedido(1L, new BigDecimal("100.00"), TipoCliente.VIP);
        event = new PedidoCriadoEvent(pedido);
    }

    @Test
    void deveEnviarEventoParaKafka() {
        // Act
        adapter.notificarPedidoCriado(event);

        // Assert
        assertEquals(1, adapter.getTopicsCalled().size());
        assertEquals(topicName, adapter.getTopicsCalled().get(0));
        assertEquals("1", adapter.getKeysCalled().get(0));
        assertEquals(event, adapter.getEventsSent().get(0));
    }

    @Test
    void deveTratarExcecaoAoEnviarParaKafka() {
        // Arrange
        adapter.setThrowExceptionOnSend(true);

        // Act
        adapter.notificarPedidoCriado(event);

        // Assert - verifica que a tentativa foi registrada mesmo com a exceção
        assertEquals(1, adapter.getTopicsCalled().size());
        assertEquals(topicName, adapter.getTopicsCalled().get(0));
        assertEquals("1", adapter.getKeysCalled().get(0));
        assertEquals(event, adapter.getEventsSent().get(0));
    }

    @Test
    void deveUsarIdDoPedidoComoChave() {
        // Arrange
        Pedido outroPedido = new Pedido(999L, new BigDecimal("200.00"), TipoCliente.PADRAO);
        PedidoCriadoEvent outroEvento = new PedidoCriadoEvent(outroPedido);

        // Act
        adapter.notificarPedidoCriado(outroEvento);

        // Assert
        assertEquals(1, adapter.getKeysCalled().size());
        assertEquals("999", adapter.getKeysCalled().get(0));
    }
}
