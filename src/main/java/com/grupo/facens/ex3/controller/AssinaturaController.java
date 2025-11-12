package com.grupo.facens.ex3.controller;

import com.grupo.facens.ex3.model.Aluno;
import com.grupo.facens.ex3.repository.AlunoRepository;
import com.grupo.facens.ex3.service.AssinaturaService;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assinaturas")
public class AssinaturaController {

    private final AssinaturaService assinaturaService;
    private final AlunoRepository alunoRepository;

    public AssinaturaController(
            AssinaturaService assinaturaService,
            AlunoRepository alunoRepository) {
        this.assinaturaService = assinaturaService;
        this.alunoRepository = alunoRepository;
    }

    @PostMapping("/{alunoId}/ativar")
    public ResponseEntity<Void> ativarAssinatura(
            @PathVariable("alunoId") Long alunoId) {
        Optional<Aluno> alunoOptional = alunoRepository.findById(alunoId);
        if (alunoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        assinaturaService.ativarAssinatura(alunoOptional.get());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{alunoId}/basico")
    public ResponseEntity<Map<String, Boolean>> verificarPlanoBasico(
            @PathVariable("alunoId") Long alunoId) {
        Optional<Aluno> alunoOptional = alunoRepository.findById(alunoId);
        if (alunoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        boolean possuiPlanoBasico = assinaturaService.verificarAcessoPlanBasico(
                alunoOptional.get());
        return ResponseEntity.ok(Map.of("planoBasico", possuiPlanoBasico));
    }
}
