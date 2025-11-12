package com.grupo.facens.ex3.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class CommentTest {

    @Test
    void devePermitirDefinirCampos() {
        Aluno autor = new Aluno();
        autor.setId(5L);
        autor.setNome("Ana");
        autor.setEmail("ana@test.com");

        Post post = new Post();
        post.setId(3L);
        post.setTitulo("Topico");

        Comment comment = new Comment();
        comment.setAutor(autor);
        comment.setPost(post);
        comment.setConteudo("Conteudo do comentario");
        comment.setDataCriacao(LocalDateTime.now());

        assertEquals(autor, comment.getAutor());
        assertEquals(post, comment.getPost());
        assertEquals("Conteudo do comentario", comment.getConteudo());
        assertNotNull(comment.getDataCriacao());
    }
}
