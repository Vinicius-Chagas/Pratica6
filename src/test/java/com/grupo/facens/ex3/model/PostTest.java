package com.grupo.facens.ex3.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class PostTest {

    @Test
    void deveInicializarListaDeComentarios() {
        Post post = new Post();
        assertNotNull(post.getComentarios());
        assertTrue(post.getComentarios().isEmpty());
    }

    @Test
    void devePermitirAtualizarCamposPrincipais() {
        Aluno autor = new Aluno();
        autor.setId(1L);
        autor.setNome("Autor Teste");
        autor.setEmail("autor@test.com");

        Post post = new Post();
        post.setTitulo("Titulo");
        post.setConteudo("Conteudo");
        post.setAutor(autor);
        post.setDataCriacao(LocalDateTime.now());

        assertEquals("Titulo", post.getTitulo());
        assertEquals("Conteudo", post.getConteudo());
        assertEquals(autor, post.getAutor());
        assertNotNull(post.getDataCriacao());
    }
}
