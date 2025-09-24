package com.grupo.facens.ex3.service;

import com.grupo.facens.ex3.dto.ForumActivityDto;
import com.grupo.facens.ex3.model.Aluno;
import com.grupo.facens.ex3.model.Comment;
import com.grupo.facens.ex3.model.Post;
import com.grupo.facens.ex3.repository.AlunoRepository;
import com.grupo.facens.ex3.repository.CommentRepository;
import com.grupo.facens.ex3.repository.PostRepository;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ForumService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    public Post criarTopico(Aluno aluno, String titulo, String conteudo) {
        Post post = new Post();
        post.setTitulo(titulo);
        post.setConteudo(conteudo);
        post.setAutor(aluno);
        post.setDataCriacao(LocalDateTime.now());

        return postRepository.save(post);
    }

    public Comment comentarTopico(
        Aluno aluno,
        Long topicoId,
        String comentario
    ) {
        Optional<Post> postOptional = postRepository.findById(topicoId);
        if (postOptional.isEmpty()) {
            throw new IllegalArgumentException("Tópico não encontrado");
        }

        Post post = postOptional.get();
        Comment comment = new Comment();
        comment.setConteudo(comentario);
        comment.setAutor(aluno);
        comment.setPost(post);
        comment.setDataCriacao(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    public boolean isAlunoMaisAtivoDoMes(Aluno aluno) {
        Aluno alunoMaisAtivo = getAlunoMaisAtivoDoMes();
        return (
            alunoMaisAtivo != null &&
            alunoMaisAtivo.getId().equals(aluno.getId())
        );
    }

    public Aluno getAlunoMaisAtivoDoMes() {
        LocalDateTime inicioDoMes = YearMonth.now().atDay(1).atStartOfDay();
        LocalDateTime fimDoMes = YearMonth.now()
            .atEndOfMonth()
            .atTime(23, 59, 59);

        List<Object[]> postsData =
            postRepository.findAlunosComTotalPostsPorPeriodo(
                inicioDoMes,
                fimDoMes
            );

        List<Object[]> commentsData =
            commentRepository.findAlunosComTotalCommentsPorPeriodo(
                inicioDoMes,
                fimDoMes
            );

        Map<Long, ForumActivityDto> activityMap = new HashMap<>();

        for (Object[] row : postsData) {
            Aluno aluno = (Aluno) row[0];
            long totalPosts = ((Number) row[1]).longValue();
            activityMap.put(
                aluno.getId(),
                new ForumActivityDto(aluno, totalPosts, 0)
            );
        }

        for (Object[] row : commentsData) {
            Aluno aluno = (Aluno) row[0];
            long totalComments = ((Number) row[1]).longValue();

            ForumActivityDto activity = activityMap.get(aluno.getId());
            if (activity == null) {
                activityMap.put(
                    aluno.getId(),
                    new ForumActivityDto(aluno, 0, totalComments)
                );
            } else {
                activity.addComments(totalComments);
            }
        }

        return activityMap
            .values()
            .stream()
            .max((a, b) ->
                Long.compare(
                    a.getTotalContribuicoes(),
                    b.getTotalContribuicoes()
                )
            )
            .map(ForumActivityDto::getAluno)
            .orElse(null);
    }

    public int contarContribuicoes(Aluno aluno) {
        long totalPosts =
            postRepository.countByAutorAndDataCriacaoGreaterThanEqual(
                aluno,
                LocalDateTime.now()
                    .withDayOfMonth(1)
                    .withHour(0)
                    .withMinute(0)
                    .withSecond(0)
            );

        long totalComentarios =
            commentRepository.countByAutorAndDataCriacaoGreaterThanEqual(
                aluno,
                LocalDateTime.now()
                    .withDayOfMonth(1)
                    .withHour(0)
                    .withMinute(0)
                    .withSecond(0)
            );

        return (int) (totalPosts + totalComentarios);
    }
}
