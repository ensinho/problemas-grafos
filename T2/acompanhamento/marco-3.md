# Marco 3 — Estratégia Algorítmica

## 1. Propriedade estrutural

O problema Flight Routes Check trabalha com um grafo direcionado:

- Vértices representam cidades.
- Arestas representam voos direcionados.
- Precisamos verificar se é possível viajar de qualquer cidade para qualquer outra.

A propriedade estrutural necessária é a **conectividade forte**.

---

## 2. Critério utilizado

Para verificar a conectividade forte, utilizamos duas buscas em profundidade (DFS).

Escolhemos uma cidade como referência, por exemplo a cidade `1`.

### Primeiro DFS — grafo original

Verificamos se:

```text
1 → todas as cidades
```

Se alguma cidade não for alcançada, o grafo não é fortemente conexo.

### Segundo DFS — grafo reverso

Invertendo todas as arestas do grafo, fazemos novamente um DFS a partir da cidade `1`.

Isso verifica, no grafo original, se:

```text
todas as cidades → 1
```

Se as duas buscas alcançarem todas as cidades:

```text
1 → todas
e
todas → 1
```

então qualquer cidade `A` consegue chegar a qualquer cidade `B` através de:

```text
A → 1 → B
```

Logo, o grafo é fortemente conexo.

---

## 3. Rastreamento manual

Utilizando uma instância pequena:

```text
4 5

1 2
2 3
3 1
1 4
3 4
```

Lista de adjacência:

```text
1: 2, 4
2: 3
3: 1, 4
4: -
```

### DFS no grafo original

Começando em `1`:

```text
1 → 2 → 3 → 4
```

Todos os vértices são visitados:

```text
Visitados = {1, 2, 3, 4}
```

Portanto, `1` consegue chegar a todas as cidades.

### DFS no grafo reverso

Invertendo as arestas:

```text
1: 3
2: 1
3: 2
4: 1, 3
```

Começando novamente em `1`:

```text
1 → 3 → 2
```

Agora:

```text
Visitados = {1, 2, 3}
```

A cidade `4` não foi alcançada.

Isso significa que, no grafo original, `4` não consegue chegar em `1`.

Portanto, o grafo não é fortemente conexo.

---

## 4. Resposta exigida pelo problema

O problema não pede apenas `YES` ou `NO`.

Quando o grafo não é fortemente conexo, precisamos informar duas cidades `a` e `b` em que `a` não consegue chegar em `b`.

Neste exemplo:

```text
4 → 1
```

não existe.

Então uma resposta válida é:

```text
NO
4 1
```

O par é obtido diretamente a partir do vértice que não foi alcançado durante uma das buscas.

---

## 5. Implementações de referência

As principais estruturas do `algs4` utilizadas como referência são:

- `Digraph` — representação do grafo direcionado.
- `DirectedDFS` — realização da busca em profundidade para verificar alcançabilidade.

As adaptações necessárias são:

- construir o grafo reverso;
- realizar as duas buscas;
- verificar quais vértices foram alcançados;
- identificar um par de cidades que não possui caminho entre si quando a resposta for `NO`.

Nesta etapa não será implementado o código. O foco é entender e justificar a estratégia.

---

## 6. Complexidade

Considerando uma representação por lista de adjacência:

- Construção do grafo: `O(V + E)`
- Construção do grafo reverso: `O(V + E)`
- Primeiro DFS: `O(V + E)`
- Segundo DFS: `O(V + E)`

Como essas operações são realizadas uma quantidade constante de vezes:

```text
Tempo total: O(V + E)
```

A representação dos dois grafos utiliza:

```text
O(V + E)
```

A memória auxiliar utilizada pelas buscas é:

```text
O(V)
```

Assim, a memória total permanece:

```text
O(V + E)
```