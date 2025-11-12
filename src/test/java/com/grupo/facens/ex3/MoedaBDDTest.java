package com.grupo.facens.ex3;

import static org.junit.jupiter.api.Assertions.*;

import com.grupo.facens.ex3.domain.entities.Aluno;
import com.grupo.facens.ex3.repository.AlunoRepository;
import com.grupo.facens.ex3.service.MoedaService;
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
class MoedaBDDTest {

    @Autowired
    private MoedaService moedaService;

    @Autowired
    private AlunoRepository alunoRepository;

    private Aluno aluno;

    @BeforeEach
    void setup() {
        aluno = new Aluno();
        aluno.setNome("Carlos Premium");
        aluno.setEmail("carlos@exemplo.com");
        aluno.setCursosCompletados(12);
        aluno.setPlano(Aluno.TipoPlano.PREMIUM);
        aluno.setMoedas(0);
        aluno.setCreditoCurso(0);

        aluno = alunoRepository.save(aluno);
    }

    @Test
    @DisplayName("BDD3 - Aluno Premium utiliza moedas após completar curso")
    void bdd3_aluno_premium_utiliza_moedas_apos_completar_curso() {
        assertTrue(aluno.temPlanoPremium());
        assertTrue(aluno.getCursosCompletados() >= 12);

        moedaService.distribuirMoedasPorCursoCompleto(aluno);

        assertEquals(36, aluno.getMoedas());

        moedaService.adicionarMoedas(aluno, 3);

        assertEquals(39, aluno.getMoedas());

        boolean conversaoSucesso = moedaService.converterMoedasParaCursos(
            aluno,
            3
        );

        assertTrue(conversaoSucesso);
        assertEquals(36, aluno.getMoedas());
        assertEquals(3, aluno.getCreditoCurso());

        boolean conversaoCripto = moedaService.converterMoedasParaCriptomoedas(
            aluno,
            3
        );

        assertTrue(conversaoCripto);
        assertEquals(33, aluno.getMoedas());

        assertEquals(33, moedaService.consultarSaldo(aluno));
    }

    @Test
    @DisplayName("Verificar opções disponíveis para uso das moedas Premium")
    void verificar_opcoes_uso_moedas_premium() {
        assertTrue(aluno.temPlanoPremium());

        moedaService.adicionarMoedas(aluno, 3);
        assertEquals(3, aluno.getMoedas());

        boolean conversaoCurso = moedaService.converterMoedasParaCursos(
            aluno,
            1
        );

        assertTrue(conversaoCurso);
        assertEquals(2, aluno.getMoedas());
        assertEquals(1, aluno.getCreditoCurso());

        boolean conversaoCripto = moedaService.converterMoedasParaCriptomoedas(
            aluno,
            1
        );

        assertTrue(conversaoCripto);
        assertEquals(1, aluno.getMoedas());

        assertEquals(1, moedaService.consultarSaldo(aluno));
    }

    @Test
    @DisplayName(
        "Testa todas as validações e casos extremos do serviço de moedas"
    )
    void testa_todas_validacoes_servico_moedas() {
        moedaService.adicionarMoedas(null, 5);
        assertFalse(moedaService.converterMoedasParaCursos(null, 3));
        assertFalse(moedaService.converterMoedasParaCriptomoedas(null, 3));
        assertEquals(0, moedaService.consultarSaldo(null));
        moedaService.distribuirMoedasPorCursoCompleto(null);

        int moedasIniciais = aluno.getMoedas();
        moedaService.adicionarMoedas(aluno, 0);
        assertEquals(moedasIniciais, aluno.getMoedas());

        moedaService.adicionarMoedas(aluno, -5);
        assertEquals(moedasIniciais, aluno.getMoedas());

        assertFalse(moedaService.converterMoedasParaCursos(aluno, 0));
        assertFalse(moedaService.converterMoedasParaCursos(aluno, -3));
        assertFalse(moedaService.converterMoedasParaCriptomoedas(aluno, 0));
        assertFalse(moedaService.converterMoedasParaCriptomoedas(aluno, -3));

        assertEquals(0, aluno.getMoedas());

        assertFalse(moedaService.converterMoedasParaCursos(aluno, 5));
        assertEquals(0, aluno.getMoedas());
        assertEquals(0, aluno.getCreditoCurso());

        assertFalse(moedaService.converterMoedasParaCriptomoedas(aluno, 5));
        assertEquals(0, aluno.getMoedas());

        moedaService.adicionarMoedas(aluno, 3);
        assertEquals(3, aluno.getMoedas());

        assertFalse(moedaService.converterMoedasParaCursos(aluno, 5));
        assertEquals(3, aluno.getMoedas());

        assertFalse(moedaService.converterMoedasParaCriptomoedas(aluno, 5));
        assertEquals(3, aluno.getMoedas());

        aluno.setCursosCompletados(0);
        aluno.setMoedas(0);
        aluno = alunoRepository.save(aluno);
        moedaService.distribuirMoedasPorCursoCompleto(aluno);
        assertEquals(0, aluno.getMoedas());

        aluno.setCursosCompletados(5);
        aluno.setMoedas(0);
        aluno = alunoRepository.save(aluno);
        moedaService.distribuirMoedasPorCursoCompleto(aluno);
        assertEquals(15, aluno.getMoedas());
    }
}
