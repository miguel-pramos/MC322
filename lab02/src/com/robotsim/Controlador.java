package com.robotsim;

import com.robotsim.environment.Ambiente;
import com.robotsim.robots.Robo;

enum GAME_STATUS {
    GAMEON,
    GAMEOVER
}

public class Controlador {
    public static final int deltaTime = 1; // Tempo arbitrário de execução em segundos
    public static GAME_STATUS gameStatus = GAME_STATUS.GAMEON;

    private static Ambiente ambiente;

    public static void main(String[] args) {
        final int COMPRIMENTO = 1920; // Pixels
        final int LARGURA = 1080; // Pixels

        ambiente = new Ambiente(COMPRIMENTO, LARGURA);
        ambiente.adicionarRobo(new Robo("Robson", COMPRIMENTO / 2, LARGURA / 2));

        
        while (gameStatus != GAME_STATUS.GAMEOVER) {
            // TODO: tudo
        }

        System.out.println("============= GAME OVER =============");

    }
}