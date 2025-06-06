package com.robotsim.util;

import com.robotsim.robots.Robo;
import com.robotsim.robots.aerials.RoboAereo;

/**
 * A classe GeometryMath fornece métodos utilitários para cálculos geométricos
 * relacionados à posição de robôs em um plano bidimensional ou tridimensional.
 *
 * <p>
 * Esta classe é utilitária e não deve ser instanciada.
 * </p>
 */
public final class GeometryMath {

    /**
     * Calcula a distância euclidiana entre a posição atual de um robô e um ponto
     * especificado pelas coordenadas (x, y).
     *
     * @param robo O objeto Robo cuja posição será usada no cálculo.
     * @param x    A coordenada x do ponto de comparação.
     * @param y    A coordenada y do ponto de comparação.
     * @return A distância euclidiana entre a posição do robô e o ponto
     *         especificado.
     */
    public static double distanciaEuclidiana(Robo robo, int x, int y) {
        int xRobo = robo.getX();
        int yRobo = robo.getY();

        return Math.sqrt(Math.pow(x - xRobo, 2) + Math.pow(y - yRobo, 2));
    }

    /**
     * Calcula a distância euclidiana entre um robô aéreo e um ponto tridimensional.
     * Este método considera a coordenada Z (altitude) para robôs aéreos.
     *
     * @param robo O objeto RoboAereo cuja posição será usada no cálculo.
     * @param x    A coordenada X do ponto de comparação.
     * @param y    A coordenada Y do ponto de comparação.
     * @param z    A coordenada Z (altitude) do ponto de comparação.
     * @return A distância euclidiana entre o robô e o ponto especificado.
     */
    public static double distanciaEuclidiana(Robo robo, int x, int y, int z) {
        int xRobo = robo.getX();
        int yRobo = robo.getY();
        int zRobo = 0;
        if (robo instanceof RoboAereo)
            zRobo = ((RoboAereo) robo).getZ();

        return Math.sqrt(Math.pow(x - xRobo, 2) + Math.pow(y - yRobo, 2) + Math.pow(z - zRobo, 2));
    }

}
