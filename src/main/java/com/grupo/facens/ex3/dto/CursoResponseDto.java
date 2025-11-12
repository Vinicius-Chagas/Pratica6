package com.grupo.facens.ex3.dto;

import com.grupo.facens.ex3.domain.entities.Curso;
import com.grupo.facens.ex3.domain.enums.Dificuldade;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CursoResponseDto {

    private Long id;
    private String titulo;
    private String descricao;
    private String categoria;
    private Dificuldade dificuldade;
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
            curso.getDataAtualizacao()
        );
    }
}
