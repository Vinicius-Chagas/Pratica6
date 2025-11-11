package com.grupo.facens.ex3.domain.valueobject;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Value Object representando a carga horária de um curso.
 * Garante que a carga horária seja sempre positiva.
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CargaHoraria {

    private static final int MINIMO_HORAS = 1;
    private static final int MAXIMO_HORAS = 500;

    @Column(name = "carga_horaria")
    private Integer horas;

    public static CargaHoraria of(Integer horas) {
        validar(horas);
        return new CargaHoraria(horas);
    }

    private static void validar(Integer horas) {
        if (horas == null) {
            throw new IllegalArgumentException("Carga horária não pode ser nula");
        }
        if (horas < MINIMO_HORAS) {
            throw new IllegalArgumentException(
                String.format("Carga horária deve ter no mínimo %d hora(s)", MINIMO_HORAS)
            );
        }
        if (horas > MAXIMO_HORAS) {
            throw new IllegalArgumentException(
                String.format("Carga horária deve ter no máximo %d horas", MAXIMO_HORAS)
            );
        }
    }

    public boolean eCursoCurto() {
        return horas < 20;
    }

    public boolean eCursoMedio() {
        return horas >= 20 && horas < 60;
    }

    public boolean eCursoLongo() {
        return horas >= 60;
    }

    @Override
    public String toString() {
        return horas + " hora(s)";
    }
}

