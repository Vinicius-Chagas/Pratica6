package com.grupo.facens.ex3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostRequest {

    private Long autorId;
    private String titulo;
    private String conteudo;
}
