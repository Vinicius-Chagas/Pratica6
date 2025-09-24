# language: pt
Funcionalidade: Sistema de Moedas
  Como um aluno premium
  Eu quero utilizar moedas após completar cursos
  Para converter em cursos gratuitos ou criptomoedas

  Cenário: Aluno Premium utiliza moedas após completar curso
    Dado que existe um aluno premium com 12 cursos completados
    E o aluno possui 0 moedas inicialmente
    Quando o sistema distribui moedas por cursos completados
    Então o aluno deve receber 36 moedas
    E pode adicionar mais 3 moedas
    E pode converter 3 moedas para cursos
    E pode converter 3 moedas para criptomoedas
    E o saldo final deve ser 33 moedas

  Cenário: Conversão de moedas para cursos
    Dado que existe um aluno premium com moedas disponíveis
    Quando o aluno converte moedas para cursos
    Então as moedas devem ser deduzidas
    E os créditos de curso devem ser adicionados

  Cenário: Conversão de moedas para criptomoedas
    Dado que existe um aluno premium com moedas disponíveis
    Quando o aluno converte moedas para criptomoedas
    Então as moedas devem ser deduzidas do saldo

  Cenário: Tentativa de conversão com saldo insuficiente
    Dado que existe um aluno premium sem moedas
    Quando o aluno tenta converter 5 moedas para cursos
    Então a conversão deve falhar
    E o saldo deve permanecer 0

  Cenário: Distribuição de moedas baseada em cursos completados
    Dado que existe um aluno premium com 5 cursos completados
    Quando o sistema distribui moedas por cursos completados
    Então o aluno deve receber 15 moedas

  Cenário: Consulta de saldo de moedas
    Dado que existe um aluno premium com moedas
    Quando consulto o saldo do aluno
    Então devo receber o valor correto de moedas
