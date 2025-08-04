package com.example.pedido;

import org.junit.jupiter.api.Test;

/**
 * Classe para documentar os testes da arquitetura hexagonal.
 * 
 * Esta classe não executa testes diretamente, mas serve como documentação
 * para os testes que foram adaptados para a nova arquitetura hexagonal.
 * 
 * Para executar os testes, use os seguintes comandos:
 * 
 * # Testes de domínio
 * mvn test -Dtest=com.example.pedido.domain.event.PedidoCriadoEventTest
 * mvn test -Dtest=com.example.pedido.domain.desconto.DescontoFactoryTest
 * mvn test -Dtest=com.example.pedido.PedidoServiceTest
 * 
 * # Testes de adaptadores
 * mvn test -Dtest=com.example.pedido.infrastructure.adapter.in.rest.PedidoControllerTest
 * mvn test -Dtest=com.example.pedido.infrastructure.adapter.out.AtualizadorEstoqueAdapterTest
 * mvn test -Dtest=com.example.pedido.infrastructure.adapter.out.GeradorNotaFiscalAdapterTest
 * mvn test -Dtest=com.example.pedido.infrastructure.adapter.out.NotificadorEmailAdapterTest
 * mvn test -Dtest=com.example.pedido.infrastructure.adapter.out.NotificadorSlackAdapterTest
 * 
 * # Testes de configuração
 * mvn test -Dtest=com.example.pedido.infrastructure.config.ApplicationConfigTest
 */
public class HexagonalArchitectureTests {

    @Test
    void documentacaoApenas() {
        // Este teste não faz nada, apenas serve como documentação
    }
}
