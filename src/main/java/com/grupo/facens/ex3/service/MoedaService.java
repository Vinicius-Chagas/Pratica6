package com.grupo.facens.ex3.service;

import com.grupo.facens.ex3.domain.entities.Aluno;
import com.grupo.facens.ex3.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MoedaService {

    @Autowired
    private AlunoRepository alunoRepository;

    public void adicionarMoedas(Aluno aluno, int quantidade) {
        if (aluno != null && quantidade > 0) {
            aluno.setMoedas(aluno.getMoedas() + quantidade);
            alunoRepository.save(aluno);
        }
    }

    public boolean converterMoedasParaCursos(
        Aluno aluno,
        int quantidadeMoedas
    ) {
        if (aluno == null || quantidadeMoedas <= 0) {
            return false;
        }

        if (aluno.getMoedas() < quantidadeMoedas) {
            return false;
        }

        aluno.setMoedas(aluno.getMoedas() - quantidadeMoedas);
        aluno.setCreditoCurso(aluno.getCreditoCurso() + quantidadeMoedas);

        alunoRepository.save(aluno);
        return true;
    }

    public boolean converterMoedasParaCriptomoedas(
        Aluno aluno,
        int quantidadeMoedas
    ) {
        // Apenas mockado para o teste
        if (aluno == null || quantidadeMoedas <= 0) {
            return false;
        }

        if (aluno.getMoedas() < quantidadeMoedas) {
            return false;
        }

        aluno.setMoedas(aluno.getMoedas() - quantidadeMoedas);

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
        aluno.setMoedas(aluno.getMoedas() + moedasParaAdicionar);

        alunoRepository.save(aluno);
    }
}
