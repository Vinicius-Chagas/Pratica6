package com.grupo.facens.ex3.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.grupo.facens.ex3.domain.entities.Aluno;
import com.grupo.facens.ex3.repository.AlunoRepository;
import com.grupo.facens.ex3.service.AssinaturaService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AssinaturaController.class)
@SuppressWarnings("removal")
class AssinaturaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AssinaturaService assinaturaService;

    @MockBean
    private AlunoRepository alunoRepository;

    private Aluno criarAlunoBasico() {
        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("Joana");
        aluno.setEmail("joana@test.com");
        return aluno;
    }

    @Test
    void deveAtivarAssinatura() throws Exception {
        Aluno aluno = criarAlunoBasico();
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));

        mockMvc
                .perform(post("/api/assinaturas/1/ativar"))
                .andExpect(status().isNoContent());

        verify(assinaturaService).ativarAssinatura(aluno);
    }

    @Test
    void deveRetornarNotFoundAoAtivarAssinaturaParaAlunoInexistente()
            throws Exception {
        when(alunoRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc
                .perform(post("/api/assinaturas/1/ativar"))
                .andExpect(status().isNotFound());

        verifyNoInteractions(assinaturaService);
    }

    @Test
    void deveVerificarPlanoBasico() throws Exception {
        Aluno aluno = criarAlunoBasico();
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(assinaturaService.verificarAcessoPlanBasico(aluno)).thenReturn(true);

        mockMvc
                .perform(get("/api/assinaturas/1/basico"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.planoBasico").value(true));
    }

    @Test
    void deveRetornarNotFoundAoConsultarPlanoDeAlunoInexistente() throws Exception {
        when(alunoRepository.findById(7L)).thenReturn(Optional.empty());

        mockMvc
                .perform(get("/api/assinaturas/7/basico"))
                .andExpect(status().isNotFound());
    }
}
