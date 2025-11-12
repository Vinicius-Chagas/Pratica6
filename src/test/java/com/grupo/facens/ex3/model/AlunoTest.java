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
}
