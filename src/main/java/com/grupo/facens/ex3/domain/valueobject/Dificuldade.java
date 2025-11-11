package com.grupo.facens.ex3.domain.valueobject;

/**
 * Value Object representando o nível de dificuldade de um curso.
 */
public enum Dificuldade {
    INICIANTE("Iniciante", 1),
    INTERMEDIARIO("Intermediário", 2),
    AVANCADO("Avançado", 3);

    private final String descricao;
    private final int nivel;

    Dificuldade(String descricao, int nivel) {
        this.descricao = descricao;
        this.nivel = nivel;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getNivel() {
        return nivel;
    }

    public boolean eIniciante() {
        return this == INICIANTE;
    }

    public boolean eIntermediario() {
        return this == INTERMEDIARIO;
    }

    public boolean eAvancado() {
        return this == AVANCADO;
    }

    public boolean maisAvancoQue(Dificuldade outra) {
        return this.nivel > outra.nivel;
    }

    public boolean menosAvancoQue(Dificuldade outra) {
        return this.nivel < outra.nivel;
    }
}

