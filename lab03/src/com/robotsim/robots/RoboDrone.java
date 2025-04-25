package com.robotsim.robots;

import java.util.concurrent.TimeUnit;

import com.robotsim.Controlador;
import com.robotsim.etc.Acao;
import com.robotsim.robots.sensors.SensorObstaculo;
import com.robotsim.robots.sensors.SensorRobo;
import com.robotsim.util.GeometryMath;

/**
 * A classe RoboDrone representa um robô aéreo com a capacidade de detectar
 * outros robôs
 * no ambiente. Este robô possui uma bateria limitada e um alcance específico
 * para detecção.
 *
 * @see RoboAereo
 */
public class RoboDrone extends RoboAereo {
    private int bateria = 50;

    public RoboDrone(String nome, int posicaoX, int posicaoY) {
        super(nome, posicaoX, posicaoY, 50, 10, 30);
        this.sensores.add(new SensorRobo(30, this));
        this.sensores.add(new SensorObstaculo(30, this));
        this.inicializarSensores();
    }


    /**
     * Método responsável por mover o RoboDrone.
     * <p>
     * A cada movimento, a bateria é reduzida proporcionalmente ao deslocamento.
     * Caso a bateria chegue a zero ou menos, o robô é removido do ambiente.
     *
     * @param deltaX Deslocamento no eixo X.
     * @param deltaY Deslocamento no eixo Y.
     */
    protected void mover(int deltaX, int deltaY) {
        this.bateria -= deltaX + deltaY;

        if (this.bateria <= 0) {
            Controlador.getAmbiente().destruirRobo(this); // Remove o robô do ambiente.
        } else {
            super.mover(deltaX, deltaY); // Chama o método da superclasse para movimentação.
            System.out.printf("Sua bateria está em %d%n", this.bateria);
        }
    }

    
}