package com.grupo.facens.ex3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentResponseDto {

    private Long id;
    private String conteudo;
    private Long autorId;
    private Long postId;
}
