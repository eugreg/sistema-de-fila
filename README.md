# Sistema de Chamados — Fila de Prioridade

Aplicação Java de console que gerencia chamados de suporte usando uma
**fila de prioridade implementada manualmente** (heap binário mínimo).

---

## Estrutura do Projeto

```
SistemaChamados/
├── pom.xml
└── src/
    └── main/
        └── java/
            └── chamados/
                ├── Chamado.java          ← entidade do domínio
                ├── SistemaDeChamado.java ← fila de prioridade manual
                └── Main.java             ← interface de console
```

---

## Pré-requisitos

| Ferramenta | Versão mínima |
|------------|---------------|
| JDK        | 17            |
| Maven      | 3.8           |

Verifique com:
```bash
java -version
mvn -version
```

---

## Como compilar e executar

```bash
# 1. Acesse a pasta do projeto
cd SistemaChamados

# 2. Compile e gere o JAR
mvn clean package

# 3. Execute
java -jar target/sistema-chamados.jar
```

---

## Menu de operações

```
 1. Adicionar chamado          — enfileira com prioridade e timestamp
 2. Atender próximo chamado    — remove e exibe o de maior prioridade
 3. Consultar próximo (peek)   — exibe sem remover
 4. Exibir chamados pendentes  — lista todos na fila
 5. Quantidade na fila         — tamanho atual
 0. Sair
```

---

## Regras de prioridade

- **Menor valor** de prioridade = atendido **primeiro** (ex.: 1 > 2 > 3).
- **Desempate por chegada**: entre chamados com mesma prioridade, o que chegou
  antes (timestamp menor) é atendido primeiro — garantindo justiça (fairness).

---

## Complexidade dos algoritmos

| Operação             | Complexidade |
|----------------------|:------------:|
| `adicionar()`        | O(log n)     |
| `proximo()`          | O(log n)     |
| `consultarOProximo()`| O(1)         |
| `tamanho()`          | O(1)         |
| Espaço total         | O(n)         |

O array interno dobra de tamanho quando necessário (crescimento amortizado).
