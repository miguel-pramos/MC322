package com.robotsim.robots;

import com.robotsim.Controlador;
import com.robotsim.util.GeometryMath;

public class RoboTanque extends RoboTerrestre {
    private int balasRestantes = 10;
    private int dano = 8;
    private int alcance = Controlador.ambiente.getLargura() / 6;
    

    public RoboTanque(String nome, int posicaoX, int posicaoY) {
        super(nome, posicaoX, posicaoY, 20, 50);
        
    }

    public void atirar(RoboTerrestre alvo) {
        if (GeometryMath.distanciaEuclidiana(alvo.getPosicaoX(), alvo.getPosicaoY()) < this.alcance) 
            alvo.tomarDano(this.dano);
        this.balasRestantes--;
    }
    
}
