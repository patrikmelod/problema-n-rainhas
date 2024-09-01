package org.paralelo;

import javax.swing.JOptionPane;

import org.util.NRainhasUtil;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

// Coleta de dados de tempo de processamento foram feitas em um Macbook Air, com chip M2 (CPU com 8 núcleos de processamento)

// Paraleliza o código ainda mais, criando uma thread para cada combinação possível das 2 primeiras linhas 
// ou seja, número de threads = (N * (N - 3)) + 2
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
                Thread thread = new Thread(() -> {
                    try {
                        // Instancia o print writer para escrever as soluções em arquivos por thread instanciada
                        PrintWriter pst = new PrintWriter(
                                System.getProperty("user.dir")
                                        + "/src/main/resources/Resultado N Rainhas Paralelo Plus T" + (linhaPrimeiraRainha + 1) + (linhaSegundaRainha + 1)
                                        + ".txt");

                        resolveRainhas(linhaPrimeiraRainha, linhaSegundaRainha, n, pst);

                        pst.close();
                    } catch (FileNotFoundException e) {
                        System.out.print("Arquivo não encontrado: " + e);
                    }
                });
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
        NRainhasUtil.tempoExecucao(inicioTempo, fimTempo);
    }

    // Método que resolve onde colocar as rainhas e conta o número de soluções
    private static void resolveRainhas(int linhaPrimeiraRainha, int linhaSegundaRainha, int n, PrintWriter ps) {
        int[][] tabuleiro = new int[n][n];

        // Coloca a primeira rainha na primeira coluna
        tabuleiro[linhaPrimeiraRainha][0] = 1;

        // Verifica se é seguro colocar a segunda rainha na segunda coluna
        if (NRainhasUtil.eSeguro(tabuleiro, linhaSegundaRainha, 1)) {
            tabuleiro[linhaSegundaRainha][1] = 1;

            // Continua a resolver o problema para as colunas subsequentes
            resolveRainhasRecursivo(tabuleiro, 2, ps);
        }
    }

    private static boolean resolveRainhasRecursivo(int[][] tabuleiro, int col, PrintWriter ps) {
        int n = tabuleiro.length;

        // Se todas as rainhas forem colocadas, conta a solução e retorna verdadeiro
        if (col >= n) {
            NRainhasUtil.tabuleiro(tabuleiro, ps, contadorSolucoes);
            synchronized (NRainhasParalelo.class) { // Sincronizado para evitar condições de corrida
                contadorSolucoes++;
            }
            // Para mostrar a primeira solução encontrada
            // if(contadorSolucoes == 1)

            return true;
        }

        boolean solucao = false;

        // Tenta colocar uma rainha em cada linha da coluna atual
        for (int i = 0; i < n; i++) {
            if (NRainhasUtil.eSeguro(tabuleiro, i, col)) {
                // Coloca a rainha na posição (i, col)
                tabuleiro[i][col] = 1;

                // Tenta colocar a rainha na próxima coluna
                solucao = resolveRainhasRecursivo(tabuleiro, col + 1, ps) || solucao;

                // Se colocar a rainha na posição (i, col) não leva a uma solução,
                // remove a rainha da posição (i, col)
                tabuleiro[i][col] = 0;
            }
        }
        return solucao;
    }
}
