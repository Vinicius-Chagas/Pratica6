# language: pt
Funcionalidade: Gerenciamento de Assinatura
  Como um aluno
  Eu quero assinar um plano básico
  Para ter acesso ao conteúdo da plataforma

  Cenário: Aluno assina plano básico com sucesso
    Dado que existe um aluno sem plano ativo
    Quando o aluno ativa uma assinatura
    Então o aluno deve ter um plano básico
    E deve ter acesso ao conteúdo do plano básico

  Cenário: Verificação de acesso ao plano básico
    Dado que existe um aluno com plano básico
    Quando verifico o acesso ao plano básico
    Então o acesso deve ser confirmado

  Cenário: Aluno sem plano não tem acesso
    Dado que existe um aluno sem plano ativo
    Quando verifico o acesso ao plano básico
    Então o acesso deve ser negado
