package com.robotsim.robots;

public class RoboTanque extends RoboTerrestre {
    private int balasRestantes = 10;
    private int alcance = 300;
    private int dano = 8;
    

    public RoboTanque(String nome, int posicaoX, int posicaoY) {
        final int HP = 20;
        final int velocidadeMaxima = 50;
        super(nome, posicaoX, posicaoY, HP, velocidadeMaxima);
    }

    public void atirar(RoboTerrestre alvo) {
        alvo.tomarDano(dano);
        this.balasRestantes--;
    }
    
}
