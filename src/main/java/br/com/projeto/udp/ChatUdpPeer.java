package br.com.projeto.udp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Exercício 2 - Chat UDP P2P.
 *
 * Cada instância atua como "cliente e servidor":
 * - Abre um DatagramSocket na porta local e fica recebendo mensagens (thread separada)
 * - Lê o teclado e envia mensagens para o peer remoto
 *
 * Uso:
 *   java -cp target/classes br.com.projeto.udp.ChatUdpPeer <portaLocal> <hostRemoto> <portaRemota> [nome]
 */
public class ChatUdpPeer {
    private static final int TAMANHO_BUFFER = 4096;

    private final int portaLocal;
    private final InetAddress enderecoRemoto;
    private final int portaRemota;
    private final String nome;

    private DatagramSocket socket;
    private volatile boolean executando;

    public ChatUdpPeer(int portaLocal, InetAddress enderecoRemoto, int portaRemota, String nome) {
        this.portaLocal = portaLocal;
        this.enderecoRemoto = enderecoRemoto;
        this.portaRemota = portaRemota;
        this.nome = nome == null || nome.isBlank() ? "Anônimo" : nome;
    }

    public void iniciar() {
        try {
            socket = new DatagramSocket(portaLocal);
            executando = true;

            System.out.println("📨 Chat UDP P2P iniciado!");
            System.out.println("👤 Você: " + nome);
            System.out.println("📍 Local: 0.0.0.0:" + portaLocal);
            System.out.println("🎯 Remoto: " + enderecoRemoto.getHostAddress() + ":" + portaRemota);
            System.out.println("\nDigite uma mensagem e pressione Enter (ou /sair)\n");

            Thread threadRecebedora = new Thread(this::loopReceber, "udp-chat-recebedor");
            threadRecebedora.setDaemon(true);
            threadRecebedora.start();

            loopEnviar();

        } catch (SocketException e) {
            System.err.println("❌ Erro ao abrir porta UDP " + portaLocal + ": " + e.getMessage());
        } finally {
            encerrar();
        }
    }

    private void loopReceber() {
        byte[] buffer = new byte[TAMANHO_BUFFER];

        while (executando) {
            try {
                DatagramPacket pacote = new DatagramPacket(buffer, buffer.length);
                socket.receive(pacote);

                ChatMensagem mensagem = ChatMensagemCodec.decodificar(pacote.getData(), pacote.getLength());
                String infoRemetente = pacote.getAddress().getHostAddress() + ":" + pacote.getPort();

                System.out.println("📩 De " + mensagem.getNomeRemetente() + " (" + infoRemetente + "): "
                        + mensagem.getTexto());

            } catch (SocketException e) {
                // O socket pode ser fechado durante o receive()
                if (executando) {
                    System.err.println("❌ Erro no socket UDP: " + e.getMessage());
                }
                break;
            } catch (IOException e) {
                System.err.println("❌ Erro ao receber mensagem: " + e.getMessage());
            }
        }
    }

    private void loopEnviar() {
        try (BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in))) {
            String linha;
            while (executando && (linha = teclado.readLine()) != null) {
                if (linha.isBlank()) {
                    continue;
                }
                if (linha.equalsIgnoreCase("/sair")) {
                    break;
                }

                enviarMensagem(linha);
            }
        } catch (IOException e) {
            System.err.println("❌ Erro ao ler teclado: " + e.getMessage());
        }
    }

    private void enviarMensagem(String texto) {
        try {
            ChatMensagem msg = new ChatMensagem(nome, texto);
            byte[] dados = ChatMensagemCodec.codificar(msg);

            DatagramPacket pacote = new DatagramPacket(dados, dados.length, enderecoRemoto, portaRemota);
            socket.send(pacote);

        } catch (IOException e) {
            System.err.println("❌ Erro ao enviar mensagem: " + e.getMessage());
        }
    }

    private void encerrar() {
        executando = false;

        if (socket != null && !socket.isClosed()) {
            socket.close();
        }

        System.out.println("\n✅ Chat finalizado.");
    }

    public static void main(String[] args) {
        if (args.length < 3 || args.length > 4) {
            imprimirUso();
            return;
        }

        try {
            int portaLocal = Integer.parseInt(args[0]);
            String hostRemoto = args[1];
            int portaRemota = Integer.parseInt(args[2]);
            String nome = args.length == 4 ? args[3] : "Anônimo";

            InetAddress enderecoRemoto = InetAddress.getByName(hostRemoto);

            new ChatUdpPeer(portaLocal, enderecoRemoto, portaRemota, nome).iniciar();

        } catch (NumberFormatException e) {
            System.err.println("❌ Porta inválida. Use números (ex: 6000).\n");
            imprimirUso();
        } catch (UnknownHostException e) {
            System.err.println("❌ Host remoto inválido: " + e.getMessage() + "\n");
            imprimirUso();
        }
    }

    private static void imprimirUso() {
        System.out.println("Uso:");
        System.out.println("  java -cp target/classes br.com.projeto.udp.ChatUdpPeer <portaLocal> <hostRemoto> <portaRemota> [nome]");
        System.out.println("\nExemplo (na mesma máquina, 2 terminais):");
        System.out.println("  Terminal A: java -cp target/classes br.com.projeto.udp.ChatUdpPeer 6000 localhost 6001 Alice");
        System.out.println("  Terminal B: java -cp target/classes br.com.projeto.udp.ChatUdpPeer 6001 localhost 6000 Bob");
    }
}
