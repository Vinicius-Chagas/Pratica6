package com.grupo.facens.ex3.infrastructure.blockchain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class BlockchainTokenService {

    private final RestTemplate restTemplate;

    @Value("${blockchain.service.url:http://localhost:8001}")
    private String blockchainServiceUrl;

    public void registrarMoedasEmBlockchain(Long alunoId, int quantidade, String tipoTransacao) {
        try {
            log.info("[BLOCKCHAIN] Registrando {} moedas para o aluno {} em blockchain. Tipo: {}", 
                    quantidade, alunoId, tipoTransacao);

            String url = blockchainServiceUrl + "/api/blockchain/registrar-moedas";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("aluno_id", alunoId);
            requestBody.put("quantidade", quantidade);
            requestBody.put("tipo_transacao", tipoTransacao);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url, 
                HttpMethod.POST, 
                request, 
                new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> result = response.getBody();
                log.info("[BLOCKCHAIN] Transação registrada com sucesso. TX Hash: {}", 
                        result.get("tx_hash"));
            } else {
                log.warn("[BLOCKCHAIN] Falha ao registrar transação. Status: {}", 
                        response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("[BLOCKCHAIN] Erro ao registrar moedas em blockchain para aluno {}: {}", 
                    alunoId, e.getMessage(), e);
            // Não lança exceção para não quebrar o fluxo principal
        }
    }

    public int consultarSaldoBlockchain(Long alunoId) {
        try {
            String url = blockchainServiceUrl + "/api/blockchain/saldo/" + alunoId;
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url, 
                HttpMethod.GET, 
                null, 
                new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> result = response.getBody();
                Object saldo = result.get("saldo");
                if (saldo instanceof Number) {
                    return ((Number) saldo).intValue();
                }
            }
        } catch (Exception e) {
            log.error("[BLOCKCHAIN] Erro ao consultar saldo na blockchain para aluno {}: {}", 
                    alunoId, e.getMessage());
        }
        return 0;
    }
}

