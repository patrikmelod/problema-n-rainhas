package org.sequencial;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javax.swing.JOptionPane;

import org.util.NRainhasUtil;

// Coleta de dados de tempo de processamento foram feitas em um Macbook Air, com chip M2 (CPU com 8 núcleos de processamento)

// Código sequencial
public class NRainhasSequencial {

    private static int contadorSolucoes = 0;

    public static void main(String[] args) throws FileNotFoundException {
        // Input do valor de N (quantidade de rainhas, linhas e colunas)
        String input = JOptionPane.showInputDialog("Digite o valor de N para o problema das N Rainhas:");
        int n = Integer.parseInt(input);

        // Cria o tabuleiro
        int[][] tabuleiro = new int[n][n];

        // Instancia o print writer para escrever o arquivo com a primeira solução encontrada
        PrintWriter ps = new PrintWriter(
                System.getProperty("user.dir")
                        + "/src/main/resources/Resultado N Rainhas Sequencial.txt");

        // Inicia a contagem do tempo de execução
        long inicioTempo = System.currentTimeMillis();

        // Resolve o problema das N Rainhas
        resolveRainhas(tabuleiro, 0, ps);

        // Finaliza a contagem do tempo de execução
        long fimTempo = System.currentTimeMillis();

        // Exibe a quantidade total de soluções encontradas
        System.out.println("SEQUENCIAL -> Total de soluções encontradas para N = " + n + ": " + contadorSolucoes);

        // Exibe o tempo de execução
        NRainhasUtil.tempoExecucao(inicioTempo, fimTempo);

        // Encerra a escrita do arquivo
        ps.close();
    }

    // Método resolve onde colocar as rainhas e conta o número de soluções
    private static boolean resolveRainhas(int[][] tabuleiro, int col, PrintWriter ps) {
        int n = tabuleiro.length;

        // Se todas as rainhas forem colocadas, conta a solução e retorna verdadeiro
        if (col >= n) {
            contadorSolucoes++;
            // Para mostrar a primeira solução
            if (contadorSolucoes == 1)
                NRainhasUtil.tabuleiro(tabuleiro, ps, contadorSolucoes);

            return true;
        }

        boolean solucao = false;

        // Tenta colocar uma rainha em cada linha da coluna atual
        for (int i = 0; i < n; i++) {
            if (NRainhasUtil.eSeguro(tabuleiro, i, col)) {
                // Coloca a rainha na posição (i, col)
                tabuleiro[i][col] = 1;

                // Tenta colocar a rainha na próxima coluna
                solucao = resolveRainhas(tabuleiro, col + 1, ps) || solucao;

                // Se colocar a rainha na posição (i, col) não leva a uma solução,
                // remove a rainha da posição (i, col)
                tabuleiro[i][col] = 0;
            }
        }
        return solucao;
    }
}