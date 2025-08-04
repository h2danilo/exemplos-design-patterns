package com.example.pedido.infrastructure.adapter.out;

import com.example.pedido.application.port.out.NotaFiscalGeneratorPort;
import com.example.pedido.domain.model.Pedido;
import org.springframework.stereotype.Component;

/**
 * Adaptador para geração de nota fiscal
 */
@Component
public class GeradorNotaFiscalAdapter implements NotaFiscalGeneratorPort {

    @Override
    public void gerarNota(Pedido pedido) {
        System.out.println("[Nota Fiscal] Gerando nota fiscal para o pedido " + pedido.getId() + 
                " no valor de R$ " + pedido.getValor());
    }
}
