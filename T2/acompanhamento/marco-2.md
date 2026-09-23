# Marco 2 — Componentes Conexas

## 1. Instância utilizada

Para este marco foi utilizado um grafo simples não direcionado com 6 vértices e 6 arestas.

### Grafo

```text
      A ------- B
     /           \
    D             C ---- E ---- F
     \           /
      -----------
```

### Arestas

- A - B
- B - C
- C - D
- D - A
- C - E
- E - F

### Lista de adjacência

```text
A: B, D
B: A, C
C: B, D, E
D: A, C
E: C, F
F: E
```

Todos os vértices possuem caminho entre si, portanto o grafo possui apenas uma componente conexa:

```text
C1 = {A, B, C, D, E, F}
```

## 2. Excentricidade, raio e diâmetro

A excentricidade de um vértice é a maior distância entre ele e qualquer outro vértice da mesma componente.

| Vértice | Excentricidade |
|---|---:|
| A | 4 |
| B | 3 |
| C | 2 |
| D | 3 |
| E | 3 |
| F | 4 |

A partir desses valores:

- Raio = 2
- Diâmetro = 4
- Centro = {C}

C possui a menor excentricidade, igual a 2, por isso é o único vértice do centro do grafo.

## 3. Execução do DFS

Para identificar as componentes conexas, é utilizada uma busca em profundidade (DFS).

A busca começa pelo vértice A.

Considerando a ordem dos vizinhos apresentada na lista de adjacência, a busca segue:

```text
A → B → C → D
        ↓
        E → F
```

Passo a passo:

1. A é visitado.
2. A leva até B.
3. B leva até C.
4. C leva até D.
5. D não possui novos vértices para visitar.
6. A busca retorna para C.
7. C leva até E.
8. E leva até F.
9. F não possui novos vértices para visitar.

Ao final, todos os vértices foram visitados:

```text
A, B, C, D, E, F
```

Como todos foram alcançados a partir de A, existe apenas uma componente conexa.

## 4. Estado utilizado pelo DFS

O DFS precisa controlar quais vértices já foram visitados para evitar que um vértice seja percorrido novamente.

Para isso, é utilizado o vetor `marcado[v]`.

Inicialmente, todos os vértices estão como não visitados.

Durante o DFS, o vértice é marcado no momento em que é visitado.

Ao final, todos os vértices estão marcados:

```text
A = visitado
B = visitado
C = visitado
D = visitado
E = visitado
F = visitado
```

Para encontrar todas as componentes do grafo, o processo pode ser repetido para cada vértice que ainda não tenha sido visitado.

Neste caso, como todos foram alcançados a partir de A, não é necessário iniciar outro DFS.

## 5. Complexidade

Considerando uma representação por lista de adjacência, o DFS percorre os vértices e as arestas do grafo.

Complexidade de tempo:

```text
O(V + E)
```

Complexidade de memória:

```text
O(V + E)
```

A lista de adjacência ocupa `O(V + E)` e o vetor de vértices visitados ocupa `O(V)`.

## 6. Conclusão

Neste caso, o DFS foi utilizado para identificar as componentes conexas do grafo.

Os resultados obtidos foram:

- Componentes conexas: 1
- Raio: 2
- Diâmetro: 4
- Centro: {C}

A execução manual mostra como o controle dos vértices visitados permite percorrer a componente sem repetir vértices e identificar todos os vértices que pertencem à mesma componente.