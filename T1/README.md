# T1 — Counting Rooms

Trabalho Prático 1 da disciplina **Resolução de Problemas com Grafos**.

O objetivo é modelar o problema **CSES 1192 — Counting Rooms** como um grafo e aplicar técnicas de busca (DFS e BFS) para resolvê-lo.

> Problema: [CSES 1192 — Counting Rooms](https://cses.fi/problemset/task/1192)

## Integrantes

* Enzo Esmeraldo
* Gustavo Andrade

## Linguagem

Java.

## Problema

O mapa é formado por paredes (`#`) e pisos (`.`). É preciso contar quantas regiões de pisos existem. Dois pisos pertencem à mesma região quando dá para ir de um ao outro andando apenas por pisos adjacentes na horizontal ou na vertical.

Restrições: `1 ≤ H, W ≤ 1000`, ou seja, até 1.000.000 de posições.

Exemplo do enunciado:

```text
Entrada          Regiões          Saída
5 8              ########
########         #AA#BBB#           3
#..#...#         ####B#B#
####.#.#         #CC#BBB#
#..#...#         ########
########
```

## Modelagem

O mapa é interpretado como um grafo:

* **Vértices:** cada posição contendo `.`;
* **Arestas:** ligam dois pisos adjacentes na horizontal ou na vertical;
* **Paredes (`#`):** não fazem parte do grafo;
* **Tipo:** não direcionado e não ponderado;
* **Característica:** pode ser desconexo.

Contar as salas equivale a **contar os componentes conexos** desse grafo.

Detalhamento em [`acompanhamento/marco-1.md`](acompanhamento/marco-1.md).

## Representação computacional

Foi usada a **representação implícita**: a própria matriz de caracteres é o grafo.

As arestas não são armazenadas. Os vizinhos de uma posição são calculados na hora, a partir dos quatro deslocamentos possíveis:

```java
private static final int[] dLinha  = { -1, 1, 0, 0 };
private static final int[] dColuna = { 0, 0, -1, 1 };
```

Uma aresta existe quando a posição vizinha está dentro dos limites da matriz **e** também contém `.`.

O motivo da escolha é o tamanho da entrada: montar listas de adjacência explícitas para 1.000.000 de vértices custaria memória sem necessidade, já que cada vértice tem no máximo 4 vizinhos e eles são obtidos por aritmética simples.

Detalhamento em [`acompanhamento/marco-2.md`](acompanhamento/marco-2.md).

## Algoritmo escolhido

**BFS (busca em largura)**, adaptada de `algs4.BreadthFirstPaths` com a estrutura `algs4.Queue`.

O laço externo percorre toda a matriz e inicia uma busca sempre que encontra um piso ainda não marcado. Cada busca iniciada corresponde a uma sala:

```java
int salas = 0;
for (int linha = 0; linha < linhas; linha++) {
    for (int coluna = 0; coluna < colunas; coluna++) {
        if (mapa[linha][coluna] == '.' && !marcado[linha][coluna]) {
            bfs(linha, coluna);
            salas++;
        }
    }
}
```

Esse laço é o mesmo de `algs4.CC`, que conta componentes conexos.

### Por que BFS e não DFS

As duas resolvem o problema e as duas são `O(H · W)`. A DFS recursiva foi implementada primeiro, no marco 3, e funciona — mas falha nas entradas grandes:

| Busca         | Estrutura de controle | Pico em 1000×1000 aberto | Resultado            |
| ------------- | --------------------- | ------------------------ | -------------------- |
| DFS recursiva | pilha de chamadas     | 1.000.000                | `StackOverflowError` |
| BFS           | fila explícita        | 1.000                    | resposta em 0,27 s   |

A fila da BFS guarda apenas a fronteira da busca, que numa grade cresce com `O(min(H, W))`. A pilha da DFS guarda todo o caminho já percorrido, que no pior caso passa por todos os vértices da região.

Quem estoura é a recursão, não o DFS. Dava para mantê-lo usando uma `Stack` explícita, como em `algs4.NonrecursiveDFS`, mas o BFS resolve o mesmo com uma adaptação mais direta da referência — e foi por isso que seguimos com ele.

A comparação completa está em [`acompanhamento/marco-4.md`](acompanhamento/marco-4.md).

## Complexidade

Sendo `H` linhas e `W` colunas:

* **Tempo:** `O(H · W)` — cada posição é lida uma vez, entra na fila no máximo uma vez e consulta 4 vizinhos;
* **Espaço:** `O(H · W)` — dominado pela matriz do mapa e pela matriz de marcação. A fila usa `O(min(H, W))`.

## Estrutura do projeto

```text
T1/
├── README.md
├── acompanhamento/
│   ├── marco-1.md          modelagem
│   ├── marco-2.md          representação computacional
│   ├── marco-3.md          aplicação de DFS
│   └── marco-4.md          aplicação de BFS e conclusão
├── src/
│   └── Main.java           solução final (BFS)
├── evidencias/
│   └── accepted.png        comprovante do CSES
├── apresentacao/
│   ├── apresentacao.pdf    slides para apresentar
│   ├── apresentacao.pptx   fonte, no template da UNIFOR
└── dados/
    ├── casos-de-teste.txt  descrição dos casos e resultados
    └── caso-1.txt … caso-6.txt
```

## Como executar

Compilar:

```sh
javac -d classes src/Main.java
```

Rodar com um caso de teste:

```sh
java -cp classes Main < dados/caso-1.txt
```

Saída esperada: `3`.

Rodar todos os casos pequenos de uma vez:

```sh
for i in 1 2 3 4 5 6; do
  echo "caso-$i: $(java -cp classes Main < dados/caso-$i.txt)"
done
```

Resultados esperados: `3, 0, 1, 1, 4, 3`.

## Testes efetuados

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

Os dois últimos são os piores casos do problema: uma região com 1.000.000 de vértices e 500.000 regiões de 1 vértice.

Descrição de cada caso em [`dados/casos-de-teste.txt`](dados/casos-de-teste.txt).

## Comprovação de Accepted

A solução foi submetida ao CSES e recebeu **ACCEPTED** em todos os 19 casos de teste do CSES.

| Item             | Valor                     |
| ---------------- | ------------------------- |
| Problema         | Counting Rooms (1192)     |
| Linguagem        | Java                      |
| Resultado        | `ACCEPTED`                |
| Testes do juiz   | 19 de 19                  |
| Pior tempo       | 0,34 s                    |
| Data             | 09/09/2026                |

Print do veredito: [`evidencias/accepted.png`](evidencias/accepted.png).

## Referências utilizadas

Todas as referências vêm do material da disciplina:

* [`carubbi/RPG` — `algs4-java`](https://github.com/carubbi/RPG/tree/main/algs4-java), cópia local do código de *Algorithms, 4th Edition*;
* [`kevin-wayne/algs4`](https://github.com/kevin-wayne/algs4), repositório original de Robert Sedgewick e Kevin Wayne.

Classes consultadas:

| Classe                    | O que foi aproveitado                                          |
| ------------------------- | -------------------------------------------------------------- |
| `algs4.CC`                | laço externo que percorre os vértices e conta componentes      |
| `algs4.DepthFirstSearch`  | padrão do vetor `marked[]` e o método recursivo `dfs`          |
| `algs4.BreadthFirstPaths` | estrutura do método `bfs` com fila                              |
| `algs4.Queue`             | fila encadeada usada pela BFS                                   |

## Alterações realizadas nas referências

O código do algs4 trabalha com a classe `Graph`, que tem lista de adjacência explícita. Como aqui o grafo é implícito, foram feitas as seguintes adaptações:

1. **`graph.adj(v)` → laço de 4 direções.** Não existe lista de adjacência; os vizinhos são calculados com `dLinha` e `dColuna`;
2. **`marked[]` → `marcado[][]`.** O vértice é um par `(linha, coluna)` em vez de um índice único;
3. **Checagem `!marked[w]` → três checagens.** Limites da matriz, depois parede, depois já visitado;
4. **`distTo[]` e `edgeTo[]` removidos.** O problema pede só a contagem de regiões, não distâncias nem caminhos;
5. **`Queue` reduzida e aninhada.** O CSES aceita apenas um arquivo por submissão, então a `Queue` virou classe aninhada em `Main.java`, mantendo somente `isEmpty()`, `enqueue()` e `dequeue()`. O par `(linha, coluna)` é codificado como `linha * colunas + coluna` para caber em um único `Integer` na fila;
6. **`StdIn` trocado por `BufferedReader` + `StringTokenizer`.** A `algs4.StdIn` usa `Scanner`, que é lento para ler 1000 linhas de 1000 caracteres dentro do limite de tempo do juiz. A troca é apenas de leitura de entrada e não afeta o algoritmo.

## Declaração de uso de IA

Foi utilizada o claude como apoio na organização da documentação dos marcos, 
e também para a criação de exemplos de casos de teste para serem aplicados.

## Acompanhamento

| Marco                                         | Situação  |
| --------------------------------------------- | --------- |
| Marco 1 — Modelagem                           | Concluído |
| Marco 2 — Representação computacional         | Concluído |
| Marco 3 — Aplicação básica de DFS             | Concluído |
| Marco 4 — Aplicação básica de BFS e conclusão | Concluído |