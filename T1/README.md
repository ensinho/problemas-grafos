# T1 — Counting Rooms

Trabalho Prático 1 da disciplina **Resolução de Problemas com Grafos**.

O trabalho tem como objetivo modelar o problema **CSES 1192 — Counting Rooms** como um grafo e, posteriormente, aplicar técnicas de busca para poder resolvê-lo.

> Problema: [CSES 1192 — Counting Rooms](https://cses.fi/problemset/task/1192)

## Integrantes

* Enzo Esmeraldo
* Gustavo Andrade

## Problema

O problema apresenta um mapa formado por paredes (`#`) e pisos (`.`).

O objetivo é saber quantas regiões de pisos existem no mapa. Pisos adjacentes horizontalmente ou verticalmente podem fazer parte dessa mesma região.

## Modelagem

O mapa é interpretado como um grafo:

* **Vértices:** sendo cada posição contendo `.`;
* **Arestas:** conexão entre pisos adjacentes em ambos os eixos;
* **Paredes (`#`):** não fazem parte do grafo;
* **Tipo:** grafo não direcionado e não ponderado;
* **Característica:** o grafo pode ser desconexo.

A modelagem vai estar mais detalhada no [`acompanhamento/marco-1.md`](acompanhamento/marco-1.md).

## Hipótese inicial

A hipótese inicial é percorrer o mapa procurando pisos ainda não visitados. Ao encontrar esse piso que não foi visitado, uma busca deverá percorrer os pisos que estão conectados à ele. Cada nova busca iniciada representa uma nova região encontrada.

A escolha e aplicação da técnica de busca ainda não foi definida.

## Estrutura do projeto

```text
T1/
├── README.md
├── acompanhamento/
│   ├── marco-1.md
│   ├── marco-2.md
│   ├── marco-3.md
│   └── marco-4.md
├── src/
├── evidencias/
├── apresentacao/
└── dados/
```

## Acompanhamento

| Marco                                         | Situação           |
| --------------------------------------------- | ------------------ |
| Marco 1 — Modelagem                           | Em desenvolvimento |
| Marco 2 — Representação computacional         | Não iniciada       |
| Marco 3 — Aplicação básica de DFS             | Não iniciada       |
| Marco 4 — Aplicação básica de BFS e conclusão | Não iniciada       |