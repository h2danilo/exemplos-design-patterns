
package com.example.pedido;

import com.example.pedido.application.PedidoService;
import com.example.pedido.domain.Pedido;
import com.example.pedido.domain.TipoCliente;
import com.example.pedido.domain.evento.PedidoNotifier;
import com.example.pedido.infrastructure.config.AppProperties;
import com.example.pedido.infrastructure.service.NotificadorEmail;
import com.example.pedido.infrastructure.service.AtualizadorEstoque;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testes unitários da regra de aplicação de desconto em pedidos.
 * Melhor prática: não usa Mockito, usa instâncias reais para garantir simplicidade e compatibilidade com Java 24+.
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

        PedidoNotifier notifier = new PedidoNotifier();
        notifier.adicionar(new NotificadorEmail(props));
        notifier.adicionar(new AtualizadorEstoque());

        PedidoService service = new PedidoService(notifier);

        Pedido pedido = new Pedido(1L, new BigDecimal("100.00"), TipoCliente.VIP);
        Pedido resultado = service.criarPedido(pedido);

        assertEquals(new BigDecimal("90.00").setScale(2), resultado.getValor().setScale(2));
    }

    @Test
    void deveManterValorParaClientePadrao() {
        Pedido pedido = new Pedido(2L, new BigDecimal("100.00"), TipoCliente.PADRAO);

        PedidoService service = new PedidoService(new PedidoNotifier());

        Pedido resultado = service.criarPedido(pedido);

        assertEquals(new BigDecimal("100.00").setScale(2), resultado.getValor().setScale(2));
    }
}
