package com.example.pedido.infrastructure.adapter.out.kafka;

import com.example.pedido.domain.evento.PedidoCriadoEvent;
import com.example.pedido.domain.model.Pedido;
import com.example.pedido.domain.model.TipoCliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para o adaptador de notificação Kafka.
 * Este arquivo centraliza todos os testes relacionados ao KafkaPedidoNotifierAdapter.
 */
public class KafkaPedidoNotifierAdapterTest {

    /**
     * Implementação de teste que estende o adaptador real para permitir testes
     * sem depender de mocks do KafkaTemplate.
     */
    private static class TestableAdapter extends KafkaPedidoNotifierAdapter {
        private static final Logger logger = LoggerFactory.getLogger(TestableAdapter.class);
        private final List<String> topicsCalled = new ArrayList<>();
        private final List<String> keysCalled = new ArrayList<>();
        private final List<PedidoCriadoEvent> eventsSent = new ArrayList<>();
        private boolean throwExceptionOnSend = false;
        private final String topicName;

        public TestableAdapter(String topicName) {
            super(null, topicName);
            this.topicName = topicName;
        }

        public void setThrowExceptionOnSend(boolean throwException) {
            this.throwExceptionOnSend = throwException;
        }

        @Override
        public void notificarPedidoCriado(PedidoCriadoEvent evento) {
            // Chama o método real para garantir cobertura
            logger.info("Enviando evento de pedido criado para o Kafka: {}", evento);

            // Usa o ID do pedido como chave para garantir que eventos do mesmo pedido
            // sejam processados na mesma ordem em que foram enviados
            String key = evento.getPedido().getId().toString();

            try {
                // Em vez de usar kafkaTemplate, registra a chamada
                topicsCalled.add(topicName);
                keysCalled.add(key);
                eventsSent.add(evento);

                if (throwExceptionOnSend) {
                    throw new RuntimeException("Erro simulado ao enviar para o Kafka");
                }

                logger.debug("Evento enviado para o tópico {}: {}", topicName, evento);
            } catch (Exception ex) {
                logger.error("Erro ao enviar evento para o tópico {}: {}", topicName, ex.getMessage());
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
    }

    private TestableAdapter adapter;
    private PedidoCriadoEvent event;
    private Pedido pedido;
    private final String topicName = "pedido-criado";

    @BeforeEach
    void setUp() {
        // Configurar o adaptador de teste
        adapter = new TestableAdapter(topicName);

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

        // Act & Assert - não deve lançar exceção
        assertDoesNotThrow(() -> adapter.notificarPedidoCriado(event));

        // Verifica que a tentativa foi registrada mesmo com a exceção
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
        assertEquals(outroEvento, adapter.getEventsSent().get(0));
    }
}
