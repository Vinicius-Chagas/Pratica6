package com.grupo.facens.ex3;

import static org.junit.jupiter.api.Assertions.*;

import com.grupo.facens.ex3.domain.entities.Aluno;
import com.grupo.facens.ex3.domain.enums.TipoPlano;
import com.grupo.facens.ex3.repository.AlunoRepository;
import com.grupo.facens.ex3.service.AssinaturaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AssinaturaBDDTest {

    @Autowired
    private AssinaturaService assinaturaService;

    @Autowired
    private AlunoRepository alunoRepository;

    private Aluno aluno;

    @BeforeEach
    void setup() {
        aluno = new Aluno();
        aluno.setNome("João Silva");
        aluno.setEmail("joao@exemplo.com");
        aluno.setPlano(null);
        aluno.setMoedas(0);
        aluno.setCursosCompletados(0);
        aluno.setCreditoCurso(0);

        aluno = alunoRepository.save(aluno);
    }

    @Test
    @DisplayName("BDD1 - Aluno assina plano básico com sucesso")
    void bdd1_aluno_assina_plano_basico_com_sucesso() {
        assertNotNull(aluno);
        assertNull(aluno.getPlano());

        assinaturaService.ativarAssinatura(aluno);
        assinaturaService.ativarAssinatura(null);

        Aluno alunoAtualizado = alunoRepository
            .findById(aluno.getId())
            .orElse(null);

        assertNotNull(alunoAtualizado);
        assertEquals(TipoPlano.BASICO, alunoAtualizado.getPlano());
        assertTrue(alunoAtualizado.temPlanoBasico());
        assertTrue(
            assinaturaService.verificarAcessoPlanBasico(alunoAtualizado)
        );
        assertFalse(assinaturaService.verificarAcessoPlanBasico(null));
    }

    @Test
    @DisplayName("Testes métodos de plano")
    void testar_funcoes_planos() {
        assinaturaService.ativarAssinatura(aluno);
        assertTrue(aluno.temPlanoBasico());
        assertFalse(aluno.temPlanoPremium());

        aluno.setPlano(TipoPlano.PREMIUM);
        aluno = alunoRepository.save(aluno);
        assertFalse(aluno.temPlanoBasico());
        assertTrue(aluno.temPlanoPremium());
    }
}
