package com.robotsim.robots;

import com.robotsim.Controlador;
import com.robotsim.util.GeometryMath;
import com.robotsim.util.Posicao;

public class RoboDrone extends RoboAereo{
    private int bateria = 200;
    private final int alcanceDeteccao = 50;

    public RoboDrone(String nome, int posicaoX, int posicaoY, int posicaoZ, int HP, int altitudeMaxima) {
        super(nome, posicaoX, posicaoY, posicaoZ, 10, 50);
    }

    public void mover(int deltaX, int deltaY, int deltaZ) {
        bateria -= deltaX + deltaY + deltaZ;

        if (bateria <= 0) {
            Controlador.ambiente.matarRobo(this);
        }
        else{
            this.posicaoX += deltaX;
            this.posicaoY += deltaY;
            this.posicaoZ += deltaZ;

            System.out.println("Sua bateria está em " + bateria);
        }
    }

    public void detectarRobo(RoboAereo alvo){
        if (GeometryMath.distanciaEuclidiana(alvo.getPosicaoX(), getPosicaoY(), getPosicaoZ()) < alcanceDeteccao){
            System.out.println("O robo " + alvo.getNome() + " está na posição:");
            System.out.println("(" + alvo.getPosicaoX() + "," + alvo.getPosicaoY() + "," + alvo.getPosicaoZ() + ")");
        }
    }

    public void detectarRobo(RoboTerrestre alvo){
        if (GeometryMath.distanciaEuclidiana(alvo.getPosicaoX(), getPosicaoY(), 0) < alcanceDeteccao){
            System.out.println("O robo " + alvo.getNome() + " está na posição:");
            System.out.println("(" + alvo.getPosicaoX() + "," + alvo.getPosicaoY() + ",0)");
        }
    }
}