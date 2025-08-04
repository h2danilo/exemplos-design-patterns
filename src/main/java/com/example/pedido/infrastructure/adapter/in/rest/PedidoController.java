package com.example.pedido.infrastructure.adapter.in.rest;

import com.example.pedido.application.port.in.PedidoUseCase;
import com.example.pedido.domain.model.Pedido;
import com.example.pedido.domain.model.TipoCliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * Adaptador de entrada REST para criação de pedidos
 */
@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoUseCase pedidoUseCase;

    @Autowired
    public PedidoController(PedidoUseCase pedidoUseCase) {
        this.pedidoUseCase = pedidoUseCase;
    }

    @PostMapping
    public ResponseEntity<Pedido> criarPedido(@RequestBody Pedido pedidoRequest) {
        /*
         * Chama o caso de uso através da porta de entrada
         * O caso de uso aplica as regras de negócio e dispara eventos
         */
        Pedido criado = pedidoUseCase.criarPedido(pedidoRequest);
        return ResponseEntity.ok(criado);
    }

    @GetMapping("/exemplo")
    public ResponseEntity<Pedido> exemploCriacaoPedido() {
        /*
         * Simula criação de pedido direto sem JSON
         */
        Pedido pedido = new Pedido(99L, new BigDecimal("200"), TipoCliente.VIP);
        Pedido criado = pedidoUseCase.criarPedido(pedido);
        return ResponseEntity.ok(criado);
    }
}
