package com.example.pedido.infrastructure.adapter.out;

import com.example.pedido.application.port.out.MeasurableOperation;
import com.example.pedido.application.port.out.MetricsPort;
import com.example.pedido.domain.evento.PedidoCriadoEvent;
import com.example.pedido.domain.model.Pedido;

/**
 * Adaptador de métricas para testes que não faz nada com as métricas
 */
public class StubMetricsAdapter implements MetricsPort {

    @Override
    public void registrarPedidoCriado(Pedido pedido) {
        // Não faz nada em testes
    }

    @Override
    public Object iniciarProcessamentoEvento(PedidoCriadoEvent evento) {
        // Retorna um objeto vazio para testes
        return new Object();
    }

    @Override
    public void finalizarProcessamentoEvento(Object timerContext) {
        // Não faz nada em testes
    }

    @Override
    public Object iniciarAtualizacaoEstoque() {
        // Retorna um objeto vazio para testes
        return new Object();
    }

    @Override
    public void finalizarAtualizacaoEstoque(Object timerContext) {
        // Não faz nada em testes
    }

    @Override
    public Object iniciarGeracaoNotaFiscal() {
        // Retorna um objeto vazio para testes
        return new Object();
    }

    @Override
    public void finalizarGeracaoNotaFiscal(Object timerContext) {
        // Não faz nada em testes
    }

    @Override
    public Object iniciarEnvioNotificacoes() {
        // Retorna um objeto vazio para testes
        return new Object();
    }

    @Override
    public void finalizarEnvioNotificacoes(Object timerContext) {
        // Não faz nada em testes
    }

    @Override
    public Object iniciarEnvioNotificacao(String tipoNotificador) {
        // Retorna um objeto vazio para testes
        return new Object();
    }

    @Override
    public void finalizarEnvioNotificacao(Object timerContext, String tipoNotificador) {
        // Não faz nada em testes
    }

    @Override
    public void registrarEventoProcessado() {
        // Não faz nada em testes
    }

    @Override
    public <T> T medirTempoCriacaoPedido(MeasurableOperation<T> codigo) {
        // Apenas executa o código sem medir o tempo em testes
        return codigo.execute();
    }

    @Override
    public <T> T medirTempoProcessamentoEvento(MeasurableOperation<T> codigo) {
        // Apenas executa o código sem medir o tempo em testes
        return codigo.execute();
    }
}
