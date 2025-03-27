package com.robotsim.robots;

public class RoboAereo extends Robo {
    protected int posicaoZ;
    protected int posicaoZMaxima;

    public RoboAereo(String nome, int posicaoX, int posicaoY, int HP, int posicaoZ, int posicaoZMaxima) {
        super(nome, posicaoX, posicaoY, HP);

        this.posicaoZ = posicaoZ;
        this.posicaoZMaxima = posicaoZMaxima;
    }

    public void subir(int posicaoZ, int deltaZ){
        if(posicaoZ + deltaZ < posicaoZMaxima){
            posicaoZ += deltaZ;
        }
        else {
            posicaoZ = posicaoZMaxima;
        }
    }

    public void descer(int posicaoZ, int deltaZ){
        if(posicaoZ - deltaZ > 0){
            posicaoZ -= deltaZ;
        }
        else {
            posicaoZ = 0;
        }
    }

    public int getPosicaoZ() { return(posicaoZ);}


}
