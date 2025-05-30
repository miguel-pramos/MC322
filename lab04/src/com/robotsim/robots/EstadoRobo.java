package com.robotsim.robots;

/**
 * Enumeração {@code EstadoRobo} representa os possíveis estados operacionais de
 * um robô.
 * Um robô pode estar {@link #LIGADO} ou {@link #DESLIGADO}.
 */
public enum EstadoRobo {
    /**
     * O robô está ligado e operacional.
     */
    LIGADO,
    /**
     * O robô está desligado e não pode executar ações.
     */
    DESLIGADO;
}