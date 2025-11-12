package com.grupo.facens.ex3.service;

import com.grupo.facens.ex3.domain.entities.Curso;
import com.grupo.facens.ex3.domain.enums.Dificuldade;
import com.grupo.facens.ex3.dto.CursoRequestDto;
import com.grupo.facens.ex3.dto.CursoResponseDto;
import com.grupo.facens.ex3.repository.CursoRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Transactional
    public CursoResponseDto criarCurso(CursoRequestDto request) {
        Curso curso = new Curso();
        curso.setTitulo(request.getTitulo());
        curso.setDescricao(request.getDescricao());
        curso.setCategoria(request.getCategoria());
        curso.setDificuldade(request.getDificuldade());
        curso.setCargaHoraria(request.getCargaHoraria());
        curso.setAtivo(request.getAtivo() != null ? request.getAtivo() : true);

        Curso cursoSalvo = cursoRepository.save(curso);
        return CursoResponseDto.fromEntity(cursoSalvo);
    }

    @Transactional(readOnly = true)
    public List<CursoResponseDto> listarTodosCursos() {
        return cursoRepository
            .findAll()
            .stream()
            .map(CursoResponseDto::fromEntity)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CursoResponseDto> listarCursosAtivos() {
        return cursoRepository
            .findByAtivoTrue()
            .stream()
            .map(CursoResponseDto::fromEntity)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CursoResponseDto buscarPorId(Long id) {
        Curso curso = cursoRepository
            .findById(id)
            .orElseThrow(() ->
                new IllegalArgumentException(
                    "Curso não encontrado com ID: " + id
                )
            );
        return CursoResponseDto.fromEntity(curso);
    }

    @Transactional(readOnly = true)
    public List<CursoResponseDto> buscarPorCategoria(String categoria) {
        return cursoRepository
            .findByCategoria(categoria)
            .stream()
            .map(CursoResponseDto::fromEntity)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CursoResponseDto> buscarPorDificuldade(
        Dificuldade dificuldade
    ) {
        return cursoRepository
            .findByDificuldade(dificuldade)
            .stream()
            .map(CursoResponseDto::fromEntity)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CursoResponseDto> buscarPorTitulo(String titulo) {
        return cursoRepository
            .findByTituloContainingIgnoreCase(titulo)
            .stream()
            .map(CursoResponseDto::fromEntity)
            .collect(Collectors.toList());
    }

    @Transactional
    public CursoResponseDto atualizarCurso(Long id, CursoRequestDto request) {
        Curso curso = cursoRepository
            .findById(id)
            .orElseThrow(() ->
                new IllegalArgumentException(
                    "Curso não encontrado com ID: " + id
                )
            );

        curso.setTitulo(request.getTitulo());
        curso.setDescricao(request.getDescricao());
        curso.setCategoria(request.getCategoria());
        curso.setDificuldade(request.getDificuldade());
        curso.setCargaHoraria(request.getCargaHoraria());

        if (request.getAtivo() != null) {
            curso.setAtivo(request.getAtivo());
        }

        Curso cursoAtualizado = cursoRepository.save(curso);
        return CursoResponseDto.fromEntity(cursoAtualizado);
    }

    @Transactional
    public void deletarCurso(Long id) {
        if (!cursoRepository.existsById(id)) {
            throw new IllegalArgumentException(
                "Curso não encontrado com ID: " + id
            );
        }
        cursoRepository.deleteById(id);
    }

    @Transactional
    public CursoResponseDto inativarCurso(Long id) {
        Curso curso = cursoRepository
            .findById(id)
            .orElseThrow(() ->
                new IllegalArgumentException(
                    "Curso não encontrado com ID: " + id
                )
            );

        curso.setAtivo(false);
        Curso cursoAtualizado = cursoRepository.save(curso);
        return CursoResponseDto.fromEntity(cursoAtualizado);
    }

    @Transactional
    public CursoResponseDto ativarCurso(Long id) {
        Curso curso = cursoRepository
            .findById(id)
            .orElseThrow(() ->
                new IllegalArgumentException(
                    "Curso não encontrado com ID: " + id
                )
            );

        curso.setAtivo(true);
        Curso cursoAtualizado = cursoRepository.save(curso);
        return CursoResponseDto.fromEntity(cursoAtualizado);
    }
}
