import os
from dotenv import load_dotenv
from web3 import Web3

load_dotenv()


class Settings:
    POLYGON_RPC: str = os.getenv("POLYGON_RPC", "")
    PRIVATE_KEY: str = os.getenv("PRIVATE_KEY", "")
    PUBLIC_ADDRESS: str = Web3.to_checksum_address(
        os.getenv("PUBLIC_ADDRESS", ""))
    CONTRACT_ADDRESS: str = Web3.to_checksum_address(
        os.getenv("CONTRACT_ADDRESS", ""))

    # Configurações do serviço
    API_PORT: int = int(os.getenv("API_PORT", "8001"))
    API_HOST: str = os.getenv("API_HOST", "0.0.0.0")


settings = Settings()
