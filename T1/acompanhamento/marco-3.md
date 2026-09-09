# Marco 3 — Aplicação Básica de DFS

Instância usada na execução manual (a mesma do marco 1):

```text
     1 2 3 4 5 6 7 8
 1   # # # # # # # #
 2   # . . # . . . #
 3   # # # # . # . #
 4   # . . # . . . #
 5   # # # # # # # #
```

12 vértices, 10 arestas, 3 regiões.

## 1. Execução manual

O DFS (Depth-First Search) explora um caminho até onde for possível antes de retornar para explorar outras possibilidades.

Para cada vértice, utilizamos a seguinte ordem de vizinhos:

1. Cima
2. Baixo
3. Esquerda
4. Direita

Ao encontrar um vértice `.` ainda não visitado, ele é marcado e seus vizinhos são explorados recursivamente. A varredura da matriz é feita linha por linha, da esquerda para a direita.

O método segue o padrão de `algs4.DepthFirstSearch`, trocando `graph.adj(v)` pelas quatro direções, já que o grafo é implícito.

## 2. Estados de visita

- `○` — não visitado
- `◐` — descoberto/em exploração
- `●` — exploração finalizada

O vetor `marked` indica se um vértice já foi visitado. Na adaptação para a grade ele vira a matriz `marcado[][]`, indexada por `(linha, coluna)`.

Evolução dos estados na busca iniciada em (2,5):

| Momento              | (2,5) | (3,5) | (4,5) | (4,6) | (4,7) | (3,7) | (2,7) | (2,6) |
| -------------------- | ----- | ----- | ----- | ----- | ----- | ----- | ----- | ----- |
| início               | ○     | ○     | ○     | ○     | ○     | ○     | ○     | ○     |
| descida até o fundo  | ◐     | ◐     | ◐     | ◐     | ◐     | ◐     | ◐     | ◐     |
| fim da busca         | ●     | ●     | ●     | ●     | ●     | ●     | ●     | ●     |

Os 8 vértices ficam simultaneamente em `◐` antes de qualquer um passar para `●`. Esse detalhe é o que explica o problema descrito na seção 7.

## 3. Árvore de busca

O `edgeTo[v]` armazena o vértice que descobriu `v`, formando a árvore de busca do DFS. Os vértices que iniciam uma nova busca não possuem predecessor.

| Vértice | edgeTo    | Vértice | edgeTo    |
| ------- | --------- | ------- | --------- |
| (2,2)   | — (raiz)  | (4,7)   | (4,6)     |
| (2,3)   | (2,2)     | (3,7)   | (4,7)     |
| (2,5)   | — (raiz)  | (2,7)   | (3,7)     |
| (3,5)   | (2,5)     | (2,6)   | (2,7)     |
| (4,5)   | (3,5)     | (4,2)   | — (raiz)  |
| (4,6)   | (4,5)     | (4,3)   | (4,2)     |

Três raízes, três regiões. A árvore da região central é um caminho de 8 vértices, sem ramificação.

Vale notar que (2,6) é vizinho direto de (2,5), mas seu predecessor é (2,7): como "direita" é a última direção testada, o DFS já havia descido por "baixo" antes de chegar nele.

## 4. Tempos de descoberta e término

São utilizados dois tempos:

- `d[v]` — momento em que o vértice é descoberto;
- `f[v]` — momento em que sua exploração termina.

Os tempos são incrementados durante a execução do DFS.

| Vértice | d  | f  | Vértice | d  | f  |
| ------- | -- | -- | ------- | -- | -- |
| (2,2)   | 1  | 4  | (3,7)   | 10 | 15 |
| (2,3)   | 2  | 3  | (2,7)   | 11 | 14 |
| (2,5)   | 5  | 20 | (2,6)   | 12 | 13 |
| (3,5)   | 6  | 19 | (4,2)   | 21 | 24 |
| (4,5)   | 7  | 18 | (4,3)   | 22 | 23 |
| (4,6)   | 8  | 17 |         |    |    |
| (4,7)   | 9  | 16 |         |    |    |

O contador final é 24, ou seja `2 × 12`: cada vértice recebeu um tempo de descoberta e um de término, nenhum ficou de fora e nenhum foi visitado duas vezes.

Os intervalos `[d, f]` são aninhados: `(2,5)` abre em 5 e só fecha em 20, depois que todos os descendentes fecharam.

## 5. Alcançabilidade

A partir de um vértice `.`, o DFS alcança todos os pisos que podem ser percorridos através de posições adjacentes horizontal ou verticalmente. Quando o percurso termina, todos os vértices alcançáveis a partir daquele início foram visitados.

Partindo de (2,5), foram alcançados 8 vértices:

```text
(2,5) (2,6) (2,7) (3,5) (3,7) (4,5) (4,6) (4,7)
```

Os outros 4 — (2,2), (2,3), (4,2) e (4,3) — não foram alcançados, porque estão separados por paredes.

É o mesmo comportamento de `algs4.DepthFirstSearch`: `marked(v)` responde se existe caminho da origem até `v`, e `count()` devolve quantos vértices foram alcançados. Como `count()` (8) é menor que o total (12), o grafo é desconexo.

## 6. Aplicação ao problema

Percorremos toda a matriz. Ao encontrar um `.` não visitado, iniciamos um DFS e incrementamos o contador de regiões. Assim, cada nova busca iniciada representa uma nova região de pisos.

Esse laço externo é o de `algs4.CC`:

```java
for (int linha = 0; linha < linhas; linha++)
    for (int coluna = 0; coluna < colunas; coluna++)
        if (mapa[linha][coluna] == '.'
                && !marcado[linha][coluna]) {
            dfs(linha, coluna);
            salas++;
        }
```

Na instância, foram iniciadas 3 buscas — em (2,2), (2,5) e (4,2). Saída obtida: `3`, igual à esperada.

O método recursivo usado nesta execução:

```java
private static void dfs(int linha, int coluna) {
    marcado[linha][coluna] = true;

    for (int direcao = 0; direcao < 4; direcao++) {
        int vizLinha  = linha  + dLinha[direcao];
        int vizColuna = coluna + dColuna[direcao];

        if (vizLinha < 0 || vizLinha >= linhas ||
            vizColuna < 0 || vizColuna >= colunas) continue;

        if (mapa[vizLinha][vizColuna] != '.' ||
            marcado[vizLinha][vizColuna]) continue;

        dfs(vizLinha, vizColuna);
    }
}
```

A solução final, em [`../src/Main.java`](../src/Main.java), mantém esse mesmo laço externo e troca apenas o `dfs` pelo `bfs` do marco 4.

## 7. Aplicabilidade

O DFS é adequado ao problema porque permite explorar completamente uma região utilizando a representação implícita da matriz, sem necessidade de armazenar explicitamente as arestas. A complexidade é `O(H × W)`.

Há, porém, um limite prático na versão recursiva. Como visto na seção 2, o DFS mantém a região inteira em exploração antes de fechar qualquer vértice — ou seja, a profundidade da recursão chega ao tamanho da região. Testes com mapas abertos:

| Mapa      | Células   | Resultado          |
| --------- | --------- | ------------------ |
| 100×100   | 10.000    | funcionou (`1`)    |
| 200×200   | 40.000    | StackOverflowError |
| 1000×1000 | 1.000.000 | StackOverflowError |

O algoritmo está correto; o limite é da pilha de execução da JVM, e `1 ≤ H, W ≤ 1000` está dentro das restrições do problema.

Quem estoura é a recursão, não o DFS: trocar a pilha de chamadas por uma pilha explícita resolveria. O marco 4 compara as duas opções e define qual foi usada na submissão.
