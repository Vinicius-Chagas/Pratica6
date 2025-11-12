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
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Test
    void deveExecutarConsultasDoRepositorio() {
        Aluno autor = salvarAluno("autor@test.com");
        Aluno outroAutor = salvarAluno("outro@test.com");
        Post post = salvarPost(autor, "Topico 1");

        LocalDateTime agora = LocalDateTime.now();
        Comment primeiroComentario = salvarComentario(
                post,
                autor,
                "Primeiro",
                agora.minusMinutes(5));
        salvarComentario(
                post,
                outroAutor,
                "Segundo",
                agora);

        List<Comment> comentariosPorPost = commentRepository.findByPostOrderByDataCriacaoAsc(
                post);
        assertThat(comentariosPorPost)
                .hasSize(2)
                .first()
                .isEqualTo(primeiroComentario);

        List<Comment> comentariosPorAutor = commentRepository.findByAutorOrderByDataCriacaoDesc(
                autor);
        assertThat(comentariosPorAutor)
                .hasSize(1)
                .first()
                .isEqualTo(primeiroComentario);

        LocalDateTime inicioPeriodo = agora.minusDays(1);
        LocalDateTime fimPeriodo = agora.plusDays(1);

        List<Comment> comentariosPeriodo = commentRepository.findCommentsByPeriodo(
                inicioPeriodo,
                fimPeriodo);
        assertThat(comentariosPeriodo).hasSize(2);

        long totalDoAutor = commentRepository.countByAutorAndDataCriacaoGreaterThanEqual(
                autor,
                agora.minusDays(1));
        assertThat(totalDoAutor).isEqualTo(1);

        long contribuicoes = commentRepository.countContribuicoesByAutorAndPeriodo(
                outroAutor,
                agora.minusDays(1));
        assertThat(contribuicoes).isEqualTo(1);

        List<Object[]> ranking = commentRepository.findAlunosComTotalCommentsPorPeriodo(
                inicioPeriodo,
                fimPeriodo);
        assertThat(ranking)
                .extracting(result -> (Aluno) result[0])
                .containsExactlyInAnyOrder(autor, outroAutor);
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

    private Post salvarPost(Aluno autor, String titulo) {
        Post post = new Post();
        post.setAutor(autor);
        post.setTitulo(titulo);
        post.setConteudo("Conteudo " + titulo);
        post.setDataCriacao(LocalDateTime.now());
        return postRepository.save(post);
    }

    private Comment salvarComentario(
            Post post,
            Aluno autor,
            String conteudo,
            LocalDateTime data) {
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setAutor(autor);
        comment.setConteudo(conteudo);
        comment.setDataCriacao(data);
        return commentRepository.save(comment);
    }
}
