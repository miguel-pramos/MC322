package com.robotsim.environment;

import java.util.ArrayList;

import com.robotsim.robots.Robo;

public class Ambiente {
    private int comprimento;
    private int largura;
    private int altura;
    private ArrayList<Robo> robos = new ArrayList<>();

    public Ambiente(int comprimento, int largura) {
        this.comprimento = comprimento;
        this.largura = largura;
    }

    public boolean dentroDosLimites(int x, int y) {
        return (x < this.comprimento && y < this.largura)
                && (x >= 0 && y >= 0);
    }

    public boolean dentroDosLimites(int x, int y, int z) {
        return (x < this.comprimento && y < this.largura && z < this.altura)
                && (x >= 0 && y >= 0 && z >= 0);
    }

    public void adicionarRobo(Robo robo) {
        this.robos.add(robo);
    }

    public int getLargura() {
        return largura;
    }

    public int getComprimento() {
        return comprimento;
    }

    public boolean matarRobo(Robo robo) {
        System.out.printf("O robo %s foi morto\n", robo.getNome());
        return this.robos.remove(robo);
    }

    public ArrayList<Robo> getRobos() {
        return robos;
    }

}
