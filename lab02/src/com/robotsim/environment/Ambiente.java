package com.robotsim.environment;

import java.util.ArrayList;

import com.robotsim.robots.Robo;

public class Ambiente {
    private int largura;
    private int altura;
    private ArrayList<Robo> robos = new ArrayList<>();

    public Ambiente(int largura, int altura) {
        this.largura = largura;
        this.altura = altura;
    }

    public boolean dentroDosLimites(int x, int y) {
        return x < this.largura && y < this.altura;
    }

    public void adicionarRobo(Robo robo) {
        this.robos.add(robo);
    }

    public void matarRobo(Robo robo) {
        this.robos.remove(robo);
    }

}
