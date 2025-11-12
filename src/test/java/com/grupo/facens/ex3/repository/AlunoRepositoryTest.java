package com.grupo.facens.ex3.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.grupo.facens.ex3.model.Aluno;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class AlunoRepositoryTest {

    @Autowired
    private AlunoRepository alunoRepository;

    @Test
    void deveBuscarAlunoPorEmail() {
        Aluno aluno = new Aluno();
        aluno.setNome("Maria Teste");
        aluno.setEmail("maria@test.com");
        aluno.setPlano(Aluno.TipoPlano.BASICO);
        aluno.setCursosCompletados(0);
        aluno.setMoedas(0);
        aluno.setCreditoCurso(0);

        alunoRepository.save(aluno);

        assertThat(alunoRepository.findByEmail("maria@test.com"))
                .isPresent()
                .get()
                .extracting(Aluno::getNome)
                .isEqualTo("Maria Teste");
    }
}
