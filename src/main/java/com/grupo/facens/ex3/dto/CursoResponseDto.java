package com.grupo.facens.ex3.dto;

import com.grupo.facens.ex3.model.Curso;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CursoResponseDto {

    private Long id;
    private String titulo;
    private String descricao;
    private String categoria;
    private Curso.Dificuldade dificuldade;
    private Integer cargaHoraria;
    private Boolean ativo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public static CursoResponseDto fromEntity(Curso curso) {
        return new CursoResponseDto(
                curso.getId(),
                curso.getTitulo(),
                curso.getDescricao(),
                curso.getCategoria(),
                curso.getDificuldade(),
                curso.getCargaHoraria(),
                curso.getAtivo(),
                curso.getDataCriacao(),
                curso.getDataAtualizacao());
    }
}
