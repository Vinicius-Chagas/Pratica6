package com.grupo.facens.ex3.dto;

import com.grupo.facens.ex3.model.Curso;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CursoRequestDto {

    @NotBlank(message = "O título é obrigatório")
    private String titulo;

    private String descricao;

    @NotBlank(message = "A categoria é obrigatória")
    private String categoria;

    @NotNull(message = "A dificuldade é obrigatória")
    private Curso.Dificuldade dificuldade;

    private Integer cargaHoraria;

    private Boolean ativo;
}
