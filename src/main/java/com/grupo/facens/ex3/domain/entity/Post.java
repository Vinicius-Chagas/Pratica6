package com.grupo.facens.ex3.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade de domínio representando um Post no fórum.
 * Contém a lógica de negócio relacionada a posts.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, length = 2000)
    private String conteudo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false)
    private Aluno autor;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Comment> comentarios = new ArrayList<>();

    /**
     * Construtor de conveniência
     */
    public Post(String titulo, String conteudo, Aluno autor) {
        this.titulo = titulo;
        this.conteudo = conteudo;
        this.autor = autor;
        this.dataCriacao = LocalDateTime.now();
        this.comentarios = new ArrayList<>();
    }

    /**
     * Adiciona um comentário ao post
     */
    public void adicionarComentario(Comment comentario) {
        if (comentario == null) {
            throw new IllegalArgumentException("Comentário não pode ser nulo");
        }
        comentarios.add(comentario);
        comentario.setPost(this);
    }

    /**
     * Remove um comentário do post
     */
    public void removerComentario(Comment comentario) {
        if (comentario == null) {
            throw new IllegalArgumentException("Comentário não pode ser nulo");
        }
        comentarios.remove(comentario);
        comentario.setPost(null);
    }

    /**
     * Retorna o número de comentários
     */
    public int getNumeroComentarios() {
        return comentarios != null ? comentarios.size() : 0;
    }

    /**
     * Verifica se o post tem comentários
     */
    public boolean temComentarios() {
        return comentarios != null && !comentarios.isEmpty();
    }

    /**
     * Verifica se o post foi criado recentemente (últimas 24 horas)
     */
    public boolean eRecente() {
        if (dataCriacao == null) {
            return false;
        }
        return dataCriacao.isAfter(LocalDateTime.now().minusHours(24));
    }

    /**
     * Verifica se um aluno é o autor do post
     */
    public boolean foiCriadoPor(Aluno aluno) {
        return autor != null && autor.equals(aluno);
    }

    /**
     * Atualiza o conteúdo do post
     */
    public void atualizar(String titulo, String conteudo) {
        if (titulo != null && !titulo.trim().isEmpty()) {
            this.titulo = titulo;
        }
        if (conteudo != null && !conteudo.trim().isEmpty()) {
            this.conteudo = conteudo;
        }
    }
}
