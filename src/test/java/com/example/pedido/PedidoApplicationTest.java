package com.example.pedido;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * Teste de integração para verificar se a aplicação Spring Boot inicia corretamente.
 */
@SpringBootTest
@TestPropertySource(properties = {
    "spring.kafka.bootstrap-servers=localhost:9092",
    "app.kafka.topic.pedido-criado=test-pedido-criado"
})
public class PedidoApplicationTest {

    /**
     * Verifica se o contexto da aplicação é carregado com sucesso.
     * Este teste garante que a aplicação Spring Boot pode ser inicializada
     * sem erros de configuração ou dependências.
     */
    @Test
    void contextLoads() {
        // O teste passa se o contexto da aplicação for carregado com sucesso
    }

    /**
     * Testa o método main da aplicação.
     * Este teste verifica se o método main pode ser chamado sem exceções.
     */
    @Test
    void mainMethodStartsApplication() {
        // Arrange & Act & Assert
        PedidoApplication.main(new String[]{});
        // O teste passa se nenhuma exceção for lançada
    }
}