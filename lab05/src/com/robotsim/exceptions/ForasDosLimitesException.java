package com.robotsim.exceptions;

/**
 * Exceção lançada quando uma entidade tenta se mover ou realizar uma ação
 * fora dos limites definidos do ambiente de simulação.
 * <p>
 * Esta exceção indica que a posição ou o alvo desejado está além das fronteiras
 * permitidas do mapa ou área de operação.
 *
 * @see java.lang.Exception
 */
public class ForasDosLimitesException extends Exception {

    /**
     * Constrói uma nova {@code ForasDosLimitesException} com a mensagem padrão "A
     * ação resultaria em sair dos limites do ambiente.".
     * Note: A mensagem original "Houve uma colisão" parecia incorreta para esta
     * exceção e foi alterada.
     */
    public ForasDosLimitesException() {
        super("A ação resultaria em sair dos limites do ambiente."); // Mensagem padrão para a exceção
    }

    /**
     * Constrói uma nova {@code ForasDosLimitesException} com a mensagem detalhada
     * especificada.
     *
     * @param message A mensagem detalhada que descreve a tentativa de sair dos
     *                limites.
     */
    public ForasDosLimitesException(String message) {
        super(message); // Passa a mensagem para o construtor da superclasse Exception
    }

    /**
     * Constrói uma nova {@code ForasDosLimitesException} com a mensagem detalhada e
     * a causa especificadas.
     *
     * @param message A mensagem detalhada que descreve a tentativa de sair dos
     *                limites.
     * @param cause   A causa da exceção (outra exceção que levou a esta).
     */
    public ForasDosLimitesException(String message, Throwable cause) {
        super(message, cause); // Passa a mensagem e a causa para o construtor da superclasse
    }

    /**
     * Constrói uma nova {@code ForasDosLimitesException} com a causa especificada.
     *
     * @param cause A causa da exceção (outra exceção que levou a esta).
     */
    public ForasDosLimitesException(Throwable cause) {
        super(cause); // Passa a causa para o construtor da superclasse
    }
}
