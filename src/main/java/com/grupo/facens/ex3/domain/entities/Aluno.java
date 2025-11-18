package com.grupo.facens.ex3.domain.entities;

import com.grupo.facens.ex3.domain.enums.TipoPlano;
import com.grupo.facens.ex3.domain.valueobject.PlanoStatus;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "alunos")
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;
    
    @Embedded
    private PlanoStatus planoStatus = new PlanoStatus();

    public boolean temPlanoBasico() {
        return getPlano() == TipoPlano.BASICO;
    }

    public boolean temPlanoPremium() {
        return getPlano() == TipoPlano.PREMIUM;
    }

    public TipoPlano getPlano() {
        return getPlanoStatus().getTipoPlano();
    }

    public void setPlano(TipoPlano plano) {
        getPlanoStatus().setTipoPlano(plano);
    }

    public int getCursosCompletados() {
        return getPlanoStatus().getCursosCompletados();
    }

    public void setCursosCompletados(int cursosCompletados) {
        getPlanoStatus().setCursosCompletados(cursosCompletados);
    }

    public int getMoedas() {
        return getPlanoStatus().getMoedas();
    }

    public void setMoedas(int moedas) {
        getPlanoStatus().setMoedas(moedas);
    }

    public void adicionarMoedas(int quantidade) {
        if (quantidade > 0) {
            getPlanoStatus().creditarMoedas(quantidade);
        }
    }

    public boolean removerMoedas(int quantidade) {
        return getPlanoStatus().debitarMoedas(quantidade);
    }

    public int getCreditoCurso() {
        return getPlanoStatus().getCreditoCurso();
    }

    public void setCreditoCurso(int creditoCurso) {
        getPlanoStatus().setCreditoCurso(creditoCurso);
    }

    public void adicionarCreditosCurso(int quantidade) {
        getPlanoStatus().adicionarCreditos(quantidade);
    }

    private PlanoStatus getPlanoStatus() {
        if (planoStatus == null) {
            planoStatus = new PlanoStatus();
        }
        return planoStatus;
    }
}
