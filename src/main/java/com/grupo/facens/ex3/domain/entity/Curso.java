package com.grupo.facens.ex3.domain.entity;

import com.grupo.facens.ex3.domain.valueobject.CargaHoraria;
import com.grupo.facens.ex3.domain.valueobject.Dificuldade;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidade de domínio representando um Curso.
 * Contém a lógica de negócio relacionada ao curso.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cursos")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(length = 2000)
    private String descricao;

    @Column(nullable = false)
    private String categoria;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "dificuldade")
    private Dificuldade dificuldade;

    @Embedded
    private CargaHoraria cargaHoraria;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    /**
     * Construtor de conveniência
     */
    public Curso(String titulo, String descricao, String categoria, Dificuldade dificuldade, CargaHoraria cargaHoraria) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.categoria = categoria;
        this.dificuldade = dificuldade;
        this.cargaHoraria = cargaHoraria;
        this.ativo = true;
    }

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
        if (ativo == null) {
            ativo = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }

    /**
     * Ativa o curso
     */
    public void ativar() {
        this.ativo = true;
    }

    /**
     * Desativa o curso
     */
    public void desativar() {
        this.ativo = false;
    }

    /**
     * Verifica se o curso está ativo
     */
    public boolean estaAtivo() {
        return ativo != null && ativo;
    }

    /**
     * Verifica se o curso é para iniciantes
     */
    public boolean eParaIniciantes() {
        return dificuldade != null && dificuldade.eIniciante();
    }

    /**
     * Verifica se o curso é avançado
     */
    public boolean eAvancado() {
        return dificuldade != null && dificuldade.eAvancado();
    }

    /**
     * Verifica se o curso foi recentemente criado (últimos 30 dias)
     */
    public boolean eNovo() {
        if (dataCriacao == null) {
            return false;
        }
        return dataCriacao.isAfter(LocalDateTime.now().minusDays(30));
    }

    /**
     * Atualiza informações do curso
     */
    public void atualizar(String titulo, String descricao, String categoria, Dificuldade dificuldade, CargaHoraria cargaHoraria) {
        if (titulo != null && !titulo.trim().isEmpty()) {
            this.titulo = titulo;
        }
        if (descricao != null) {
            this.descricao = descricao;
        }
        if (categoria != null && !categoria.trim().isEmpty()) {
            this.categoria = categoria;
        }
        if (dificuldade != null) {
            this.dificuldade = dificuldade;
        }
        if (cargaHoraria != null) {
            this.cargaHoraria = cargaHoraria;
        }
    }
}

