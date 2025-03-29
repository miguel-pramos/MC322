package com.robotsim.robots;

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
        double distancia = GeometryMath.distanciaEuclidiana(deltaX, deltaY);
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
        public void executar(Object... args) {
            if (args.length != 1 || !(args[0] instanceof Integer && args[1] instanceof Integer)) {
                throw new IllegalArgumentException("Mover requer dois inteiros."); // Estudar necessidade disso
            }
            int deltaX = (int) args[0];
            int deltaY = (int) args[1];
            robo.mover(deltaX, deltaY);
        }
    }
}

