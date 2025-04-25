package com.robotsim.robots.sensors;

import com.robotsim.etc.Acao;
import com.robotsim.robots.Robo;

public abstract class Sensor {
    private double raioDeAlcance;
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