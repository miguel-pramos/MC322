package com.robotsim.robots;

import com.robotsim.Controlador;

public abstract class Robo {
    private String nome;
    private int HP;
    private int posicaoX;
    private int posicaoY;

    public Robo(String nome, int posicaoX, int posicaoY, int HP) {
        this.nome = nome;
        this.posicaoX = posicaoX;
        this.posicaoY = posicaoY;
        this.HP = HP;
    }

    public void mover(int deltaX, int deltaY) {
        this.posicaoX += deltaX;
        this.posicaoY += deltaY;
    }

    public void tomarDano(int dano) {
        this.HP -= dano;
        if (this.HP < 0) {
            Controlador.ambiente.matarRobo(this);
        }
    }

    public void exibirPosicao() {
        System.out.println(nome + " está na posição (" + this.posicaoX + ", " + this.posicaoY + ")");
    }

    public int getPosicaoX() {
        return posicaoX;
    }

    public int getPosicaoY() {
        return posicaoY;
    }
}
