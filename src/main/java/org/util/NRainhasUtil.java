package org.util;

import java.io.PrintWriter;

// Métodos comuns aos códigos
public class NRainhasUtil {

    // Método para verificar se é seguro colocar uma rainha na posição
    public static boolean eSeguro(int[][] tabuleiro, int linha, int coluna) {
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
    public static void tabuleiro(int[][] tabuleiro, PrintWriter ps, int contadorSolucoes) {
        int N = tabuleiro.length;
        ps.println("Solução " + contadorSolucoes + ":");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tabuleiro[i][j] == 1) {
                    ps.print("X ");
                } else {
                    ps.print(". ");
                }
            }
            ps.println();
        }
        ps.println();
    }

    // Método para imprimir o tempo de execução
    public static void tempoExecucao(long inicio, long fim) {
        long tempoExecucao = fim - inicio;
        System.out.println("Tempo de execução: " + tempoExecucao + " ms");
    }

}
