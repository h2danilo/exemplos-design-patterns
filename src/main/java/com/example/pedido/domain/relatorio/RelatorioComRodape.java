
package com.example.pedido.domain.relatorio;

public class RelatorioComRodape implements Relatorio {
    private final Relatorio relatorio;

    public RelatorioComRodape(Relatorio relatorio) {
        this.relatorio = relatorio;
    }

    @Override
    public String gerar() {
        return relatorio.gerar() + "\nRodapé: confidencial";
    }
}
