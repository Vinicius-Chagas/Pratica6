package com.grupo.facens.ex3.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Health Check", description = "Endpoint para verificação de saúde da aplicação")
@RestController
@RequestMapping("/api")
public class HealthController {

    @Operation(summary = "Verificar saúde da aplicação", description = "Retorna o status da aplicação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aplicação funcionando corretamente")
    })
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}