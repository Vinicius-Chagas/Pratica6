package com.grupo.facens.ex3.domain.valueobject;

/**
 * Value Object representando os tipos de plano disponíveis.
 */
public enum TipoPlano {
    BASICO("Plano Básico", 1),
    PREMIUM("Plano Premium", 3);

    private final String descricao;
    private final int creditosMensais;

    TipoPlano(String descricao, int creditosMensais) {
        this.descricao = descricao;
        this.creditosMensais = creditosMensais;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getCreditosMensais() {
        return creditosMensais;
    }

    public boolean eBasico() {
        return this == BASICO;
    }

    public boolean ePremium() {
        return this == PREMIUM;
    }

    public boolean permiteAcessoIlimitado() {
        return this == PREMIUM;
    }
}
