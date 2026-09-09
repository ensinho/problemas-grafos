/******************************************************************************
 *  CSES 1192 - Counting Rooms
 *  Solucao final: BFS sobre o grafo implicito da grade.
 *
 *  A classe Queue e uma copia reduzida de algs4.Queue (Sedgewick & Wayne),
 *  mantida aninhada porque o CSES aceita apenas um arquivo por submissao.
 ******************************************************************************/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class Main {

    private static final int[] dLinha  = { -1, 1, 0, 0 };
    private static final int[] dColuna = { 0, 0, -1, 1 };

    private static int linhas;
    private static int colunas;
    private static char[][] mapa;
    private static boolean[][] marcado;

    public static void main(String[] args) throws IOException {
        BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer cabecalho = new StringTokenizer(entrada.readLine());
        linhas = Integer.parseInt(cabecalho.nextToken());
        colunas = Integer.parseInt(cabecalho.nextToken());

        mapa = new char[linhas][];
        for (int linha = 0; linha < linhas; linha++) {
            mapa[linha] = entrada.readLine().toCharArray();
        }

        marcado = new boolean[linhas][colunas];

        int salas = 0;
        for (int linha = 0; linha < linhas; linha++) {
            for (int coluna = 0; coluna < colunas; coluna++) {
                if (mapa[linha][coluna] == '.' && !marcado[linha][coluna]) {
                    bfs(linha, coluna);
                    salas++;
                }
            }
        }

        System.out.println(salas);
    }

    // busca em largura a partir de um piso ainda nao visitado
    private static void bfs(int linhaInicial, int colunaInicial) {
        Queue<Integer> fila = new Queue<Integer>();
        marcado[linhaInicial][colunaInicial] = true;
        fila.enqueue(linhaInicial * colunas + colunaInicial);

        while (!fila.isEmpty()) {
            int atual = fila.dequeue();
            int linha  = atual / colunas;
            int coluna = atual % colunas;

            for (int direcao = 0; direcao < 4; direcao++) {
                int vizLinha  = linha  + dLinha[direcao];
                int vizColuna = coluna + dColuna[direcao];

                if (vizLinha < 0 || vizLinha >= linhas ||
                    vizColuna < 0 || vizColuna >= colunas) continue;

                if (mapa[vizLinha][vizColuna] != '.' ||
                    marcado[vizLinha][vizColuna]) continue;

                marcado[vizLinha][vizColuna] = true;
                fila.enqueue(vizLinha * colunas + vizColuna);
            }
        }
    }

    // adaptado de algs4.Queue: mantidos apenas isEmpty, enqueue e dequeue
    private static class Queue<Item> {
        private Node<Item> first;
        private Node<Item> last;

        private static class Node<Item> {
            private Item item;
            private Node<Item> next;
        }

        public boolean isEmpty() {
            return first == null;
        }

        public void enqueue(Item item) {
            Node<Item> oldlast = last;
            last = new Node<Item>();
            last.item = item;
            last.next = null;
            if (isEmpty()) first = last;
            else           oldlast.next = last;
        }

        public Item dequeue() {
            if (isEmpty()) throw new NoSuchElementException("Queue underflow");
            Item item = first.item;
            first = first.next;
            if (isEmpty()) last = null;
            return item;
        }
    }
}
