# Marco 1 — Problema e Conhecimento Prévio

## 1. Enunciado

Existem `n` cidades e `m` voos. Cada voo vai de uma cidade `a` para uma cidade `b`, apenas nesse sentido. É preciso verificar se é possível viajar de qualquer cidade para qualquer outra usando os voos disponíveis.

## 2. Entrada

A primeira linha contém dois inteiros:

* `n` — quantidade de cidades, numeradas `1, 2, …, n`;
* `m` — quantidade de voos.

Em seguida, são fornecidas `m` linhas com dois inteiros `a` e `b`: existe um voo de `a` para `b`.

## 3. Saída

* `YES`, se for possível ir de qualquer cidade para qualquer outra;
* caso contrário, `NO` e, na linha seguinte, duas cidades `a` e `b` tais que não exista rota de `a` para `b`. Qualquer par válido é aceito.

## 4. Restrições

* `1 ≤ n ≤ 10⁵`;
* `1 ≤ m ≤ 2 · 10⁵`;
* `1 ≤ a, b ≤ n`;
* limite de tempo: 1 s; limite de memória: 512 MB.

## 5. Modelagem como Grafo

* **Vértices:** as cidades;
* **Arestas:** os voos, dirigidos de `a` para `b`;
* **Tipo:** grafo dirigido e não ponderado.

O grafo é dirigido porque os voos são só de ida: existir `a → b` não garante `b → a`. Ele pode conter ciclos. O enunciado não proíbe voos repetidos nem voos com `a = b`, então o grafo não é necessariamente simples. Isso não altera a resposta, porque nenhum dos dois casos cria caminhos novos entre cidades diferentes.

## 6. Resultado de Aprendizagem

O problema afere a **conectividade forte** em grafos dirigidos. Não basta que as cidades estejam "ligadas": é preciso existir caminho nos **dois sentidos** entre todo par de cidades.

## 7. Participação da DFS/BFS

Uma busca a partir de uma cidade `s` responde a pergunta "quem `s` alcança?". Para conectividade forte é preciso também saber "quem alcança `s`?". Essa segunda pergunta é respondida pela mesma busca, rodando no grafo com as arestas invertidas. A forma exata de combinar as buscas é definida no marco 3.

## 8. Instância Pequena

Entrada (exemplo do enunciado):

```text
4 5
1 2
2 3
3 1
1 4
3 4
```

```text
  1 ───→ 2
  ↑ ↘    │
  │  4   │
  │  ↑   ↓
  └──┴── 3
```

As cidades `1`, `2` e `3` formam um ciclo e chegam umas às outras. A cidade `4` recebe voos, mas não tem nenhum voo de saída.

Saída esperada:

```text
NO
4 2
```

Não existe rota de `4` para `2`. Os pares `4 1` e `4 3` também seriam aceitos.
