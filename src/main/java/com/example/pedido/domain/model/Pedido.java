package com.example.pedido.domain.model;

import java.math.BigDecimal;

public class Pedido {
    private Long id;
    private BigDecimal valor;
    private TipoCliente tipoCliente;

    public Pedido() {}

    public Pedido(Long id, BigDecimal valor, TipoCliente tipoCliente) {
        this.id = id;
        this.valor = valor;
        this.tipoCliente = tipoCliente;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public TipoCliente getTipoCliente() { return tipoCliente; }
    public void setTipoCliente(TipoCliente tipoCliente) { this.tipoCliente = tipoCliente; }
}