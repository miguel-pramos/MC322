package com.robotsim.robots;

import com.robotsim.util.GeometryMath;

public class RoboAntiAereo extends RoboTerrestre {
    private int balasRestantes = 10;
    private int dano = 4;
    private int alcance = 200;
    
    public RoboAntiAereo(String nome, int posicaoX, int posicaoY) {
        super(nome, posicaoX, posicaoY, 0); // Robo antiaéreo é fixo;
    }

    public void atirar(RoboAereo alvo) {
        if (GeometryMath.distanciaEuclidiana(alvo.getPosicaoX(), alvo.getPosicaoY()) < this.alcance) 
            alvo.tomarDano(this.dano);   
    }
    
}
