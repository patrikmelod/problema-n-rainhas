package org.paralelo;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;

// Paraleliza o código criando uma thread para cada posição inicial da primeira linha (ou seja, N threads)
public class NRainhasParalelo {

    private static int contadorSolucoes = 0;

    public static void main(String[] args) {
        // Input do valor de N (quantidade de rainhas, linhas e colunas)
        String input = JOptionPane.showInputDialog("Digite o valor de N para o problema das N Rainhas:");
        int n = Integer.parseInt(input);

        // Inicia a contagem do tempo de execução
        long inicioTempo = System.currentTimeMillis();

        // Lista para armazenar as threads
        List<Thread> threads = new ArrayList<>();

        // Cria e inicia uma thread para cada linha inicial possível
        for (int i = 0; i < n; i++) {
            int linhaInicial = i;
            Thread thread = new Thread(() -> resolveRainhas(linhaInicial, n));
            threads.add(thread);
            thread.start();
        }

        // Aguarda todas as threads terminarem e realiza o join
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Finaliza a contagem do tempo de execução
        long fimTempo = System.currentTimeMillis();

        // Exibe a quantidade total de soluções encontradas
        System.out.println("PARALELO -> Total de soluções encontradas para N = " + n + ": " + contadorSolucoes);

        // Exibe o tempo de execução
        tempoExecucao(inicioTempo, fimTempo);
    }

    // Método que resolve onde colocar as rainhas e conta o número de soluções
    private static void resolveRainhas(int linhaInicial, int n) {
        // Cria um novo tabuleiro para cada thread, assim não há risco de convergência
        int[][] tabuleiro = new int[n][n];

        // Tenta colocar a primeira rainha na linha especificada pela thread
        tabuleiro[linhaInicial][0] = 1;

        // Continua a resolver o problema para as colunas subsequentes
        resolveRainhasRecursivo(tabuleiro, 1);
    }

    private static boolean resolveRainhasRecursivo(int[][] tabuleiro, int col) {
        int n = tabuleiro.length;

        // Se todas as rainhas forem colocadas, conta a solução e retorna verdadeiro
        if (col >= n) {
            // Contador sincronizado para evitar erros na soma
            synchronized (NRainhasParalelo.class) { 
                contadorSolucoes++;
            }
            tabuleiro(tabuleiro);
            return true;
        }

        boolean solucao = false;

        // Tenta colocar uma rainha em cada linha da coluna atual
        for (int i = 0; i < n; i++) {
            if (eSeguro(tabuleiro, i, col)) {
                // Coloca a rainha na posição (i, col)
                tabuleiro[i][col] = 1;

                // Tenta colocar a rainha na próxima coluna
                solucao = resolveRainhasRecursivo(tabuleiro, col + 1) || solucao;

                // Se colocar a rainha na posição (i, col) não leva a uma solução, remove a rainha da posição (i, col)
                tabuleiro[i][col] = 0;
            }
        }

        return solucao;
    }

    // Método para verificar se é seguro colocar uma rainha na posição
    private static boolean eSeguro(int[][] tabuleiro, int linha, int coluna) {
        int n = tabuleiro.length;

        // Verifica a linha à esquerda
        for (int i = 0; i < coluna; i++) {
            if (tabuleiro[linha][i] == 1) {
                return false;
            }
        }

        // Verifica a diagonal superior esquerda
        for (int i = linha, j = coluna; i >= 0 && j >= 0; i--, j--) {
            if (tabuleiro[i][j] == 1) {
                return false;
            }
        }

        // Verifica a diagonal inferior esquerda
        for (int i = linha, j = coluna; i < n && j >= 0; i++, j--) {
            if (tabuleiro[i][j] == 1) {
                return false;
            }
        }

        return true;
    }

    // Método para imprimir o tabuleiro com as rainhas
    private static void tabuleiro(int[][] tabuleiro) {
        int n = tabuleiro.length;
        System.out.println("Solução " + contadorSolucoes + ":");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tabuleiro[i][j] == 1) {
                    System.out.print("X ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    // Método para imprimir o tempo de execução
    private static void tempoExecucao(long inicio, long fim) {
        long tempoExecucao = fim - inicio;
        System.out.println("Tempo de execução: " + tempoExecucao + " ms");
    }
}