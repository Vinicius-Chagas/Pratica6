package com.grupo.facens.ex3.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupo.facens.ex3.domain.enums.Dificuldade;
import com.grupo.facens.ex3.dto.CursoRequestDto;
import com.grupo.facens.ex3.dto.CursoResponseDto;
import com.grupo.facens.ex3.service.CursoService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CursoController.class)
@Import(CursoControllerTest.MockConfig.class)
class CursoControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Autowired
        private CursoService cursoService;

        @BeforeEach
        void resetMock() {
                reset(cursoService);
        }

        @Test
        void deveCriarCurso() throws Exception {
                CursoRequestDto request = criarRequest();
                CursoResponseDto response = criarResponse(1L, true);
                when(cursoService.criarCurso(request)).thenReturn(response);

                mockMvc
                                .perform(
                                                post("/api/cursos")
                                                                .contentType(MediaType.APPLICATION_JSON)
                                                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(1L))
                                .andExpect(jsonPath("$.titulo").value("API Clean Architecture"));
        }

        @Test
        void deveRetornarBadRequestQuandoCriacaoFalhar() throws Exception {
                CursoRequestDto request = criarRequest();
                when(cursoService.criarCurso(request))
                                .thenThrow(new IllegalArgumentException("Dados inválidos"));

                mockMvc
                                .perform(
                                                post("/api/cursos")
                                                                .contentType(MediaType.APPLICATION_JSON)
                                                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void deveListarCursosAtivos() throws Exception {
                when(cursoService.listarCursosAtivos())
                                .thenReturn(List.of(criarResponse(2L, true)));

                mockMvc
                                .perform(get("/api/cursos").param("apenasAtivos", "true"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].id").value(2L));

                verify(cursoService).listarCursosAtivos();
        }

        @Test
        void deveListarTodosCursosQuandoParametroNaoInformado() throws Exception {
                when(cursoService.listarTodosCursos())
                                .thenReturn(List.of(criarResponse(3L, false)));

                mockMvc
                                .perform(get("/api/cursos"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].id").value(3L));

                verify(cursoService).listarTodosCursos();
        }

        @Test
        void deveRetornarErroInternoAoListarCursos() throws Exception {
                when(cursoService.listarTodosCursos())
                                .thenThrow(new RuntimeException("Falha inesperada"));

                mockMvc.perform(get("/api/cursos")).andExpect(status().isInternalServerError());
        }

        @Test
        void deveBuscarCursoPorId() throws Exception {
                when(cursoService.buscarPorId(10L)).thenReturn(criarResponse(10L, true));

                mockMvc
                                .perform(get("/api/cursos/10"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(10L));
        }

        @Test
        void deveRetornarNotFoundAoBuscarCursoInexistente() throws Exception {
                when(cursoService.buscarPorId(11L))
                                .thenThrow(new IllegalArgumentException("Não encontrado"));

                mockMvc.perform(get("/api/cursos/11")).andExpect(status().isNotFound());
        }

        @Test
        void deveRetornarErroInternoAoBuscarCursoPorId() throws Exception {
                when(cursoService.buscarPorId(12L))
                                .thenThrow(new RuntimeException("Erro inesperado"));

                mockMvc.perform(get("/api/cursos/12")).andExpect(status().isInternalServerError());
        }

        @Test
        void deveBuscarPorCategoria() throws Exception {
                when(cursoService.buscarPorCategoria("Backend"))
                                .thenReturn(List.of(criarResponse(13L, true)));

                mockMvc
                                .perform(get("/api/cursos/categoria/Backend"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].categoria").value("Backend"));
        }

        @Test
        void deveRetornarErroAoBuscarCategoria() throws Exception {
                when(cursoService.buscarPorCategoria("Backend"))
                                .thenThrow(new RuntimeException("Erro"));

                mockMvc
                                .perform(get("/api/cursos/categoria/Backend"))
                                .andExpect(status().isInternalServerError());
        }

        @Test
        void deveBuscarPorDificuldade() throws Exception {
                when(cursoService.buscarPorDificuldade(Dificuldade.AVANCADO))
                                .thenReturn(List.of(criarResponse(14L, true, Dificuldade.AVANCADO)));

                mockMvc
                                .perform(get("/api/cursos/dificuldade/AVANCADO"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].dificuldade").value("AVANCADO"));
        }

        @Test
        void deveRetornarErroAoBuscarDificuldade() throws Exception {
                when(cursoService.buscarPorDificuldade(Dificuldade.AVANCADO))
                                .thenThrow(new RuntimeException("Erro"));

                mockMvc
                                .perform(get("/api/cursos/dificuldade/AVANCADO"))
                                .andExpect(status().isInternalServerError());
        }

        @Test
        void deveBuscarPorTitulo() throws Exception {
                when(cursoService.buscarPorTitulo("API"))
                                .thenReturn(List.of(criarResponse(15L, true)));

                mockMvc
                                .perform(get("/api/cursos/buscar").param("titulo", "API"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].titulo").value("API Clean Architecture"));
        }

        @Test
        void deveRetornarErroAoBuscarPorTitulo() throws Exception {
                when(cursoService.buscarPorTitulo("API")).thenThrow(new RuntimeException("Erro"));

                mockMvc
                                .perform(get("/api/cursos/buscar").param("titulo", "API"))
                                .andExpect(status().isInternalServerError());
        }

        @Test
        void deveAtualizarCurso() throws Exception {
                CursoRequestDto request = criarRequest();
                CursoResponseDto response = criarResponse(16L, true);
                when(cursoService.atualizarCurso(16L, request)).thenReturn(response);

                mockMvc
                                .perform(
                                                put("/api/cursos/16")
                                                                .contentType(MediaType.APPLICATION_JSON)
                                                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(16L));
        }

        @Test
        void deveRetornarNotFoundAoAtualizarCursoInexistente() throws Exception {
                CursoRequestDto request = criarRequest();
                when(cursoService.atualizarCurso(17L, request))
                                .thenThrow(new IllegalArgumentException("ID inválido"));

                mockMvc
                                .perform(
                                                put("/api/cursos/17")
                                                                .contentType(MediaType.APPLICATION_JSON)
                                                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isNotFound());
        }

        @Test
        void deveRetornarBadRequestAoAtualizarCursoComErro() throws Exception {
                CursoRequestDto request = criarRequest();
                when(cursoService.atualizarCurso(18L, request))
                                .thenThrow(new RuntimeException("Erro"));

                mockMvc
                                .perform(
                                                put("/api/cursos/18")
                                                                .contentType(MediaType.APPLICATION_JSON)
                                                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void deveDeletarCurso() throws Exception {
                mockMvc.perform(delete("/api/cursos/19")).andExpect(status().isNoContent());
                verify(cursoService).deletarCurso(19L);
        }

        @Test
        void deveRetornarNotFoundAoDeletarCursoInexistente() throws Exception {
                doThrow(new IllegalArgumentException("ID inválido"))
                                .when(cursoService)
                                .deletarCurso(20L);

                mockMvc.perform(delete("/api/cursos/20")).andExpect(status().isNotFound());
        }

        @Test
        void deveRetornarErroInternoAoDeletarCurso() throws Exception {
                doThrow(new RuntimeException("Erro"))
                                .when(cursoService)
                                .deletarCurso(21L);

                mockMvc
                                .perform(delete("/api/cursos/21"))
                                .andExpect(status().isInternalServerError());
        }

        @Test
        void deveInativarCurso() throws Exception {
                when(cursoService.inativarCurso(22L)).thenReturn(criarResponse(22L, false));

                mockMvc
                                .perform(patch("/api/cursos/22/inativar"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.ativo").value(false));
        }

        @Test
        void deveRetornarNotFoundAoInativarCursoInexistente() throws Exception {
                when(cursoService.inativarCurso(23L))
                                .thenThrow(new IllegalArgumentException("ID inválido"));

                mockMvc.perform(patch("/api/cursos/23/inativar")).andExpect(status().isNotFound());
        }

        @Test
        void deveRetornarErroInternoAoInativarCurso() throws Exception {
                when(cursoService.inativarCurso(23L))
                                .thenThrow(new RuntimeException("Erro"));

                mockMvc.perform(patch("/api/cursos/23/inativar"))
                                .andExpect(status().isInternalServerError());
        }

        @Test
        void deveAtivarCurso() throws Exception {
                when(cursoService.ativarCurso(24L)).thenReturn(criarResponse(24L, true));

                mockMvc
                                .perform(patch("/api/cursos/24/ativar"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.ativo").value(true));
        }

        @Test
        void deveRetornarNotFoundAoAtivarCursoInexistente() throws Exception {
                when(cursoService.ativarCurso(25L))
                                .thenThrow(new IllegalArgumentException("ID inválido"));

                mockMvc.perform(patch("/api/cursos/25/ativar")).andExpect(status().isNotFound());
        }

        @Test
        void deveRetornarErroInternoAoAtivarCurso() throws Exception {
                when(cursoService.ativarCurso(25L)).thenThrow(new RuntimeException("Erro"));

                mockMvc.perform(patch("/api/cursos/25/ativar"))
                                .andExpect(status().isInternalServerError());
        }

        private CursoRequestDto criarRequest() {
                return new CursoRequestDto(
                                "API Clean Architecture",
                                "Estruture camadas usando DDD",
                                "Backend",
                                Dificuldade.INTERMEDIARIO,
                                20,
                                true);
        }

        private CursoResponseDto criarResponse(Long id, boolean ativo) {
                return criarResponse(id, ativo, Dificuldade.INTERMEDIARIO);
        }

        private CursoResponseDto criarResponse(
                        Long id,
                        boolean ativo,
                        Dificuldade dificuldade) {
                return new CursoResponseDto(
                                id,
                                "API Clean Architecture",
                                "Estruture camadas usando DDD",
                                "Backend",
                                dificuldade,
                                20,
                                ativo,
                                LocalDateTime.now(),
                                LocalDateTime.now());
        }

        @TestConfiguration
        static class MockConfig {

                @Bean
                @Primary
                CursoService cursoService() {
                        return mock(CursoService.class);
                }
        }
}
