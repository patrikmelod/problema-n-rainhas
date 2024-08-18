package org.paralelo;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;

// Paraleliza o código ainda mais, criando uma thread para cada combinação possível das 2 primeiras linhas (ou seja, Nˆ2 - configurações inválidas)
public class NRainhasParaleloPlus {

    private static int contadorSolucoes = 0;

    public static void main(String[] args) {
        // Input do valor de N (quantidade de rainhas, linhas e colunas)
        String input = JOptionPane.showInputDialog("Digite o valor de N para o problema das N Rainhas:");
        int n = Integer.parseInt(input);

        // Inicia a contagem do tempo de execução
        long inicioTempo = System.currentTimeMillis();

        // Lista para armazenar as threads
        List<Thread> threads = new ArrayList<>();

        // Cria e inicia uma thread para cada configuração das duas primeiras colunas
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int linhaPrimeiraRainha = i;
                int linhaSegundaRainha = j;
                Thread thread = new Thread(() -> resolveRainhas(linhaPrimeiraRainha, linhaSegundaRainha, n));
                threads.add(thread);
                thread.start();
            }
        }

        // Aguarda todas as threads terminarem
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
        System.out.println("PARALELO PLUS -> Total de soluções encontradas para N = " + n + ": " + contadorSolucoes);

        // Exibe o tempo de execução
        tempoExecucao(inicioTempo, fimTempo);
    }

    // Método que resolve onde colocar as rainhas e conta o número de soluções
    private static void resolveRainhas(int linhaPrimeiraRainha, int linhaSegundaRainha, int n) {
        int[][] tabuleiro = new int[n][n];

        // Coloca a primeira rainha na primeira coluna
        tabuleiro[linhaPrimeiraRainha][0] = 1;

        // Verifica se é seguro colocar a segunda rainha na segunda coluna
        if (eSeguro(tabuleiro, linhaSegundaRainha, 1)) {
            tabuleiro[linhaSegundaRainha][1] = 1;

            // Continua a resolver o problema para as colunas subsequentes
            resolveRainhasRecursivo(tabuleiro, 2);
        }
    }

    private static boolean resolveRainhasRecursivo(int[][] tabuleiro, int col) {
        int n = tabuleiro.length;

        // Se todas as rainhas forem colocadas, conta a solução e retorna verdadeiro
        if (col >= n) {
            synchronized (NRainhasParalelo.class) { // Sincronizado para evitar condições de corrida
                contadorSolucoes++;
                // Para mostrar cada solução
                tabuleiro(tabuleiro);
            }
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

                // Se colocar a rainha na posição (i, col) não leva a uma solução,
                // remove a rainha da posição (i, col)
                tabuleiro[i][col] = 0;
            }
        }

        return solucao;
    }

    // Método para verificar se é seguro colocar uma rainha na posição
    private static boolean eSeguro(int[][] tabuleiro, int linha, int coluna) {
        int N = tabuleiro.length;

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
        for (int i = linha, j = coluna; i < N && j >= 0; i++, j--) {
            if (tabuleiro[i][j] == 1) {
                return false;
            }
        }

        return true;
    }

    // Método para imprimir o tabuleiro com as rainhas
    private static void tabuleiro(int[][] tabuleiro) {
        int N = tabuleiro.length;
        System.out.println("Solução " + contadorSolucoes + ":");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
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
