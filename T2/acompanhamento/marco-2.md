# Marco 2 — Componentes Conexas

## 1. Instância utilizada

Para este marco foi utilizado um grafo simples não direcionado com 6 vértices e 5 arestas, dividido em duas componentes conexas.

### Grafo

```text
      1 ------- 2
      |         |
      |         |
      4 ------- 3


      5 ------- 6
```

### Arestas

- 1 - 2
- 2 - 3
- 3 - 4
- 4 - 1
- 5 - 6

### Lista de adjacência

```text
1: 2, 4
2: 1, 3
3: 2, 4
4: 1, 3
5: 6
6: 5
```

O grafo possui duas componentes conexas:

```text
C1 = {1, 2, 3, 4}
C2 = {5, 6}
```

Para identificar a qual componente cada vértice pertence, é utilizado um vetor de IDs de componentes:

```text
id(1..6) = [0, 0, 0, 0, 1, 1]
```

Assim, os vértices 1, 2, 3 e 4 pertencem à componente `0`, enquanto os vértices 5 e 6 pertencem à componente `1`.

## 2. Excentricidade, raio e diâmetro

A excentricidade de um vértice é a maior distância entre ele e qualquer outro vértice da mesma componente.

### Componente 1

Para a componente `{1, 2, 3, 4}`:

```text
ecc(1) = 2
ecc(2) = 2
ecc(3) = 2
ecc(4) = 2
```

Portanto:

```text
Raio = min{2, 2, 2, 2} = 2
Diâmetro = max{2, 2, 2, 2} = 2
Centro = {1, 2, 3, 4}
```

### Componente 2

Para a componente `{5, 6}`:

```text
ecc(5) = 1
ecc(6) = 1
```

Portanto:

```text
Raio = min{1, 1} = 1
Diâmetro = max{1, 1} = 1
Centro = {5, 6}
```

## 3. Execução do DFS

Para identificar as componentes conexas, é utilizada uma busca em profundidade (DFS).

A busca começa pelo vértice `1`:

```text
1 → 2 → 3 → 4
```

Todos esses vértices recebem o mesmo ID de componente:

```text
id[1] = 0
id[2] = 0
id[3] = 0
id[4] = 0
```

Depois, o algoritmo encontra o vértice `5`, que ainda não foi visitado, e inicia uma nova busca:

```text
5 → 6
```

Esses vértices recebem um novo ID:

```text
id[5] = 1
id[6] = 1
```

Ao final:

```text
id(1..6) = [0, 0, 0, 0, 1, 1]
```

Portanto, foram identificadas duas componentes conexas.

## 4. Estado utilizado pelo DFS

O DFS utiliza duas informações principais:

- `marcado[v]` → indica se o vértice já foi visitado.
- `id[v]` → indica a qual componente conexa o vértice pertence.

Durante a primeira busca:

```text
1, 2, 3, 4 → id = 0
```

Durante a segunda busca:

```text
5, 6 → id = 1
```

O vetor de IDs permite identificar se dois vértices pertencem à mesma componente.

Por exemplo:

```text
id[1] == id[4] → mesma componente
id[1] != id[5] → componentes diferentes
```

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

A lista de adjacência ocupa `O(V + E)` e os vetores `marcado` e `id` ocupam `O(V)`.

## 6. Conclusão

Neste caso, o DFS foi utilizado para identificar as componentes conexas e atribuir um ID para cada uma.

Os resultados obtidos foram:

```text
Componentes conexas = 2

Componente 0:
Raio = 2
Diâmetro = 2
Centro = {1, 2, 3, 4}

Componente 1:
Raio = 1
Diâmetro = 1
Centro = {5, 6}
```

O vetor final de componentes é:

```text
id(1..6) = [0, 0, 0, 0, 1, 1]
```

A execução mostra como o DFS identifica cada componente e como o vetor de IDs permite saber a qual componente cada vértice pertence.