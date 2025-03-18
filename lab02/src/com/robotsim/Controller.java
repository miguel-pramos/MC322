package com.robotsim;

import com.robotsim.environment.Ambiente;
import com.robotsim.robots.Robo;

public class Controller {
    public static final int deltaTime = 1; // Tempo arbitrário de execução em segundos

    public static void main(String[] args) {
        final int COMPRIMENTO = 1920; // Pixels
        final int LARGURA = 1080; // Pixels

        Ambiente ambiente = new Ambiente(COMPRIMENTO, LARGURA);
        Robo robson = new Robo("Robson", COMPRIMENTO/2, LARGURA/2); 

        int deltaX = Integer.parseInt(System.console().readLine("Digite o quanto " + robson.getNome() + " deve se mover no eixo X: "));
        int deltaY = Integer.parseInt(System.console().readLine("Digite o quanto " + robson.getNome() + " deve se mover no eixo Y: "));
        
        if (ambiente.dentroDosLimites(robson.getPosicaoX() + deltaX, robson.getPosicaoY() + deltaY)) {
            robson.mover(deltaX, deltaY);
            System.out.println(robson.getNome() + " se moveu com sucesso!");
        }
        else {
            System.out.println(robson.getNome() + " não pode se mover para fora dos limites do ambiente.");
        }

        robson.exibirPosicao();
    }
}