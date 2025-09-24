# language: pt
Funcionalidade: Sistema de Fórum
  Como um aluno
  Eu quero participar do fórum criando posts e comentários
  Para ser reconhecido como o aluno mais ativo e ganhar um curso gratuito

  Cenário: Verificar aluno mais ativo quando não há atividade
    Dado que não há atividade no fórum
    Quando verifico quem é o aluno mais ativo
    Então não deve haver aluno mais ativo

  Cenário: Aluno mais ativo do fórum ganha curso gratuito
    Dado que existem alunos cadastrados no sistema
    E que não há atividade no fórum
    Quando um aluno cria um tópico no fórum
    E o mesmo aluno comenta no tópico
    E outro aluno cria apenas um tópico
    Então o primeiro aluno deve ser o mais ativo do mês
    E deve receber crédito para curso gratuito

  Cenário: Contagem de contribuições no fórum
    Dado que existe um aluno ativo no fórum
    Quando o aluno cria 1 tópico
    E faz 1 comentário
    Então o total de contribuições deve ser 2

  Cenário: Comentar em tópico inexistente
    Dado que existe um aluno cadastrado
    Quando o aluno tenta comentar em um tópico que não existe
    Então deve receber uma mensagem de erro
