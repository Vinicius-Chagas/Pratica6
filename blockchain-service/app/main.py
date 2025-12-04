from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.controllers.moeda import router as moeda_router
from app.core.config import settings

app = FastAPI(
    title="Blockchain Service - Moedas do Aluno",
    version="1.0.0",
    description="API para registro de transações de moedas do aluno na blockchain Polygon"
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(moeda_router, prefix="/api/blockchain", tags=["Blockchain"])


@app.get("/health")
async def health():
    return {"status": "ok"}


if __name__ == "__main__":
    import uvicorn
    uvicorn.run(
        "app.main:app",
        host=settings.API_HOST,
        port=settings.API_PORT,
        reload=True
    )

