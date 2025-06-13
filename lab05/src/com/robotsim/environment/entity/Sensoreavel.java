package com.robotsim.environment.entity;

import com.robotsim.exceptions.RoboDesligadoException;

/**
 * Interface que define o comportamento de entidades capazes de possuir e
 * adicionar sensores.
 * <p>
 * Entidades que implementam esta interface, tipicamente robôs, podem ter
 * sensores adicionados
 * a elas para interagir e perceber o ambiente de simulação.
 *
 * @see RoboDesligadoException
 */
public interface Sensoreavel {
    /**
     * Adiciona sensores à entidade.
     * <p>
     * A implementação deste método deve lidar com a lógica de inicialização e
     * configuração
     * dos sensores que a entidade irá utilizar.
     *
     * @throws RoboDesligadoException Se a entidade (robô) estiver desligada e,
     *                                portanto,
     *                                não puder ter sensores adicionados ou
     *                                ativados.
     */
    public void adicionarSensores() throws RoboDesligadoException;
}
