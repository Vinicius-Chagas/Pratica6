package com.grupo.facens.ex3.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.grupo.facens.ex3.domain.entities.Aluno;
import com.grupo.facens.ex3.domain.enums.TipoPlano;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class AlunoRepositoryTest {

    @Autowired
    private AlunoRepository alunoRepository;

    @Test
    void deveBuscarAlunoPorEmail() {
        Aluno aluno = new Aluno();
        aluno.setNome("Maria Teste");
        aluno.setEmail("maria@test.com");
        aluno.setPlano(TipoPlano.BASICO);
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
