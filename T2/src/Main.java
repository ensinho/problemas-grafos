/******************************************************************************
 *  CSES 1682 - Flight Routes Check
 *  Solucao final: componentes fortemente conexas (Kosaraju-Sharir).
 *
 *  As classes Digraph, DepthFirstOrder, KosarajuSharirSCC, Bag e Stack sao
 *  copias reduzidas de algs4 (Sedgewick & Wayne), mantidas aninhadas porque
 *  o CSES aceita apenas um arquivo por submissao.
 ******************************************************************************/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class Main {

    // a recursao da DFS pode chegar a 100.000 niveis; a thread recebe 256 MB de pilha
    private static final long TAMANHO_PILHA = 1L << 28;

    public static void main(String[] args) throws InterruptedException {
        Thread principal = new Thread(null, () -> {
            try {
                resolver();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, "principal", TAMANHO_PILHA);
        principal.start();
        principal.join();
    }

    private static void resolver() throws IOException {
        BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer cabecalho = new StringTokenizer(entrada.readLine());
        int cidades = Integer.parseInt(cabecalho.nextToken());
        int voos = Integer.parseInt(cabecalho.nextToken());

        // cidades 1..n viram vertices 0..n-1
        Digraph grafo = new Digraph(cidades);
        for (int i = 0; i < voos; i++) {
            StringTokenizer linha = new StringTokenizer(entrada.readLine());
            int a = Integer.parseInt(linha.nextToken()) - 1;
            int b = Integer.parseInt(linha.nextToken()) - 1;
            grafo.addEdge(a, b);
        }

        KosarajuSharirSCC scc = new KosarajuSharirSCC(grafo);

        if (scc.count() == 1) {
            System.out.println("YES");
            return;
        }

        // a componente 0 e um sumidouro em G: quem esta nela nao sai dela
        int origem = -1;
        int destino = -1;
        for (int v = 0; v < cidades; v++) {
            if (scc.id(v) == 0) { if (origem == -1) origem = v; }
            else if (destino == -1) destino = v;
        }

        System.out.println("NO");
        System.out.println((origem + 1) + " " + (destino + 1));
    }

    // adaptado de algs4.KosarajuSharirSCC: removidos check(), validateVertex() e stronglyConnected()
    private static class KosarajuSharirSCC {
        private boolean[] marked;
        private int[] id;
        private int count;

        public KosarajuSharirSCC(Digraph digraph) {

            // fase 1: pos-ordem reversa do grafo reverso
            DepthFirstOrder dfs = new DepthFirstOrder(digraph.reverse());

            // fase 2: DFS em G seguindo a pos-ordem reversa de G^R
            marked = new boolean[digraph.V()];
            id = new int[digraph.V()];
            for (int v : dfs.reversePost()) {
                if (!marked[v]) {
                    dfs(digraph, v);
                    count++;
                }
            }
        }

        private void dfs(Digraph digraph, int v) {
            marked[v] = true;
            id[v] = count;
            for (int w : digraph.adj(v)) {
                if (!marked[w]) dfs(digraph, w);
            }
        }

        public int count() {
            return count;
        }

        public int id(int v) {
            return id[v];
        }
    }

    // adaptado de algs4.DepthFirstOrder: mantida apenas a pos-ordem,
    // empilhada direto na Stack que e devolvida por reversePost()
    private static class DepthFirstOrder {
        private boolean[] marked;
        private Stack<Integer> reversePost;

        public DepthFirstOrder(Digraph digraph) {
            marked = new boolean[digraph.V()];
            reversePost = new Stack<Integer>();
            for (int v = 0; v < digraph.V(); v++)
                if (!marked[v]) dfs(digraph, v);
        }

        private void dfs(Digraph digraph, int v) {
            marked[v] = true;
            for (int w : digraph.adj(v)) {
                if (!marked[w]) {
                    dfs(digraph, w);
                }
            }
            reversePost.push(v);
        }

        public Iterable<Integer> reversePost() {
            return reversePost;
        }
    }

    // adaptado de algs4.Digraph: mantidos V(), addEdge(), adj() e reverse()
    private static class Digraph {
        private final int V;
        private Bag<Integer>[] adj;

        @SuppressWarnings("unchecked")
        public Digraph(int V) {
            this.V = V;
            adj = (Bag<Integer>[]) new Bag[V];
            for (int v = 0; v < V; v++) {
                adj[v] = new Bag<Integer>();
            }
        }

        public int V() {
            return V;
        }

        public void addEdge(int v, int w) {
            adj[v].add(w);
        }

        public Iterable<Integer> adj(int v) {
            return adj[v];
        }

        public Digraph reverse() {
            Digraph reverse = new Digraph(V);
            for (int v = 0; v < V; v++) {
                for (int w : adj(v)) {
                    reverse.addEdge(w, v);
                }
            }
            return reverse;
        }
    }

    // adaptado de algs4.Bag: mantidos add() e o iterador
    private static class Bag<Item> implements Iterable<Item> {
        private Node<Item> first;

        public void add(Item item) {
            Node<Item> oldfirst = first;
            first = new Node<Item>();
            first.item = item;
            first.next = oldfirst;
        }

        public Iterator<Item> iterator() {
            return new LinkedIterator<Item>(first);
        }
    }

    // adaptado de algs4.Stack: mantidos push() e o iterador
    private static class Stack<Item> implements Iterable<Item> {
        private Node<Item> first;

        public void push(Item item) {
            Node<Item> oldfirst = first;
            first = new Node<Item>();
            first.item = item;
            first.next = oldfirst;
        }

        public Iterator<Item> iterator() {
            return new LinkedIterator<Item>(first);
        }
    }

    // no e iterador compartilhados por Bag e Stack (iguais nas duas classes do algs4)
    private static class Node<Item> {
        private Item item;
        private Node<Item> next;
    }

    private static class LinkedIterator<Item> implements Iterator<Item> {
        private Node<Item> current;

        public LinkedIterator(Node<Item> first) {
            current = first;
        }

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
}
