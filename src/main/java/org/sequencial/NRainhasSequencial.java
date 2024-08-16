package org.sequencial;

import javax.swing.JOptionPane;

public class NRainhasSequencial {

    private static int contadorSolucoes = 0;

    public static void main(String[] args) {
        // Input do valor de N (quantidade de rainhas, linhas e colunas)
        String input = JOptionPane.showInputDialog("Digite o valor de N para o problema das N Rainhas:");
        int n = Integer.parseInt(input);

        // Cria o tabuleiro
        int[][] tabuleiro = new int[n][n];

        // Inicia a contagem do tempo de execução
        long inicioTempo = System.currentTimeMillis();

        // Resolve o problema das N Rainhas
        resolveRainhas(tabuleiro, 0);

        // Finaliza a contagem do tempo de execução
        long fimTempo = System.currentTimeMillis();

        // Exibe a quantidade total de soluções encontradas
        System.out.println("Total de soluções encontradas para N = " + n + ": " + contadorSolucoes);

        // Exibe o tempo de execução
        tempoExecucao(inicioTempo, fimTempo);
    }

    // Método resolve onde colocar as rainhas e conta o número de soluções
    private static boolean resolveRainhas(int[][] tabuleiro, int col) {
        int n = tabuleiro.length;

        // Se todas as rainhas forem colocadas, conta a solução e retorna verdadeiro
        if (col >= n) {
            contadorSolucoes++;
            tabuleiro(tabuleiro);  // Para mostrar cada solução
            return true;
        }

        boolean solucao = false;

        // Tenta colocar uma rainha em cada linha da coluna atual
        for (int i = 0; i < n; i++) {
            if (eSeguro(tabuleiro, i, col)) {
                // Coloca a rainha na posição (i, col)
                tabuleiro[i][col] = 1;

                // Tenta colocar a rainha na próxima coluna
                solucao = resolveRainhas(tabuleiro, col + 1) || solucao;

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
        long executionTime = fim - inicio;
        System.out.println("Tempo de execução: " + executionTime + " ms");
    }
}