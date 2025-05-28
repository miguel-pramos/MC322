package com.robotsim.robots.sensors;

import com.robotsim.etc.Acao;
import com.robotsim.robots.Robo;

/**
 * A classe Sensor é uma abstração para sensores utilizados por robôs no simulador.
 * Cada sensor possui um raio de alcance e está associado a um robô.
 * Além disso, cada sensor pode executar uma ação específica definida pela interface {@link Acao}.
 *
 * <p>
 * Esta classe deve ser herdada para criar sensores específicos, como sensores de robôs ou de obstáculos.
 */
public abstract class Sensor {
    private final double raioDeAlcance;
    private Acao acao;
    private Robo robo;

    public Sensor(double raioDeAlcance, Robo robo) {
        this.raioDeAlcance = raioDeAlcance;
        this.robo = robo;
    }

    public Robo getRobo() {
        return robo;
    }

    public Acao getAcao() {
        return acao;
    }

    public void setAcao(Acao acao) {
        this.acao = acao;
    }

    public double getRaioDeAlcance() {
        return raioDeAlcance;
    }
}