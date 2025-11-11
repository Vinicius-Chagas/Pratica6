# Domain Layer - Domain-Driven Design (DDD)

Esta pasta contém a camada de domínio do projeto, seguindo os princípios de **Domain-Driven Design (DDD)**.

## Estrutura

```
domain/
├── entity/          # Entidades de domínio
│   ├── Aluno.java
│   ├── Curso.java
│   ├── Post.java
│   └── Comment.java
└── valueobject/     # Value Objects
    ├── Email.java
    ├── Moedas.java
    ├── CreditoCurso.java
    ├── CargaHoraria.java
    ├── TipoPlano.java
    └── Dificuldade.java
```

## 📦 Entities (Entidades)

As entidades são objetos que possuem **identidade única** e ciclo de vida próprio. São mutáveis e rastreáveis através de seu ID.

### 1. **Aluno**
- Representa um aluno do sistema
- Contém lógica de negócio para gerenciamento de planos, moedas e créditos
- Métodos principais:
  - `completarCurso(int moedasRecompensa)` - Completa um curso e adiciona moedas
  - `adicionarCreditos(int quantidade)` - Adiciona créditos de curso
  - `usarCreditos(int quantidade)` - Usa créditos de curso
  - `podeAcessarCurso()` - Verifica permissão de acesso

### 2. **Curso**
- Representa um curso disponível no sistema
- Gerencia estado (ativo/inativo) e metadados do curso
- Métodos principais:
  - `ativar()` / `desativar()` - Gerencia status do curso
  - `atualizar(...)` - Atualiza informações do curso
  - `eNovo()` - Verifica se foi criado nos últimos 30 dias
  - `eParaIniciantes()` / `eAvancado()` - Verificações de dificuldade

### 3. **Post**
- Representa uma postagem no fórum
- Gerencia relacionamento com comentários
- Métodos principais:
  - `adicionarComentario(Comment)` - Adiciona comentário
  - `removerComentario(Comment)` - Remove comentário
  - `foiCriadoPor(Aluno)` - Verifica autoria
  - `eRecente()` - Verifica se foi criado nas últimas 24h

### 4. **Comment**
- Representa um comentário em um post
- Métodos principais:
  - `atualizar(String)` - Atualiza conteúdo com validação
  - `foiCriadoPor(Aluno)` - Verifica autoria
  - `pertenceAo(Post)` - Valida relacionamento com post

## 💎 Value Objects

Value Objects são objetos **imutáveis** que não possuem identidade própria. São definidos apenas por seus atributos e encapsulam lógica de validação e comportamento.

### 1. **Email**
- Representa um endereço de email válido
- **Validações:**
  - Formato válido de email
  - Não pode ser vazio
  - Sempre armazenado em lowercase
- **Características:**
  - Imutável
  - Único no sistema
  - Validação no momento da criação

### 2. **Moedas**
- Representa a quantidade de moedas virtuais de um aluno
- **Regras de negócio:**
  - Nunca pode ser negativo
  - Operações de adicionar e subtrair retornam nova instância
  - Validação de saldo antes de subtrair
- **Métodos:**
  - `adicionar(int)` - Adiciona moedas
  - `subtrair(int)` - Subtrai moedas (valida saldo)
  - `temSaldo(int)` - Verifica se tem saldo suficiente

### 3. **CreditoCurso**
- Representa créditos que permitem acesso a cursos
- **Regras de negócio:**
  - Nunca pode ser negativo
  - Operações retornam nova instância (imutabilidade)
  - Validação antes de usar créditos
- **Métodos:**
  - `adicionar(int)` - Adiciona créditos
  - `usar(int)` - Usa créditos (valida disponibilidade)
  - `temCredito(int)` - Verifica disponibilidade

### 4. **CargaHoraria**
- Representa a duração de um curso em horas
- **Validações:**
  - Mínimo: 1 hora
  - Máximo: 500 horas
  - Não pode ser nulo
- **Métodos auxiliares:**
  - `eCursoCurto()` - Menos de 20 horas
  - `eCursoMedio()` - Entre 20 e 60 horas
  - `eCursoLongo()` - 60 horas ou mais

### 5. **TipoPlano**
- Enum representando tipos de plano de assinatura
- **Valores:**
  - `BASICO` - Plano básico (1 crédito mensal)
  - `PREMIUM` - Plano premium (3 créditos mensais)
- **Métodos:**
  - `getCreditosMensais()` - Retorna quantidade de créditos
  - `permiteAcessoIlimitado()` - Apenas PREMIUM
  - `eBasico()` / `ePremium()` - Verificações de tipo

### 6. **Dificuldade**
- Enum representando nível de dificuldade de curso
- **Valores:**
  - `INICIANTE` - Nível 1
  - `INTERMEDIARIO` - Nível 2
  - `AVANCADO` - Nível 3
- **Métodos:**
  - `maisAvancoQue(Dificuldade)` - Compara níveis
  - `menosAvancoQue(Dificuldade)` - Compara níveis
  - Métodos de verificação de tipo

## 🎯 Benefícios da Estrutura DDD

### 1. **Separação de Responsabilidades**
- Lógica de negócio isolada na camada de domínio
- Fácil de testar unitariamente
- Reduz acoplamento com infraestrutura

### 2. **Validação Centralizada**
- Value Objects garantem estado sempre válido
- Impossível criar objetos inválidos
- Validações acontecem na criação

### 3. **Imutabilidade**
- Value Objects são imutáveis
- Reduz bugs relacionados a estado compartilhado
- Thread-safe por natureza

### 4. **Expressividade**
- Código mais legível e autodocumentado
- Regras de negócio explícitas
- Linguagem ubíqua do domínio

### 5. **Manutenibilidade**
- Mudanças em regras de negócio concentradas
- Fácil adicionar novos comportamentos
- Refatoração mais segura

## 📋 Uso dos Value Objects

### Exemplo: Criando um Email
```java
// ✅ Correto - valida automaticamente
Email email = Email.of("aluno@facens.br");

// ❌ Lança exceção - email inválido
Email emailInvalido = Email.of("email-invalido");
```

### Exemplo: Trabalhando com Moedas
```java
Moedas saldo = Moedas.of(100);
saldo = saldo.adicionar(50);  // saldo = 150

if (saldo.temSaldo(30)) {
    saldo = saldo.subtrair(30);  // saldo = 120
}
```

### Exemplo: Criando um Curso
```java
CargaHoraria carga = CargaHoraria.of(40);
Dificuldade nivel = Dificuldade.INTERMEDIARIO;

Curso curso = new Curso(
    "Java Avançado",
    "Curso de Java avançado",
    "Programação",
    nivel,
    carga
);
```

## 🔄 Migração das Entities Antigas

As entities originais em `model/` ainda existem para compatibilidade. A migração gradual deve:

1. Atualizar repositories para usar as novas entities
2. Atualizar services para usar value objects
3. Ajustar DTOs conforme necessário
4. Remover entities antigas quando não houver mais referências

## 📚 Referências

- **Domain-Driven Design** by Eric Evans
- **Implementing Domain-Driven Design** by Vaughn Vernon
- [DDD Community](https://dddcommunity.org/)

