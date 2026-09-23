# Marco 2 — Conectividade Forte

## 1. Propriedade estrutural

O problema Flight Routes Check utiliza um grafo direcionado.
A propriedade que precisa ser verificada é a **conectividade forte**, ou seja, deve ser possível sair de qualquer cidade e chegar a qualquer outra cidade.
Para isso, foi utilizada uma pequena instância com 4 vértices e 5 arestas:

```text
1 → 2
↑   ↓
3 ←─┘

1 → 4
3 → 4
```

Lista de adjacência:

```text
1: 2, 4
2: 3
3: 1, 4
4: -
```

## 2. Critério utilizado

Para verificar a conectividade forte, são realizados dois DFS:

1. DFS no grafo original, partindo de um vértice.
2. DFS no grafo reverso, com todas as arestas invertidas, partindo do mesmo vértice.

Se os dois DFS alcançarem todos os vértices, o grafo é fortemente conexo.

## 3. Execução manual

### DFS no grafo original

Começando pelo vértice `1`:

```text
1 → 2 → 3 → 4
```

Todos os vértices são alcançados:

```text
Visitados = {1, 2, 3, 4}
```

### DFS no grafo reverso

Invertendo as arestas:

```text
1: 3
2: 1
3: 2
4: 1, 3
```

Começando novamente pelo vértice `1`:

```text
1 → 3 → 2
```

O vértice `4` não é alcançado:

```text
Visitados = {1, 2, 3}
```

Logo, o grafo não é fortemente conexo.

## 4. Estado utilizado pelo DFS

O DFS utiliza o vetor `marcado[v]` para controlar os vértices já visitados.

O estado é reiniciado antes da segunda busca:

```text
DFS no grafo original
→ marcado[v]

DFS no grafo reverso
→ marcado[v] novamente
```

Além do vetor de visitados, é necessário manter o **grafo reverso**, utilizado na segunda busca.

## 5. Complexidade

Considerando uma representação por lista de adjacência:

- Construção do grafo: `O(V + E)`
- Construção do grafo reverso: `O(V + E)`
- Dois DFS: `O(V + E)`

Portanto:

```text
Tempo: O(V + E)
Espaço: O(V + E)
```

## 6. Conclusão

Neste caso, o DFS foi utilizado para verificar a conectividade forte do grafo direcionado.

O primeiro DFS alcançou todas as cidades, porém o DFS no grafo reverso não alcançou a cidade `4`.

Portanto:

```text
Grafo fortemente conexo = NÃO
```

Um par válido de cidades para o problema é:

```text
4 1
```

pois não existe caminho da cidade `4` até a cidade `1`.
