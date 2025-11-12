package com.grupo.facens.ex3.repository;

import com.grupo.facens.ex3.domain.entities.Curso;
import com.grupo.facens.ex3.domain.enums.Dificuldade;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    List<Curso> findByAtivoTrue();

    List<Curso> findByCategoria(String categoria);

    List<Curso> findByDificuldade(Dificuldade dificuldade);

    List<Curso> findByTituloContainingIgnoreCase(String titulo);

    @Query(
        "SELECT c FROM Curso c WHERE c.ativo = true AND c.categoria = :categoria"
    )
    List<Curso> findCursosAtivosPorCategoria(
        @Param("categoria") String categoria
    );

    @Query(
        "SELECT c FROM Curso c WHERE c.ativo = true AND c.dificuldade = :dificuldade"
    )
    List<Curso> findCursosAtivosPorDificuldade(
        @Param("dificuldade") Dificuldade dificuldade
    );
}
