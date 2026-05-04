package br.com.projeto.udp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ChatMensagemCodecTest {

    @Test
    void deveCodificarEDecodificarMensagemSimples() {
        ChatMensagem original = new ChatMensagem("Alice", "Olá!");

        byte[] dados = ChatMensagemCodec.codificar(original);
        ChatMensagem decodificada = ChatMensagemCodec.decodificar(dados, dados.length);

        assertEquals("Alice", decodificada.getNomeRemetente());
        assertEquals("Olá!", decodificada.getTexto());
    }

    @Test
    void devePreservarSeparadorNoTexto() {
        ChatMensagem original = new ChatMensagem("Bob", "Mensagem com | separador");

        byte[] dados = ChatMensagemCodec.codificar(original);
        ChatMensagem decodificada = ChatMensagemCodec.decodificar(dados, dados.length);

        assertEquals("Bob", decodificada.getNomeRemetente());
        assertEquals("Mensagem com | separador", decodificada.getTexto());
    }

    @Test
    void quandoNaoHaSeparador_deveTratarComoFormatoDesconhecido() {
        String payload = "Mensagem sem formato";
        byte[] dados = payload.getBytes(java.nio.charset.StandardCharsets.UTF_8);

        ChatMensagem decodificada = ChatMensagemCodec.decodificar(dados, dados.length);

        assertEquals("Desconhecido", decodificada.getNomeRemetente());
        assertEquals("Mensagem sem formato", decodificada.getTexto());
    }
}
