package br.com.projeto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.CyclicBarrier;

/**
 * Classe que gerencia a comunicação com um cliente individual.
 * Corrigida para sincronização exata com o novo Cliente.java.
 */
public class GerenciadorCliente implements Runnable {
    private Socket socket;
    private int idCliente;
    private BufferedReader leitor;
    private PrintWriter escritor;
    private int[] respostas;
    private CyclicBarrier barreira;
    private volatile boolean ativo;

    public GerenciadorCliente(Socket socket, int idCliente, CyclicBarrier barreira) {
        this.socket = socket;
        this.idCliente = idCliente;
        this.barreira = barreira;
        this.respostas = new int[3]; 
        this.ativo = true;
    }

    @Override
    public void run() {
        try {
            // Streams agora inicializados pelo Servidor antes de disparar a thread
            
            escritor.println("Bem-vindo ao Quiz de Sockets TCP!");
            escritor.println("ID do Cliente: " + idCliente);
            escritor.println("Aguardando os outros clientes...\n");
            escritor.flush();

            barreira.await();

            System.out.println("[Servidor] Cliente " + idCliente + " iniciando o quiz.");

        } catch (Exception e) {
            System.err.println("Erro no ciclo de vida do cliente " + idCliente + ": " + e.getMessage());
            fecharConexao();
        }
    }

    public void processarPergunta(Pergunta pergunta, int numeroPergunta) 
            throws IOException, InterruptedException {
        try {
            // CORREÇÃO 1: Uso de println em vez de print para garantir que o cliente 
            // receba a linha completa e o leitor.readLine() não trave.
            escritor.println("\n--- PERGUNTA " + numeroPergunta + " ---");
            escritor.println(pergunta.toString());
            escritor.println("Digite o número da sua resposta (1-4):"); // Frase gatilho exata
            escritor.flush();

            // Lê a resposta
            String respostaStr = leitor.readLine();
            if (respostaStr == null) {
                ativo = false;
                return;
            }

            int resposta;
            try {
                resposta = Integer.parseInt(respostaStr.trim()) - 1;
            } catch (NumberFormatException e) {
                resposta = -1;
            }

            // Validação e Feedback
            if (resposta < 0 || resposta > 3) {
                respostas[numeroPergunta - 1] = -1;
                escritor.println("⚠️ Resposta inválida ou fora do intervalo!");
            } else {
                respostas[numeroPergunta - 1] = resposta;
                if (pergunta.estaCorreta(resposta)) {
                    escritor.println("✅ Correto!");
                } else {
                    escritor.println("❌ Incorreto! A resposta correta era: " 
                            + (pergunta.getRespostaCorreta() + 1));
                }
            }
            
            // CORREÇÃO 2: Flush antes de entrar na barreira. 
            // Garante que o cliente veja se acertou/errou antes de ficar esperando os outros.
            escritor.flush();

            // Aguarda os outros clientes terminarem a mesma pergunta
            barreira.await();

        } catch (java.util.concurrent.BrokenBarrierException e) {
            System.err.println("Sincronização perdida para cliente " + idCliente);
        }
    }

    public int calcularAcertos(Pergunta[] perguntas) {
        int acertos = 0;
        for (int i = 0; i < respostas.length; i++) {
            if (respostas[i] != -1 && perguntas[i].estaCorreta(respostas[i])) {
                acertos++;
            }
        }
        return acertos;
    }

    public void enviarPlacarFinal(java.util.Map<Integer, Integer> placarFinal, int acertosDoCliente) 
            throws IOException {
        escritor.println("\n========== PLACAR FINAL ==========");
        
        int maiorPontuacao = placarFinal.values().stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0);
        
        placarFinal.forEach((id, acertos) -> {
            String resultado = "Cliente " + id + ": " + acertos + " acerto(s)";
            if (id == idCliente) resultado += " <- Você";
            if (acertos == maiorPontuacao && acertos > 0) resultado += " 🏆 VENCEDOR!";
            escritor.println(resultado);
        });
        
        escritor.println("==================================");
        // CORREÇÃO 3: Mensagem clara de encerramento para o loop do Cliente.java
        escritor.println("Obrigado por participar! Conexão encerrada.");
        escritor.flush();
    }

    public void enviarMensagem(String mensagem) {
        escritor.println(mensagem);
        escritor.flush();
    }

    public void inicializarStreams() throws IOException {
        // 'true' para auto-flush
        escritor = new PrintWriter(socket.getOutputStream(), true);
        leitor = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public int getIdCliente() { return idCliente; }

    public boolean isAtivo() { return ativo; }

    private void fecharConexao() {
        ativo = false;
        try {
            if (leitor != null) leitor.close();
            if (escritor != null) escritor.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            System.err.println("Erro ao fechar socket do cliente " + idCliente);
        }
    }
}