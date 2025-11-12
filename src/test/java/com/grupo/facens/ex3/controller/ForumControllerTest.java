package com.grupo.facens.ex3.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupo.facens.ex3.dto.CreateCommentRequest;
import com.grupo.facens.ex3.dto.CreatePostRequest;
import com.grupo.facens.ex3.model.Aluno;
import com.grupo.facens.ex3.model.Comment;
import com.grupo.facens.ex3.model.Post;
import com.grupo.facens.ex3.repository.AlunoRepository;
import com.grupo.facens.ex3.service.ForumService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ForumController.class)
@SuppressWarnings("removal")
class ForumControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ForumService forumService;

    @MockBean
    private AlunoRepository alunoRepository;

    private Aluno aluno;

    @BeforeEach
    void setUp() {
        aluno = new Aluno();
        aluno.setId(4L);
        aluno.setNome("Forum User");
        aluno.setEmail("forum@test.com");
    }

    @Test
    void deveCriarTopico() throws Exception {
        CreatePostRequest request = new CreatePostRequest(
                aluno.getId(),
                "Titulo",
                "Conteudo");
        when(alunoRepository.findById(aluno.getId()))
                .thenReturn(Optional.of(aluno));
        Post post = new Post();
        post.setId(100L);
        post.setTitulo("Titulo");
        post.setConteudo("Conteudo");
        post.setAutor(aluno);
        when(forumService.criarTopico(aluno, "Titulo", "Conteudo"))
                .thenReturn(post);

        mockMvc
                .perform(
                        post("/api/forum/topicos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.titulo").value("Titulo"))
                .andExpect(jsonPath("$.autorId").value(aluno.getId()));
    }

    @Test
    void deveRetornarNotFoundAoCriarTopicoSemAutor() throws Exception {
        CreatePostRequest request = new CreatePostRequest(9L, "Titulo", "Conteudo");
        when(alunoRepository.findById(9L)).thenReturn(Optional.empty());

        mockMvc
                .perform(
                        post("/api/forum/topicos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveComentarTopico() throws Exception {
        CreateCommentRequest request = new CreateCommentRequest(
                aluno.getId(),
                "Comentario");
        when(alunoRepository.findById(aluno.getId()))
                .thenReturn(Optional.of(aluno));
        Comment comment = new Comment();
        comment.setId(200L);
        comment.setConteudo("Comentario");
        comment.setAutor(aluno);
        Post post = new Post();
        post.setId(10L);
        comment.setPost(post);
        when(forumService.comentarTopico(aluno, 10L, "Comentario"))
                .thenReturn(comment);

        mockMvc
                .perform(
                        post("/api/forum/topicos/10/comentarios")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(200L))
                .andExpect(jsonPath("$.postId").value(10L));
    }

    @Test
    void deveRetornarBadRequestQuandoComentarioInvalido() throws Exception {
        CreateCommentRequest request = new CreateCommentRequest(
                aluno.getId(),
                "Comentario");
        when(alunoRepository.findById(aluno.getId()))
                .thenReturn(Optional.of(aluno));
        when(forumService.comentarTopico(aluno, 5L, "Comentario"))
                .thenThrow(new IllegalArgumentException("Topico nao encontrado"));

        mockMvc
                .perform(
                        post("/api/forum/topicos/5/comentarios")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarNotFoundAoComentarSemAutor() throws Exception {
        CreateCommentRequest request = new CreateCommentRequest(99L, "Comentario");
        when(alunoRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc
                .perform(
                        post("/api/forum/topicos/5/comentarios")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornarAlunoMaisAtivo() throws Exception {
        when(forumService.getAlunoMaisAtivoDoMes()).thenReturn(aluno);

        mockMvc
                .perform(get("/api/forum/alunos/mais-ativo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(aluno.getId()))
                .andExpect(jsonPath("$.email").value(aluno.getEmail()));
    }

    @Test
    void deveRetornarNoContentQuandoNaoHaAlunoMaisAtivo() throws Exception {
        when(forumService.getAlunoMaisAtivoDoMes()).thenReturn(null);

        mockMvc
                .perform(get("/api/forum/alunos/mais-ativo"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveVerificarSeAlunoEhMaisAtivo() throws Exception {
        when(alunoRepository.findById(aluno.getId()))
                .thenReturn(Optional.of(aluno));
        when(forumService.isAlunoMaisAtivoDoMes(aluno)).thenReturn(true);

        mockMvc
                .perform(get("/api/forum/alunos/4/mais-ativo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.maisAtivo").value(true));
    }

    @Test
    void deveRetornarNotFoundAoVerificarAlunoNaoExistente() throws Exception {
        when(alunoRepository.findById(111L)).thenReturn(Optional.empty());

        mockMvc
                .perform(get("/api/forum/alunos/111/mais-ativo"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveContarContribuicoes() throws Exception {
        when(alunoRepository.findById(aluno.getId()))
                .thenReturn(Optional.of(aluno));
        when(forumService.contarContribuicoes(aluno)).thenReturn(7);

        mockMvc
                .perform(get("/api/forum/alunos/4/contribuicoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(7));
    }
}
