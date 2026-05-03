package br.com.projeto;

/**
 * Classe que representa uma pergunta do quiz com suas opções de resposta.
 * 
 * @author Desenvolvedor
 * @version 1.0
 */
public class Pergunta {
    private String enunciado;
    private String[] opcoes;
    private int respostaCorreta; // índice da resposta correta (0-3)

    /**
     * Construtor da classe Pergunta.
     *
     * @param enunciado o texto da pergunta
     * @param opcoes array com as 4 opções de resposta
     * @param respostaCorreta índice da resposta correta (0-3)
     */
    public Pergunta(String enunciado, String[] opcoes, int respostaCorreta) {
        this.enunciado = enunciado;
        this.opcoes = opcoes;
        this.respostaCorreta = respostaCorreta;
    }

    /**
     * Retorna o enunciado da pergunta.
     *
     * @return o enunciado
     */
    public String getEnunciado() {
        return enunciado;
    }

    /**
     * Retorna as opções de resposta.
     *
     * @return array com as opções
     */
    public String[] getOpcoes() {
        return opcoes;
    }

    /**
     * Retorna o índice da resposta correta.
     *
     * @return índice da resposta correta (0-3)
     */
    public int getRespostaCorreta() {
        return respostaCorreta;
    }

    /**
     * Verifica se a resposta fornecida está correta.
     *
     * @param resposta o índice da resposta fornecida
     * @return true se a resposta estiver correta, false caso contrário
     */
    public boolean estaCorreta(int resposta) {
        return resposta == respostaCorreta;
    }

    /**
     * Retorna uma representação em string da pergunta formatada.
     *
     * @return string formatada da pergunta
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(enunciado).append("\n");
        for (int i = 0; i < opcoes.length; i++) {
            sb.append((i + 1)).append(". ").append(opcoes[i]).append("\n");
        }
        return sb.toString();
    }
}
