package com.grupo.facens.ex3.controller;

import com.grupo.facens.ex3.domain.entities.Aluno;
import com.grupo.facens.ex3.repository.AlunoRepository;
import com.grupo.facens.ex3.service.MoedaService;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/moedas")
public class MoedaController {

    private final MoedaService moedaService;
    private final AlunoRepository alunoRepository;

    public MoedaController(
            MoedaService moedaService,
            AlunoRepository alunoRepository) {
        this.moedaService = moedaService;
        this.alunoRepository = alunoRepository;
    }

    @PostMapping("/{alunoId}/adicionar")
    public ResponseEntity<Map<String, Integer>> adicionarMoedas(
            @PathVariable("alunoId") Long alunoId,
            @RequestParam("quantidade") int quantidade) {
        Optional<Aluno> alunoOptional = alunoRepository.findById(alunoId);
        if (alunoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Aluno aluno = alunoOptional.get();
        moedaService.adicionarMoedas(aluno, quantidade);
        return ResponseEntity.ok(Map.of("saldo", aluno.getMoedas()));
    }

    @PostMapping("/{alunoId}/converter-cursos")
    public ResponseEntity<Map<String, Object>> converterParaCursos(
            @PathVariable("alunoId") Long alunoId,
            @RequestParam("quantidade") int quantidade) {
        Optional<Aluno> alunoOptional = alunoRepository.findById(alunoId);
        if (alunoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Aluno aluno = alunoOptional.get();
        boolean sucesso = moedaService.converterMoedasParaCursos(
                aluno,
                quantidade);
        if (!sucesso) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("sucesso", false));
        }

        return ResponseEntity.ok(
                Map.of(
                        "sucesso",
                        true,
                        "saldo",
                        aluno.getMoedas(),
                        "creditos",
                        aluno.getCreditoCurso()));
    }

    @PostMapping("/{alunoId}/converter-criptomoedas")
    public ResponseEntity<Map<String, Object>> converterParaCriptomoedas(
            @PathVariable("alunoId") Long alunoId,
            @RequestParam("quantidade") int quantidade) {
        Optional<Aluno> alunoOptional = alunoRepository.findById(alunoId);
        if (alunoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Aluno aluno = alunoOptional.get();
        boolean sucesso = moedaService.converterMoedasParaCriptomoedas(
                aluno,
                quantidade);
        if (!sucesso) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("sucesso", false));
        }

        return ResponseEntity.ok(
                Map.of("sucesso", true, "saldo", aluno.getMoedas()));
    }

    @GetMapping("/{alunoId}/saldo")
    public ResponseEntity<Map<String, Integer>> consultarSaldo(
            @PathVariable("alunoId") Long alunoId) {
        Optional<Aluno> alunoOptional = alunoRepository.findById(alunoId);
        if (alunoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        int saldo = moedaService.consultarSaldo(alunoOptional.get());
        return ResponseEntity.ok(Map.of("saldo", saldo));
    }
}
