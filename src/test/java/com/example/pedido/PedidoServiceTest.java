
package com.example.pedido;

import com.example.pedido.application.port.in.PedidoUseCase;
import com.example.pedido.application.port.out.EstoqueUpdaterPort;
import com.example.pedido.application.port.out.NotaFiscalGeneratorPort;
import com.example.pedido.application.port.out.PedidoNotifierPort;
import com.example.pedido.application.useCase.PedidoUseCaseImpl;
import com.example.pedido.domain.evento.PedidoCriadoEvent;
import com.example.pedido.domain.model.Pedido;
import com.example.pedido.domain.model.TipoCliente;
import com.example.pedido.infrastructure.config.AppProperties;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testes unitários da regra de aplicação de desconto em pedidos.
 * Adaptado para arquitetura hexagonal com portas e adaptadores.
 */
public class PedidoServiceTest {

    @Test
    void deveAplicarDescontoParaClienteVIP() {
        // Instância manual de AppProperties simulando ambiente "producao"
        AppProperties props = new AppProperties() {
            @Override
            public boolean isProducao() {
                return true;
            }

            @Override
            public String getAmbiente() {
                return "producao";
            }
        };

        // Criando adaptadores de teste
        List<PedidoNotifierPort> notificadores = new ArrayList<>();
        notificadores.add(evento -> System.out.println("[Email] Enviando email de confirmação para o pedido " + evento.getPedido().getId()));

        EstoqueUpdaterPort estoqueUpdater = pedido -> 
            System.out.println("[Estoque] Atualizando estoque para o pedido " + pedido.getId());

        NotaFiscalGeneratorPort notaFiscalGenerator = pedido -> 
            System.out.println("[Nota Fiscal] Gerando nota fiscal para o pedido " + pedido.getId());

        // Criando o serviço de aplicação com as portas
        PedidoUseCase pedidoUseCase = new PedidoUseCaseImpl(notificadores, estoqueUpdater, notaFiscalGenerator);

        // Criando e processando o pedido
        Pedido pedido = new Pedido(1L, new BigDecimal("100.00"), TipoCliente.VIP);
        Pedido resultado = pedidoUseCase.criarPedido(pedido);

        assertEquals(new BigDecimal("90.00").setScale(2), resultado.getValor().setScale(2));
    }

    @Test
    void deveManterValorParaClientePadrao() {
        // Criando adaptadores de teste vazios
        List<PedidoNotifierPort> notificadores = new ArrayList<>();
        notificadores.add(evento -> {}); // Implementação vazia do notificador
        EstoqueUpdaterPort estoqueUpdater = pedido -> {};
        NotaFiscalGeneratorPort notaFiscalGenerator = pedido -> {};

        // Criando o serviço de aplicação com as portas
        PedidoUseCase pedidoUseCase = new PedidoUseCaseImpl(notificadores, estoqueUpdater, notaFiscalGenerator);

        // Criando e processando o pedido
        Pedido pedido = new Pedido(2L, new BigDecimal("100.00"), TipoCliente.PADRAO);
        Pedido resultado = pedidoUseCase.criarPedido(pedido);

        assertEquals(new BigDecimal("100.00").setScale(2), resultado.getValor().setScale(2));
    }
}
