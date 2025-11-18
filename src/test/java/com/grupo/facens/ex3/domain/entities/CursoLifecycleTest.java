package com.grupo.facens.ex3.domain.entities;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CursoLifecycleTest {

    @Test
    void devePopularCamposDeAuditoriaDurantePersistenciaEAtualizacao() {
        Curso curso = new Curso();
        curso.setAtivo(null);

        curso.onCreate();

        assertThat(curso.getDataCriacao()).isNotNull();
        assertThat(curso.getAtivo()).isTrue();

        curso.onUpdate();
        assertThat(curso.getDataAtualizacao()).isNotNull();
    }
}
