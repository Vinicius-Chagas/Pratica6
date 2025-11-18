package com.grupo.facens.ex3.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.grupo.facens.ex3.domain.entities.Curso;
import com.grupo.facens.ex3.domain.enums.Dificuldade;
import com.grupo.facens.ex3.dto.CursoRequestDto;
import com.grupo.facens.ex3.dto.CursoResponseDto;
import com.grupo.facens.ex3.repository.CursoRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CursoServiceTest {

    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private CursoService cursoService;

    @Test
    void deveCriarCurso() {
        CursoRequestDto request = new CursoRequestDto(
                "Titulo",
                "Descricao",
                "Categoria",
                Dificuldade.INTERMEDIARIO,
                30,
                null);

        when(cursoRepository.save(any(Curso.class))).thenAnswer(invocation -> {
            Curso curso = invocation.getArgument(0);
            curso.setId(1L);
            curso.setDataCriacao(LocalDateTime.now());
            return curso;
        });

        CursoResponseDto response = cursoService.criarCurso(request);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitulo()).isEqualTo("Titulo");
        assertThat(response.getCategoria()).isEqualTo("Categoria");
        assertThat(response.getDificuldade()).isEqualTo(
                Dificuldade.INTERMEDIARIO);
        assertThat(response.getAtivo()).isTrue();
        verify(cursoRepository).save(any(Curso.class));
    }

    @Test
    void deveCriarCursoDesativadoQuandoCampoAtivoForFalse() {
        CursoRequestDto request = new CursoRequestDto(
                "Titulo Inativo",
                "Descricao",
                "Categoria",
                Dificuldade.AVANCADO,
                40,
                false);

        when(cursoRepository.save(any(Curso.class))).thenAnswer(invocation -> {
            Curso curso = invocation.getArgument(0);
            curso.setId(2L);
            curso.setDataCriacao(LocalDateTime.now());
            return curso;
        });

        CursoResponseDto response = cursoService.criarCurso(request);

        assertThat(response.getId()).isEqualTo(2L);
        assertThat(response.getAtivo()).isFalse();
        verify(cursoRepository).save(any(Curso.class));
    }

    @Test
    void deveListarTodosCursos() {
        Curso curso = criarCurso(1L, true);
        when(cursoRepository.findAll()).thenReturn(List.of(curso));

        List<CursoResponseDto> cursos = cursoService.listarTodosCursos();

        assertThat(cursos).hasSize(1);
        assertThat(cursos.get(0).getTitulo()).isEqualTo("Curso 1");
        verify(cursoRepository).findAll();
    }

    @Test
    void deveListarCursosAtivos() {
        Curso curso = criarCurso(2L, true);
        when(cursoRepository.findByAtivoTrue()).thenReturn(List.of(curso));

        List<CursoResponseDto> cursos = cursoService.listarCursosAtivos();

        assertThat(cursos).hasSize(1);
        assertThat(cursos.get(0).getAtivo()).isTrue();
        verify(cursoRepository).findByAtivoTrue();
    }

    @Test
    void deveBuscarPorId() {
        Curso curso = criarCurso(3L, true);
        when(cursoRepository.findById(3L)).thenReturn(Optional.of(curso));

        CursoResponseDto response = cursoService.buscarPorId(3L);

        assertThat(response.getId()).isEqualTo(3L);
        verify(cursoRepository).findById(3L);
    }

    @Test
    void deveLancarExcecaoQuandoCursoNaoEncontradoPorId() {
        when(cursoRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> cursoService.buscarPorId(5L));
    }

    @Test
    void deveBuscarPorCategoria() {
        Curso curso = criarCurso(4L, true);
        when(cursoRepository.findByCategoria("Backend")).thenReturn(
                List.of(curso));

        List<CursoResponseDto> cursos = cursoService.buscarPorCategoria(
                "Backend");

        assertThat(cursos).hasSize(1);
        verify(cursoRepository).findByCategoria("Backend");
    }

    @Test
    void deveBuscarPorDificuldade() {
        Curso curso = criarCurso(6L, true);
        when(
                cursoRepository.findByDificuldade(Dificuldade.INICIANTE)).thenReturn(List.of(curso));

        List<CursoResponseDto> cursos = cursoService.buscarPorDificuldade(
                Dificuldade.INICIANTE);

        assertThat(cursos).hasSize(1);
        verify(cursoRepository).findByDificuldade(Dificuldade.INICIANTE);
    }

    @Test
    void deveBuscarPorTitulo() {
        Curso curso = criarCurso(7L, true);
        when(
                cursoRepository.findByTituloContainingIgnoreCase("curso")).thenReturn(List.of(curso));

        List<CursoResponseDto> cursos = cursoService.buscarPorTitulo("curso");

        assertThat(cursos).hasSize(1);
        verify(cursoRepository).findByTituloContainingIgnoreCase("curso");
    }

    @Test
    void deveAtualizarCurso() {
        Curso curso = criarCurso(8L, true);
        when(cursoRepository.findById(8L)).thenReturn(Optional.of(curso));
        when(cursoRepository.save(curso)).thenReturn(curso);
        CursoRequestDto request = new CursoRequestDto(
                "Novo Titulo",
                "Nova Descricao",
                "Nova Categoria",
                Dificuldade.AVANCADO,
                45,
                false);

        CursoResponseDto response = cursoService.atualizarCurso(8L, request);

        assertThat(response.getTitulo()).isEqualTo("Novo Titulo");
        assertThat(response.getDificuldade()).isEqualTo(Dificuldade.AVANCADO);
        assertThat(response.getAtivo()).isFalse();
        verify(cursoRepository).save(curso);
    }

    @Test
    void deveManterStatusQuandoAtualizacaoNaoInformarCampoAtivo() {
        Curso curso = criarCurso(81L, true);
        when(cursoRepository.findById(81L)).thenReturn(Optional.of(curso));
        when(cursoRepository.save(curso)).thenReturn(curso);

        CursoRequestDto request = new CursoRequestDto(
                "Atualizado",
                "Descricao X",
                "Categoria X",
                Dificuldade.INICIANTE,
                25,
                null);

        CursoResponseDto response = cursoService.atualizarCurso(81L, request);

        assertThat(response.getAtivo()).isTrue();
        verify(cursoRepository).save(curso);
    }

    @Test
    void deveLancarExcecaoAoAtualizarCursoInexistente() {
        CursoRequestDto request = new CursoRequestDto(
                "Titulo",
                "Descricao",
                "Categoria",
                Dificuldade.INICIANTE,
                20,
                true);
        when(cursoRepository.findById(9L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> cursoService.atualizarCurso(9L, request));
    }

    @Test
    void deveDeletarCurso() {
        when(cursoRepository.existsById(10L)).thenReturn(true);

        cursoService.deletarCurso(10L);

        verify(cursoRepository).deleteById(10L);
    }

    @Test
    void deveLancarExcecaoAoDeletarCursoInexistente() {
        when(cursoRepository.existsById(11L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> cursoService.deletarCurso(11L));
    }

    @Test
    void deveInativarCurso() {
        Curso curso = criarCurso(12L, true);
        when(cursoRepository.findById(12L)).thenReturn(Optional.of(curso));
        when(cursoRepository.save(curso)).thenReturn(curso);

        CursoResponseDto response = cursoService.inativarCurso(12L);

        assertThat(response.getAtivo()).isFalse();
        verify(cursoRepository).save(curso);
    }

    @Test
    void deveAtivarCurso() {
        Curso curso = criarCurso(13L, false);
        when(cursoRepository.findById(13L)).thenReturn(Optional.of(curso));
        when(cursoRepository.save(curso)).thenReturn(curso);

        CursoResponseDto response = cursoService.ativarCurso(13L);

        assertThat(response.getAtivo()).isTrue();
        verify(cursoRepository).save(curso);
    }

    private Curso criarCurso(Long id, boolean ativo) {
        Curso curso = new Curso();
        curso.setId(id);
        curso.setTitulo("Curso " + id);
        curso.setDescricao("Descricao " + id);
        curso.setCategoria("Backend");
        curso.setDificuldade(Dificuldade.INICIANTE);
        curso.setCargaHoraria(20);
        curso.setAtivo(ativo);
        curso.setDataCriacao(LocalDateTime.now());
        return curso;
    }
}
