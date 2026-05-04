package br.com.projeto.udp;

import java.nio.charset.StandardCharsets;

/**
 * Codec simples para codificar/decodificar mensagens em UDP.
 *
 * Formato do payload:
 *   nomeRemetente|texto
 *
 * Observação: o texto pode conter '|'. Apenas o PRIMEIRO '|' separa nome e texto.
 */
public final class ChatMensagemCodec {
    private static final String SEPARADOR = "|";

    private ChatMensagemCodec() {
        // utilitário
    }

    public static byte[] codificar(ChatMensagem mensagem) {
        if (mensagem == null) {
            throw new IllegalArgumentException("mensagem não pode ser null");
        }

        String nome = mensagem.getNomeRemetente();
        if (nome == null || nome.isBlank()) {
            nome = "Anônimo";
        }

        // Evita quebrar o parser (o separador só pode aparecer depois do nome)
        nome = nome.replace('|', ' ').trim();

        String texto = mensagem.getTexto();
        if (texto == null) {
            texto = "";
        }

        String payload = nome + SEPARADOR + texto;
        return payload.getBytes(StandardCharsets.UTF_8);
    }

    public static ChatMensagem decodificar(byte[] dados, int tamanho) {
        if (dados == null) {
            throw new IllegalArgumentException("dados não pode ser null");
        }
        if (tamanho < 0 || tamanho > dados.length) {
            throw new IllegalArgumentException("tamanho inválido");
        }

        String payload = new String(dados, 0, tamanho, StandardCharsets.UTF_8);

        int idx = payload.indexOf(SEPARADOR);
        if (idx < 0) {
            // Formato desconhecido (compatibilidade): mostra tudo como texto
            return new ChatMensagem("Desconhecido", payload);
        }

        String nome = payload.substring(0, idx).trim();
        String texto = payload.substring(idx + 1);

        return new ChatMensagem(nome, texto);
    }
}
