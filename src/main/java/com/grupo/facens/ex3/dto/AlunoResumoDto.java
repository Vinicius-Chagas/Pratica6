package com.grupo.facens.ex3.dto;

import com.grupo.facens.ex3.model.Aluno;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AlunoResumoDto {

    private Long id;
    private String nome;
    private String email;
    private Aluno.TipoPlano plano;

    public static AlunoResumoDto fromEntity(Aluno aluno) {
        return new AlunoResumoDto(
                aluno.getId(),
                aluno.getNome(),
                aluno.getEmail(),
                aluno.getPlano());
    }
}
