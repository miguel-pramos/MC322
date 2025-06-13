package com.robotsim.robots.abilities;

/**
 * Interface para robôs com capacidade de operar de forma autônoma.
 */
public interface Autonomo {
    /**
     * Ativa ou desativa o modo de operação autônoma do robô.
     * @param ativar true para ativar, false para desativar.
     */
    void setModoAutonomo(boolean ativar);

    /**
     * Verifica se o robô está atualmente operando em modo autônomo.
     * @return true se o modo autônomo estiver ativo, false caso contrário.
     */
    boolean isAutonomo();
}
