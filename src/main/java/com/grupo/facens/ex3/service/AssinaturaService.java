package com.grupo.facens.ex3.service;

import com.grupo.facens.ex3.model.Aluno;
import com.grupo.facens.ex3.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssinaturaService {

    @Autowired
    private AlunoRepository alunoRepository;

    public boolean verificarAcessoPlanBasico(Aluno aluno) {
        if (aluno == null) {
            return false;
        }
        return aluno.temPlanoBasico();
    }

    public void ativarAssinatura(Aluno aluno) {
        if (aluno != null) {
            aluno.setPlano(Aluno.TipoPlano.BASICO);
            alunoRepository.save(aluno);
        }
    }
}
