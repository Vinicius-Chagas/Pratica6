package com.grupo.facens.ex3.infrastructure.blockchain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class BlockchainTokenServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private BlockchainTokenService blockchainTokenService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(blockchainTokenService, "blockchainServiceUrl", "http://localhost:8001");
    }

    @Test
    void deveRegistrarMoedasEmBlockchainComSucesso() {
        // Arrange
        Long alunoId = 1L;
        int quantidade = 100;
        String tipoTransacao = "CREDITO";

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("tx_hash", "0x123abc");
        responseBody.put("hash_dados", "hash123");
        responseBody.put("status", "pending");

        ResponseEntity<Map<String, Object>> response = new ResponseEntity<>(
                responseBody,
                HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class))).thenReturn(response);

        // Act
        blockchainTokenService.registrarMoedasEmBlockchain(alunoId, quantidade, tipoTransacao);

        // Assert
        verify(restTemplate).exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class));
    }

    @Test
    void deveLidarComErroAoRegistrarMoedas() {
        // Arrange
        Long alunoId = 1L;
        int quantidade = 100;
        String tipoTransacao = "CREDITO";

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class))).thenThrow(new RuntimeException("Erro de conexão"));

        // Act & Assert - não deve lançar exceção
        blockchainTokenService.registrarMoedasEmBlockchain(alunoId, quantidade, tipoTransacao);

        verify(restTemplate).exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class));
    }

    @Test
    void deveConsultarSaldoBlockchainComSucesso() {
        // Arrange
        Long alunoId = 1L;
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("aluno_id", alunoId);
        responseBody.put("saldo", 500);

        ResponseEntity<Map<String, Object>> response = new ResponseEntity<>(
                responseBody,
                HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                eq(null),
                any(ParameterizedTypeReference.class))).thenReturn(response);

        // Act
        int saldo = blockchainTokenService.consultarSaldoBlockchain(alunoId);

        // Assert
        assertThat(saldo).isEqualTo(500);
        verify(restTemplate).exchange(
                anyString(),
                eq(HttpMethod.GET),
                eq(null),
                any(ParameterizedTypeReference.class));
    }

    @Test
    void deveRetornarZeroQuandoErroAoConsultarSaldo() {
        // Arrange
        Long alunoId = 1L;

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                eq(null),
                any(ParameterizedTypeReference.class))).thenThrow(new RuntimeException("Erro de conexão"));

        // Act
        int saldo = blockchainTokenService.consultarSaldoBlockchain(alunoId);

        // Assert
        assertThat(saldo).isEqualTo(0);
    }

    @Test
    void deveRetornarZeroQuandoRespostaVazia() {
        // Arrange
        Long alunoId = 1L;

        ResponseEntity<Map<String, Object>> response = new ResponseEntity<>(
                null,
                HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                eq(null),
                any(ParameterizedTypeReference.class))).thenReturn(response);

        // Act
        int saldo = blockchainTokenService.consultarSaldoBlockchain(alunoId);

        // Assert
        assertThat(saldo).isEqualTo(0);
    }
}
