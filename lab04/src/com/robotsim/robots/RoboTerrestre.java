package com.robotsim.robots;

import java.util.Scanner;

import com.robotsim.Controlador;
import com.robotsim.etc.Acao;
import com.robotsim.util.GeometryMath;

/**
 * Representa um robô terrestre genérico, uma subclasse de {@link Robo}.
 * Define comportamentos e propriedades comuns a todos os robôs que operam
 * primariamente no solo,
 * como a limitação de movimento pela {@link #velocidadeMaxima}.
 *
 * @see Robo
 */
public abstract class RoboTerrestre extends Robo {
    /**
     * Velocidade máxima de movimento do robô terrestre, em pixels por unidade de
     * tempo (definida em {@link Controlador#DELTA_TIME}).
     */
    protected int velocidadeMaxima; // Pixels por segundo

    /**
     * Construtor para RoboTerrestre.
     *
     * @param nome     Nome do robô.
     * @param posicaoX Posição inicial no eixo X.
     * @param posicaoY Posição inicial no eixo Y.
     * @param HP       Pontos de vida iniciais.
     */
    public RoboTerrestre(String nome, int posicaoX, int posicaoY, int HP) {
        super(nome, posicaoX, posicaoY, HP);
    }

    /**
     * Inicializa as ações básicas do RoboTerrestre.
     * Adiciona a ação "Mover" específica para robôs terrestres.
     */
    @Override
    protected void inicializarAcoes() {
        acoes.add(new Mover(this));
    }

    /**
     * Move o robô terrestre, respeitando sua {@link #velocidadeMaxima}.
     * Se a distância do movimento solicitado exceder a capacidade de movimento em
     * um {@link Controlador#DELTA_TIME},
     * o movimento não é realizado e uma mensagem é exibida.
     *
     * @param deltaX Deslocamento desejado no eixo X.
     * @param deltaY Deslocamento desejado no eixo Y.
     */
    @Override
    protected void mover(int deltaX, int deltaY) {
        double distancia = GeometryMath.distanciaEuclidiana(this, this.getX() + deltaX, this.getY() + deltaY);
        if (distancia / Controlador.DELTA_TIME > this.velocidadeMaxima) {
            System.out.printf("%s tentou se mover rápido demais!", this.getNome());
            return;
        }
        super.mover(deltaX, deltaY);
    }

    /**
     * Classe interna que implementa a ação de mover para o {@link RoboTerrestre}.
     * Solicita ao usuário os deslocamentos nos eixos X e Y e chama o método
     * {@link RoboTerrestre#mover(int, int)}.
     */
    private class Mover implements Acao {
        /** O robô terrestre que executará a ação de mover. */
        RoboTerrestre robo;

        /**
         * Construtor para a ação Mover.
         * 
         * @param robo O {@link RoboTerrestre} associado a esta ação.
         */
        public Mover(RoboTerrestre robo) {
            this.robo = robo;
        }

        /**
         * Obtém o nome da ação.
         * 
         * @return O nome da ação ("Mover").
         */
        @Override
        public String getNome() {
            return "Mover";
        }

        /**
         * Executa a ação de mover.
         * Solicita ao usuário os deslocamentos deltaX e deltaY e move o robô terrestre.
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
