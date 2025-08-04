package com.example.pedido.infrastructure.config;

import com.example.pedido.application.port.in.PedidoUseCase;
import com.example.pedido.application.port.out.EstoqueUpdaterPort;
import com.example.pedido.application.port.out.NotaFiscalGeneratorPort;
import com.example.pedido.application.port.out.PedidoNotifierPort;
import com.example.pedido.application.service.PedidoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuração da aplicação para injeção de dependências
 * Seguindo os princípios da arquitetura hexagonal
 */
@Configuration
public class ApplicationConfig {

    /**
     * Cria o bean do caso de uso de pedido
     * Injeta as portas de saída necessárias
     */
    @Bean
    public PedidoUseCase pedidoUseCase(
            List<PedidoNotifierPort> notificadores,
            EstoqueUpdaterPort estoqueUpdater,
            NotaFiscalGeneratorPort notaFiscalGenerator) {
        return new PedidoService(notificadores, estoqueUpdater, notaFiscalGenerator);
    }
}