from typing import List, Dict, Any
from app.blockchain.polygon import (
    registrar_moedas as registrar_na_blockchain,
    obter_transacoes_aluno,
    obter_saldo_aluno,
    obter_total_transacoes
)


class BlockchainService:
    """Service para interação com blockchain de moedas"""

    def registrar_moedas(
        self,
        aluno_id: int,
        quantidade: int,
        tipo_transacao: str
    ) -> Dict[str, Any]:
        """
        Registra transação de moedas na blockchain

        Args:
            aluno_id: ID do aluno
            quantidade: Quantidade de moedas
            tipo_transacao: Tipo da transação (CREDITO, DEBITO, CONVERSAO)

        Returns:
            Dict com informações da transação
        """
        resultado = registrar_na_blockchain(
            aluno_id, quantidade, tipo_transacao)
        resultado["aluno_id"] = aluno_id
        resultado["quantidade"] = quantidade
        return resultado

    def obter_transacoes_aluno(self, aluno_id: int) -> List[Dict[str, Any]]:
        """Obtém todas as transações de um aluno"""
        transacoes = obter_transacoes_aluno(aluno_id)
        return [
            {
                "aluno_id": t[0],
                "quantidade": int(t[1]),
                "timestamp": t[2],
                "tipo_transacao": t[3],
                "hash_dados": t[4]
            }
            for t in transacoes
        ]

    def obter_saldo_aluno(self, aluno_id: int) -> int:
        """Obtém o saldo de moedas de um aluno na blockchain"""
        return obter_saldo_aluno(aluno_id)

    def obter_total_transacoes(self) -> int:
        """Obtém o total de transações registradas"""
        return obter_total_transacoes()
