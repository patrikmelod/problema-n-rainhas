package org.paralelo;

import javax.swing.JOptionPane;

import org.util.NRainhasUtil;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

// Coleta de dados de tempo de processamento foram feitas em um Macbook Air, com chip M2 (CPU com 8 núcleos de processamento)

// Paraleliza o código criando uma thread para cada posição inicial da primeira linha 
// ou seja, número de threada = N
public class NRainhasParalelo {

    private static int contadorSolucoes = 0;

    public static void main(String[] args) throws FileNotFoundException {
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
            Thread thread = new Thread(() -> {
                try {
                    // Instancia o print writer para escrever as soluções em arquivos por thread instanciada
                    PrintWriter pst = new PrintWriter(
                            System.getProperty("user.dir")
                                    + "/src/main/resources/Resultado N Rainhas Paralelo T" + (linhaInicial + 1)
                                    + ".txt");

                    resolveRainhas(linhaInicial, n, pst);
                    
                    pst.close();
                } catch (FileNotFoundException e) {
                    System.out.print("Arquivo não encontrado: " + e);
                }
            });
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
        NRainhasUtil.tempoExecucao(inicioTempo, fimTempo);
    }

    // Método que resolve onde colocar as rainhas e conta o número de soluções
    private static void resolveRainhas(int linhaInicial, int n, PrintWriter ps) throws FileNotFoundException {
        // Cria um novo tabuleiro para cada thread, assim não há risco de convergência
        int[][] tabuleiro = new int[n][n];

        // Tenta colocar a primeira rainha na linha especificada pela thread
        tabuleiro[linhaInicial][0] = 1;

        // Continua a resolver o problema para as colunas subsequentes
        resolveRainhasRecursivo(tabuleiro, 1, ps);
    }

    private static boolean resolveRainhasRecursivo(int[][] tabuleiro, int col, PrintWriter ps)
            throws FileNotFoundException {
        int n = tabuleiro.length;

        // Se todas as rainhas forem colocadas, conta a solução e retorna verdadeiro
        if (col >= n) {
            // Para mostrar a primeira solução encontrada
            NRainhasUtil.tabuleiro(tabuleiro, ps, contadorSolucoes);

            // Contador sincronizado para evitar erros na soma
            synchronized (NRainhasParalelo.class) {
                contadorSolucoes++;
            }
        
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

                // Se colocar a rainha na posição (i, col) não leva a uma solução, remove a
                // rainha da posição (i, col)
                tabuleiro[i][col] = 0;
            }
        }
        return solucao;
    }
}