# T2 — Flight Routes Check

Trabalho Prático 2 da disciplina **Resolução de Problemas com Grafos**.

O objetivo é modelar o problema **CSES 1682 — Flight Routes Check** como um grafo dirigido e verificar a sua **conectividade forte**, usando o algoritmo de Kosaraju-Sharir.

> Problema: [CSES 1682 — Flight Routes Check](https://cses.fi/problemset/task/1682)

## Integrantes

* Enzo Esmeraldo
* Gustavo Andrade

## Linguagem

Java.

## Problema

Existem `n` cidades e `m` voos, e cada voo vai de uma cidade `a` para uma cidade `b`, só nesse sentido. A pergunta é se dá para sair de **qualquer** cidade e chegar a **qualquer** outra usando os voos.

* Se der, imprimir `YES`.
* Se não der, imprimir `NO` e duas cidades `a` e `b` tais que não exista rota de `a` para `b`. Qualquer par válido é aceito.

Restrições: `1 ≤ n ≤ 100.000` e `1 ≤ m ≤ 200.000`.

Exemplo do enunciado:

```text
Entrada     Saída
4 5         NO
1 2         4 2
2 3
3 1
1 4
3 4
```

## Modelagem

* **Vértices:** as cidades;
* **Arestas:** os voos, com direção `a → b`;
* **Tipo:** dirigido, não ponderado, podendo ter ciclos.

Detalhamento em [`acompanhamento/marco-1.md`](acompanhamento/marco-1.md).

## Propriedade estrutural

O que o problema pede é a **conectividade forte**: para todo par `v`, `w` existe caminho de `v` para `w` **e** de `w` para `v`.

Os vértices se agrupam em **componentes f-conexas** (SCCs). O critério usado é:

```text
fortemente conexo  ⇔  existe exatamente 1 componente f-conexa
```

## Algoritmo

**Kosaraju-Sharir**, adaptado de `algs4.KosarajuSharirSCC`:

1. **Fase 1:** DFS no grafo reverso `Gᵀ` para obter a pós-ordem reversa (`DepthFirstOrder.reversePost()`);
2. **Fase 2:** DFS em `G`, iniciando nos vértices não marcados na ordem da fase 1. Cada DFS iniciada marca uma componente e recebe um `id`.

Resposta:

* `count() == 1` → `YES`;
* `count() > 1` → `NO`. A fase 2 começa sempre por uma componente **sumidouro** de `G`, então a componente `id = 0` não tem voo saindo dela. Basta tomar `a` com `id[a] = 0` e `b` com `id[b] ≠ 0`.

Estratégia e rastreamento manual em [`acompanhamento/marco-3.md`](acompanhamento/marco-3.md).

## Implementação de referência

Todas as classes vêm de [`carubbi/RPG` — `algs4-java`](https://github.com/carubbi/RPG/tree/main/algs4-java). O algoritmo segue o material [E1 — Componentes f-conexas](https://github.com/carubbi/RPG/blob/main/mat-didatico/aulas/extras/E1_SC.pdf), seção 3.

| Classe              | Papel                                               | Situação    |
| ------------------- | --------------------------------------------------- | ----------- |
| `KosarajuSharirSCC` | fase 2, `marked[]`, `id[]`, `count`                 | reutilizada |
| `DepthFirstOrder`   | fase 1, pós-ordem reversa                           | modificada  |
| `Digraph`           | listas de adjacência e `reverse()`                  | reduzida    |
| `Bag`               | lista de adjacência de cada vértice                 | reduzida    |
| `Stack`             | guarda a pós-ordem reversa                          | reduzida    |

## Alterações realizadas e justificativas

1. **Classes aninhadas em `Main.java`.** O CSES aceita apenas um arquivo por submissão;
2. **`check()` removido do `KosarajuSharirSCC`.** Ele usa `TransitiveClosure`, uma matriz `V × V`, inviável com `V = 100.000`;
3. **`DepthFirstOrder` só com a pós-ordem.** O vértice é empilhado direto em `reversePost` ao terminar, em vez de passar pela fila `postorder` e depois ser copiado;
4. **`Digraph`, `Bag` e `Stack` reduzidos** aos métodos usados. `indegree[]`, `E` e as validações foram retirados;
5. **Cidades `1..n` → vértices `0..n-1`** na leitura, e `+1` na impressão;
6. **`StdIn` trocado por `BufferedReader` + `StringTokenizer`**, para ler 200.000 linhas dentro do tempo;
7. **Execução numa `Thread` com pilha de 256 MB.** Com a pilha padrão, a DFS recursiva estoura (`StackOverflowError`) a partir de ~10.000 vértices em ciclo. Diferente do T1, aqui não dá para trocar por BFS, porque o Kosaraju depende da **pós-ordem**, que só a DFS produz. Mantivemos então a DFS da referência intacta e aumentamos a pilha.

Detalhamento em [`acompanhamento/marco-4.md`](acompanhamento/marco-4.md).

## Complexidade

* **Tempo:** `O(V + E)`. São duas DFS e a construção de `Gᵀ`;
* **Memória:** `O(V + E)` para `G` e `Gᵀ`, mais `O(V)` auxiliar (`marked[]`, `id[]`, `reversePost` e a pilha de recursão).

## Casos especiais

* **Uma única cidade** (`n = 1`): uma componente → `YES`;
* **Laços** (`a = b`): não mudam as componentes;
* **Cidade sem nenhum voo:** vira uma componente sozinha → `NO`;
* **Dois grupos ligados em um só sentido:** são componentes diferentes → `NO`. Tratar o grafo como não direcionado daria `YES`, o que seria errado;
* **Par da resposta diferente do enunciado:** no exemplo oficial, a solução imprime `4 1` em vez de `4 2`. Os dois são válidos, porque a cidade 4 não tem voo de saída;
* **Grafos profundos** (ciclo com 100.000 cidades): exigem a pilha aumentada (item 7 das alterações).

## Estrutura do projeto

```text
T2/
├── README.md
├── acompanhamento/
│   ├── marco-1.md          problema e modelagem
│   ├── marco-2.md          componentes conexas (caso particular)
│   ├── marco-3.md          estratégia: Kosaraju-Sharir
│   └── marco-4.md          implementação final e conclusão
├── src/
│   └── Main.java           solução final
├── evidencias/
│   └── accepted.png        comprovante do CSES
├── apresentacao/
│   └── apresentacao.pdf
└── dados/
    ├── casos-de-teste.txt  descrição dos casos e resultados
    └── caso-1.txt … caso-8.txt
```

## Como executar

Abra o terminal na pasta `T2`.

Compilar:

```bash
javac -d classes src/Main.java
```

Rodar com um caso de teste:

```bash
java -cp classes Main < dados/caso-1.txt
```

Saída esperada:

```text
NO
4 1
```

Rodar todos os casos:

```bash
for i in 1 2 3 4 5 6 7 8; do echo "caso-$i: $(java -cp classes Main < dados/caso-$i.txt | tr '\n' ' ')"; done
```

No PowerShell:

```powershell
1..8 | ForEach-Object { "caso-$($_): " + ((Get-Content "dados\caso-$_.txt" | java -cp classes Main) -join ' ') }
```

## Testes efetuados

| Caso                                  | Esperado | Obtido            | Situação |
| ------------------------------------- | -------- | ----------------- | -------- |
| Exemplo oficial do enunciado          | NO       | `NO` / `4 1`      | passou   |
| Ciclo único                           | YES      | `YES`             | passou   |
| Uma cidade com laço                   | YES      | `YES`             | passou   |
| Caminho sem volta                     | NO       | `NO` / `3 1`      | passou   |
| Dois ciclos sem ligação               | NO       | `NO` / `3 1`      | passou   |
| Dois ciclos ligados num só sentido    | NO       | `NO` / `3 1`      | passou   |
| Cidade isolada                        | NO       | `NO` / `3 1`      | passou   |
| Dois ciclos com vértice em comum      | YES      | `YES`             | passou   |
| Ciclo de 100.000 (estresse)           | YES      | `YES`             | 0,22 s   |
| 100.000 vértices / 200.000 arestas    | NO       | `NO` / `99995 1`  | 0,27 s   |

Os pares das respostas `NO` foram conferidos por um verificador, que faz uma BFS a partir de `a` e confirma que `b` não é alcançado. A lista completa, com os outros casos de estresse, está em [`acompanhamento/marco-4.md`](acompanhamento/marco-4.md).

## Comprovação de Accepted

A solução foi submetida ao CSES e recebeu **ACCEPTED** em todos os 20 casos de teste do juiz.

| Item           | Valor                          |
| -------------- | ------------------------------ |
| Problema       | Flight Routes Check (1682)     |
| Linguagem      | Java                           |
| Resultado      | `ACCEPTED`                     |
| Testes do juiz | 20 de 20                       |
| Pior tempo     | 0,97 s (teste #14), limite de 1,00 s |
| Data           | 07/10/2026                     |

Print do veredito: [`evidencias/accepted.png`](evidencias/accepted.png).

## Declaração de uso de IA

Foi utilizado o claude como apoio na organização da documentação dos marcos, na revisão do uso do Kosaraju-Sharir e na criação de casos de teste para aplicação.


Foi utilizada o claude como apoio na organização da documentação dos marcos, 
e também para a criação de exemplos de casos de teste para serem aplicados.

## Acompanhamento

| Marco                                          | Situação     |
| ---------------------------------------------- | ------------ |
| Marco 1 — Problema e conhecimento prévio       | Concluído    |
| Marco 2 — Componentes conexas                  | Concluído    |
| Marco 3 — Estratégia algorítmica               | Concluído    |
| Marco 4 — Implementação final e conclusão      | Concluído    |
