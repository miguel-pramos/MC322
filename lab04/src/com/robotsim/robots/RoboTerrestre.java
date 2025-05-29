package com.robotsim.robots;

import java.util.Scanner;

import com.robotsim.Controlador;
import com.robotsim.etc.Acao;
import com.robotsim.util.GeometryMath;

/**
 * A classe RoboTerrestre representa um robô terrestre genérico que se move em
 * duas dimensões.
 * Robôs terrestres possuem velocidade máxima e podem ser danificados por
 * ataques.
 *
 * @see Robo
 */
public abstract class RoboTerrestre extends Robo {
    protected int velocidadeMaxima; // Pixels por segundo

    public RoboTerrestre(String nome, int posicaoX, int posicaoY, int HP) {
        super(nome, posicaoX, posicaoY, HP);
    }

    @Override
    protected void inicializarAcoes() {
        acoes.add(new Mover(this));
    }

    /**
     * Método responsável por mover o RoboTerrestre.
     * <p>
     * O robô não pode se mover além de sua velocidade máxima.
     * Caso o deslocamento exceda a velocidade máxima, ele será ajustado para o
     * limite permitido.
     *
     * @param deltaX Deslocamento no eixo X.
     * @param deltaY Deslocamento no eixo Y.
     */
    @Override
    protected void mover(int deltaX, int deltaY) {
        double distancia = GeometryMath.distanciaEuclidiana(this, this.x + deltaX, this.y + deltaY);
        if (distancia / Controlador.DELTA_TIME > this.velocidadeMaxima) {
            System.out.printf("%s tentou se mover rápido demais!", this.nome);
            return;
        }
        super.mover(deltaX, deltaY);
    }

    /**
     * Classe interna que representa a ação de mover o RoboTerrestre.
     */
    private class Mover implements Acao {
        RoboTerrestre robo;

        public Mover(RoboTerrestre robo) {
            this.robo = robo;
        }

        @Override
        public String getNome() {
            return "Mover";
        }

        /**
         * Executa a ação de mover o RoboTerrestre.
         * Solicita ao usuário os deslocamentos nos eixos X e Y.
         */
        @Override
        public void executar() {
            Scanner scanner = Controlador.getScanner();

            System.out.print("O quanto quer andar no eixo X? ");
            int deltaX = scanner.nextInt();
            scanner.nextLine(); // Consumir \n

            System.out.print("O quanto quer andar no eixo Y? ");
            int deltaY = scanner.nextInt();
            scanner.nextLine(); // Consumir \n

            robo.mover(deltaX, deltaY);
        }
    }
}
