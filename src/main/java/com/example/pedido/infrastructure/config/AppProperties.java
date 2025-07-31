package com.example.pedido.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppProperties {

    @Value("${app.ambiente:desconhecido}")
    private String ambiente;

    public boolean isProducao() {
        return "producao".equalsIgnoreCase(ambiente);
    }

    public String getAmbiente() {
        return ambiente;
    }
}
