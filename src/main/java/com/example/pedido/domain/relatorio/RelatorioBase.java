
package com.example.pedido.domain.relatorio;

public class RelatorioBase implements Relatorio {
    @Override
    public String gerar() {
        return "Relatório base do pedido.";
    }
}
