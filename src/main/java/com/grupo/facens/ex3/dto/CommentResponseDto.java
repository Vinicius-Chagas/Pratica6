package com.grupo.facens.ex3.dto;

import com.grupo.facens.ex3.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {

    private Long id;
    private String conteudo;
    private Long autorId;
    private String autorNome;
    private Long postId;
    private LocalDateTime dataCriacao;

    public static CommentResponseDto fromEntity(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getConteudo(),
                comment.getAutor().getId(),
                comment.getAutor().getNome(),
                comment.getPost().getId(),
                comment.getDataCriacao());
    }
}
