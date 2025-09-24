package com.grupo.facens.ex3.repository;

import com.grupo.facens.ex3.model.Aluno;
import com.grupo.facens.ex3.model.Post;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByAutorOrderByDataCriacaoDesc(Aluno autor);

    List<Post> findByOrderByDataCriacaoDesc();

    List<Post> findByTituloContainingIgnoreCase(String titulo);

    @Query(
        "SELECT p FROM Post p WHERE p.dataCriacao >= :dataInicio AND p.dataCriacao <= :dataFim ORDER BY p.dataCriacao DESC"
    )
    List<Post> findPostsByPeriodo(
        @Param("dataInicio") LocalDateTime dataInicio,
        @Param("dataFim") LocalDateTime dataFim
    );

    @Query("SELECT p FROM Post p ORDER BY SIZE(p.comentarios) DESC")
    List<Post> findPostsOrderByTotalComentarios();

    long countByAutorAndDataCriacaoGreaterThanEqual(
        Aluno autor,
        LocalDateTime dataInicio
    );

    @Query(
        "SELECT p.autor as aluno, COUNT(p) as totalPosts FROM Post p WHERE p.dataCriacao >= :dataInicio AND p.dataCriacao <= :dataFim GROUP BY p.autor ORDER BY COUNT(p) DESC"
    )
    List<Object[]> findAlunosComTotalPostsPorPeriodo(
        @Param("dataInicio") LocalDateTime dataInicio,
        @Param("dataFim") LocalDateTime dataFim
    );
}
