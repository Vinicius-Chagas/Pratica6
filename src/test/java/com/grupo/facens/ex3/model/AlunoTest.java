package com.grupo.facens.ex3.model;

import static org.junit.jupiter.api.Assertions.*;

import com.grupo.facens.ex3.domain.entities.Aluno;
import com.grupo.facens.ex3.domain.enums.TipoPlano;
import org.junit.jupiter.api.Test;

class AlunoTest {

    @Test
    void deveValidarPlanoBasico() {
        Aluno aluno = new Aluno();
        aluno.setPlano(null);

        assertFalse(aluno.temPlanoBasico());

        aluno.setPlano(TipoPlano.BASICO);
        assertTrue(aluno.temPlanoBasico());
        assertFalse(aluno.temPlanoPremium());
    }

    @Test
    void deveValidarPlanoPremium() {
        Aluno aluno = new Aluno();
        aluno.setPlano(TipoPlano.PREMIUM);

        assertTrue(aluno.temPlanoPremium());
        assertFalse(aluno.temPlanoBasico());
    }

    @Test
    void deveGerenciarMoedasECreditos() {
        Aluno aluno = new Aluno();

        aluno.adicionarMoedas(5);
        aluno.adicionarMoedas(-10);
        assertEquals(5, aluno.getMoedas());

        assertFalse(aluno.removerMoedas(-1));
        assertFalse(aluno.removerMoedas(10));
        assertTrue(aluno.removerMoedas(2));
        assertEquals(3, aluno.getMoedas());

        aluno.adicionarCreditosCurso(4);
        aluno.setCreditoCurso(10);
        assertEquals(10, aluno.getCreditoCurso());
    }

    @Test
    void deveInicializarPlanoStatusQuandoNulo() {
        Aluno aluno = new Aluno();
        aluno.setPlanoStatus(null);

        aluno.setMoedas(7);
        aluno.setCursosCompletados(2);
        aluno.setCreditoCurso(1);

        assertEquals(7, aluno.getMoedas());
        assertEquals(2, aluno.getCursosCompletados());
        assertEquals(1, aluno.getCreditoCurso());
    }
}
