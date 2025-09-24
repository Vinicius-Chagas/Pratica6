package com.grupo.facens.ex3.steps;

import com.grupo.facens.ex3.model.Aluno;
import com.grupo.facens.ex3.repository.AlunoRepository;
import com.grupo.facens.ex3.service.AssinaturaService;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

public class AssinaturaSteps {

    @Autowired
    private AssinaturaService assinaturaService;

    @Autowired
    private AlunoRepository alunoRepository;

    private Aluno aluno;
    private boolean acessoVerificado;

    @Dado("que existe um aluno sem plano ativo")
    public void que_existe_um_aluno_sem_plano_ativo() {
        aluno = new Aluno();
        aluno.setNome("João Silva");
        aluno.setEmail("joao" + System.currentTimeMillis() + "@exemplo.com");
        aluno.setPlano(null);
        aluno.setMoedas(0);
        aluno.setCursosCompletados(0);
        aluno.setCreditoCurso(0);
        aluno = alunoRepository.save(aluno);
        assertNull(aluno.getPlano());
    }

    @Dado("que existe um aluno com plano básico")
    public void que_existe_um_aluno_com_plano_basico() {
        aluno = new Aluno();
        aluno.setNome("Maria Silva");
        aluno.setEmail("maria" + System.currentTimeMillis() + "@exemplo.com");
        aluno.setPlano(Aluno.TipoPlano.BASICO);
        aluno.setMoedas(0);
        aluno.setCursosCompletados(0);
        aluno.setCreditoCurso(0);
        aluno = alunoRepository.save(aluno);
        assertTrue(aluno.temPlanoBasico());
    }

    @Quando("o aluno ativa uma assinatura")
    public void o_aluno_ativa_uma_assinatura() {
        assinaturaService.ativarAssinatura(aluno);
        aluno = alunoRepository.findById(aluno.getId()).orElse(null);
    }

    @Quando("verifico o acesso ao plano básico")
    public void verifico_o_acesso_ao_plano_basico() {
        acessoVerificado = assinaturaService.verificarAcessoPlanBasico(aluno);
    }

    @Então("o aluno deve ter um plano básico")
    public void o_aluno_deve_ter_um_plano_basico() {
        assertNotNull(aluno);
        assertEquals(Aluno.TipoPlano.BASICO, aluno.getPlano());
        assertTrue(aluno.temPlanoBasico());
    }

    @Então("deve ter acesso ao conteúdo do plano básico")
    public void deve_ter_acesso_ao_conteudo_do_plano_basico() {
        assertTrue(assinaturaService.verificarAcessoPlanBasico(aluno));
    }

    @Então("o acesso deve ser confirmado")
    public void o_acesso_deve_ser_confirmado() {
        assertTrue(acessoVerificado);
    }

    @Então("o acesso deve ser negado")
    public void o_acesso_deve_ser_negado() {
        assertFalse(acessoVerificado);
    }
}
