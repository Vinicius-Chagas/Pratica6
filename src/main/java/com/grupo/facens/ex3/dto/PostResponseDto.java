package com.grupo.facens.ex3.dto;

import com.grupo.facens.ex3.domain.entities.Post;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {

    private Long id;
    private String titulo;
    private String conteudo;
    private Long autorId;
    private String autorNome;
    private LocalDateTime dataCriacao;
    private Integer totalComentarios;

    public static PostResponseDto fromEntity(Post post) {
        return new PostResponseDto(
            post.getId(),
            post.getTitulo(),
            post.getConteudo(),
            post.getAutor().getId(),
            post.getAutor().getNome(),
            post.getDataCriacao(),
            post.getComentarios() != null ? post.getComentarios().size() : 0
        );
    }

    public static PostResponseDto fromEntitySimple(Post post) {
        return new PostResponseDto(
            post.getId(),
            post.getTitulo(),
            post.getConteudo(),
            post.getAutor().getId(),
            post.getAutor().getNome(),
            post.getDataCriacao(),
            0
        );
    }
}
