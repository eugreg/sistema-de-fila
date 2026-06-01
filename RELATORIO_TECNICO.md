# Relatório Técnico — Sistema de Chamados com Fila de Prioridade

## 1. Introdução

Este documento descreve as decisões arquiteturais adotadas no desenvolvimento do
**Sistema de Chamados**, aplicação Java de console que gerencia chamados de suporte
utilizando uma fila de prioridade implementada manualmente, sem uso de classes da
biblioteca padrão (`PriorityQueue`, `Collections`, etc.).

---

## 2. Estrutura de Dados Escolhida: Heap Binário Mínimo

### 2.1 Por que heap e não fila encadeada?

Duas opções foram avaliadas:

| Critério | Lista encadeada ordenada | Heap binário mínimo |
|---|---|---|
| Inserção | O(n) — percorre até achar posição | **O(log n)** |
| Remoção do mínimo | O(1) — cabeça da lista | **O(log n)** |
| Consulta do mínimo | O(1) | **O(1)** |
| Implementação | Simples | Moderada |

Para um sistema de chamados real, o volume de inserções pode ser alto e contínuo.
A inserção O(n) da lista encadeada ordenada tornaria o sistema lento sob carga.
O heap garante **O(log n) para inserção e remoção**, sendo a escolha mais adequada.

### 2.2 Representação em array

O heap é armazenado em um **array dinâmico** (sem ponteiros), aproveitando a
propriedade indexável do heap binário:

```
Para o nó no índice i:
  - filho esquerdo: 2*i + 1
  - filho direito:  2*i + 2
  - pai:            (i - 1) / 2
```

Essa representação elimina o overhead de memória de nós encadeados e melhora a
localidade de cache (cache-friendly), resultando em melhor desempenho prático.

---

## 3. Regras de Prioridade e Desempate

### 3.1 Prioridade principal

O campo `prioridade` é um inteiro onde **menor valor = maior urgência**.  
Exemplo: prioridade 1 (crítico) é atendido antes de prioridade 3 (baixo).

### 3.2 Desempate por ordem de chegada (FIFO)

Chamados com a mesma prioridade são ordenados pelo **timestamp de criação**
(`System.nanoTime()`). O chamado que chegou primeiro tem menor timestamp e,
portanto, é atendido antes — garantindo justiça (fairness) e comportamento FIFO
dentro de cada nível de prioridade.

A comparação está centralizada no método privado `temPrioridade(Chamado a, Chamado b)`:

```java
private boolean temPrioridade(Chamado a, Chamado b) {
    if (a.getPrioridade() != b.getPrioridade()) {
        return a.getPrioridade() < b.getPrioridade();
    }
    return a.getTimestamp() < b.getTimestamp(); // desempate por chegada
}
```

---

## 4. Crescimento Dinâmico do Array

O array interno inicia com capacidade 16. Quando fica cheio, é substituído por um
novo array com o **dobro da capacidade** (`garantirCapacidade()`).

O custo amortizado por inserção permanece **O(1)** para o redimensionamento
(análise amortizada: cada elemento é copiado no máximo O(log n) vezes ao longo
da vida do array).

---

## 5. Complexidade dos Algoritmos

| Método | Complexidade Temporal | Complexidade Espacial | Justificativa |
|---|---|---|---|
| `adicionar()` | O(log n) | O(1) | sift-up percorre altura do heap |
| `proximo()` | O(log n) | O(1) | sift-down percorre altura do heap |
| `consultarOProximo()` | O(1) | O(1) | acesso direto ao índice 0 |
| `tamanho()` | O(1) | O(1) | campo inteiro mantido atualizado |
| Espaço total | — | O(n) | array proporcional ao número de elementos |

A **altura de um heap com n elementos** é ⌊log₂ n⌋, daí o O(log n).

---

## 6. Organização das Classes

```
chamados/
├── Chamado.java          — Entidade de domínio
│                           Atributos: prioridade (int), descricao (String),
│                           timestamp (long, imutável, atribuído no construtor)
│
├── SistemaDeChamado.java — Fila de prioridade (heap binário mínimo manual)
│                           Implementa: adicionar, proximo, consultarOProximo,
│                           tamanho, exibirChamadosPendentes
│                           Operações internas: siftUp, siftDown, trocar,
│                           garantirCapacidade
│
└── Main.java             — Interface de console com menu interativo
                            Usa Scanner para entrada e switch expression (Java 17)
```

### Decisões de encapsulamento

- `heap`, `tamanho` e todos os métodos de manipulação interna são **privados**.
  A classe `SistemaDeChamado` expõe apenas a interface definida no diagrama.
- `timestamp` em `Chamado` é `final` — não pode ser alterado após a criação,
  garantindo integridade do critério de desempate.

---

## 7. Como Compilar e Executar

**Pré-requisitos:** JDK 17+ e Maven 3.8+

```bash
cd SistemaChamados
mvn clean package
java -jar target/sistema-chamados.jar
```

Sem Maven (javac direto):

```bash
cd SistemaChamados/src/main/java
javac chamados/*.java -d ../../../target/classes
cd ../../../target/classes
jar cfe ../sistema-chamados.jar chamados.Main chamados/*.class
java -jar ../sistema-chamados.jar
```

---

## 8. Conclusão

A escolha do **heap binário mínimo implementado manualmente em array** atende
todos os requisitos do projeto:

- Respeita rigorosamente a prioridade (menor valor primeiro).
- Desempata por tempo de chegada (FIFO entre iguais).
- Oferece inserção e remoção em O(log n), adequado para alto volume.
- Não utiliza nenhuma estrutura pronta da API Java.
- Cresce dinamicamente sem limite fixo de chamados.
