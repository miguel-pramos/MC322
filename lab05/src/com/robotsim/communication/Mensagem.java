package com.robotsim.communication;

/**
 * Representa uma mensagem com remetente e conteúdo.
 */
public class Mensagem {
    private String remetente;
    private String conteudo;

    /**
     * Cria uma nova instância de Mensagem.
     *
     * @param remetente O nome do remetente da mensagem.
     * @param conteudo  O conteúdo da mensagem.
     */
    public Mensagem(String remetente, String conteudo) {
        this.remetente = remetente;
        this.conteudo = conteudo;
    }

    /**
     * Retorna o remetente da mensagem.
     *
     * @return O nome do remetente.
     */
    public String getRemetente() {
        return remetente;
    }

    /**
     * Retorna o conteúdo da mensagem.
     *
     * @return O conteúdo da mensagem.
     */
    public String getConteudo() {
        return conteudo;
    }

    /**
     * Retorna uma representação em string da mensagem, formatada como "Remetente:
     * Conteúdo".
     *
     * @return A string formatada da mensagem.
     */
    @Override
    public String toString() {
        return String.format("%s: %s", remetente, conteudo);
    }
}
