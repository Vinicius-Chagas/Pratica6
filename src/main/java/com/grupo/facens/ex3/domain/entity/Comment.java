package com.grupo.facens.ex3.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidade de domínio representando um Comentário em um post.
 * Contém a lógica de negócio relacionada a comentários.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String conteudo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false)
    private Aluno autor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    public Comment(String conteudo, Aluno autor, Post post) {
        this.conteudo = conteudo;
        this.autor = autor;
        this.post = post;
        this.dataCriacao = LocalDateTime.now();
    }

    /**
     * Verifica se o comentário foi criado recentemente (últimas 24 horas)
     */
    public boolean eRecente() {
        if (dataCriacao == null) {
            return false;
        }
        return dataCriacao.isAfter(LocalDateTime.now().minusHours(24));
    }

    /**
     * Verifica se um aluno é o autor do comentário
     */
    public boolean foiCriadoPor(Aluno aluno) {
        return autor != null && autor.equals(aluno);
    }

    /**
     * Atualiza o conteúdo do comentário
     */
    public void atualizar(String novoConteudo) {
        if (novoConteudo == null || novoConteudo.trim().isEmpty()) {
            throw new IllegalArgumentException("Conteúdo do comentário não pode ser vazio");
        }
        if (novoConteudo.length() > 1000) {
            throw new IllegalArgumentException("Conteúdo do comentário não pode exceder 1000 caracteres");
        }
        this.conteudo = novoConteudo;
    }

    /**
     * Valida se o comentário pertence ao post especificado
     */
    public boolean pertenceAo(Post post) {
        return this.post != null && this.post.equals(post);
    }
}
