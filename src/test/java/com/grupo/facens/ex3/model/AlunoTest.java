package com.grupo.facens.ex3.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AlunoTest {

    @Test
    void deveValidarPlanoBasico() {
        Aluno aluno = new Aluno();
        aluno.setPlano(null);

        assertFalse(aluno.temPlanoBasico());

        aluno.setPlano(Aluno.TipoPlano.BASICO);
        assertTrue(aluno.temPlanoBasico());
        assertFalse(aluno.temPlanoPremium());
    }

    @Test
    void deveValidarPlanoPremium() {
        Aluno aluno = new Aluno();
        aluno.setPlano(Aluno.TipoPlano.PREMIUM);

        assertTrue(aluno.temPlanoPremium());
        assertFalse(aluno.temPlanoBasico());
    }
}
