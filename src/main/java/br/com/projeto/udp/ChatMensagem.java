package br.com.projeto.udp;

/**
 * Representa uma mensagem simples do Chat UDP.
 * Contém o nome do remetente e o texto enviado.
 */
public class ChatMensagem {
    private final String nomeRemetente;
    private final String texto;

    public ChatMensagem(String nomeRemetente, String texto) {
        this.nomeRemetente = nomeRemetente == null || nomeRemetente.isBlank() ? "Anônimo" : nomeRemetente;
        this.texto = texto == null ? "" : texto;
    }

    public String getNomeRemetente() {
        return nomeRemetente;
    }

    public String getTexto() {
        return texto;
    }
}
