package com.robotsim.robots.abilities;

import com.robotsim.environment.entity.Entidade;

/**
 * Interface para robôs com capacidade de ataque.
 */
public interface Atacante {
    /**
     * Executa um ataque do robô contra o alvo especificado.
     * 
     * @param alvo       A entidade a ser atacada.
     * @param tipoAtaque O tipo de ataque a ser executado (PRIMARIO ou SECUNDARIO).
     */
    void executarAtaque(Entidade alvo);

    /**
     * Verifica se o robô pode atacar um determinado tipo de alvo.
     * 
     * @param alvo A entidade alvo.
     * @return true se o robô pode atacar o alvo, false caso contrário.
     */
    boolean podeAtacar(Entidade alvo);
}
