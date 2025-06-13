package com.robotsim.missions.missionTypes;

import com.robotsim.robots.Robo;
import com.robotsim.environment.Ambiente;

public class MissaoExplorar implements Missao {

    @Override
    public void executar(Robo robo, Ambiente ambiente) {
        System.out.println("Robô " + r.getId() + " está explorando...");
        // Adicione aqui a lógica de movimentação aleatória
        // ou outra lógica de exploração.
    }
}
