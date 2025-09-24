package com.grupo.facens.ex3.steps;

import com.grupo.facens.ex3.model.Aluno;
import com.grupo.facens.ex3.repository.AlunoRepository;
import com.grupo.facens.ex3.service.MoedaService;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

public class MoedasSteps {

    @Autowired
    private MoedaService moedaService;

    @Autowired
    private AlunoRepository alunoRepository;

    private Aluno aluno;
    private boolean conversaoSucesso;
    private int saldoConsultado;

    @Dado("que existe um aluno premium com {int} cursos completados")
    public void que_existe_um_aluno_premium_com_cursos_completados(int cursosCompletados) {
        aluno = new Aluno();
        aluno.setNome("Carlos Premium");
        aluno.setEmail("carlos" + System.currentTimeMillis() + "@exemplo.com");
        aluno.setCursosCompletados(cursosCompletados);
        aluno.setPlano(Aluno.TipoPlano.PREMIUM);
        aluno.setMoedas(0);
        aluno.setCreditoCurso(0);
        aluno = alunoRepository.save(aluno);

        assertTrue(aluno.temPlanoPremium());
        assertEquals(cursosCompletados, aluno.getCursosCompletados());
    }

    @Dado("o aluno possui {int} moedas inicialmente")
    public void o_aluno_possui_moedas_inicialmente(int moedasIniciais) {
        assertEquals(moedasIniciais, aluno.getMoedas());
    }

    @Dado("que existe um aluno premium com moedas disponíveis")
    public void que_existe_um_aluno_premium_com_moedas_disponiveis() {
        aluno = new Aluno();
        aluno.setNome("Ana Premium");
        aluno.setEmail("ana" + System.currentTimeMillis() + "@exemplo.com");
        aluno.setPlano(Aluno.TipoPlano.PREMIUM);
        aluno.setMoedas(10);
        aluno.setCursosCompletados(0);
        aluno.setCreditoCurso(0);
        aluno = alunoRepository.save(aluno);

        assertTrue(aluno.temPlanoPremium());
        assertTrue(aluno.getMoedas() > 0);
    }

    @Dado("que existe um aluno premium sem moedas")
    public void que_existe_um_aluno_premium_sem_moedas() {
        aluno = new Aluno();
        aluno.setNome("Pedro Premium");
        aluno.setEmail("pedro" + System.currentTimeMillis() + "@exemplo.com");
        aluno.setPlano(Aluno.TipoPlano.PREMIUM);
        aluno.setMoedas(0);
        aluno.setCursosCompletados(0);
        aluno.setCreditoCurso(0);
        aluno = alunoRepository.save(aluno);

        assertTrue(aluno.temPlanoPremium());
        assertEquals(0, aluno.getMoedas());
    }

    @Dado("que existe um aluno premium com moedas")
    public void que_existe_um_aluno_premium_com_moedas() {
        aluno = new Aluno();
        aluno.setNome("Sofia Premium");
        aluno.setEmail("sofia" + System.currentTimeMillis() + "@exemplo.com");
        aluno.setPlano(Aluno.TipoPlano.PREMIUM);
        aluno.setMoedas(25);
        aluno.setCursosCompletados(0);
        aluno.setCreditoCurso(0);
        aluno = alunoRepository.save(aluno);

        assertTrue(aluno.temPlanoPremium());
        assertTrue(aluno.getMoedas() > 0);
    }

    @Quando("o sistema distribui moedas por cursos completados")
    public void o_sistema_distribui_moedas_por_cursos_completados() {
        moedaService.distribuirMoedasPorCursoCompleto(aluno);
        aluno = alunoRepository.findById(aluno.getId()).orElse(null);
    }

    @Quando("pode adicionar mais {int} moedas")
    public void pode_adicionar_mais_moedas(int quantidade) {
        moedaService.adicionarMoedas(aluno, quantidade);
        aluno = alunoRepository.findById(aluno.getId()).orElse(null);
    }

    @Quando("pode converter {int} moedas para cursos")
    public void pode_converter_moedas_para_cursos(int quantidade) {
        conversaoSucesso = moedaService.converterMoedasParaCursos(aluno, quantidade);
        aluno = alunoRepository.findById(aluno.getId()).orElse(null);
    }

    @Quando("pode converter {int} moedas para criptomoedas")
    public void pode_converter_moedas_para_criptomoedas(int quantidade) {
        conversaoSucesso = moedaService.converterMoedasParaCriptomoedas(aluno, quantidade);
        aluno = alunoRepository.findById(aluno.getId()).orElse(null);
    }

    @Quando("o aluno converte moedas para cursos")
    public void o_aluno_converte_moedas_para_cursos() {
        int moedasAntes = aluno.getMoedas();
        int creditosAntes = aluno.getCreditoCurso();

        conversaoSucesso = moedaService.converterMoedasParaCursos(aluno, 2);
        aluno = alunoRepository.findById(aluno.getId()).orElse(null);

        if (conversaoSucesso) {
            assertEquals(moedasAntes - 2, aluno.getMoedas());
            assertEquals(creditosAntes + 2, aluno.getCreditoCurso());
        }
    }

    @Quando("o aluno converte moedas para criptomoedas")
    public void o_aluno_converte_moedas_para_criptomoedas() {
        int moedasAntes = aluno.getMoedas();

        conversaoSucesso = moedaService.converterMoedasParaCriptomoedas(aluno, 3);
        aluno = alunoRepository.findById(aluno.getId()).orElse(null);

        if (conversaoSucesso) {
            assertEquals(moedasAntes - 3, aluno.getMoedas());
        }
    }

    @Quando("o aluno tenta converter {int} moedas para cursos")
    public void o_aluno_tenta_converter_moedas_para_cursos(int quantidade) {
        conversaoSucesso = moedaService.converterMoedasParaCursos(aluno, quantidade);
        aluno = alunoRepository.findById(aluno.getId()).orElse(null);
    }

    @Quando("consulto o saldo do aluno")
    public void consulto_o_saldo_do_aluno() {
        saldoConsultado = moedaService.consultarSaldo(aluno);
    }

    @Então("o aluno deve receber {int} moedas")
    public void o_aluno_deve_receber_moedas(int moedasEsperadas) {
        assertEquals(moedasEsperadas, aluno.getMoedas());
    }

    @Então("o saldo final deve ser {int} moedas")
    public void o_saldo_final_deve_ser_moedas(int saldoEsperado) {
        assertEquals(saldoEsperado, aluno.getMoedas());
        assertEquals(saldoEsperado, moedaService.consultarSaldo(aluno));
    }

    @Então("as moedas devem ser deduzidas")
    public void as_moedas_devem_ser_deduzidas() {
        assertTrue(conversaoSucesso);
        // A verificação específica de valores é feita no @Quando
    }

    @Então("os créditos de curso devem ser adicionados")
    public void os_creditos_de_curso_devem_ser_adicionados() {
        assertTrue(conversaoSucesso);
        assertTrue(aluno.getCreditoCurso() > 0);
    }

    @Então("as moedas devem ser deduzidas do saldo")
    public void as_moedas_devem_ser_deduzidas_do_saldo() {
        assertTrue(conversaoSucesso);
        // A verificação específica de valores é feita no @Quando
    }

    @Então("a conversão deve falhar")
    public void a_conversao_deve_falhar() {
        assertFalse(conversaoSucesso);
    }

    @Então("o saldo deve permanecer {int}")
    public void o_saldo_deve_permanecer(int saldoEsperado) {
        assertEquals(saldoEsperado, aluno.getMoedas());
    }

    @Então("devo receber o valor correto de moedas")
    public void devo_receber_o_valor_correto_de_moedas() {
        assertEquals(aluno.getMoedas(), saldoConsultado);
        assertTrue(saldoConsultado > 0);
    }
}
