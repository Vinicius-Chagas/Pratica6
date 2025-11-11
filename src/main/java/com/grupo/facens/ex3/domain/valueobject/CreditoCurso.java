package com.grupo.facens.ex3.domain.valueobject;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Value Object representando créditos de curso de um aluno.
 * Garante que a quantidade nunca seja negativa.
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CreditoCurso {

    @Column(name = "credito_curso")
    private int quantidade;

    public static CreditoCurso of(int quantidade) {
        validar(quantidade);
        return new CreditoCurso(quantidade);
    }

    public static CreditoCurso zero() {
        return new CreditoCurso(0);
    }

    private static void validar(int quantidade) {
        if (quantidade < 0) {
            throw new IllegalArgumentException("Quantidade de créditos não pode ser negativa");
        }
    }

    public CreditoCurso adicionar(int valor) {
        if (valor < 0) {
            throw new IllegalArgumentException("Valor a adicionar não pode ser negativo");
        }
        return new CreditoCurso(this.quantidade + valor);
    }

    public CreditoCurso usar(int valor) {
        if (valor < 0) {
            throw new IllegalArgumentException("Valor a usar não pode ser negativo");
        }
        if (this.quantidade < valor) {
            throw new IllegalStateException("Créditos insuficientes");
        }
        return new CreditoCurso(this.quantidade - valor);
    }

    public boolean temCredito(int valor) {
        return this.quantidade >= valor;
    }

    @Override
    public String toString() {
        return String.valueOf(quantidade);
    }
}

