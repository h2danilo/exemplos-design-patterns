package com.example.pedido.application.port.out;

/**
 * Interface funcional para operações que podem ser medidas para métricas de tempo.
 * Permite que adaptadores de métricas meçam o tempo de execução de operações
 * sem depender diretamente de frameworks específicos.
 *
 * @param <T> Tipo de retorno da operação
 */
@FunctionalInterface
public interface MeasurableOperation<T> {
    
    /**
     * Executa a operação que será medida.
     *
     * @return Resultado da operação
     */
    T execute();
}