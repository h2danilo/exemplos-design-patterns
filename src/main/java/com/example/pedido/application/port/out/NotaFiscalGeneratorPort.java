package com.example.pedido.application.port.out;

import com.example.pedido.domain.model.Pedido;

/**
 * Porta de saída para geração de nota fiscal
 */
public interface NotaFiscalGeneratorPort {
    void gerarNota(Pedido pedido);
}
