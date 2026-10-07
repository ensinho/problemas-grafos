# Marco 4 — Implementação Final e Conclusão

A solução final está em [`../src/Main.java`](../src/Main.java) e implementa a estratégia do marco 3.

## 1. Estrutura da solução

```text
resolver()
 ├── lê n, m e monta o Digraph G        (cidades 1..n → vértices 0..n-1)
 ├── new KosarajuSharirSCC(G)
 │    ├── fase 1: DepthFirstOrder(G.reverse())   → reversePost de Gᵀ
 │    └── fase 2: dfs em G na ordem reversePost  → id[], count
 ├── count == 1 → YES
 └── count  > 1 → NO + (a com id 0, b com id ≠ 0)
```

Trecho que transforma o resultado do `algs4` na resposta do problema:

```java
if (scc.count() == 1) {
    System.out.println("YES");
    return;
}

// a componente 0 e um sumidouro em G: quem esta nela nao sai dela
for (int v = 0; v < cidades; v++) {
    if (scc.id(v) == 0) { if (origem == -1) origem = v; }
    else if (destino == -1) destino = v;
}
```

Como no T1, o CSES aceita um único arquivo por submissão, então as classes do `algs4` ficaram aninhadas em `Main.java`.

## 2. Classes reutilizadas e modificadas

| Classe `algs4`      | Situação    | O que mudou                                                                  |
| ------------------- | ----------- | ---------------------------------------------------------------------------- |
| `KosarajuSharirSCC` | reutilizada | construtor, `dfs`, `count()` e `id()` idênticos; removidos `check()`, `stronglyConnected()`, `validateVertex()` e `main()` |
| `DepthFirstOrder`   | modificada  | mantida só a pós-ordem, empilhada direto em `reversePost`                    |
| `Digraph`           | reduzida    | mantidos `V()`, `addEdge()`, `adj()` e `reverse()`                           |
| `Bag`               | reduzida    | mantidos `add()` e o iterador                                                |
| `Stack`             | reduzida    | mantidos `push()` e o iterador                                               |

Justificativas:

1. **`check()` removido.** Ele usa `TransitiveClosure`, uma matriz `V × V`. Com `V = 100.000`, seriam 10¹⁰ posições. A validação foi feita por um verificador externo (seção 5);
2. **`DepthFirstOrder` só com a pós-ordem.** A referência guarda `pre[]`, `post[]`, `preorder` e `postorder`, e depois copia `postorder` para uma `Stack`. O vértice é empilhado no mesmo instante em que a referência faria `postorder.enqueue(v)`, então a ordem é a mesma, sem a fila intermediária;
3. **`Digraph` reduzido.** `E` e `indegree[]` não são usados. As validações foram removidas porque o enunciado garante `1 ≤ a, b ≤ n`;
4. **`1..n` → `0..n-1`.** Subtrai-se 1 na leitura e soma-se 1 na impressão;
5. **`StdIn` → `BufferedReader` + `StringTokenizer`.** Mesmo motivo do T1: o `Scanner` é lento para 200.000 linhas. A troca não afeta o algoritmo;
6. **`Node` e `LinkedIterator` compartilhados.** Em `Bag` e `Stack` eles são idênticos; no mesmo arquivo, basta uma cópia;
7. **Execução numa `Thread` com pilha de 256 MB.** Explicada na seção 3.

## 3. Profundidade da recursão

No T1, a DFS recursiva estourou a pilha e foi trocada pela BFS. Aqui isso não é possível: o Kosaraju-Sharir depende da **pós-ordem**, o momento em que cada vértice termina, e a BFS não tem esse conceito.

Mesma solução, com e sem a `Thread`, num ciclo `1 → 2 → … → n → 1`:

| n       | Pilha padrão da JVM  | `Thread` com 256 MB |
| ------- | -------------------- | ------------------- |
| 5.000   | `YES`                | `YES`               |
| 10.000  | `StackOverflowError` | `YES`               |
| 100.000 | `StackOverflowError` | `YES`               |

O ciclo é o pior caso, porque a DFS desce pelos `n` vértices antes de terminar qualquer um. Já o caminho `1 → 2 → … → 100.000` passa mesmo sem a `Thread`, pois nas duas fases cada DFS começa por um vértice sem saída. Quem define a profundidade é o formato do grafo, não só o tamanho.

As alternativas eram duas:

* reescrever as duas DFS de forma iterativa, no estilo de `algs4.NonrecursiveDirectedDFS`, guardando o iterador de cada vértice na pilha para saber quando ele termina;
* manter a DFS recursiva e rodá-la numa `Thread` com pilha maior.

Ficamos com a segunda, porque ela mantém o `dfs` da referência intacto (o mesmo rastreado no marco 3) e a mudança fica só no `main`.

## 4. Complexidade

* **Tempo:** `O(V + E)`, com duas DFS, a construção de `Gᵀ` e uma varredura `O(V)` para o par;
* **Memória:** `O(V + E)` para `G` e `Gᵀ`, mais `O(V)` auxiliar (`marked[]`, `id[]`, `reversePost` e a pilha de recursão).

No maior teste local (100.000 vértices, 200.000 arestas), o pico foi de ~90 MB, contra um limite de 512 MB.

## 5. Testes e validação

Como a resposta `NO` aceita qualquer par válido, cada saída foi conferida por um verificador à parte:

* **`YES`:** BFS a partir de `1` em `G` e em `Gᵀ` precisa alcançar todos os vértices;
* **`NO a b`:** BFS a partir de `a` não pode alcançar `b`.

| Caso                                      | Esperado | Obtido            | Situação |
| ----------------------------------------- | -------- | ----------------- | -------- |
| 1. Exemplo oficial do enunciado           | NO       | `NO` / `4 1`      | passou   |
| 2. Ciclo único                            | YES      | `YES`             | passou   |
| 3. Uma cidade com laço (`n = 1`)          | YES      | `YES`             | passou   |
| 4. Caminho 1→2→3                          | NO       | `NO` / `3 1`      | passou   |
| 5. Dois ciclos sem ligação                | NO       | `NO` / `3 1`      | passou   |
| 6. Dois ciclos ligados num só sentido     | NO       | `NO` / `3 1`      | passou   |
| 7. Cidade isolada                         | NO       | `NO` / `3 1`      | passou   |
| 8. Dois ciclos com vértice em comum       | YES      | `YES`             | passou   |
| Ciclo de 100.000 (estresse)               | YES      | `YES`             | 0,22 s   |
| Caminho de 100.000 (estresse)             | NO       | `NO` / `100000 1` | 0,19 s   |
| Ciclo de 100.000 quebrado no meio         | NO       | `NO` / `50000 1`  | 0,16 s   |
| 100.000 vértices, 200.000 arestas         | NO       | `NO` / `99995 1`  | 0,27 s   |
| Ciclo de 100.000 + 100.000 arestas extras | YES      | `YES`             | 0,26 s   |

O caso 6 foi incluído de propósito: tratando o grafo como não direcionado, como no caso particular do marco 2, a resposta seria `YES`. O caso 1 mostra que o par impresso pode diferir do enunciado (`4 2`) e continuar correto.

Descrição de cada caso em [`../dados/casos-de-teste.txt`](../dados/casos-de-teste.txt).

O arquivo submetido ao juiz online é [`../src/Main.java`](../src/Main.java). A submissão de 07/10/2026 recebeu `ACCEPTED` nos 20 casos de teste do CSES. A maioria rodou entre 0,07 s e 0,09 s. Os testes grandes (#6 a #12) ficaram entre 0,41 s e 0,66 s, e o pior foi o #14, com 0,97 s, dentro do limite de 1,00 s.

A margem no #14 é pequena. Os tempos do juiz ficaram bem acima dos medidos localmente, onde o pior caso levou 0,27 s. Isso depende da máquina do CSES, e não vemos o conteúdo dos testes para saber qual formato de grafo é o #14.

Print do veredito em [`../evidencias/accepted.png`](../evidencias/accepted.png).

## 6. Conclusão

O problema pedia exatamente a definição de **conectividade forte**, e o Kosaraju-Sharir a reduz a uma contagem: o grafo é fortemente conexo se, e somente se, `count() == 1`.

O ponto central foi entender por que a ordem da fase 1 importa. A pós-ordem reversa de `Gᵀ` faz a fase 2 começar sempre por um sumidouro de `G`. Isso garante que cada DFS marque uma única componente e também resolve a segunda parte da saída: qualquer vértice da componente `0` serve como `a`, e qualquer vértice fora dela serve como `b`.

| T1 — Counting Rooms                     | T2 — Flight Routes Check                               |
| --------------------------------------- | ------------------------------------------------------ |
| grafo não direcionado e implícito       | grafo dirigido com listas de adjacência                |
| componentes conexas (`algs4.CC`)        | componentes f-conexas (`algs4.KosarajuSharirSCC`)      |
| uma busca por componente                | duas DFS completas (em `Gᵀ` e em `G`)                  |
| recursão estourou → DFS trocada por BFS | recursão estourou → DFS mantida, pilha aumentada       |

A última linha resume a diferença. No T1, qualquer busca servia, porque só importava **quem** era alcançado. No T2, a **ordem de término** da DFS faz parte do critério, então a DFS não podia ser substituída.
