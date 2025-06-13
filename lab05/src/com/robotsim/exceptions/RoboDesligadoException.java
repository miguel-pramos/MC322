package com.robotsim.exceptions;

/**
 * Exceção lançada quando uma operação é tentada em um robô que está desligado.
 * <p>
 * Esta exceção indica que o robô precisa ser ligado antes que a operação
 * solicitada
 * possa ser executada.
 *
 * @see java.lang.Exception
 */
public class RoboDesligadoException extends Exception {

    /**
     * Constrói uma nova {@code RoboDesligadoException} com a mensagem padrão "O
     * robô está desligado.".
     */
    public RoboDesligadoException() {
        super("O robô está desligado."); // Mensagem padrão para a exceção
    }

    /**
     * Constrói uma nova {@code RoboDesligadoException} com a mensagem detalhada
     * especificada.
     *
     * @param message A mensagem detalhada que descreve a situação.
     */
    public RoboDesligadoException(String message) {
        super(message); // Passa a mensagem para o construtor da superclasse Exception
    }
}
