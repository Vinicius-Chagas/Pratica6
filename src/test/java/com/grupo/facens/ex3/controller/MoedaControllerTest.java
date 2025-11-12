package com.grupo.facens.ex3.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.grupo.facens.ex3.model.Aluno;
import com.grupo.facens.ex3.repository.AlunoRepository;
import com.grupo.facens.ex3.service.MoedaService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MoedaController.class)
@SuppressWarnings("removal")
class MoedaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MoedaService moedaService;

    @MockBean
    private AlunoRepository alunoRepository;

    private Aluno aluno;

    @BeforeEach
    void setUp() {
        aluno = new Aluno();
        aluno.setId(3L);
        aluno.setNome("Moedas");
        aluno.setEmail("moedas@test.com");
        aluno.setMoedas(10);
        aluno.setCreditoCurso(0);
    }

    @Test
    void deveAdicionarMoedas() throws Exception {
        when(alunoRepository.findById(3L)).thenReturn(Optional.of(aluno));
        doAnswer(invocation -> {
            Aluno alvo = invocation.getArgument(0);
            int quantidade = invocation.getArgument(1);
            alvo.setMoedas(alvo.getMoedas() + quantidade);
            return null;
        })
                .when(moedaService)
                .adicionarMoedas(any(Aluno.class), eq(5));

        mockMvc
                .perform(post("/api/moedas/3/adicionar").param("quantidade", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.saldo").value(15));
    }

    @Test
    void deveRetornarNotFoundAoAdicionarMoedasParaAlunoInexistente()
            throws Exception {
        when(alunoRepository.findById(9L)).thenReturn(Optional.empty());

        mockMvc
                .perform(post("/api/moedas/9/adicionar").param("quantidade", "5"))
                .andExpect(status().isNotFound());

        verifyNoInteractions(moedaService);
    }

    @Test
    void deveConverterMoedasParaCursos() throws Exception {
        when(alunoRepository.findById(3L)).thenReturn(Optional.of(aluno));
        when(moedaService.converterMoedasParaCursos(aluno, 4)).thenAnswer(invocation -> {
            aluno.setMoedas(aluno.getMoedas() - 4);
            aluno.setCreditoCurso(aluno.getCreditoCurso() + 4);
            return true;
        });

        mockMvc
                .perform(
                        post("/api/moedas/3/converter-cursos").param("quantidade", "4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sucesso").value(true))
                .andExpect(jsonPath("$.saldo").value(6))
                .andExpect(jsonPath("$.creditos").value(4));
    }

    @Test
    void deveRetornarBadRequestQuandoConversaoDeCursosFalhar() throws Exception {
        when(alunoRepository.findById(3L)).thenReturn(Optional.of(aluno));
        when(moedaService.converterMoedasParaCursos(aluno, 30)).thenReturn(false);

        mockMvc
                .perform(
                        post("/api/moedas/3/converter-cursos").param("quantidade", "30"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.sucesso").value(false));
    }

    @Test
    void deveConverterMoedasParaCriptomoedas() throws Exception {
        when(alunoRepository.findById(3L)).thenReturn(Optional.of(aluno));
        when(moedaService.converterMoedasParaCriptomoedas(aluno, 5)).thenAnswer(invocation -> {
            aluno.setMoedas(aluno.getMoedas() - 5);
            return true;
        });

        mockMvc
                .perform(
                        post("/api/moedas/3/converter-criptomoedas").param(
                                "quantidade",
                                "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sucesso").value(true))
                .andExpect(jsonPath("$.saldo").value(5));
    }

    @Test
    void deveRetornarBadRequestQuandoConversaoCriptoFalhar() throws Exception {
        when(alunoRepository.findById(3L)).thenReturn(Optional.of(aluno));
        when(moedaService.converterMoedasParaCriptomoedas(aluno, 20))
                .thenReturn(false);

        mockMvc
                .perform(
                        post("/api/moedas/3/converter-criptomoedas").param(
                                "quantidade",
                                "20"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.sucesso").value(false));
    }

    @Test
    void deveConsultarSaldo() throws Exception {
        when(alunoRepository.findById(3L)).thenReturn(Optional.of(aluno));
        when(moedaService.consultarSaldo(aluno)).thenReturn(10);

        mockMvc
                .perform(get("/api/moedas/3/saldo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.saldo").value(10));
    }

    @Test
    void deveRetornarNotFoundAoConsultarSaldoDeAlunoInexistente()
            throws Exception {
        when(alunoRepository.findById(50L)).thenReturn(Optional.empty());

        mockMvc
                .perform(get("/api/moedas/50/saldo"))
                .andExpect(status().isNotFound());
    }
}
