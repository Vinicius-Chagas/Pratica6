package com.grupo.facens.ex3.domain.valueobject;

import static org.junit.jupiter.api.Assertions.*;

import com.grupo.facens.ex3.domain.enums.TipoPlano;
import org.junit.jupiter.api.Test;

class PlanoStatusTest {

    @Test
    void deveGerenciarMoedasComValidacoesDeEntrada() {
        PlanoStatus status = PlanoStatus.builder()
                .moedas(0)
                .creditoCurso(0)
                .cursosCompletados(0)
                .tipoPlano(TipoPlano.BASICO)
                .build();

        status.creditarMoedas(15);
        status.creditarMoedas(-5);
        assertEquals(15, status.getMoedas());

        assertFalse(status.debitarMoedas(-1));
        assertFalse(status.debitarMoedas(20));
        assertTrue(status.debitarMoedas(5));
        assertEquals(10, status.getMoedas());
    }

    @Test
    void deveAdicionarCreditosERegistrarCursos() {
        PlanoStatus status = new PlanoStatus();

        status.adicionarCreditos(3);
        status.adicionarCreditos(0);
        assertEquals(3, status.getCreditoCurso());

        status.registrarCursoConcluido();
        status.registrarCursoConcluido();
        assertEquals(2, status.getCursosCompletados());
    }
}
