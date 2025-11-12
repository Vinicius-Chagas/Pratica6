package com.grupo.facens.ex3.controller;

import com.grupo.facens.ex3.domain.entities.Aluno;
import com.grupo.facens.ex3.domain.entities.Comment;
import com.grupo.facens.ex3.domain.entities.Post;
import com.grupo.facens.ex3.dto.CommentRequestDto;
import com.grupo.facens.ex3.dto.CommentResponseDto;
import com.grupo.facens.ex3.dto.PostRequestDto;
import com.grupo.facens.ex3.dto.PostResponseDto;
import com.grupo.facens.ex3.repository.AlunoRepository;
import com.grupo.facens.ex3.repository.CommentRepository;
import com.grupo.facens.ex3.repository.PostRepository;
import com.grupo.facens.ex3.service.ForumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
    name = "Fórum",
    description = "Endpoints para gerenciamento de posts e comentários do fórum"
)
@RestController
@RequestMapping("/api/forum")
public class ForumController {

    @Autowired
    private ForumService forumService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Operation(
        summary = "Criar post",
        description = "Cria um novo post no fórum"
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "201",
                description = "Post criado com sucesso"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Autor não encontrado"
            ),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        }
    )
    @PostMapping("/posts")
    public ResponseEntity<PostResponseDto> criarPost(
        @Valid @RequestBody PostRequestDto request
    ) {
        try {
            Aluno autor = alunoRepository
                .findById(request.getAutorId())
                .orElseThrow(() ->
                    new IllegalArgumentException("Autor não encontrado")
                );

            Post post = forumService.criarTopico(
                autor,
                request.getTitulo(),
                request.getConteudo()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(
                PostResponseDto.fromEntitySimple(post)
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostResponseDto>> listarPosts(
        @RequestParam(required = false) Long autorId,
        @RequestParam(required = false) String titulo
    ) {
        try {
            List<Post> posts;

            if (autorId != null) {
                Aluno autor = alunoRepository
                    .findById(autorId)
                    .orElseThrow(() ->
                        new IllegalArgumentException("Autor não encontrado")
                    );
                posts = postRepository.findByAutorOrderByDataCriacaoDesc(autor);
            } else if (titulo != null && !titulo.isEmpty()) {
                posts = postRepository.findByTituloContainingIgnoreCase(titulo);
            } else {
                posts = postRepository.findByOrderByDataCriacaoDesc();
            }

            List<PostResponseDto> response = posts
                .stream()
                .map(PostResponseDto::fromEntitySimple)
                .collect(Collectors.toList());

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostResponseDto> buscarPostPorId(
        @PathVariable Long id
    ) {
        try {
            Post post = postRepository
                .findById(id)
                .orElseThrow(() ->
                    new IllegalArgumentException("Post não encontrado")
                );
            return ResponseEntity.ok(PostResponseDto.fromEntity(post));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    @GetMapping("/posts/populares")
    public ResponseEntity<List<PostResponseDto>> listarPostsPopulares() {
        try {
            List<Post> posts =
                postRepository.findPostsOrderByTotalComentarios();
            List<PostResponseDto> response = posts
                .stream()
                .map(PostResponseDto::fromEntity)
                .collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<PostResponseDto> atualizarPost(
        @PathVariable Long id,
        @Valid @RequestBody PostRequestDto request
    ) {
        try {
            Post post = postRepository
                .findById(id)
                .orElseThrow(() ->
                    new IllegalArgumentException("Post não encontrado")
                );

            post.setTitulo(request.getTitulo());
            post.setConteudo(request.getConteudo());

            Post postAtualizado = postRepository.save(post);
            return ResponseEntity.ok(
                PostResponseDto.fromEntitySimple(postAtualizado)
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletarPost(@PathVariable Long id) {
        try {
            if (!postRepository.existsById(id)) {
                throw new IllegalArgumentException("Post não encontrado");
            }
            postRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    @Operation(
        summary = "Criar comentário",
        description = "Adiciona um comentário a um post existente"
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "201",
                description = "Comentário criado com sucesso"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Autor ou post não encontrado"
            ),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        }
    )
    @PostMapping("/comments")
    public ResponseEntity<CommentResponseDto> criarComentario(
        @Valid @RequestBody CommentRequestDto request
    ) {
        try {
            Aluno autor = alunoRepository
                .findById(request.getAutorId())
                .orElseThrow(() ->
                    new IllegalArgumentException("Autor não encontrado")
                );

            Comment comment = forumService.comentarTopico(
                autor,
                request.getPostId(),
                request.getConteudo()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(
                CommentResponseDto.fromEntity(comment)
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/comments")
    public ResponseEntity<List<CommentResponseDto>> listarComentarios(
        @RequestParam(required = false) Long postId,
        @RequestParam(required = false) Long autorId
    ) {
        try {
            List<Comment> comments;

            if (postId != null) {
                Post post = postRepository
                    .findById(postId)
                    .orElseThrow(() ->
                        new IllegalArgumentException("Post não encontrado")
                    );
                comments = commentRepository.findByPostOrderByDataCriacaoAsc(
                    post
                );
            } else if (autorId != null) {
                Aluno autor = alunoRepository
                    .findById(autorId)
                    .orElseThrow(() ->
                        new IllegalArgumentException("Autor não encontrado")
                    );
                comments = commentRepository.findByAutorOrderByDataCriacaoDesc(
                    autor
                );
            } else {
                comments = commentRepository.findAll();
            }

            List<CommentResponseDto> response = comments
                .stream()
                .map(CommentResponseDto::fromEntity)
                .collect(Collectors.toList());

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<CommentResponseDto> buscarComentarioPorId(
        @PathVariable Long id
    ) {
        try {
            Comment comment = commentRepository
                .findById(id)
                .orElseThrow(() ->
                    new IllegalArgumentException("Comentário não encontrado")
                );
            return ResponseEntity.ok(CommentResponseDto.fromEntity(comment));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<CommentResponseDto> atualizarComentario(
        @PathVariable Long id,
        @RequestParam String conteudo
    ) {
        try {
            Comment comment = commentRepository
                .findById(id)
                .orElseThrow(() ->
                    new IllegalArgumentException("Comentário não encontrado")
                );

            comment.setConteudo(conteudo);
            Comment commentAtualizado = commentRepository.save(comment);

            return ResponseEntity.ok(
                CommentResponseDto.fromEntity(commentAtualizado)
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deletarComentario(@PathVariable Long id) {
        try {
            if (!commentRepository.existsById(id)) {
                throw new IllegalArgumentException("Comentário não encontrado");
            }
            commentRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    @GetMapping("/aluno-mais-ativo")
    public ResponseEntity<?> buscarAlunoMaisAtivo() {
        try {
            Aluno alunoMaisAtivo = forumService.getAlunoMaisAtivoDoMes();
            if (alunoMaisAtivo == null) {
                return ResponseEntity.ok().body("Nenhum aluno ativo neste mês");
            }
            return ResponseEntity.ok(alunoMaisAtivo);
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }

    @GetMapping("/contribuicoes/{alunoId}")
    public ResponseEntity<Integer> contarContribuicoes(
        @PathVariable Long alunoId
    ) {
        try {
            Aluno aluno = alunoRepository
                .findById(alunoId)
                .orElseThrow(() ->
                    new IllegalArgumentException("Aluno não encontrado")
                );

            int contribuicoes = forumService.contarContribuicoes(aluno);
            return ResponseEntity.ok(contribuicoes);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
            ).build();
        }
    }
}
