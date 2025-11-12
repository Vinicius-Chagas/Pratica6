package com.grupo.facens.ex3.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {

    @NotBlank(message = "O conteúdo é obrigatório")
    private String conteudo;

    @NotNull(message = "O ID do autor é obrigatório")
    private Long autorId;

    @NotNull(message = "O ID do post é obrigatório")
    private Long postId;
}
