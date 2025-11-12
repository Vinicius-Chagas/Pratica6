package com.grupo.facens.ex3.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.grupo.facens.ex3.model.Aluno;
import com.grupo.facens.ex3.repository.AlunoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MoedaServiceTest {

    @Mock
    private AlunoRepository alunoRepository;

    @InjectMocks
    private MoedaService moedaService;

    private Aluno aluno;

    @BeforeEach
    void setUp() {
        aluno = new Aluno();
        aluno.setId(10L);
        aluno.setNome("Aluno Teste");
        aluno.setEmail("teste@faculdade.com");
        aluno.setMoedas(10);
        aluno.setCreditoCurso(0);
        aluno.setCursosCompletados(2);
    }

    @Test
    void deveAdicionarMoedasQuandoQuantidadeValida() {
        moedaService.adicionarMoedas(aluno, 5);

        assertThat(aluno.getMoedas()).isEqualTo(15);
        verify(alunoRepository).save(aluno);
    }

    @Test
    void naoDeveAdicionarMoedasQuandoQuantidadeInvalida() {
        moedaService.adicionarMoedas(aluno, -3);
        verifyNoMoreInteractions(alunoRepository);
    }

    @Test
    void naoDeveAdicionarMoedasQuandoAlunoNulo() {
        moedaService.adicionarMoedas(null, 5);
        verifyNoInteractions(alunoRepository);
    }

    @Test
    void deveConverterMoedasParaCursos() {
        boolean resultado = moedaService.converterMoedasParaCursos(aluno, 4);

        assertThat(resultado).isTrue();
        assertThat(aluno.getMoedas()).isEqualTo(6);
        assertThat(aluno.getCreditoCurso()).isEqualTo(4);
        verify(alunoRepository).save(aluno);
    }

    @Test
    void naoDeveConverterCursosQuandoQuantidadeInvalida() {
        boolean resultado = moedaService.converterMoedasParaCursos(aluno, 0);
        assertThat(resultado).isFalse();
        verifyNoMoreInteractions(alunoRepository);
    }

    @Test
    void naoDeveConverterCursosQuandoSaldoInsuficiente() {
        boolean resultado = moedaService.converterMoedasParaCursos(aluno, 50);
        assertThat(resultado).isFalse();
        verifyNoMoreInteractions(alunoRepository);
    }

    @Test
    void deveConverterMoedasParaCriptomoedas() {
        boolean resultado = moedaService.converterMoedasParaCriptomoedas(
                aluno,
                5);

        assertThat(resultado).isTrue();
        assertThat(aluno.getMoedas()).isEqualTo(5);
        verify(alunoRepository).save(aluno);
    }

    @Test
    void naoDeveConverterCriptoQuandoSaldoInsuficiente() {
        boolean resultado = moedaService.converterMoedasParaCriptomoedas(
                aluno,
                100);

        assertThat(resultado).isFalse();
        verifyNoMoreInteractions(alunoRepository);
    }

    @Test
    void deveConsultarSaldoDoAluno() {
        assertThat(moedaService.consultarSaldo(aluno)).isEqualTo(10);
        assertThat(moedaService.consultarSaldo(null)).isZero();
    }

    @Test
    void deveDistribuirMoedasPorCursoCompleto() {
        aluno.setMoedas(0);
        moedaService.distribuirMoedasPorCursoCompleto(aluno);

        assertThat(aluno.getMoedas()).isEqualTo(6);
        verify(alunoRepository).save(aluno);
    }

    @Test
    void naoDeveDistribuirMoedasQuandoAlunoNulo() {
        moedaService.distribuirMoedasPorCursoCompleto(null);
        verifyNoMoreInteractions(alunoRepository);
    }
}
