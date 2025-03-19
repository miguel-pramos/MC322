package com.robotsim.robots;

import com.robotsim.Controlador;
import com.robotsim.util.GeometryMath;

public class RoboTerrestre extends Robo {
    private int velocidadeMaxima; // Pixels por segundo

    public RoboTerrestre(String nome, int posicaoX, int posicaoY, int velocidadeMaxima) {
        this.velocidadeMaxima = velocidadeMaxima;
        super(nome, posicaoX, posicaoY);
    }

    public void mover(int deltaX, int deltaY) {
        double distancia = GeometryMath.distanciaEuclidiana(deltaX, deltaY);
        if (distancia / Controlador.deltaTime > this.velocidadeMaxima) {
            return; // TODO: implementar interação
        }
        super.mover(deltaX, deltaY);
    }
}
