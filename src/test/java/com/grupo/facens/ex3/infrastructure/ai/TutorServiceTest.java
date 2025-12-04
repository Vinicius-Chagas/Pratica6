package com.grupo.facens.ex3.infrastructure.ai;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.grupo.facens.ex3.domain.entities.Aluno;
import com.grupo.facens.ex3.domain.entities.Curso;
import com.grupo.facens.ex3.domain.enums.Dificuldade;
import com.grupo.facens.ex3.domain.enums.TipoPlano;
import dev.langchain4j.model.chat.ChatLanguageModel;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TutorServiceTest {

    @Mock
    private ChatLanguageModel model;

    @InjectMocks
    private TutorService tutorService;

    private Aluno aluno;
    private List<Curso> cursos;

    @BeforeEach
    void setUp() {
        aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("João Silva");
        aluno.setEmail("joao@test.com");
        aluno.setPlano(TipoPlano.PREMIUM);
        aluno.setCursosCompletados(5);
        aluno.setMoedas(100);
        aluno.setCreditoCurso(3);

        Curso curso1 = new Curso();
        curso1.setId(1L);
        curso1.setTitulo("Java Básico");
        curso1.setCategoria("Programação");
        curso1.setDificuldade(Dificuldade.INICIANTE);
        curso1.setAtivo(true);

        Curso curso2 = new Curso();
        curso2.setId(2L);
        curso2.setTitulo("Spring Boot Avançado");
        curso2.setCategoria("Programação");
        curso2.setDificuldade(Dificuldade.AVANCADO);
        curso2.setAtivo(true);

        Curso curso3 = new Curso();
        curso3.setId(3L);
        curso3.setTitulo("Design Patterns");
        curso3.setCategoria("Arquitetura");
        curso3.setDificuldade(Dificuldade.INTERMEDIARIO);
        curso3.setAtivo(true);

        cursos = Arrays.asList(curso1, curso2, curso3);
    }

    @Test
    void deveSugerirProximosCursos() {
        String respostaEsperada = "Sugestão de cursos mockada";
        when(model.generate(anyString())).thenReturn(respostaEsperada);

        String resultado = tutorService.sugerirProximosCursos(aluno, cursos);

        assertThat(resultado).isEqualTo(respostaEsperada);
    }

    @Test
    void deveSugerirProximosCursosComListaVazia() {
        String respostaEsperada = "Sugestão sem cursos";
        when(model.generate(anyString())).thenReturn(respostaEsperada);

        String resultado = tutorService.sugerirProximosCursos(aluno, Collections.emptyList());

        assertThat(resultado).isEqualTo(respostaEsperada);
    }

    @Test
    void deveSugerirProximosCursosComAlunoSemPlano() {
        aluno.setPlano(null);
        String respostaEsperada = "Sugestão para aluno sem plano";
        when(model.generate(anyString())).thenReturn(respostaEsperada);

        String resultado = tutorService.sugerirProximosCursos(aluno, cursos);

        assertThat(resultado).isEqualTo(respostaEsperada);
    }

    @Test
    void deveSugerirEstrategiasEstudo() {
        String respostaEsperada = "Estratégias de estudo mockadas";
        when(model.generate(anyString())).thenReturn(respostaEsperada);

        String resultado = tutorService.sugerirEstrategiasEstudo(aluno);

        assertThat(resultado).isEqualTo(respostaEsperada);
    }

    @Test
    void deveSugerirEstrategiasEstudoComAlunoSemPlano() {
        aluno.setPlano(null);
        String respostaEsperada = "Estratégias para aluno sem plano";
        when(model.generate(anyString())).thenReturn(respostaEsperada);

        String resultado = tutorService.sugerirEstrategiasEstudo(aluno);

        assertThat(resultado).isEqualTo(respostaEsperada);
    }

    @Test
    void deveSugerirCursosPorCategoria() {
        String categoria = "Programação";
        String respostaEsperada = "Cursos de programação recomendados";
        when(model.generate(anyString())).thenReturn(respostaEsperada);

        String resultado = tutorService.sugerirCursosPorCategoria(aluno, categoria, cursos);

        assertThat(resultado).isEqualTo(respostaEsperada);
    }

    @Test
    void deveSugerirCursosPorCategoriaComListaVazia() {
        String categoria = "Inexistente";
        String respostaEsperada = "Nenhum curso encontrado";
        when(model.generate(anyString())).thenReturn(respostaEsperada);

        String resultado = tutorService.sugerirCursosPorCategoria(aluno, categoria, Collections.emptyList());

        assertThat(resultado).isEqualTo(respostaEsperada);
    }

    @Test
    void deveSugerirCursosPorCategoriaComCursosDeOutraCategoria() {
        String categoria = "Design";
        Curso cursoDesign = new Curso();
        cursoDesign.setTitulo("Design UI/UX");
        cursoDesign.setCategoria("Design");
        cursoDesign.setDificuldade(Dificuldade.INTERMEDIARIO);
        
        String respostaEsperada = "Cursos de design recomendados";
        when(model.generate(anyString())).thenReturn(respostaEsperada);

        String resultado = tutorService.sugerirCursosPorCategoria(aluno, categoria, cursos);

        assertThat(resultado).isEqualTo(respostaEsperada);
    }

    @Test
    void deveSugerirCursosPorDificuldade() {
        Dificuldade dificuldade = Dificuldade.INTERMEDIARIO;
        String respostaEsperada = "Cursos intermediários recomendados";
        when(model.generate(anyString())).thenReturn(respostaEsperada);

        String resultado = tutorService.sugerirCursosPorDificuldade(aluno, dificuldade, cursos);

        assertThat(resultado).isEqualTo(respostaEsperada);
    }

    @Test
    void deveSugerirCursosPorDificuldadeComListaVazia() {
        Dificuldade dificuldade = Dificuldade.AVANCADO;
        String respostaEsperada = "Nenhum curso encontrado";
        when(model.generate(anyString())).thenReturn(respostaEsperada);

        String resultado = tutorService.sugerirCursosPorDificuldade(aluno, dificuldade, Collections.emptyList());

        assertThat(resultado).isEqualTo(respostaEsperada);
    }

    @Test
    void deveSugerirCursosPorDificuldadeComCursosDeOutraDificuldade() {
        Dificuldade dificuldade = Dificuldade.AVANCADO;
        String respostaEsperada = "Cursos avançados recomendados";
        when(model.generate(anyString())).thenReturn(respostaEsperada);

        String resultado = tutorService.sugerirCursosPorDificuldade(aluno, dificuldade, cursos);

        assertThat(resultado).isEqualTo(respostaEsperada);
    }

    @Test
    void deveFornecerDicasPersonalizadas() {
        String contexto = "Estou tendo dificuldades com programação orientada a objetos";
        String respostaEsperada = "Dicas personalizadas mockadas";
        when(model.generate(anyString())).thenReturn(respostaEsperada);

        String resultado = tutorService.fornecerDicasPersonalizadas(aluno, contexto);

        assertThat(resultado).isEqualTo(respostaEsperada);
    }

    @Test
    void deveFornecerDicasPersonalizadasComAlunoSemPlano() {
        aluno.setPlano(null);
        String contexto = "Preciso melhorar minhas habilidades";
        String respostaEsperada = "Dicas para aluno sem plano";
        when(model.generate(anyString())).thenReturn(respostaEsperada);

        String resultado = tutorService.fornecerDicasPersonalizadas(aluno, contexto);

        assertThat(resultado).isEqualTo(respostaEsperada);
    }
}
