package com.robotsim.environment.entity;

/**
 * Enumeração que representa os diferentes tipos de entidades que podem existir
 * no ambiente da simulação.
 * <p>
 * Cada valor do enum define uma categoria específica de entidade, permitindo a
 * diferenciação
 * e o tratamento específico para cada tipo no sistema de simulação.
 * </p>
 */
public enum TipoEntidade {
    /** Representa um espaço vazio ou não ocupado no ambiente. */
    VAZIO,
    /** Representa um robô. */
    ROBO,
    /** Representa um obstáculo físico no ambiente. */
    OBSTACULO,
    /** Representa um tipo de entidade desconhecido ou não especificado. */
    DESCONHECIDO
}
