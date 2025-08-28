package com.example.pedido.application.port.out;

import com.example.pedido.domain.model.Pedido;
import com.example.pedido.domain.evento.PedidoCriadoEvent;

/**
 * Porta de saída para métricas relacionadas a pedidos.
 * Esta interface define o contrato para adaptadores que coletam métricas
 * sobre operações relacionadas a pedidos.
 */
public interface MetricsPort {

    /**
     * Registra a criação de um pedido
     *
     * @param pedido O pedido criado
     */
    void registrarPedidoCriado(Pedido pedido);

    /**
     * Mede o tempo de execução da criação de um pedido
     *
     * @param codigo Código a ser executado e medido
     * @param <T> Tipo de retorno do código
     * @return Resultado da execução do código
     */
    <T> T medirTempoCriacaoPedido(MeasurableOperation<T> codigo);

    /**
     * Mede o tempo de execução do processamento de um evento
     *
     * @param codigo Código a ser executado e medido
     * @param <T> Tipo de retorno do código
     * @return Resultado da execução do código
     */
    <T> T medirTempoProcessamentoEvento(MeasurableOperation<T> codigo);

    /**
     * Registra o início do processamento de um evento
     *
     * @param evento O evento sendo processado
     * @return Um identificador para o timer iniciado
     */
    Object iniciarProcessamentoEvento(PedidoCriadoEvent evento);

    /**
     * Registra o fim do processamento de um evento
     *
     * @param timerContext O contexto do timer retornado por iniciarProcessamentoEvento
     */
    void finalizarProcessamentoEvento(Object timerContext);

    /**
     * Registra o início da atualização de estoque
     *
     * @return Um identificador para o timer iniciado
     */
    Object iniciarAtualizacaoEstoque();

    /**
     * Registra o fim da atualização de estoque
     *
     * @param timerContext O contexto do timer retornado por iniciarAtualizacaoEstoque
     */
    void finalizarAtualizacaoEstoque(Object timerContext);

    /**
     * Registra o início da geração de nota fiscal
     *
     * @return Um identificador para o timer iniciado
     */
    Object iniciarGeracaoNotaFiscal();

    /**
     * Registra o fim da geração de nota fiscal
     *
     * @param timerContext O contexto do timer retornado por iniciarGeracaoNotaFiscal
     */
    void finalizarGeracaoNotaFiscal(Object timerContext);

    /**
     * Registra o início do envio de notificações
     *
     * @return Um identificador para o timer iniciado
     */
    Object iniciarEnvioNotificacoes();

    /**
     * Registra o fim do envio de notificações
     *
     * @param timerContext O contexto do timer retornado por iniciarEnvioNotificacoes
     */
    void finalizarEnvioNotificacoes(Object timerContext);

    /**
     * Registra o início do envio de uma notificação específica
     *
     * @param tipoNotificador O tipo de notificador
     * @return Um identificador para o timer iniciado
     */
    Object iniciarEnvioNotificacao(String tipoNotificador);

    /**
     * Registra o fim do envio de uma notificação específica
     *
     * @param timerContext O contexto do timer retornado por iniciarEnvioNotificacao
     * @param tipoNotificador O tipo de notificador
     */
    void finalizarEnvioNotificacao(Object timerContext, String tipoNotificador);

    /**
     * Registra que um evento foi processado
     */
    void registrarEventoProcessado();
}
