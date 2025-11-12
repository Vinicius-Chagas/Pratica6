package com.grupo.facens.ex3.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.grupo.facens.ex3.model.Aluno;
import com.grupo.facens.ex3.model.Comment;
import com.grupo.facens.ex3.model.Post;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Test
    void deveExecutarConsultasPersonalizadas() {
        Aluno autorPrincipal = salvarAluno("principal@test.com");
        Aluno autorSecundario = salvarAluno("secundario@test.com");

        LocalDateTime agora = LocalDateTime.now();
        Post postPrincipal = salvarPost(
                autorPrincipal,
                "Primeiro post",
                agora.minusHours(2));
        Post segundoPostPrincipal = salvarPost(
                autorPrincipal,
                "Segundo post",
                agora.minusHours(1));
        salvarPost(
                autorSecundario,
                "Outro assunto",
                agora.minusHours(3));

        salvarComentario(postPrincipal, autorSecundario, agora);
        salvarComentario(postPrincipal, autorPrincipal, agora.plusMinutes(5));
        salvarComentario(segundoPostPrincipal, autorPrincipal, agora.plusMinutes(10));

        List<Post> postsPorAutor = postRepository.findByAutorOrderByDataCriacaoDesc(
                autorPrincipal);
        assertThat(postsPorAutor).hasSize(2);
        assertThat(postsPorAutor.get(0).getTitulo()).isEqualTo("Segundo post");

        List<Post> postsComTitulo = postRepository.findByTituloContainingIgnoreCase(
                "post");
        assertThat(postsComTitulo).hasSize(2);

        List<Post> ordenadosPorComentarios = postRepository.findPostsOrderByTotalComentarios();
        assertThat(ordenadosPorComentarios.get(0).getId())
                .isEqualTo(postPrincipal.getId());

        LocalDateTime inicioPeriodo = agora.minusDays(1);
        LocalDateTime fimPeriodo = agora.plusDays(1);

        List<Post> postsNoPeriodo = postRepository.findPostsByPeriodo(
                inicioPeriodo,
                fimPeriodo);
        assertThat(postsNoPeriodo).hasSize(3);

        long totalPostagensAutorPrincipal = postRepository.countByAutorAndDataCriacaoGreaterThanEqual(
                autorPrincipal,
                agora.minusDays(1));
        assertThat(totalPostagensAutorPrincipal).isEqualTo(2);

        List<Object[]> ranking = postRepository.findAlunosComTotalPostsPorPeriodo(
                inicioPeriodo,
                fimPeriodo);
        assertThat(ranking).hasSize(2);
        assertThat(ranking.get(0)[0]).isEqualTo(autorPrincipal);
        assertThat(((Number) ranking.get(0)[1]).longValue()).isEqualTo(2L);
    }

    private Aluno salvarAluno(String email) {
        Aluno aluno = new Aluno();
        aluno.setNome(email.split("@")[0]);
        aluno.setEmail(email);
        aluno.setPlano(Aluno.TipoPlano.BASICO);
        aluno.setCursosCompletados(0);
        aluno.setMoedas(0);
        aluno.setCreditoCurso(0);
        return alunoRepository.save(aluno);
    }

    private Post salvarPost(Aluno autor, String titulo, LocalDateTime data) {
        Post post = new Post();
        post.setAutor(autor);
        post.setTitulo(titulo);
        post.setConteudo("Conteudo " + titulo);
        post.setDataCriacao(data);
        return postRepository.save(post);
    }

    private void salvarComentario(
            Post post,
            Aluno autor,
            LocalDateTime data) {
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setAutor(autor);
        comment.setConteudo("Comentario");
        comment.setDataCriacao(data);
        commentRepository.save(comment);
    }
}
