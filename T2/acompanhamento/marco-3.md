# Marco 3 — Estratégia Algorítmica

## 1. Propriedade estrutural

A propriedade exigida é a **conectividade forte**: para todo par de cidades `v` e `w`, existe caminho dirigido de `v` para `w` **e** de `w` para `v`.

Essa relação divide o dígrafo em **componentes fortemente conexas** (componentes f-conexas). O grafo é fortemente conexo quando todos os vértices ficam na mesma componente.

## 2. Critério utilizado

```text
fortemente conexo  ⇔  existe exatamente 1 componente f-conexa
```

As componentes são contadas pelo **algoritmo de Kosaraju-Sharir** (material E1, seção 3.3):

1. **Fase 1:** calcular a pós-ordem reversa (`reversePost`) de `Gᵀ`, o grafo com as arestas invertidas;
2. **Fase 2:** executar a DFS em `G`, iniciando nos vértices ainda não marcados na ordem da fase 1. Cada DFS iniciada marca exatamente uma componente.

Por que funciona:

* `G` e `Gᵀ` têm as mesmas componentes, porque inverter todas as arestas não desfaz nenhum ciclo;
* contraindo cada componente num vértice, obtém-se o **grafo condensado**, que é um DAG;
* a pós-ordem reversa em um DAG é uma ordenação topológica (Prop. A do material). Assim, o primeiro vértice da `reversePost` de `Gᵀ` está numa componente **fonte** de `Gᵀ`, que é uma componente **sumidouro** de `G`;
* a DFS em `G` iniciada num sumidouro não consegue sair dele, então marca só aquela componente. As seguintes repetem o raciocínio entre os vértices restantes.

A primeira ideia era fazer duas DFS a partir da cidade `1`: uma em `G` (`1` alcança todas?) e outra em `Gᵀ` (todas alcançam `1`?). Ela também está correta e custa o mesmo `O(V + E)`. Mesmo assim, trocamos pelo Kosaraju-Sharir por dois motivos. Ele é a referência da disciplina para conectividade forte (`algs4.KosarajuSharirSCC`). E o vetor `id[]` que ele produz já fornece o par pedido na resposta `NO` (seção 4).

## 3. Rastreamento manual

Mesma instância do marco 1:

```text
4 5          1 ───→ 2
1 2          ↑ ↘    │
2 3          │  4   │
3 1          │  ↑   ↓
1 4          └──┴── 3
3 4
```

### Listas de adjacência

A `Bag` do `algs4` insere no início da lista, então os vizinhos aparecem na ordem inversa da leitura:

```text
G               Gᵀ = G.reverse()
1: 4, 2         1: 3
2: 3            2: 1
3: 4, 1         3: 2
4: -            4: 3, 1
```

### Fase 1 — DFS em Gᵀ (`DepthFirstOrder`)

Os vértices são percorridos em ordem crescente. Cada vértice é empilhado em `reversePost` quando sua exploração termina.

| Passo | Ação                       | reversePost (topo à esquerda) |
| ----- | -------------------------- | ----------------------------- |
| 1     | raiz 1, visita 1           | —                             |
| 2     | 1 → 3, visita 3            | —                             |
| 3     | 3 → 2, visita 2            | —                             |
| 4     | 2 → 1 já marcado           | —                             |
| 5     | termina 2                  | 2                             |
| 6     | termina 3                  | 3, 2                          |
| 7     | termina 1                  | 1, 3, 2                       |
| 8     | raiz 4, visita 4           | 1, 3, 2                       |
| 9     | 4 → 3 e 4 → 1 já marcados  | 1, 3, 2                       |
| 10    | termina 4                  | 4, 1, 3, 2                    |

A ordem da fase 2 começa pelo `4`, que é justamente a cidade sem voo de saída em `G`, ou seja, o sumidouro.

### Fase 2 — DFS em G na ordem `4, 1, 3, 2`

| Ordem | Situação         | DFS em G                                 | id atribuído              |
| ----- | ---------------- | ---------------------------------------- | ------------------------- |
| 4     | não marcado      | visita 4, sem saída                      | id[4] = 0                 |
| 1     | não marcado      | 1 → 4 (marcado), 1 → 2 → 3, 3 → 1 (marcado) | id[1] = id[2] = id[3] = 1 |
| 3     | já marcado, pula | —                                        | —                         |
| 2     | já marcado, pula | —                                        | —                         |

```text
id(1..4) = [1, 1, 1, 0]
count    = 2
```

A DFS iniciada em `1` encontra o `4` já marcado e não entra nele. É isso que impede que duas componentes se misturem. Como `count = 2`, o grafo **não** é fortemente conexo.

## 4. Resposta exigida pelo problema

A componente `id = 0` é sempre um sumidouro de `G`, ou seja, nenhuma aresta sai dela. Por isso:

* `a` = qualquer vértice com `id[a] = 0`;
* `b` = qualquer vértice com `id[b] ≠ 0`;

e não existe rota de `a` para `b`.

Na instância, `a = 4` e `b = 1`:

```text
NO
4 1
```

O enunciado mostra `4 2`, mas aceita qualquer par válido.

## 5. Implementações de referência

| Classe `algs4`      | Papel                                                |
| ------------------- | ---------------------------------------------------- |
| `Digraph`           | listas de adjacência e `reverse()`, que gera `Gᵀ`    |
| `Bag`               | lista de adjacência de cada vértice                  |
| `DepthFirstOrder`   | fase 1: pós-ordem reversa                            |
| `Stack`             | guarda a `reversePost`                               |
| `KosarajuSharirSCC` | fase 2: `marked[]`, `id[]` e `count`                 |

Adaptações previstas:

* converter as cidades `1..n` para os vértices `0..n-1` e voltar ao imprimir;
* trocar `StdIn` por uma leitura mais rápida, já que são até 200.000 arestas;
* usar `count()` para decidir entre `YES` e `NO`, e `id()` para escolher o par;
* remover o `check()`, que usa `TransitiveClosure`, uma matriz `V × V` inviável para `V = 100.000`;
* tratar a profundidade da recursão, que pode chegar a 100.000 chamadas, por exemplo num ciclo que passa por todas as cidades.

Nesta etapa não foi implementado código.

## 6. Complexidade

| Item                         | Tempo      | Memória    |
| ---------------------------- | ---------- | ---------- |
| `G` (listas de adjacência)   | `O(V + E)` | `O(V + E)` |
| `Gᵀ` (`reverse()`)           | `O(V + E)` | `O(V + E)` |
| Fase 1 (DFS em `Gᵀ`)         | `O(V + E)` | `O(V)`     |
| Fase 2 (DFS em `G`)          | `O(V + E)` | `O(V)`     |
| Escolha do par `a b`         | `O(V)`     | —          |

* **Representação do grafo:** `O(V + E)`;
* **Memória auxiliar:** `O(V)`, para `marked[]`, `id[]`, `reversePost` e a pilha de recursão;
* **Tempo total:** `O(V + E)`. O gargalo são as duas DFS e a construção de `Gᵀ`.

Depois de pronto, `count()` e `id(v)` respondem em `O(1)`.
