package com.robotsim.robots;

import java.util.Scanner;

import com.robotsim.Controlador;
import com.robotsim.etc.Acao;

/**
 * A classe RoboAereo representa um robô aéreo genérico que pode se mover em
 * três dimensões.
 * Robôs aéreos possuem altitude variável e podem subir ou descer dentro de um
 * limite máximo.
 * 
 * @see Robo
 */
public abstract class RoboAereo extends Robo {
    protected static int altitudeMaxima;
    protected int altitude;

    public RoboAereo(String nome, int posicaoX, int posicaoY, int HP) {
        super(nome, posicaoX, posicaoY, HP);
    }

    @Override
    protected void inicializarAcoes() {
        super.inicializarAcoes();
        acoes.add(new Subir(this));
        acoes.add(new Descer(this));
    }

    /**
     * Método que permite ao robô subir uma determinada altitude.
     * <p>
     * O robô não pode ultrapassar a altitude máxima. Caso o incremento ultrapasse o
     * limite, a altitude será ajustada para o valor máximo.
     * 
     * @param deltaZ Valor a ser incrementado na altitude.
     */
    public void subir(int deltaZ) {
        if (Controlador.getAmbiente()
                .dentroDosLimites(this.posicaoX, this.posicaoY,
                        this.altitude + deltaZ, altitudeMaxima)) {
            this.altitude += deltaZ;
        } else {
            this.altitude = altitudeMaxima;
            System.out.printf("\nVocê subiu... E subiu... Até atingir " +
                    "os limites das máquinas modernas e parou na altura " +
                    "máxima de %d", altitudeMaxima);
        }
    }

    /**
     * Método que permite ao robô descer uma determinada altitude.
     * <p>
     * O robô não pode descer abaixo de 0. Caso o decremento ultrapasse o limite, a
     * altitude será ajustada para 0.
     * 
     * @param deltaZ Valor a ser decrementado na altitude.
     */
    public void descer(int deltaZ) {
        if (Controlador.getAmbiente()
                .dentroDosLimites(this.posicaoX, this.posicaoY,
                        this.altitude - deltaZ, altitudeMaxima)) {
            this.altitude -= deltaZ;
        } else {
            this.altitude = 0;
        }
    }

    /**
     * Método que retorna a altitude atual do robô.
     * 
     * @return A altitude atual.
     */
    public int getAltitude() {
        return this.altitude;
    }

    /**
     * Classe interna que representa a ação de descer de um RoboAereo.
     * Implementa a interface Acao, permitindo que o robô execute a ação de descida.
     */
    private class Descer implements Acao {
        RoboAereo robo;

        public Descer(RoboAereo robo) {
            this.robo = robo;
        }

        @Override
        public String getNome() {
            return "Descer";
        }

        /**
         * Método que executa a ação de descida.
         * Solicita ao usuário o valor de altitude a ser decrementado e ajusta a
         * altitude do robô.
         */
        @Override
        public void executar() {
            Scanner scanner = Controlador.getScanner();
            System.out.print("Digite a altitude a descer: ");
            int deltaZ = scanner.nextInt();

            if (deltaZ <= 0) {
                System.out.println("A altitude deve ser um valor positivo.");
                return;
            }

            robo.descer(deltaZ);
        }
    }

    /**
     * Classe interna que representa a ação de subir de um RoboAereo.
     * Implementa a interface Acao, permitindo que o robô execute a ação de subida.
     */
    private class Subir implements Acao {
        RoboAereo robo;

        public Subir(RoboAereo robo) {
            this.robo = robo;
        }

        @Override
        public String getNome() {
            return "Subir";
        }

        /**
         * Método que executa a ação de subida.
         * Solicita ao usuário o valor de altitude a ser incrementado e ajusta a
         * altitude do robô.
         */
        @Override
        public void executar() {
            Scanner scanner = Controlador.getScanner();
            System.out.print("Digite a altitude a subir: ");
            int deltaZ = scanner.nextInt();

            scanner.nextLine(); // Consumir \n.

            if (deltaZ <= 0) {
                System.out.println("A altitude deve ser um valor positivo.");
                return;
            }

            robo.subir(deltaZ);
        }
    }
}
