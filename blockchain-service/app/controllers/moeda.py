from fastapi import APIRouter, HTTPException
from app.schemas.moeda import (
    RegistrarMoedasRequest,
    RegistrarMoedasResponse,
    TransacaoResponse,
    SaldoResponse
)
from app.services.blockchain_service import BlockchainService

router = APIRouter()
blockchain_service = BlockchainService()


@router.post("/registrar-moedas", response_model=RegistrarMoedasResponse)
async def registrar_moedas(request: RegistrarMoedasRequest):
    """
    Registra uma transação de moedas na blockchain
    """
    try:
        resultado = blockchain_service.registrar_moedas(
            request.aluno_id,
            request.quantidade,
            request.tipo_transacao
        )
        return RegistrarMoedasResponse(**resultado)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


@router.get("/transacoes/{aluno_id}", response_model=list[TransacaoResponse])
async def obter_transacoes_aluno(aluno_id: int):
    """
    Obtém todas as transações de moedas de um aluno
    """
    try:
        transacoes = blockchain_service.obter_transacoes_aluno(aluno_id)
        return [TransacaoResponse(**t) for t in transacoes]
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


@router.get("/saldo/{aluno_id}", response_model=SaldoResponse)
async def obter_saldo_aluno(aluno_id: int):
    """
    Obtém o saldo de moedas de um aluno na blockchain
    """
    try:
        saldo = blockchain_service.obter_saldo_aluno(aluno_id)
        return SaldoResponse(aluno_id=aluno_id, saldo=saldo)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


@router.get("/total-transacoes")
async def obter_total_transacoes():
    """
    Obtém o total de transações registradas na blockchain
    """
    try:
        total = blockchain_service.obter_total_transacoes()
        return {"total": total}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
