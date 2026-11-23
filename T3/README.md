# T3 — Shortest Routes I

Trabalho Prático 3 da disciplina **Resolução de Problemas com Grafos**.

O objetivo é modelar o problema **CSES 1671 — Shortest Routes I** como um grafo dirigido e ponderado e encontrar os **caminhos mínimos** a partir da cidade `1`.

> Problema: [CSES 1671 — Shortest Routes I](https://cses.fi/problemset/task/1671)

## Integrantes

* Enzo Esmeraldo
* Gustavo Andrade

## Problema

Existem `n` cidades e `m` voos. Cada voo vai de uma cidade `a` para uma cidade `b`, só nesse sentido, e tem comprimento `c`. A tarefa é imprimir a menor distância da cidade `1` até cada cidade.

Restrições: `1 ≤ n ≤ 100.000`, `1 ≤ m ≤ 200.000` e `1 ≤ c ≤ 10⁹`.

Exemplo do enunciado:

```text
Entrada:
3 4
1 2 6
1 3 2
3 2 3
1 3 4

Saída:
0 5 2
```

## Modelagem

* **Vértices:** as cidades;
* **Arestas:** os voos, com direção `a → b`;
* **Peso:** o comprimento `c` do voo;
* **Tipo:** dirigido, ponderado, com pesos positivos.

Detalhamento em [`acompanhamento/marcos.md`](acompanhamento/marcos.md).

## Representação

Lista de adjacência com pares `(destino, peso)`. Detalhamento e validação da instância pequena em [`acompanhamento/marcos.md`](acompanhamento/marcos.md).

## Algoritmo

**Dijkstra**, adaptado de `algs4.DijkstraSP`, com fila de prioridade indexada (`IndexMinPQ`).

* **Tempo:** `O(E log V)`;
* **Memória:** `O(V + E)`.

Justificativa no marco 2 e rastreamento manual no marco 3, em [`acompanhamento/marcos.md`](acompanhamento/marcos.md).

## Andamento

* [x] Marco 1 — Formulação
* [x] Marco 2 — Escolha do algoritmo
* [x] Marco 3 — Rastreamento manual
* [ ] Marco 4 — Adaptação e integração
* [ ] Marco 5 — Testes e análise
* [ ] Marco 6 — Submissão e apresentação
