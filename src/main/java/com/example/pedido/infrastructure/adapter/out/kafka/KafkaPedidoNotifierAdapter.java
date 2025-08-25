package com.example.pedido.infrastructure.adapter.out.kafka;

import com.example.pedido.application.port.out.PedidoNotifierPort;
import com.example.pedido.domain.evento.PedidoCriadoEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Adaptador para enviar eventos de pedido para o Kafka.
 * Implementa a porta de saída PedidoNotifierPort usando o Kafka como mecanismo de mensageria.
 */
@Component
public class KafkaPedidoNotifierAdapter implements PedidoNotifierPort {

    private static final Logger logger = LoggerFactory.getLogger(KafkaPedidoNotifierAdapter.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String pedidoCriadoTopic;

    /**
     * Construtor para injeção de dependências.
     *
     * @param kafkaTemplate Template Kafka para envio de mensagens
     * @param pedidoCriadoTopic Nome do tópico para eventos de pedido criado
     */
    public KafkaPedidoNotifierAdapter(
            KafkaTemplate<String, Object> kafkaTemplate,
            @Value("${app.kafka.topic.pedido-criado:pedido-criado}") String pedidoCriadoTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.pedidoCriadoTopic = pedidoCriadoTopic;
    }

    /**
     * Envia um evento de pedido criado para o tópico Kafka correspondente.
     * Usa o ID do pedido como chave para garantir ordenação de eventos relacionados ao mesmo pedido.
     *
     * @param evento O evento de pedido criado
     */
    @Override
    public void notificarPedidoCriado(PedidoCriadoEvent evento) {
        logger.info("Enviando evento de pedido criado para o Kafka: {}", evento);

        // Usa o ID do pedido como chave para garantir que eventos do mesmo pedido
        // sejam processados na mesma ordem em que foram enviados
        String key = evento.getPedido().getId().toString();

        try {
            kafkaTemplate.send(pedidoCriadoTopic, key, evento);
            logger.debug("Evento enviado para o tópico {}: {}", pedidoCriadoTopic, evento);
        } catch (Exception ex) {
            logger.error("Erro ao enviar evento para o tópico {}: {}", pedidoCriadoTopic, ex.getMessage());
        }
    }
}
