package com.grupo.facens.ex3.dto;

import com.grupo.facens.ex3.domain.entities.Aluno;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForumActivityDto {

    private Aluno aluno;
    private long totalPosts;
    private long totalComments;
    private long totalContribuicoes;

    public ForumActivityDto(Aluno aluno, long totalPosts, long totalComments) {
        this.aluno = aluno;
        this.totalPosts = totalPosts;
        this.totalComments = totalComments;
        this.totalContribuicoes = totalPosts + totalComments;
    }

    public void addComments(long comments) {
        this.totalComments = comments;
        this.totalContribuicoes = this.totalPosts + this.totalComments;
    }
}
