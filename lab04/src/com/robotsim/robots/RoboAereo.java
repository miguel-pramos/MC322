package com.robotsim.robots;

import java.util.Scanner;

import com.robotsim.Controlador;
import com.robotsim.etc.Acao;
import com.robotsim.exceptions.ColisaoException;

/**
 * Representa um robô aéreo genérico.
 * Esta classe abstrata estende {@link Robo} e adiciona funcionalidades
 * específicas para robôs que operam em três dimensões, como controle de
 * altitude.
 *
 * @see Robo
 */
public abstract class RoboAereo extends Robo { // Implementa Atacante
    /**
     * Altitude máxima que o robô aéreo pode atingir.
     */
    protected int altitudeMaxima;

    /**
     * Construtor para RoboAereo.
     *
     * @param nome           Nome do robô.
     * @param posicaoX       Posição inicial no eixo X.
     * @param posicaoY       Posição inicial no eixo Y.
     * @param HP             Pontos de vida iniciais.
     * @param altitude       Altitude inicial do robô.
     * @param altitudeMaxima Altitude máxima permitida para o robô.
     */
    public RoboAereo(String nome, int posicaoX, int posicaoY, int HP, int altitude, int altitudeMaxima) {
        super(nome, posicaoX, posicaoY, HP);
        this.setZ(altitude);
        this.altitudeMaxima = altitudeMaxima;
    }

    /**
     * Inicializa as ações específicas do RoboAereo.
     * Adiciona as ações de "Subir" e "Descer" à lista de ações do robô.
     * Chama o método da superclasse para inicializar ações comuns.
     */
    @Override
    protected void inicializarAcoes() {
        super.inicializarAcoes();
        acoes.add(new Subir(this));
        acoes.add(new Descer(this));
    }

    /**
     * Permite ao robô subir, aumentando sua altitude (coordenada Z).
     * A subida é limitada pela {@link #altitudeMaxima} e pelos limites do ambiente.
     *
     * @param deltaZ A quantidade a ser adicionada à altitude atual.
     */
    public void subir(int deltaZ) {
        try {
            Controlador.getAmbiente()
                    .dentroDosLimites(this.getX(), this.getY(),
                            this.getZ() + deltaZ);
            this.setZ(this.getZ() + deltaZ);
        } catch (ColisaoException e) {

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
        try {
            Controlador.getAmbiente()
                    .dentroDosLimites(this.getX(), this.getY(),
                            this.getZ() - deltaZ);
            this.setZ(this.getZ() - deltaZ);
        } catch (ColisaoException e) {
            this.setZ(0);
        }
    }

    /**
     * Obtém a altitude máxima que este robô aéreo pode alcançar.
     *
     * @return A altitude máxima permitida.
     */
    public int getAltitudeMaxima() {
        return this.altitudeMaxima;
    }

    /**
     * Classe interna que implementa a ação de descer para um {@link RoboAereo}.
     */
    private class Descer implements Acao {
        /** O robô aéreo que executará a ação de descer. */
        RoboAereo robo;

        /**
         * Construtor para a ação Descer.
         * 
         * @param robo O {@link RoboAereo} associado a esta ação.
         */
        public Descer(RoboAereo robo) {
            this.robo = robo;
        }

        /**
         * Obtém o nome da ação.
         * 
         * @return O nome da ação ("Descer").
         */
        @Override
        public String getNome() {
            return "Descer";
        }

        /**
         * Executa a ação de descer.
         * Solicita ao usuário a quantidade de altitude a ser decrementada e chama o
         * método {@link RoboAereo#descer(int)}.
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
     * Classe interna que implementa a ação de subir para um {@link RoboAereo}.
     */
    private class Subir implements Acao {
        /** O robô aéreo que executará a ação de subir. */
        RoboAereo robo;

        /**
         * Construtor para a ação Subir.
         * 
         * @param robo O {@link RoboAereo} associado a esta ação.
         */
        public Subir(RoboAereo robo) {
            this.robo = robo;
        }

        /**
         * Obtém o nome da ação.
         * 
         * @return O nome da ação ("Subir").
         */
        @Override
        public String getNome() {
            return "Subir";
        }

        /**
         * Executa a ação de subir.
         * Solicita ao usuário a quantidade de altitude a ser incrementada e chama o
         * método {@link RoboAereo#subir(int)}.
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
