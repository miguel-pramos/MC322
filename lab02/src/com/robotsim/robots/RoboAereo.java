package com.robotsim.robots;

public class RoboAereo extends Robo {
    private int altitude;
    private int altitudeMaxima;

    public RoboAereo(String nome, int posicaoX, int posicaoY, int altitude, int altitudeMaxima) {
        super(nome, posicaoX, posicaoY);
        
        this.altitude = altitude;
        this.altitudeMaxima = altitudeMaxima;
        
    }

}
