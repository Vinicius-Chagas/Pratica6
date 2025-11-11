package com.grupo.facens.ex3.domain.entity;

import com.grupo.facens.ex3.domain.valueobject.CreditoCurso;
import com.grupo.facens.ex3.domain.valueobject.Email;
import com.grupo.facens.ex3.domain.valueobject.Moedas;
import com.grupo.facens.ex3.domain.valueobject.TipoPlano;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade de domínio representando um Aluno.
 * Contém a lógica de negócio relacionada ao aluno.
 */
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

    @Embedded
    private Email email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, name = "plano")
    private TipoPlano plano;

    @Column(name = "cursos_completados")
    private int cursosCompletados;

    @Embedded
    private Moedas moedas;

    @Embedded
    private CreditoCurso creditoCurso;

    public Aluno(String nome, Email email, TipoPlano plano) {
        this.nome = nome;
        this.email = email;
        this.plano = plano;
        this.cursosCompletados = 0;
        this.moedas = Moedas.zero();
        this.creditoCurso = CreditoCurso.zero();
    }

    /**
     * Verifica se o aluno tem plano básico
     */
    public boolean temPlanoBasico() {
        return plano != null && plano.eBasico();
    }

    /**
     * Verifica se o aluno tem plano premium
     */
    public boolean temPlanoPremium() {
        return plano != null && plano.ePremium();
    }

    /**
     * Completa um curso e adiciona moedas como recompensa
     */
    public void completarCurso(int moedasRecompensa) {
        this.cursosCompletados++;
        if (this.moedas == null) {
            this.moedas = Moedas.zero();
        }
        this.moedas = this.moedas.adicionar(moedasRecompensa);
    }

    /**
     * Adiciona créditos de curso
     */
    public void adicionarCreditos(int quantidade) {
        if (this.creditoCurso == null) {
            this.creditoCurso = CreditoCurso.zero();
        }
        this.creditoCurso = this.creditoCurso.adicionar(quantidade);
    }

    /**
     * Usa créditos de curso
     */
    public void usarCreditos(int quantidade) {
        if (this.creditoCurso == null) {
            throw new IllegalStateException("Aluno não possui créditos");
        }
        this.creditoCurso = this.creditoCurso.usar(quantidade);
    }

    /**
     * Verifica se o aluno tem créditos suficientes
     */
    public boolean temCreditosSuficientes(int quantidade) {
        return this.creditoCurso != null && this.creditoCurso.temCredito(quantidade);
    }

    /**
     * Verifica se o aluno pode acessar um curso
     */
    public boolean podeAcessarCurso() {
        return temPlanoPremium() || temCreditosSuficientes(1);
    }
}
