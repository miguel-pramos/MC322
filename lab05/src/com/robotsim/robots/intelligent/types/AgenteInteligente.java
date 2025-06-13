package com.robotsim.robots.intelligent.types;
import com.robotsim.environment.Ambiente;
import com.robotsim.missions.Missao;
import com.robotsim.robots.Robo;

public abstract class AgenteInteligente extends Robo {
    protected Missao missao;

    public void definirMissao(Missao m) {
        this.missao = m;
    }

    public boolean temMissao() {
        return missao != null;
    }

    public abstract void executarMissao(Ambiente a);
}