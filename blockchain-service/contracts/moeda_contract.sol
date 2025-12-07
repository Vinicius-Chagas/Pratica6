// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract MoedaAluno {
    struct TransacaoMoeda {
        uint256 alunoId;
        int256 quantidade;  // positivo para crédito, negativo para débito
        uint256 timestamp;
        string tipoTransacao;  // "CREDITO", "DEBITO", "CONVERSAO"
        string hashDados;
    }

    mapping(uint256 => TransacaoMoeda[]) public transacoesPorAluno;
    TransacaoMoeda[] public todasTransacoes;

    event MoedaRegistrada(
        uint256 indexed alunoId,
        int256 quantidade,
        uint256 timestamp,
        string tipoTransacao,
        string hashDados
    );

    function registrarMoedas(
        uint256 _alunoId,
        int256 _quantidade,
        string memory _tipoTransacao,
        string memory _hashDados
    ) public {
        TransacaoMoeda memory transacao = TransacaoMoeda(
            _alunoId,
            _quantidade,
            block.timestamp,
            _tipoTransacao,
            _hashDados
        );
        
        transacoesPorAluno[_alunoId].push(transacao);
        todasTransacoes.push(transacao);
        
        emit MoedaRegistrada(
            _alunoId,
            _quantidade,
            block.timestamp,
            _tipoTransacao,
            _hashDados
        );
    }

    function obterTotalTransacoes() public view returns (uint256) {
        return todasTransacoes.length;
    }

    function obterTransacoesAluno(uint256 _alunoId) 
        public 
        view 
        returns (TransacaoMoeda[] memory) 
    {
        return transacoesPorAluno[_alunoId];
    }

    function obterSaldoAluno(uint256 _alunoId) public view returns (int256) {
        TransacaoMoeda[] memory transacoes = transacoesPorAluno[_alunoId];
        int256 saldo = 0;
        
        for (uint256 i = 0; i < transacoes.length; i++) {
            saldo += transacoes[i].quantidade;
        }
        
        return saldo;
    }

    function obterTransacao(uint256 _transacaoId) 
        public 
        view 
        returns (TransacaoMoeda memory) 
    {
        return todasTransacoes[_transacaoId];
    }
}

