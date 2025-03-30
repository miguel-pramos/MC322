package com.robotsim.robots;

import java.util.Scanner;

import com.robotsim.Controlador;
import com.robotsim.etc.Acao;
import com.robotsim.util.GeometryMath;

public abstract class RoboTerrestre extends Robo {
    protected int velocidadeMaxima; // Pixels por segundo

    public RoboTerrestre(String nome, int posicaoX, int posicaoY) {
        super(nome, posicaoX, posicaoY);
    }

    @Override
    protected void inicializarAcoes() {
        acoes.add(new Mover(this));
    }

    @Override
    protected void mover(int deltaX, int deltaY) {
        double distancia = GeometryMath.distanciaEuclidiana(this, deltaX, deltaY);
        if (distancia / Controlador.deltaTime > this.velocidadeMaxima) {
            return; // TODO: implementar interação
        }
        super.mover(deltaX, deltaY);
    }

    private class Mover implements Acao {
        RoboTerrestre robo;

        public Mover(RoboTerrestre robo) {
            this.robo = robo;
        }

        @Override
        public String getNome() {
            return "Mover";
        }

        @Override
        public void executar() {
            Scanner scanner = Controlador.getScanner();

            System.out.print("O quento quer andar no eixo X?");
            int deltaX = scanner.nextInt();
            scanner.nextLine(); // Consumir \n

            System.out.print("O quento quer andar no eixo Y?");
            int deltaY = scanner.nextInt();
            scanner.nextLine(); // Consumir \n

            robo.mover(deltaX, deltaY);
        }
    }
}
