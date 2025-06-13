package com.robotsim.robots.intelligent.types;

import com.robotsim.environment.Ambiente;

public class RoboExplorador  extends AgenteInteligente {
    @Override
    public void executarMissao(Ambiente a) {
        if (temMissao()) {
            System.out.println("Executando missão exploratória...");
            missao.executar(this, a); // Polimorfismo em ação!
        }
    }
}