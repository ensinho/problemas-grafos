# Marco 1 — Modelagem

## 1. Enunciado

Dado um mapa representado por uma grade contendo paredes (`#`) e pisos (`.`), determinar a quantidade de regiões de pisos existentes. Um piso pertence à mesma região de outro quando é possível chegar até ele passando somente por pisos adjacentes horizontal ou verticalmente.

## 2. Entrada

A primeira linha contém dois inteiros:

* `H` — quantidade de linhas;
* `W` — quantidade de colunas.

Em seguida, são fornecidas `H` linhas contendo `#` e `.`.

## 3. Saída

Um único inteiro indicando a quantidade de regiões de pisos existentes.

## 4. Restrições

* `1 ≤ H, W ≤ 1000`;
* Cada posição do mapa contém `#` ou `.`.

## 5. Modelagem como Grafo

O mapa pode ser representado como um grafo:

* **Vértices:** cada posição do mapa que contém `.`;
* **Arestas:** conectam dois vértices quando os respectivos pisos são adjacentes horizontal ou verticalmente;
* **Paredes (`#`):** não fazem parte do grafo;
* **Tipo:** grafo não direcionado e não ponderado.

O grafo pode ser desconexo, pois podem existir regiões onde os pisos não têm conexão entre si. O grafo é não direcionado porque a movimentação entre dois pisos adjacentes pode ocorrer nos dois sentidos.

## 6. Instância Pequena

Entrada:

```text
5 8
########
#..#...#
####.#.#
#..#...#
########
```

O mapa possui três regiões distintas de pisos:

```text
########
#AA#BBB#
####B#B#
#CC#BBB#
########
```

Saída esperada:

```text
3
```

## 7. Hipótese Inicial de Solução

Percorrer todas as posições do mapa. Ao encontrar um piso (`.`) que ainda não foi visitado, iniciar uma busca para visitar todos os pisos conectados a ele e incrementar o contador de regiões.

Cada nova busca iniciada representa uma nova região encontrada. A técnica de busca será definida e analisada nos próximos marcos.
