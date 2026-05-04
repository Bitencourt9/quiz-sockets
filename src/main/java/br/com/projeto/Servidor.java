package br.com.projeto;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Servidor de Quiz Corrigido.
 * Sincroniza o fluxo paralelo dos clientes e garante o envio do placar final.
 */
public class Servidor {
    private static final int PORTA = 5000;
    private static final int NUMERO_CLIENTES = 3;
    private static final int NUMERO_PERGUNTAS = 3;

    private ServerSocket serverSocket;
    private List<GerenciadorCliente> clientes;
    private ExecutorService executorService;
    private Pergunta[] perguntas;
    private CyclicBarrier barreira;

    public Servidor() {
        this.clientes = Collections.synchronizedList(new ArrayList<>());
        this.executorService = Executors.newFixedThreadPool(NUMERO_CLIENTES);
        this.perguntas = criarPerguntas();
        this.barreira = new CyclicBarrier(NUMERO_CLIENTES);
    }

    public void iniciar() {
        try {
            serverSocket = new ServerSocket(PORTA);
            System.out.println("🚀 Servidor iniciado na porta " + PORTA);
            System.out.println("⏳ Aguardando " + NUMERO_CLIENTES + " clientes...\n");

            for (int i = 1; i <= NUMERO_CLIENTES; i++) {
                Socket socketCliente = serverSocket.accept();
                System.out.println("✅ Cliente " + i + " conectado.");

                GerenciadorCliente gerenciador = new GerenciadorCliente(socketCliente, i, barreira);
                // Inicializa os canais de texto antes de começar a thread
                gerenciador.inicializarStreams(); 
                clientes.add(gerenciador);
            }

            System.out.println("\n📊 Todos conectados. Iniciando o quiz simultaneamente...");
            
            // Dispara as threads
            for (GerenciadorCliente c : clientes) {
                executorService.execute(c); // Chama o run() do GerenciadorCliente
            }

            // Inicia o controle de perguntas em paralelo
            for (GerenciadorCliente c : clientes) {
                executorService.execute(() -> {
                    try {
                        executarCicloPerguntas(c);
                    } catch (Exception e) {
                        System.err.println("Erro no ciclo do cliente " + c.getIdCliente());
                    }
                });
            }

            // Aguarda a conclusão de todas as atividades
            executorService.shutdown();
            executorService.awaitTermination(10, TimeUnit.MINUTES);

            // Envia o placar para todos
            calcularEEnviarPlacarFinal();

            // PAUSA CRUCIAL: Dá tempo para os clientes lerem o placar antes do fechamento
            Thread.sleep(2000); 

        } catch (IOException | InterruptedException e) {
            System.err.println("Erro no servidor: " + e.getMessage());
        } finally {
            encerrar();
        }
    }

    private void executarCicloPerguntas(GerenciadorCliente cliente) throws Exception {
        // Aguarda todos passarem pelo "Bem-vindo" e chegarem na barreira inicial do run()
        // Isso garante sincronia perfeita antes da Pergunta 1
        for (int i = 0; i < NUMERO_PERGUNTAS; i++) {
            cliente.processarPergunta(perguntas[i], i + 1);
        }
    }

    private void calcularEEnviarPlacarFinal() {
        Map<Integer, Integer> placarFinal = new LinkedHashMap<>();
        
        // Coleta pontuações
        for (GerenciadorCliente cliente : clientes) {
            placarFinal.put(cliente.getIdCliente(), cliente.calcularAcertos(perguntas));
        }

        // Distribui o placar
        for (GerenciadorCliente cliente : clientes) {
            try {
                if (cliente.isAtivo()) {
                    cliente.enviarPlacarFinal(placarFinal, cliente.calcularAcertos(perguntas));
                }
            } catch (IOException e) {
                System.err.println("Não foi possível enviar placar ao cliente " + cliente.getIdCliente());
            }
        }
    }

    private Pergunta[] criarPerguntas() {
        Pergunta[] questoes = new Pergunta[NUMERO_PERGUNTAS];
        questoes[0] = new Pergunta("O que é um Socket?", 
            new String[]{"Interface entre aplicação e rede", "Conexão serial", "Camada criptográfica", "Protocolo de roteamento"}, 0);
        questoes[1] = new Pergunta("Qual a diferença entre TCP e UDP?", 
            new String[]{"Velocidade", "TCP garante entrega e conexão", "Segurança", "Nenhuma"}, 1);
        questoes[2] = new Pergunta("Qual classe inicia um servidor em Java?", 
            new String[]{"Socket", "InputStream", "ServerSocket", "DatagramSocket"}, 2);
        return questoes;
    }

    private void encerrar() {
        try {
            if (serverSocket != null) serverSocket.close();
            System.out.println("\n✅ Servidor finalizado com sucesso.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Servidor().iniciar();
    }
}