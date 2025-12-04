package com.grupo.facens.ex3.controller;

import com.grupo.facens.ex3.domain.enums.Dificuldade;
import com.grupo.facens.ex3.domain.entities.Aluno;
import com.grupo.facens.ex3.domain.entities.Curso;
import com.grupo.facens.ex3.infrastructure.ai.TutorService;
import com.grupo.facens.ex3.repository.AlunoRepository;
import com.grupo.facens.ex3.repository.CursoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Tutor IA", description = "Endpoints para sugestões e recomendações do tutor IA")
@RestController
@RequestMapping("/api/tutor")
@RequiredArgsConstructor
public class TutorController {

    private final TutorService tutorService;
    private final AlunoRepository alunoRepository;
    private final CursoRepository cursoRepository;

    @Operation(summary = "Sugerir próximos cursos", description = "O tutor IA sugere os próximos cursos mais adequados para o aluno baseado em seu perfil e progresso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sugestões geradas com sucesso"),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{alunoId}/sugerir-cursos")
    public ResponseEntity<String> sugerirProximosCursos(
            @Parameter(description = "ID do aluno") @PathVariable Long alunoId) {
        try {
            Aluno aluno = alunoRepository.findById(alunoId)
                    .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado com ID: " + alunoId));

            List<Curso> cursosDisponiveis = cursoRepository.findByAtivoTrue();

            String sugestao = tutorService.sugerirProximosCursos(aluno, cursosDisponiveis);
            return ResponseEntity.ok(sugestao);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao gerar sugestões: " + e.getMessage());
        }
    }

    @Operation(summary = "Sugerir estratégias de estudo", description = "O tutor IA fornece estratégias personalizadas de estudo baseadas no perfil do aluno")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estratégias geradas com sucesso"),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{alunoId}/estrategias-estudo")
    public ResponseEntity<String> sugerirEstrategiasEstudo(
            @Parameter(description = "ID do aluno") @PathVariable Long alunoId) {
        try {
            Aluno aluno = alunoRepository.findById(alunoId)
                    .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado com ID: " + alunoId));

            String estrategias = tutorService.sugerirEstrategiasEstudo(aluno);
            return ResponseEntity.ok(estrategias);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao gerar estratégias: " + e.getMessage());
        }
    }

    @Operation(summary = "Sugerir cursos por categoria", description = "O tutor IA recomenda cursos de uma categoria específica baseado no perfil do aluno")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sugestões geradas com sucesso"),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{alunoId}/categoria/{categoria}")
    public ResponseEntity<String> sugerirCursosPorCategoria(
            @Parameter(description = "ID do aluno") @PathVariable Long alunoId,
            @Parameter(description = "Categoria dos cursos") @PathVariable String categoria) {
        try {
            Aluno aluno = alunoRepository.findById(alunoId)
                    .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado com ID: " + alunoId));

            List<Curso> cursos = cursoRepository.findByCategoria(categoria);

            String sugestao = tutorService.sugerirCursosPorCategoria(aluno, categoria, cursos);
            return ResponseEntity.ok(sugestao);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao gerar sugestões: " + e.getMessage());
        }
    }

    @Operation(summary = "Sugerir cursos por dificuldade", description = "O tutor IA recomenda cursos de uma dificuldade específica baseado no perfil do aluno")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sugestões geradas com sucesso"),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{alunoId}/dificuldade/{dificuldade}")
    public ResponseEntity<String> sugerirCursosPorDificuldade(
            @Parameter(description = "ID do aluno") @PathVariable Long alunoId,
            @Parameter(description = "Dificuldade dos cursos") @PathVariable Dificuldade dificuldade) {
        try {
            Aluno aluno = alunoRepository.findById(alunoId)
                    .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado com ID: " + alunoId));

            List<Curso> cursos = cursoRepository.findByDificuldade(dificuldade);

            String sugestao = tutorService.sugerirCursosPorDificuldade(aluno, dificuldade, cursos);
            return ResponseEntity.ok(sugestao);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao gerar sugestões: " + e.getMessage());
        }
    }

    @Operation(summary = "Fornecer dicas personalizadas", description = "O tutor IA fornece dicas personalizadas baseadas em um contexto específico fornecido pelo aluno")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dicas geradas com sucesso"),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/{alunoId}/dicas")
    public ResponseEntity<String> fornecerDicasPersonalizadas(
            @Parameter(description = "ID do aluno") @PathVariable Long alunoId,
            @Parameter(description = "Contexto para as dicas") @RequestParam String contexto) {
        try {
            Aluno aluno = alunoRepository.findById(alunoId)
                    .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado com ID: " + alunoId));

            String dicas = tutorService.fornecerDicasPersonalizadas(aluno, contexto);
            return ResponseEntity.ok(dicas);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao gerar dicas: " + e.getMessage());
        }
    }
}
