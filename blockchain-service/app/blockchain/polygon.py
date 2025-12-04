from web3 import Web3
import json
import os
import hashlib
from app.core.config import settings

BASE_DIR = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
ABI_PATH = os.path.join(BASE_DIR, "contracts", "moeda_abi.json")

# Carrega o ABI do contrato
try:
    with open(ABI_PATH, "r") as f:
        ABI_CONTRATO = json.load(f)
except FileNotFoundError:
    # ABI mockado para desenvolvimento
    ABI_CONTRATO = []

w3 = Web3(Web3.HTTPProvider(settings.POLYGON_RPC))
contract = None

if settings.CONTRACT_ADDRESS and ABI_CONTRATO:
    contract = w3.eth.contract(address=settings.CONTRACT_ADDRESS, abi=ABI_CONTRATO)


def gerar_hash_transacao(aluno_id: int, quantidade: int, tipo: str) -> str:
    """Gera hash único para a transação"""
    import time
    dados = f"{aluno_id}{quantidade}{tipo}{int(time.time())}"
    return hashlib.sha256(dados.encode()).hexdigest()


def registrar_moedas(
    aluno_id: int, 
    quantidade: int, 
    tipo_transacao: str
) -> dict:
    """
    Registra transação de moedas na blockchain.
    
    Args:
        aluno_id: ID do aluno
        quantidade: Quantidade de moedas (positivo para crédito, negativo para débito)
        tipo_transacao: Tipo da transação (CREDITO, DEBITO, CONVERSAO)
    
    Returns:
        dict com tx_hash e hash_dados
    """
    if not contract:
        # Modo mock para desenvolvimento
        hash_dados = gerar_hash_transacao(aluno_id, quantidade, tipo_transacao)
        return {
            "tx_hash": f"mock_tx_{hash_dados[:16]}",
            "hash_dados": hash_dados,
            "status": "mock"
        }
    
    hash_dados = gerar_hash_transacao(aluno_id, quantidade, tipo_transacao)
    quantidade_signed = quantidade if tipo_transacao == "CREDITO" else -quantidade
    
    nonce = w3.eth.get_transaction_count(settings.PUBLIC_ADDRESS)
    
    txn = contract.functions.registrarMoedas(
        aluno_id,
        quantidade_signed,
        tipo_transacao,
        hash_dados
    ).build_transaction({
        'nonce': nonce,
        'gas': 300000,
        'maxPriorityFeePerGas': w3.to_wei('25', 'gwei'),
        'maxFeePerGas': w3.to_wei('50', 'gwei'),
    })
    
    signed_txn = w3.eth.account.sign_transaction(txn, private_key=settings.PRIVATE_KEY)
    tx_hash = w3.eth.send_raw_transaction(signed_txn.raw_transaction)
    
    return {
        "tx_hash": w3.to_hex(tx_hash),
        "hash_dados": hash_dados,
        "status": "pending"
    }


def obter_transacoes_aluno(aluno_id: int) -> list:
    """Obtém todas as transações de um aluno"""
    if not contract:
        return []
    
    return contract.functions.obterTransacoesAluno(aluno_id).call()


def obter_saldo_aluno(aluno_id: int) -> int:
    """Obtém o saldo de moedas de um aluno na blockchain"""
    if not contract:
        return 0
    
    saldo = contract.functions.obterSaldoAluno(aluno_id).call()
    return int(saldo) if saldo > 0 else 0


def obter_total_transacoes() -> int:
    """Obtém o total de transações registradas"""
    if not contract:
        return 0
    
    return contract.functions.obterTotalTransacoes().call()

