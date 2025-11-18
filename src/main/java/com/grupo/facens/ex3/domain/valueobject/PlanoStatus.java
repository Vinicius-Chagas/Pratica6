package com.grupo.facens.ex3.domain.valueobject;

import com.grupo.facens.ex3.domain.enums.TipoPlano;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanoStatus {

    @Enumerated(EnumType.STRING)
    @Column(name = "plano")
    private TipoPlano tipoPlano;

    @Column(name = "cursos_completados")
    private int cursosCompletados;

    @Column(name = "moedas")
    private int moedas;

    @Column(name = "credito_curso")
    private int creditoCurso;

    public void creditarMoedas(int quantidade) {
        if (quantidade > 0) {
            this.moedas += quantidade;
        }
    }

    public boolean debitarMoedas(int quantidade) {
        if (quantidade <= 0 || this.moedas < quantidade) {
            return false;
        }
        this.moedas -= quantidade;
        return true;
    }

    public void adicionarCreditos(int quantidade) {
        if (quantidade > 0) {
            this.creditoCurso += quantidade;
        }
    }

    public void registrarCursoConcluido() {
        this.cursosCompletados += 1;
    }
}
