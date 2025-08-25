package com.example.pedido.infrastructure.adapter.in.kafka;

import com.example.pedido.domain.evento.PedidoCriadoEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Consumidor de eventos de pedido do Kafka.
 * Esta classe demonstra como consumir eventos de pedido publicados no Kafka.
 */
@Component
public class PedidoEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(PedidoEventConsumer.class);

    /**
     * Consome eventos de pedido criado do tópico Kafka.
     * O método é anotado com @KafkaListener para configurar o consumo automático de mensagens.
     *
     * @param event O evento de pedido criado recebido do Kafka
     */
    @KafkaListener(
        topics = "${app.kafka.topic.pedido-criado:pedido-criado}",
        groupId = "pedido-processor-group"
    )
    public void consumePedidoCriadoEvent(PedidoCriadoEvent event) {
        logger.info("Recebido evento de pedido criado do Kafka: {}", event);
        
        try {
            // Aqui seria implementada a lógica de processamento do evento
            // Por exemplo, atualizar o estoque, enviar e-mail de confirmação, etc.
            logger.info("Processando pedido: {}", event.getPedido().getId());
            
            // Simulação de processamento
            Thread.sleep(100);
            
            logger.info("Pedido processado com sucesso: {}", event.getPedido().getId());
        } catch (Exception e) {
            // Em um cenário real, poderia implementar retry, dead-letter queue, etc.
            logger.error("Erro ao processar evento de pedido: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Exemplo de outro consumidor no mesmo grupo, mas com um filtro diferente.
     * Este método demonstra como você pode ter múltiplos consumidores para diferentes
     * propósitos usando o mesmo evento.
     *
     * @param event O evento de pedido criado recebido do Kafka
     */
    @KafkaListener(
        topics = "${app.kafka.topic.pedido-criado:pedido-criado}",
        groupId = "pedido-notification-group"
    )
    public void sendPedidoNotification(PedidoCriadoEvent event) {
        logger.info("Enviando notificação para o pedido: {}", event.getPedido().getId());
        
        // Aqui seria implementada a lógica de envio de notificação
        // Por exemplo, enviar SMS, push notification, etc.
    }
}