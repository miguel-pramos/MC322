package com.robotsim.robots;

public class Robo {
    private String nome;
    private int HP;
    private int posicaoX;
    private int posicaoY;

    public String getNome() {
        return nome;
    }
    
    public int getPosicaoY() {
        return posicaoY;
    }

    public int getPosicaoX() {
        return posicaoX;
    }

    public Robo(String nome, int posicaoX, int posicaoY) {
        this.nome = nome;
        this.posicaoX = posicaoX;
        this.posicaoY = posicaoY;
    }

    public void mover(int deltaX, int deltaY) {
        this.posicaoX += deltaX;
        this.posicaoY += deltaY;
    }

    public void exibirPosicao() {
        System.out.println(nome + " está na posição (" + this.posicaoX + ", " + this.posicaoY + ")");
    }
}
