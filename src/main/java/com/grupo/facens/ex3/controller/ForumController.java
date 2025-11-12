package com.grupo.facens.ex3.controller;

import com.grupo.facens.ex3.dto.AlunoResumoDto;
import com.grupo.facens.ex3.dto.CommentResponseDto;
import com.grupo.facens.ex3.dto.CreateCommentRequest;
import com.grupo.facens.ex3.dto.CreatePostRequest;
import com.grupo.facens.ex3.dto.PostResponseDto;
import com.grupo.facens.ex3.domain.entities.Aluno;
import com.grupo.facens.ex3.domain.entities.Comment;
import com.grupo.facens.ex3.domain.entities.Post;
import com.grupo.facens.ex3.repository.AlunoRepository;
import com.grupo.facens.ex3.service.ForumService;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/forum")
public class ForumController {

    private final ForumService forumService;
    private final AlunoRepository alunoRepository;

    public ForumController(
            ForumService forumService,
            AlunoRepository alunoRepository) {
        this.forumService = forumService;
        this.alunoRepository = alunoRepository;
    }

    @PostMapping("/topicos")
    public ResponseEntity<PostResponseDto> criarTopico(
            @RequestBody CreatePostRequest request) {
        Optional<Aluno> alunoOptional = alunoRepository.findById(
                request.getAutorId());
        if (alunoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Aluno autor = alunoOptional.get();
        Post post = forumService.criarTopico(
                autor,
                request.getTitulo(),
                request.getConteudo());
        PostResponseDto response = new PostResponseDto(
                post.getId(),
                post.getTitulo(),
                post.getConteudo(),
                autor.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/topicos/{postId}/comentarios")
    public ResponseEntity<CommentResponseDto> comentarTopico(
            @PathVariable("postId") Long postId,
            @RequestBody CreateCommentRequest request) {
        Optional<Aluno> alunoOptional = alunoRepository.findById(
                request.getAutorId());
        if (alunoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            Aluno autor = alunoOptional.get();
            Comment comment = forumService.comentarTopico(
                    autor,
                    postId,
                    request.getConteudo());
            CommentResponseDto response = new CommentResponseDto(
                    comment.getId(),
                    comment.getConteudo(),
                    autor.getId(),
                    postId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/alunos/mais-ativo")
    public ResponseEntity<AlunoResumoDto> obterAlunoMaisAtivo() {
        Aluno alunoMaisAtivo = forumService.getAlunoMaisAtivoDoMes();
        if (alunoMaisAtivo == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(AlunoResumoDto.fromEntity(alunoMaisAtivo));
    }

    @GetMapping("/alunos/{alunoId}/mais-ativo")
    public ResponseEntity<Map<String, Boolean>> verificarSeAlunoEhMaisAtivo(
            @PathVariable("alunoId") Long alunoId) {
        Optional<Aluno> alunoOptional = alunoRepository.findById(alunoId);
        if (alunoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        boolean maisAtivo = forumService.isAlunoMaisAtivoDoMes(
                alunoOptional.get());
        return ResponseEntity.ok(Map.of("maisAtivo", maisAtivo));
    }

    @GetMapping("/alunos/{alunoId}/contribuicoes")
    public ResponseEntity<Map<String, Integer>> contarContribuicoes(
            @PathVariable("alunoId") Long alunoId) {
        Optional<Aluno> alunoOptional = alunoRepository.findById(alunoId);
        if (alunoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        int total = forumService.contarContribuicoes(alunoOptional.get());
        return ResponseEntity.ok(Map.of("total", total));
    }
}
