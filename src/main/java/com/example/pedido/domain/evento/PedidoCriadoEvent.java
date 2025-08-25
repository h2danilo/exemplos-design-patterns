package com.example.pedido.domain.evento;

import com.example.pedido.domain.model.Pedido;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

/**
 * Evento de domínio que representa a criação de um pedido.
 * Este evento pode ser publicado em um tópico Kafka para notificar outros sistemas.
 */
public class PedidoCriadoEvent {

    private Pedido pedido;
    private LocalDateTime timestamp;

    /**
     * Construtor padrão necessário para deserialização JSON.
     * Usado pelo Jackson quando deserializa mensagens do Kafka.
     */
    public PedidoCriadoEvent() {
        // Construtor vazio necessário para deserialização
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Cria um novo evento de pedido criado.
     *
     * @param pedido O pedido que foi criado
     */
    public PedidoCriadoEvent(Pedido pedido) {
        this.pedido = pedido;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Retorna o pedido associado ao evento.
     *
     * @return O pedido criado
     */
    public Pedido getPedido() {
        return pedido;
    }

    /**
     * Retorna o timestamp de quando o evento foi criado.
     *
     * @return O timestamp do evento
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "PedidoCriadoEvent{" +
                "pedido=" + pedido +
                ", timestamp=" + timestamp +
                '}';
    }
}
