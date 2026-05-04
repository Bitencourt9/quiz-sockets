package br.com.projeto.udp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.junit.jupiter.api.Test;

public class ChatUdpIntegracaoTest {

    @Test
    void deveEnviarEReceberMensagemEmLocalhost() throws Exception {
        InetAddress loopback = InetAddress.getLoopbackAddress();

        try (DatagramSocket socketA = new DatagramSocket(0, loopback);
             DatagramSocket socketB = new DatagramSocket(0, loopback)) {

            socketB.setSoTimeout(1500);

            ChatMensagem original = new ChatMensagem("Alice", "Teste UDP");
            byte[] dados = ChatMensagemCodec.codificar(original);

            DatagramPacket pacote = new DatagramPacket(dados, dados.length, loopback, socketB.getLocalPort());
            socketA.send(pacote);

            byte[] buffer = new byte[4096];
            DatagramPacket recebido = new DatagramPacket(buffer, buffer.length);
            socketB.receive(recebido);

            ChatMensagem decodificada = ChatMensagemCodec.decodificar(recebido.getData(), recebido.getLength());

            assertEquals("Alice", decodificada.getNomeRemetente());
            assertEquals("Teste UDP", decodificada.getTexto());
            assertEquals(socketA.getLocalPort(), recebido.getPort());
        }
    }
}
