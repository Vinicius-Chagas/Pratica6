package com.grupo.facens.ex3.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.grupo.facens.ex3.domain.entities.Aluno;
import com.grupo.facens.ex3.domain.enums.TipoPlano;
import com.grupo.facens.ex3.repository.AlunoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AssinaturaServiceTest {

    @Mock
    private AlunoRepository alunoRepository;

    @InjectMocks
    private AssinaturaService assinaturaService;

    private Aluno aluno;

    @BeforeEach
    void setUp() {
        aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("Maria");
        aluno.setEmail("maria@test.com");
        aluno.setPlano(null);
    }

    @Test
    void deveAtivarPlanoBasicoQuandoAlunoValido() {
        assinaturaService.ativarAssinatura(aluno);

        assertThat(aluno.getPlano()).isEqualTo(TipoPlano.BASICO);
        verify(alunoRepository).save(aluno);
    }

    @Test
    void naoDeveAtivarAssinaturaQuandoAlunoNulo() {
        assinaturaService.ativarAssinatura(null);
        verifyNoInteractions(alunoRepository);
    }

    @Test
    void deveVerificarAcessoPlanoBasico() {
        aluno.setPlano(TipoPlano.BASICO);
        assertThat(assinaturaService.verificarAcessoPlanBasico(aluno)).isTrue();
        assertThat(assinaturaService.verificarAcessoPlanBasico(null)).isFalse();
    }

    @Test
    void deveRetornarFalsoQuandoNaoPossuiPlanoBasico() {
        aluno.setPlano(TipoPlano.PREMIUM);
        assertThat(assinaturaService.verificarAcessoPlanBasico(aluno)).isFalse();
    }
}
