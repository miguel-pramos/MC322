package com.robotsim.exceptions;

/**
 * Exceção lançada quando ocorre uma colisão entre entidades no ambiente de
 * simulação.
 * <p>
 * Esta exceção pode ser usada para indicar que um robô ou outra entidade
 * tentou se mover para uma posição já ocupada ou que resultaria em uma colisão.
 *
 * @see java.lang.Exception
 */
public class ColisaoException extends Exception {

    /**
     * Constrói uma nova {@code ColisaoException} com a mensagem padrão "Houve uma
     * colisão".
     */
    public ColisaoException() {
        super("Houve uma colisão"); // Mensagem padrão para a exceção
    }

    /**
     * Constrói uma nova {@code ColisaoException} com a mensagem detalhada
     * especificada.
     *
     * @param message A mensagem detalhada que descreve a colisão.
     */
    public ColisaoException(String message) {
        super(message); // Passa a mensagem para o construtor da superclasse Exception
    }

    /**
     * Constrói uma nova {@code ColisaoException} com a mensagem detalhada e a causa
     * especificadas.
     *
     * @param message A mensagem detalhada que descreve a colisão.
     * @param cause   A causa da exceção (outra exceção que levou a esta).
     */
    public ColisaoException(String message, Throwable cause) {
        super(message, cause); // Passa a mensagem e a causa para o construtor da superclasse
    }

    /**
     * Constrói uma nova {@code ColisaoException} com a causa especificada.
     *
     * @param cause A causa da exceção (outra exceção que levou a esta).
     */
    public ColisaoException(Throwable cause) {
        super(cause); // Passa a causa para o construtor da superclasse
    }
}
