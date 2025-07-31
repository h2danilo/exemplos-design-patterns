
package com.example.pedido.infrastructure.rest;

import com.example.pedido.domain.Pedido;
import com.example.pedido.domain.TipoCliente;
import com.example.pedido.application.PedidoService;
import com.example.pedido.domain.evento.PedidoNotifier;
import com.example.pedido.infrastructure.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoNotifier notifier) {
        this.pedidoService = new PedidoService(notifier);
    }

    @PostMapping
    public ResponseEntity<Pedido> criarPedido(@RequestBody Pedido pedidoRequest) {
        /*
         * O Service usa Factory para descobrir qual Strategy aplicar
         * baseado no TipoCliente (PADRAO ou VIP)
         * Após criação, ele dispara todos os observers registrados
         */
        Pedido criado = pedidoService.criarPedido(pedidoRequest);
        return ResponseEntity.ok(criado);
    }

    @GetMapping("/exemplo")
    public ResponseEntity<Pedido> exemploCriacaoPedido() {
        /*
         * Simula criação de pedido direto sem JSON, com Strategy e Observer ativados
         */
        Pedido pedido = new Pedido(99L, new BigDecimal("200"), TipoCliente.VIP);
        Pedido criado = pedidoService.criarPedido(pedido);
        return ResponseEntity.ok(criado);
    }
}
