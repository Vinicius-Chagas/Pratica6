package com.grupo.facens.ex3.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.grupo.facens.ex3.model.Aluno;
import com.grupo.facens.ex3.model.Comment;
import com.grupo.facens.ex3.model.Post;
import com.grupo.facens.ex3.repository.CommentRepository;
import com.grupo.facens.ex3.repository.PostRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ForumServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private ForumService forumService;

    private Aluno aluno;

    @BeforeEach
    void setUp() {
        aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("Aluno Teste");
        aluno.setEmail("aluno@test.com");
    }

    @Test
    void deveCriarTopico() {
        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> {
            Post post = invocation.getArgument(0);
            post.setId(10L);
            return post;
        });

        Post post = forumService.criarTopico(
                aluno,
                "Titulo",
                "Conteudo");

        verify(postRepository).save(postCaptor.capture());
        Post salvo = postCaptor.getValue();
        assertThat(salvo.getAutor()).isEqualTo(aluno);
        assertThat(salvo.getTitulo()).isEqualTo("Titulo");
        assertThat(post.getId()).isEqualTo(10L);
    }

    @Test
    void deveComentarTopico() {
        Post post = new Post();
        post.setId(5L);
        when(postRepository.findById(5L)).thenReturn(Optional.of(post));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> {
            Comment comment = invocation.getArgument(0);
            comment.setId(99L);
            return comment;
        });

        Comment comment = forumService.comentarTopico(
                aluno,
                5L,
                "Comentario");

        assertThat(comment.getAutor()).isEqualTo(aluno);
        assertThat(comment.getPost()).isEqualTo(post);
        assertThat(comment.getConteudo()).isEqualTo("Comentario");
        assertThat(comment.getId()).isEqualTo(99L);
    }

    @Test
    void deveLancarExcecaoQuandoTopicoNaoEncontrado() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(
                IllegalArgumentException.class,
                () -> forumService.comentarTopico(aluno, 1L, "falha"));
    }

    @Test
    void deveCalcularAlunoMaisAtivoComPostsEComentarios() {
        Aluno outroAluno = new Aluno();
        outroAluno.setId(2L);
        outroAluno.setNome("Outro");
        outroAluno.setEmail("outro@test.com");

        when(postRepository.findAlunosComTotalPostsPorPeriodo(any(), any()))
                .thenReturn(
                        java.util.Collections.singletonList(
                                new Object[] { aluno, 3L }));
        when(commentRepository.findAlunosComTotalCommentsPorPeriodo(any(), any()))
                .thenReturn(
                        java.util.Arrays.asList(
                                new Object[] { aluno, 2L },
                                new Object[] { outroAluno, 4L }));

        Aluno alunoMaisAtivo = forumService.getAlunoMaisAtivoDoMes();
        assertThat(alunoMaisAtivo).isEqualTo(aluno);
    }

    @Test
    void deveRetornarNuloQuandoNaoHaContribuicoes() {
        when(postRepository.findAlunosComTotalPostsPorPeriodo(any(), any()))
                .thenReturn(java.util.Collections.emptyList());
        when(commentRepository.findAlunosComTotalCommentsPorPeriodo(any(), any()))
                .thenReturn(java.util.Collections.emptyList());

        assertThat(forumService.getAlunoMaisAtivoDoMes()).isNull();
        assertThat(forumService.isAlunoMaisAtivoDoMes(aluno)).isFalse();
    }

    @Test
    void deveVerificarSeAlunoEhMaisAtivo() {
        when(postRepository.findAlunosComTotalPostsPorPeriodo(any(), any()))
                .thenReturn(
                        java.util.Collections.singletonList(
                                new Object[] { aluno, 1L }));
        when(commentRepository.findAlunosComTotalCommentsPorPeriodo(any(), any()))
                .thenReturn(java.util.Collections.emptyList());

        assertThat(forumService.isAlunoMaisAtivoDoMes(aluno)).isTrue();
    }

    @Test
    void deveContarContribuicoesDoAluno() {
        when(postRepository.countByAutorAndDataCriacaoGreaterThanEqual(
                eq(aluno),
                any(LocalDateTime.class))).thenReturn(3L);
        when(commentRepository.countByAutorAndDataCriacaoGreaterThanEqual(
                eq(aluno),
                any(LocalDateTime.class))).thenReturn(4L);

        assertThat(forumService.contarContribuicoes(aluno)).isEqualTo(7);
    }
}
