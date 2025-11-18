package com.grupo.facens.ex3.service;

import com.grupo.facens.ex3.domain.entities.Aluno;
import com.grupo.facens.ex3.repository.AlunoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MoedaService {

    private final AlunoRepository alunoRepository;

    public void adicionarMoedas(Aluno aluno, int quantidade) {
        if (aluno != null && quantidade > 0) {
            aluno.adicionarMoedas(quantidade);
            alunoRepository.save(aluno);
        }
    }

    public boolean converterMoedasParaCursos(
            Aluno aluno,
            int quantidadeMoedas) {
        if (aluno == null || quantidadeMoedas <= 0) {
            return false;
        }

        if (!aluno.removerMoedas(quantidadeMoedas)) {
            return false;
        }

        aluno.adicionarCreditosCurso(quantidadeMoedas);
        alunoRepository.save(aluno);
        return true;
    }

    public boolean converterMoedasParaCriptomoedas(
            Aluno aluno,
            int quantidadeMoedas) {
        if (aluno == null || quantidadeMoedas <= 0) {
            return false;
        }

        if (!aluno.removerMoedas(quantidadeMoedas)) {
            return false;
        }

        alunoRepository.save(aluno);
        return true;
    }

    public int consultarSaldo(Aluno aluno) {
        if (aluno == null) {
            return 0;
        }
        return aluno.getMoedas();
    }

    public void distribuirMoedasPorCursoCompleto(Aluno aluno) {
        if (aluno == null) {
            return;
        }

        int moedasParaAdicionar = aluno.getCursosCompletados() * 3;
        aluno.adicionarMoedas(moedasParaAdicionar);
        alunoRepository.save(aluno);
    }
}
