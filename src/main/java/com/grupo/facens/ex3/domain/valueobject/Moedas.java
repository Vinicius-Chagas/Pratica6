package com.grupo.facens.ex3.domain.valueobject;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Value Object representando a quantidade de moedas de um aluno.
 * Garante que a quantidade nunca seja negativa.
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Moedas {

    @Column(name = "moedas")
    private int quantidade;

    public static Moedas of(int quantidade) {
        validar(quantidade);
        return new Moedas(quantidade);
    }

    public static Moedas zero() {
        return new Moedas(0);
    }

    private static void validar(int quantidade) {
        if (quantidade < 0) {
            throw new IllegalArgumentException("Quantidade de moedas não pode ser negativa");
        }
    }

    public Moedas adicionar(int valor) {
        if (valor < 0) {
            throw new IllegalArgumentException("Valor a adicionar não pode ser negativo");
        }
        return new Moedas(this.quantidade + valor);
    }

    public Moedas subtrair(int valor) {
        if (valor < 0) {
            throw new IllegalArgumentException("Valor a subtrair não pode ser negativo");
        }
        if (this.quantidade < valor) {
            throw new IllegalStateException("Saldo insuficiente de moedas");
        }
        return new Moedas(this.quantidade - valor);
    }

    public boolean temSaldo(int valor) {
        return this.quantidade >= valor;
    }

    @Override
    public String toString() {
        return String.valueOf(quantidade);
    }
}
