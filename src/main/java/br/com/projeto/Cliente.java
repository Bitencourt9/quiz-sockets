package br.com.projeto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;

public class Cliente {
    private static final String HOST = "localhost";
    private static final int PORTA = 5000;

    private Socket socket;
    private BufferedReader leitor;
    private PrintWriter escritor;
    private BufferedReader teclado;

    public Cliente() {
        this.teclado = new BufferedReader(new InputStreamReader(System.in));
    }

    public void conectar() {
        try {
            socket = new Socket(HOST, PORTA);
            System.out.println("✅ Conectado ao servidor em " + HOST + ":" + PORTA + "\n");

            inicializarStreams();
            processarFluxoServidor();

        } catch (ConnectException e) {
            System.err.println("❌ Erro: Não foi possível conectar ao servidor.");
        } catch (SocketException e) {
            System.out.println("\n⚠️  Conexão encerrada pelo servidor.");
        } catch (IOException e) {
            System.err.println("Erro de I/O: " + e.getMessage());
        } finally {
            desconectar();
        }
    }

    private void processarFluxoServidor() throws IOException {
        String linha;
        // O loop principal agora é mais robusto para ler até o fim da stream
        while ((linha = leitor.readLine()) != null) {
            System.out.println(linha);

            // CORREÇÃO 1: Gatilho específico para entrada de dados.
            // O servidor DEVE enviar exatamente esta frase para liberar o teclado.
            if (linha.contains("Digite o número da sua resposta")) {
                String resposta = teclado.readLine();
                if (resposta != null) {
                    escritor.println(resposta);
                    escritor.flush();
                }
            }

            // CORREÇÃO 3: Condição de saída para evitar que o cliente tente ler 
            // após o servidor ter enviado o placar e fechado a conexão.
            if (linha.contains("Conexão encerrada") || linha.contains("Obrigado por participar")) {
                break;
            }
        }
    }

    private void inicializarStreams() throws IOException {
        // 'true' habilita o autoflush para o PrintWriter
        escritor = new PrintWriter(socket.getOutputStream(), true);
        leitor = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    private void desconectar() {
        try {
            if (leitor != null) leitor.close();
            if (escritor != null) escritor.close();
            if (socket != null && !socket.isClosed()) socket.close();
            System.out.println("✅ Processo finalizado.");
        } catch (IOException e) {
            System.err.println("Erro ao desconectar: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Cliente().conectar();
    }
}