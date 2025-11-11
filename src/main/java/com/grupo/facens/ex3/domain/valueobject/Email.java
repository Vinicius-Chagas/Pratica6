package com.grupo.facens.ex3.domain.valueobject;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

/**
 * Value Object representando um endereço de email válido.
 * Garante que o email sempre esteja em um estado válido.
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Email {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    @Column(nullable = false, unique = true, name = "email")
    private String endereco;

    public static Email of(String endereco) {
        validar(endereco);
        return new Email(endereco.toLowerCase().trim());
    }

    private static void validar(String endereco) {
        if (endereco == null || endereco.trim().isEmpty()) {
            throw new IllegalArgumentException("Email não pode ser vazio");
        }
        if (!EMAIL_PATTERN.matcher(endereco).matches()) {
            throw new IllegalArgumentException("Email inválido: " + endereco);
        }
    }

    @Override
    public String toString() {
        return endereco;
    }
}

