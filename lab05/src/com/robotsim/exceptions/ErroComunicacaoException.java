package com.robotsim.exceptions;

/**
 * Exceção lançada quando ocorre um erro durante a comunicação entre entidades
 * ou com a central de comunicação no ambiente de simulação.
 * <p>
 * Esta exceção pode indicar problemas como falha ao enviar ou receber
 * mensagens,
 * perda de conexão, ou formato de mensagem inválido.
 *
 * @see java.lang.Exception
 */
public class ErroComunicacaoException extends Exception {

    /**
     * Constrói uma nova {@code ErroComunicacaoException} com a mensagem padrão
     * "Ocorreu um erro na comunicação".
     */
    public ErroComunicacaoException() {
        super("Ocorreu um erro na comunicação"); // Mensagem padrão para a exceção
    }

    /**
     * Constrói uma nova {@code ErroComunicacaoException} com a mensagem detalhada
     * especificada.
     *
     * @param message A mensagem detalhada que descreve o erro de comunicação.
     */
    public ErroComunicacaoException(String message) {
        super(message); // Passa a mensagem para o construtor da superclasse Exception
    }

    /**
     * Constrói uma nova {@code ErroComunicacaoException} com a mensagem detalhada e
     * a causa especificadas.
     *
     * @param message A mensagem detalhada que descreve o erro de comunicação.
     * @param cause   A causa da exceção (outra exceção que levou a esta).
     */
    public ErroComunicacaoException(String message, Throwable cause) {
        super(message, cause); // Passa a mensagem e a causa para o construtor da superclasse
    }

    /**
     * Constrói uma nova {@code ErroComunicacaoException} com a causa especificada.
     *
     * @param cause A causa da exceção (outra exceção que levou a esta).
     */
    public ErroComunicacaoException(Throwable cause) {
        super(cause); // Passa a causa para o construtor da superclasse
    }
}
