from pydantic import BaseModel, Field
from typing import Optional
from datetime import datetime


class RegistrarMoedasRequest(BaseModel):
    aluno_id: int = Field(..., description="ID do aluno")
    quantidade: int = Field(..., description="Quantidade de moedas")
    tipo_transacao: str = Field(..., description="Tipo: CREDITO, DEBITO, CONVERSAO")


class TransacaoResponse(BaseModel):
    aluno_id: int
    quantidade: int
    timestamp: int
    tipo_transacao: str
    hash_dados: str
    tx_hash: Optional[str] = None


class SaldoResponse(BaseModel):
    aluno_id: int
    saldo: int


class RegistrarMoedasResponse(BaseModel):
    tx_hash: str
    hash_dados: str
    status: str
    aluno_id: int
    quantidade: int

