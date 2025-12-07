# Blockchain Service - Moedas do Aluno

Microsserviço Python para registro de transações de moedas do aluno na blockchain Polygon.

## Estrutura

```
blockchain-service/
├── app/
│   ├── blockchain/          # Interação com blockchain
│   ├── controllers/         # Endpoints da API
│   ├── core/                # Configuração
│   ├── schemas/             # DTOs
│   └── services/            # Lógica de negócios
├── contracts/               # Contratos Solidity
└── requirements.txt
```

## Configuração

1. Crie um arquivo `.env` baseado em `env.example`
2. Configure as credenciais da Polygon
3. Instale as dependências: `pip install -r requirements.txt`
4. Execute: `uvicorn app.main:app --reload --port 8001`

## Endpoints

- `POST /api/blockchain/registrar-moedas` - Registra moedas na blockchain
- `GET /api/blockchain/transacoes/{alunoId}` - Lista transações do aluno
- `GET /api/blockchain/saldo/{alunoId}` - Consulta saldo na blockchain

