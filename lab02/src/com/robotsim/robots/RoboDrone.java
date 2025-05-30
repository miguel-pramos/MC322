package com.robotsim.robots;

import java.util.concurrent.TimeUnit;

import com.robotsim.Controlador;
import com.robotsim.etc.Acao;
import com.robotsim.robots.terrestrials.RoboTerrestre;
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
    private final int alcanceDeteccao = 30;

    public RoboDrone(String nome, int posicaoX, int posicaoY) {
        super(nome, posicaoX, posicaoY, 50);
    }

    /**
     * Método responsável por detectar outros robôs no ambiente.
     * <p>
     * Detecta robôs terrestres e aéreos dentro do alcance de detecção e exibe a
     * posição dos robôs detectados.
     * <p>
     * Ignora a si mesmo durante o processo de detecção.
     */
    protected void detectarRobos() {
        System.out.println("Iniciando o processo de detecção...");

        try {
            TimeUnit.MILLISECONDS.sleep(1600); // Simula o tempo necessário para detecção.
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Boa prática: reinterromper a thread.
            System.out.println("A execução foi interrompida.");
        }

        double distancia;
        for (Robo robo : Controlador.getAmbiente().getRobos()) {
            if (robo instanceof RoboAereo && robo != this) {
                // Calcula a distância considerando a altitude.
                distancia = GeometryMath.distanciaEuclidiana(this, robo.getX(),
                        robo.getY(), ((RoboAereo) robo).getAltitude());

                if (distancia < alcanceDeteccao) {
                    System.out.printf("O robô %s está na posição (%d, %d, %d)%n", robo.getNome(),
                            robo.getX(), robo.getY(), ((RoboAereo) robo).getAltitude());
                }
            } else if (robo instanceof RoboTerrestre) {
                // Calcula a distância considerando altitude zero para robôs terrestres.
                distancia = GeometryMath.distanciaEuclidiana(this, robo.getX(),
                        robo.getY(), 0);

                if (distancia < alcanceDeteccao) {
                    System.out.printf("O robô %s está na posição (%d, %d, %d)%n", robo.getNome(),
                            robo.getX(), robo.getY(), 0);
                }
            } else {
                System.out.println("Tipo desconhecido");
            }
        }
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

    @Override
    protected void inicializarAcoes() {
        acoes.add(new DetectarRobos(this));
        super.inicializarAcoes();
    }

    /**
     * Classe interna que representa a ação de detectar robôs de um RoboDrone.
     * Implementa a interface Acao, permitindo que o robô execute a ação de detecção
     * no ambiente.
     */
    private class DetectarRobos implements Acao {
        RoboDrone robo;

        public DetectarRobos(RoboDrone robo) {
            this.robo = robo;
        }

        @Override
        public String getNome() {
            return "Detectar robôs";
        }

        /**
         * Método que executa a ação de detecção.
         * Chama o método `detectarRobos` do RoboDrone.
         */
        @Override
        public void executar() {
            robo.detectarRobos();
        }
    }
}