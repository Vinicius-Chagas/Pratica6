package com.grupo.facens.ex3.repository;

import com.grupo.facens.ex3.domain.entities.Aluno;
import com.grupo.facens.ex3.domain.entities.Comment;
import com.grupo.facens.ex3.domain.entities.Post;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostOrderByDataCriacaoAsc(Post post);

    List<Comment> findByAutorOrderByDataCriacaoDesc(Aluno autor);

    long countByAutorAndDataCriacaoGreaterThanEqual(
        Aluno autor,
        LocalDateTime dataInicio
    );

    @Query(
        "SELECT c FROM Comment c WHERE c.dataCriacao >= :dataInicio AND c.dataCriacao <= :dataFim ORDER BY c.dataCriacao DESC"
    )
    List<Comment> findCommentsByPeriodo(
        @Param("dataInicio") LocalDateTime dataInicio,
        @Param("dataFim") LocalDateTime dataFim
    );

    @Query(
        "SELECT COUNT(c) FROM Comment c WHERE c.autor = :autor AND c.dataCriacao >= :dataInicio"
    )
    long countContribuicoesByAutorAndPeriodo(
        @Param("autor") Aluno autor,
        @Param("dataInicio") LocalDateTime dataInicio
    );

    @Query(
        "SELECT c.autor as aluno, COUNT(c) as totalComments FROM Comment c WHERE c.dataCriacao >= :dataInicio AND c.dataCriacao <= :dataFim GROUP BY c.autor ORDER BY COUNT(c) DESC"
    )
    List<Object[]> findAlunosComTotalCommentsPorPeriodo(
        @Param("dataInicio") LocalDateTime dataInicio,
        @Param("dataFim") LocalDateTime dataFim
    );
}
