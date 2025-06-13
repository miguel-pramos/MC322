package com.robotsim.communication;

import java.util.ArrayList;

/**
 * A classe {@code CentralComunicacao} é responsável por armazenar e exibir
 * mensagens
 * trocadas entre entidades no simulador.
 * 
 * <p>
 * Ela mantém uma lista de objetos {@link Mensagem}.
 * </p>
 * 
 * <p>
 * Esta classe é final, o que significa que não pode ser estendida.
 * </p>
 */
public final class CentralComunicacao {
    // Lista para armazenar os objetos Mensagem registrados.
    private ArrayList<Mensagem> mensagens = new ArrayList<>();

    /**
     * Registra uma nova mensagem na central de comunicação.
     * A mensagem é encapsulada em um objeto {@link Mensagem} e adicionada
     * ao final da lista de mensagens existentes.
     *
     * @param remetente O identificador do remetente da mensagem.
     * @param conteudo  O conteúdo da mensagem a ser registrada.
     */
    public void registrarMensagem(String remetente, String conteudo) {
        // Cria um novo objeto Mensagem e o adiciona à lista.
        this.mensagens.add(new Mensagem(remetente, conteudo));
    }

    /**
     * Exibe todas as mensagens registradas no console.
     * Cada mensagem é formatada usando o método toString() da classe
     * {@link Mensagem}
     * e impressa em uma nova linha.
     */
    public void exibirMensagens() {
        // Itera sobre la lista de mensagens e imprime cada uma usando seu método
        // toString().
        for (Mensagem mensagem : this.mensagens)
            System.out.println(mensagem.toString());
    }
}
