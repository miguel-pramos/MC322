package com.robotsim.robots;

public class RoboTanque extends RoboTerrestre {
    private int balasRestantes = 10;
    

    public RoboTanque(String nome, int posicaoX, int posicaoY, int velocidadeMaxima) {
        super(nome, posicaoX, posicaoY, velocidadeMaxima);
    }

    public void atirar(RoboTerrestre alvo) {
        alvo.tomarDano(balasRestantes);
    }
    
}
