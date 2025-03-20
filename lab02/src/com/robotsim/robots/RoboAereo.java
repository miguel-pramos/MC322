package com.robotsim.robots;

public class RoboAereo extends Robo {
    private int altitude;
    private int altitudeMaxima;

    public RoboAereo(String nome, int posicaoX, int posicaoY, int altitude, int HP, int altitudeMaxima) {
        super(nome, posicaoX, posicaoY, HP);

        this.altitude = altitude;
        this.altitudeMaxima = altitudeMaxima;

    }


}
