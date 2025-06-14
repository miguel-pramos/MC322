package com.robotsim.robots.intelligent;
import com.robotsim.environment.Ambiente;
import com.robotsim.missions.Missao;
import com.robotsim.robots.Robo;

public abstract class AgenteInteligente extends Robo {
    protected Missao missao;

    public AgenteInteligente(String nome, int posicaoX, int posicaoY, int HP) {
        super(nome, posicaoX, posicaoY, HP);
        this.missao = null; // Inicialmente sem missão
    }

    public void definirMissao(Missao m) {
        this.missao = m;
    }

    public boolean temMissao() {
        return missao != null;
    }

    public abstract void executarMissao(Ambiente a);
}