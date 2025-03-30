package com.robotsim.util;

import com.robotsim.robots.Robo;
import com.robotsim.robots.RoboAereo;

public class GeometryMath {

    public static double distanciaEuclidiana(Robo robo, int x, int y) {
        int xRobo = robo.getPosicaoX();
        int yRobo = robo.getPosicaoY();

        return Math.sqrt(Math.pow(x - xRobo, 2) + Math.pow(y - yRobo, 2));
    }

    public static double distanciaEuclidiana(RoboAereo robo, int x, int y, int z) {
        int xRobo = robo.getPosicaoX();
        int yRobo = robo.getPosicaoY();
        int zRobo = robo.getAltitude();
        return Math.sqrt(Math.pow(x - xRobo, 2) + Math.pow(y - yRobo, 2) + Math.pow(z - zRobo, 2));
    }
}
