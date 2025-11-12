package com.grupo.facens.ex3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostResponseDto {

    private Long id;
    private String titulo;
    private String conteudo;
    private Long autorId;
}
