package com.grupo.facens.ex3.domain.entities;

import com.grupo.facens.ex3.domain.enums.TipoPlano;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private TipoPlano plano;

    @Column(name = "cursos_completados")
    private int cursosCompletados;

    @Column
    private int moedas;

    @Column
    private int creditoCurso;

    public boolean temPlanoBasico() {
        return plano == TipoPlano.BASICO;
    }

    public boolean temPlanoPremium() {
        return plano == TipoPlano.PREMIUM;
    }
}
