package com.grupo.facens.ex3.controller;

import com.grupo.facens.ex3.domain.enums.Dificuldade;
import com.grupo.facens.ex3.dto.CursoRequestDto;
import com.grupo.facens.ex3.dto.CursoResponseDto;
import com.grupo.facens.ex3.service.CursoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Cursos", description = "Endpoints para gerenciamento de cursos")
@RestController
@RequestMapping("/api/cursos")
@RequiredArgsConstructor
public class CursoController {

    private final CursoService cursoService;

    @Operation(summary = "Criar um novo curso", description = "Cria um novo curso no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Curso criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
    })
    @PostMapping
    public ResponseEntity<CursoResponseDto> criarCurso(
            @Valid @RequestBody CursoRequestDto request) {
        try {
            CursoResponseDto response = cursoService.criarCurso(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Listar cursos", description = "Lista todos os cursos ou apenas os ativos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de cursos retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor"),
    })
    @GetMapping
    public ResponseEntity<List<CursoResponseDto>> listarCursos(
            @Parameter(description = "Filtrar apenas cursos ativos") @RequestParam(required = false) Boolean apenasAtivos) {
        try {
            List<CursoResponseDto> cursos;
            if (apenasAtivos != null && apenasAtivos) {
                cursos = cursoService.listarCursosAtivos();
            } else {
                cursos = cursoService.listarTodosCursos();
            }
            return ResponseEntity.ok(cursos);
        } catch (Exception e) {
            return ResponseEntity.status(
                    HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Buscar curso por ID", description = "Retorna um curso específico pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curso encontrado"),
            @ApiResponse(responseCode = "404", description = "Curso não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<CursoResponseDto> buscarCursoPorId(
            @Parameter(description = "ID do curso") @PathVariable Long id) {
        try {
            CursoResponseDto curso = cursoService.buscarPorId(id);
            return ResponseEntity.ok(curso);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(
                    HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<CursoResponseDto>> buscarPorCategoria(
            @PathVariable String categoria) {
        try {
            List<CursoResponseDto> cursos = cursoService.buscarPorCategoria(
                    categoria);
            return ResponseEntity.ok(cursos);
        } catch (Exception e) {
            return ResponseEntity.status(
                    HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/dificuldade/{dificuldade}")
    public ResponseEntity<List<CursoResponseDto>> buscarPorDificuldade(
            @PathVariable Dificuldade dificuldade) {
        try {
            List<CursoResponseDto> cursos = cursoService.buscarPorDificuldade(
                    dificuldade);
            return ResponseEntity.ok(cursos);
        } catch (Exception e) {
            return ResponseEntity.status(
                    HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<CursoResponseDto>> buscarPorTitulo(
            @RequestParam String titulo) {
        try {
            List<CursoResponseDto> cursos = cursoService.buscarPorTitulo(
                    titulo);
            return ResponseEntity.ok(cursos);
        } catch (Exception e) {
            return ResponseEntity.status(
                    HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CursoResponseDto> atualizarCurso(
            @PathVariable Long id,
            @Valid @RequestBody CursoRequestDto request) {
        try {
            CursoResponseDto response = cursoService.atualizarCurso(
                    id,
                    request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCurso(@PathVariable Long id) {
        try {
            cursoService.deletarCurso(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(
                    HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<CursoResponseDto> inativarCurso(
            @PathVariable Long id) {
        try {
            CursoResponseDto response = cursoService.inativarCurso(id);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(
                    HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<CursoResponseDto> ativarCurso(@PathVariable Long id) {
        try {
            CursoResponseDto response = cursoService.ativarCurso(id);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(
                    HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
