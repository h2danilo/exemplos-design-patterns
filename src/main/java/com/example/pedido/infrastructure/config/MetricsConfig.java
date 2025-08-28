package com.example.pedido.infrastructure.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.micrometer.core.aop.TimedAspect;

/**
 * Configuração de métricas usando Micrometer Prometheus
 * Permite monitoramento em tempo real da aplicação
 */
@Configuration
public class MetricsConfig {

    /**
     * Configura o aspecto TimedAspect para permitir o uso da anotação @Timed
     * em métodos que queremos medir o tempo de execução
     */
    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }
}