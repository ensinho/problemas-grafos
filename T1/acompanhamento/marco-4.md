# Marco 4 — Aplicação Básica de BFS e Conclusão

Mesma instância do marco 3, com a busca partindo de (2,5) para permitir a comparação:

```text
     1 2 3 4 5 6 7 8
 1   # # # # # # # #
 2   # . . # . . . #
 3   # # # # . # . #
 4   # . . # . . . #
 5   # # # # # # # #
```

## 1. Execução manual

O BFS (Breadth-First Search) explora os vértices por níveis, utilizando uma fila (FIFO).

Para cada vértice, os vizinhos são verificados na ordem:

1. Cima
2. Baixo
3. Esquerda
4. Direita

Ao descobrir um novo vértice, ele é marcado e inserido na fila.

| Passo | Desenfileira | Nível | Enfileira    | Fila depois do passo |
| ----- | ------------ | ----- | ------------ | -------------------- |
| 0     | —            | —     | (2,5)        | (2,5)                |
| 1     | (2,5)        | 0     | (3,5), (2,6) | (3,5) (2,6)          |
| 2     | (3,5)        | 1     | (4,5)        | (2,6) (4,5)          |
| 3     | (2,6)        | 1     | (2,7)        | (4,5) (2,7)          |
| 4     | (4,5)        | 2     | (4,6)        | (2,7) (4,6)          |
| 5     | (2,7)        | 2     | (3,7)        | (4,6) (3,7)          |
| 6     | (4,6)        | 3     | (4,7)        | (3,7) (4,7)          |
| 7     | (3,7)        | 3     | —            | (4,7)                |
| 8     | (4,7)        | 4     | —            | (vazia)              |

No passo 7, (3,7) olha para baixo e encontra (4,7), que já estava marcado desde o passo 6. Por isso ele não entra na fila de novo.

## 2. Variáveis utilizadas

- `marked[v]` — indica se o vértice já foi descoberto;
- `edgeTo[v]` — armazena o predecessor de `v`;
- `distTo[v]` — armazena a distância de `v` até a origem;
- `queue` — fila utilizada pelo BFS;
- `rooms` — contador de regiões.

A distância é calculada por:

`distTo[v] = distTo[u] + 1`

Na adaptação, `marked` vira a matriz `marcado[][]` e a fila é a `algs4.Queue`. O par `(linha, coluna)` é guardado como um único inteiro `linha * colunas + coluna`, recuperado com `atual / colunas` e `atual % colunas`.

Como a saída pedida é apenas a contagem de regiões, `edgeTo[]` e `distTo[]` são usados na execução manual, mas não foram mantidos no código submetido.

## 3. Níveis e distâncias

O vértice inicial possui distância `0`. Seus vizinhos possuem distância `1`, os vizinhos desses possuem distância `2` e assim sucessivamente.

| Nível | Vértices     |
| ----- | ------------ |
| 0     | (2,5)        |
| 1     | (3,5), (2,6) |
| 2     | (4,5), (2,7) |
| 3     | (4,6), (3,7) |
| 4     | (4,7)        |

Como o grafo não possui pesos, o BFS encontra a menor quantidade de arestas entre a origem e cada vértice alcançado.

O vértice mais distante é (4,7), a 4 arestas de (2,5). Existem dois caminhos mínimos, ambos com 4 arestas:

```text
(2,5) → (3,5) → (4,5) → (4,6) → (4,7)
(2,5) → (2,6) → (2,7) → (3,7) → (4,7)
```

O BFS chegou pelo primeiro porque (4,6) foi desenfileirado antes de (3,7).

## 4. Predecessores

Quando um vértice é descoberto pela primeira vez:

`edgeTo[v] = u`

onde `u` é o vértice que o descobriu. Os predecessores formam a árvore de busca do BFS.

| Vértice | edgeTo   | distTo |
| ------- | -------- | ------ |
| (2,5)   | — (raiz) | 0      |
| (3,5)   | (2,5)    | 1      |
| (2,6)   | (2,5)    | 1      |
| (4,5)   | (3,5)    | 2      |
| (2,7)   | (2,6)    | 2      |
| (4,6)   | (4,5)    | 3      |
| (3,7)   | (2,7)    | 3      |
| (4,7)   | (4,6)    | 4      |

Diferente do DFS, cuja árvore nesta região era um caminho de 8 vértices sem ramificação, a árvore do BFS ramifica logo na raiz e tem altura 4.

## 5. DFS x BFS

| DFS                                         | BFS                                                |
| ------------------------------------------- | -------------------------------------------------- |
| Explora em profundidade                     | Explora por níveis                                  |
| Utiliza pilha/recursão                      | Utiliza fila                                        |
| Não calcula menor distância naturalmente    | Calcula menor distância em grafos não ponderados    |
| Adequado para contar regiões                | Também adequado para contar regiões                 |
| Pilha guarda o caminho inteiro percorrido   | Fila guarda apenas a fronteira da busca             |

Para o problema Counting Rooms, ambos alcançam a mesma resposta e ambos são `O(H × W)`. O BFS fornece níveis e distâncias que não são necessários para a saída.

A diferença que pesa é a última linha da tabela. Medindo o pico de cada estrutura em um mapa 1000×1000 sem paredes:

| Busca         | Estrutura         | Pico medido | Resultado            |
| ------------- | ----------------- | ----------- | -------------------- |
| DFS recursiva | pilha de chamadas | 1.000.000   | `StackOverflowError` |
| BFS           | fila explícita    | 1.000       | resposta em 0,27 s   |

A fronteira do BFS numa grade cresce com `O(min(H, W))`; a pilha do DFS cresce com o tamanho da região. Por isso o BFS foi o escolhido para a submissão.

## 6. Adaptação ao problema

A matriz é percorrida. Ao encontrar um `.` não visitado:

1. `rooms++`;
2. inicia-se um BFS;
3. todos os pisos alcançáveis são marcados;
4. o algoritmo continua percorrendo a matriz.

Ao final, `rooms` contém a quantidade de regiões.

```java
for (int linha = 0; linha < linhas; linha++)
    for (int coluna = 0; coluna < colunas; coluna++)
        if (mapa[linha][coluna] == '.'
                && !marcado[linha][coluna]) {
            bfs(linha, coluna);
            salas++;
        }
```

O laço externo vem de `algs4.CC` e o método `bfs` de `algs4.BreadthFirstPaths`. Código completo em [`../src/Main.java`](../src/Main.java).

## 7. Complexidade

Cada posição da matriz é processada no máximo uma vez e são verificadas no máximo quatro direções.

- Tempo: `O(H × W)`
- Espaço: `O(H × W)`

O espaço é dominado pela matriz do mapa e pela matriz de marcação; a fila em si usa `O(min(H, W))`.

## 8. Testes e validação

| Caso                         | Esperado | Obtido | Situação |
| ---------------------------- | -------- | ------ | -------- |
| Exemplo oficial do enunciado | 3        | 3      | passou   |
| Mapa só de paredes           | 0        | 0      | passou   |
| Mapa 1×1 com um piso         | 1        | 1      | passou   |
| Mapa 3×3 totalmente aberto   | 1        | 1      | passou   |
| Pisos apenas em diagonal     | 4        | 4      | passou   |
| Mapa de uma única linha      | 3        | 3      | passou   |
| 1000×1000 aberto (estresse)  | 1        | 1      | 0,27 s   |
| 1000×1000 xadrez (estresse)  | 500000   | 500000 | 0,26 s   |

O caso das diagonais foi incluído de propósito: se a adjacência aceitasse movimento diagonal, ele devolveria `1` em vez de `4`. Os dois casos de estresse cobrem os extremos — uma região com 1.000.000 de vértices e 500.000 regiões de um vértice.

Descrição de cada caso em [`../dados/casos-de-teste.txt`](../dados/casos-de-teste.txt).

O arquivo submetido ao juiz online é [`../src/Main.java`](../src/Main.java).

A submissão recebeu `ACCEPTED` nos 19 casos de teste do CSES, com pior tempo de 0,34 s — bem abaixo do limite de 1 s do problema. A maioria dos testes rodou em 0,05 s; os mais pesados, entre 0,26 s e 0,34 s, são os mapas grandes.

Print do veredito em [`../evidencias/accepted.png`](../evidencias/accepted.png).

## 9. Conclusão

O BFS e o DFS são capazes de explorar todas as regiões do mapa utilizando a representação implícita do grafo, e os dois foram executados manualmente sobre a mesma instância: o DFS no marco 3, com tempos de descoberta e término, e o BFS neste marco, com níveis e distâncias.

Para a solução final foi escolhido o BFS. Não por ser mais rápido — as duas buscas são `O(H × W)` e devolvem a mesma resposta —, mas porque a fila fica no heap enquanto a recursão do DFS depende da pilha da JVM, que não suporta a profundidade de 1.000.000 permitida pelas restrições do problema.

Havia uma segunda saída: manter o DFS trocando a recursão por uma `Stack` explícita, no estilo de `algs4.NonrecursiveDFS`. Ela também resolveria, mas exigiria reescrever o percurso; o BFS chega ao mesmo resultado sendo uma adaptação direta do `BreadthFirstPaths`. Ficamos com o caminho mais simples.

O DFS não foi descartado por estar errado: ele produziu a resposta correta em todos os casos pequenos e foi o que tornou visível, no marco 3, o comportamento que motivou a troca.
