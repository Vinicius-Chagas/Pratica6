package com.grupo.facens.ex3.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.grupo.facens.ex3.domain.entities.Aluno;
import com.grupo.facens.ex3.domain.entities.Curso;
import com.grupo.facens.ex3.domain.enums.Dificuldade;
import com.grupo.facens.ex3.domain.enums.TipoPlano;
import com.grupo.facens.ex3.infrastructure.ai.TutorService;
import com.grupo.facens.ex3.repository.AlunoRepository;
import com.grupo.facens.ex3.repository.CursoRepository;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TutorController.class)
@Import(TutorControllerTest.MockConfig.class)
class TutorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TutorService tutorService;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    private Aluno aluno;
    private Curso curso;

    @BeforeEach
    void setUp() {
        org.mockito.Mockito.reset(tutorService, alunoRepository, cursoRepository);

        aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("João Silva");
        aluno.setEmail("joao@test.com");
        aluno.setPlano(TipoPlano.PREMIUM);
        aluno.setCursosCompletados(5);
        aluno.setMoedas(100);

        curso = new Curso();
        curso.setId(1L);
        curso.setTitulo("Java Básico");
        curso.setCategoria("Programação");
        curso.setDificuldade(Dificuldade.INICIANTE);
        curso.setAtivo(true);
    }

    @Test
    void deveSugerirProximosCursos() throws Exception {
        String sugestao = "Sugestão de cursos mockada";
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(cursoRepository.findByAtivoTrue()).thenReturn(Arrays.asList(curso));
        when(tutorService.sugerirProximosCursos(any(Aluno.class), anyList()))
                .thenReturn(sugestao);

        mockMvc.perform(get("/api/tutor/1/sugerir-cursos"))
                .andExpect(status().isOk())
                .andExpect(content().string(sugestao));
    }

    @Test
    void deveRetornarNotFoundQuandoAlunoNaoExiste() throws Exception {
        when(alunoRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/tutor/999/sugerir-cursos"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveSugerirEstrategiasEstudo() throws Exception {
        String estrategias = "Estratégias de estudo mockadas";
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(tutorService.sugerirEstrategiasEstudo(any(Aluno.class)))
                .thenReturn(estrategias);

        mockMvc.perform(get("/api/tutor/1/estrategias-estudo"))
                .andExpect(status().isOk())
                .andExpect(content().string(estrategias));
    }

    @Test
    void deveRetornarNotFoundQuandoAlunoNaoExisteParaEstrategias() throws Exception {
        when(alunoRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/tutor/999/estrategias-estudo"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveSugerirCursosPorCategoria() throws Exception {
        String sugestao = "Cursos de programação recomendados";
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(cursoRepository.findByCategoria("Programação"))
                .thenReturn(Arrays.asList(curso));
        when(tutorService.sugerirCursosPorCategoria(
                any(Aluno.class), eq("Programação"), anyList()))
                .thenReturn(sugestao);

        mockMvc.perform(get("/api/tutor/1/categoria/Programação"))
                .andExpect(status().isOk())
                .andExpect(content().string(sugestao));
    }

    @Test
    void deveRetornarNotFoundQuandoAlunoNaoExisteParaCategoria() throws Exception {
        when(alunoRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/tutor/999/categoria/Programação"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveSugerirCursosPorDificuldade() throws Exception {
        String sugestao = "Cursos intermediários recomendados";
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(cursoRepository.findByDificuldade(Dificuldade.INTERMEDIARIO))
                .thenReturn(Arrays.asList(curso));
        when(tutorService.sugerirCursosPorDificuldade(
                any(Aluno.class), eq(Dificuldade.INTERMEDIARIO), anyList()))
                .thenReturn(sugestao);

        mockMvc.perform(get("/api/tutor/1/dificuldade/INTERMEDIARIO"))
                .andExpect(status().isOk())
                .andExpect(content().string(sugestao));
    }

    @Test
    void deveRetornarNotFoundQuandoAlunoNaoExisteParaDificuldade() throws Exception {
        when(alunoRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/tutor/999/dificuldade/INTERMEDIARIO"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveFornecerDicasPersonalizadas() throws Exception {
        String dicas = "Dicas personalizadas mockadas";
        String contexto = "Preciso melhorar minhas habilidades";
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(tutorService.fornecerDicasPersonalizadas(
                any(Aluno.class), eq(contexto)))
                .thenReturn(dicas);

        mockMvc.perform(post("/api/tutor/1/dicas")
                .param("contexto", contexto))
                .andExpect(status().isOk())
                .andExpect(content().string(dicas));
    }

    @Test
    void deveRetornarNotFoundParaDicasQuandoAlunoNaoExiste() throws Exception {
        when(alunoRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/tutor/999/dicas")
                .param("contexto", "contexto"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornarInternalServerErrorQuandoOcorreExcecaoGenericaEmSugerirCursos() throws Exception {
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(cursoRepository.findByAtivoTrue()).thenThrow(new RuntimeException("Erro inesperado"));

        mockMvc.perform(get("/api/tutor/1/sugerir-cursos"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deveRetornarInternalServerErrorQuandoOcorreExcecaoGenericaEmEstrategias() throws Exception {
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        doThrow(new RuntimeException("Erro inesperado"))
                .when(tutorService).sugerirEstrategiasEstudo(any(Aluno.class));

        mockMvc.perform(get("/api/tutor/1/estrategias-estudo"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deveRetornarInternalServerErrorQuandoOcorreExcecaoGenericaEmCategoria() throws Exception {
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(cursoRepository.findByCategoria("Programação"))
                .thenThrow(new RuntimeException("Erro inesperado"));

        mockMvc.perform(get("/api/tutor/1/categoria/Programação"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deveRetornarInternalServerErrorQuandoOcorreExcecaoGenericaEmDificuldade() throws Exception {
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(cursoRepository.findByDificuldade(Dificuldade.INTERMEDIARIO))
                .thenThrow(new RuntimeException("Erro inesperado"));

        mockMvc.perform(get("/api/tutor/1/dificuldade/INTERMEDIARIO"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deveRetornarInternalServerErrorQuandoOcorreExcecaoGenericaEmDicas() throws Exception {
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        doThrow(new RuntimeException("Erro inesperado"))
                .when(tutorService).fornecerDicasPersonalizadas(any(Aluno.class), any(String.class));

        mockMvc.perform(post("/api/tutor/1/dicas")
                .param("contexto", "contexto"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deveRetornarInternalServerErrorQuandoTutorServiceLancaExcecaoEmSugerirCursos() throws Exception {
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(cursoRepository.findByAtivoTrue()).thenReturn(Arrays.asList(curso));
        doThrow(new RuntimeException("Erro no serviço"))
                .when(tutorService).sugerirProximosCursos(any(Aluno.class), anyList());

        mockMvc.perform(get("/api/tutor/1/sugerir-cursos"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deveRetornarInternalServerErrorQuandoTutorServiceLancaExcecaoEmCategoria() throws Exception {
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(cursoRepository.findByCategoria("Programação")).thenReturn(Arrays.asList(curso));
        doThrow(new RuntimeException("Erro no serviço"))
                .when(tutorService).sugerirCursosPorCategoria(any(Aluno.class), eq("Programação"), anyList());

        mockMvc.perform(get("/api/tutor/1/categoria/Programação"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deveRetornarInternalServerErrorQuandoTutorServiceLancaExcecaoEmDificuldade() throws Exception {
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(cursoRepository.findByDificuldade(Dificuldade.INTERMEDIARIO)).thenReturn(Arrays.asList(curso));
        doThrow(new RuntimeException("Erro no serviço"))
                .when(tutorService)
                .sugerirCursosPorDificuldade(any(Aluno.class), eq(Dificuldade.INTERMEDIARIO), anyList());

        mockMvc.perform(get("/api/tutor/1/dificuldade/INTERMEDIARIO"))
                .andExpect(status().isInternalServerError());
    }

    @TestConfiguration
    static class MockConfig {

        @Bean
        @Primary
        public TutorService tutorService() {
            return org.mockito.Mockito.mock(TutorService.class);
        }

        @Bean
        @Primary
        public AlunoRepository alunoRepository() {
            return org.mockito.Mockito.mock(AlunoRepository.class);
        }

        @Bean
        @Primary
        public CursoRepository cursoRepository() {
            return org.mockito.Mockito.mock(CursoRepository.class);
        }
    }
}
