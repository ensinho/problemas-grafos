# Marco 2 — Representação Computacional

## 1. Representação escolhida

Foi escolhida a representação implícita desse grafo utilizando a própria matriz do mapa.

Cada posição contendo `.` representa um vértice. As posições contendo `#` representam as paredes e não pertencem ao grafo.

As arestas não são armazenadas explicitamente. Para cada vértice, seus possíveis vizinhos são determinados pelas quatro posições adjacentes: cima, baixo, esquerda e direita.

A representação implícita foi escolhida porque o mapa já possui uma estrutura de grade e cada vértice possui no máximo quatro possíveis vizinhos.

## 2. Leitura da entrada

A primeira linha da entrada fornece:

- `H`: número de linhas;
- `W`: número de colunas.

As `H` linhas seguintes são armazenadas em uma matriz de caracteres.

Exemplo:

    ########
    #..#...#
    ####.#.#
    #..#...#
    ########

Cada posição da matriz é interpretada desta forma:

- `.` → vértice;
- `#` → parede.

## 3. Construção do grafo

Para cada posição contendo `.`, são verificadas as quatro posições adjacentes.

Uma aresta só existe quando:

1. a posição vizinha está dentro dos limites da matriz; e
2. a posição vizinha também contém `.`.

Assim, não é necessário armazenar uma lista de adjacência. As conexões são obtidas diretamente a partir da matriz.

Representação das possíveis adjacências:

         cima
           ↑
esquerda ← . → direita
           ↓
         baixo

## 4. Medidas estruturais

Considerando a instância:

    5 8
    ########
    #..#...#
    ####.#.#
    #..#...#
    ########

Temos:

- Número de vértices: `|V| = 11`;
- Número de arestas: `|E| = 8`;
- Grau máximo da instância: `3`;
- Grau máximo possível no problema: `4`;
- Grau médio: `2|E| / |V| ≈ 1,45`.

## 5. Validação da representação

A representação é validada utilizando a instância pequena do marco 1.

Por exemplo, o vértice `(2,6)` possui como vizinhos os pisos em `(2,5)`, `(2,7)` e `(3,6)`.

A posição `(2,4)` contém `#`, portanto não representa um vértice e não gera uma aresta.

Dessa forma, a matriz consegue representar os vértices e determinar corretamente suas adjacências.

## 6. Conclusão

A representação escolhida permite representar o grafo diretamente a partir da estrutura do mapa, evitando o armazenamento explícito das arestas.

Essa representação será utilizada nos próximos marcos para aplicar as buscas sobre o grafo.