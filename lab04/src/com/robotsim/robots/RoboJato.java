package com.robotsim.robots;

import java.util.ArrayList;
import java.util.Scanner;

import com.robotsim.Controlador;
import com.robotsim.etc.Acao;
import com.robotsim.util.GeometryMath;

/**
 * A classe RoboJato representa um robô aéreo especializado que possui a
 * capacidade
 * de lançar mísseis e atirar rajadas de metralhadora, a depender do tipo de
 * alvo.
 *
 * @see RoboAereo
 */
public class RoboJato extends RoboAereo {
    private int misseisRestantes = 4;
    private int rajadasRestantes = 10;
    private final int alcanceMissil = 20;
    private final int alcanceMetralhadora = 15;
    private final int danoMissil = 250;
    private final int danoMetralhadora = 180;

    public RoboJato(String nome, int posicaoX, int posicaoY) {
        super(nome, posicaoX, posicaoY, 200, 50, 200);
    }

    /**
     * Lança um míssil em direção a um alvo aéreo, causando dano se o alvo estiver
     * dentro do alcance do míssil.
     *
     * @param alvo O alvo aéreo (RoboAereo) que será atacado.
     * @throws IllegalStateException Se não houver mísseis restantes para lançar.
     */
    protected void lancarMissil(RoboAereo alvo) {
        if (misseisRestantes <= 0)
            throw new IllegalStateException("Nenhum míssil restante");

        if (GeometryMath.distanciaEuclidiana(this, alvo.getPosicaoX(), alvo.getPosicaoY(),
                alvo.getAltitude()) < alcanceMissil) {
            alvo.tomarDano(danoMissil);
            this.misseisRestantes--;
        } else {
            System.out.println("O inimigo estava longe demais... Não acertou");
        }
    }

    /**
     * Lança uma rajada em direção a um alvo terrestre, causando dano se o alvo
     * estiver
     * dentro do alcance do míssil.
     *
     * @param alvo O alvo aéreo (RoboAereo) que será atacado.
     * @throws IllegalStateException Se não houver mísseis restantes para lançar.
     */
    protected void atirarRajada(RoboTerrestre alvo) {
        if (rajadasRestantes <= 0)
            throw new IllegalStateException("Nenhuma rajada restante");

        if (GeometryMath.distanciaEuclidiana(this, alvo.getPosicaoX(), alvo.getPosicaoY(), 0) < alcanceMetralhadora) {
            alvo.tomarDano(danoMetralhadora);
            this.rajadasRestantes--;
        } else {
            System.out.println("O inimigo estava longe demais... Não acertou");
        }
    }

    @Override
    protected void inicializarAcoes() {
        super.inicializarAcoes();
        acoes.add(new AtirarRajada(this));
        acoes.add(new LancarMissil(this));
    }

    /**
     * Classe interna que representa a ação de lançar um míssil por um RoboJato.
     */
    private class LancarMissil implements Acao {
        RoboJato robo;

        public LancarMissil(RoboJato robo) {
            this.robo = robo;
        }

        @Override
        public String getNome() {
            return "Lançar míssil";
        }

        /**
         * Método que executa a ação de ataque de um robô aéreo utilizando um míssil.
         * O método apresenta uma lista de robôs disponíveis no ambiente para serem
         * atacados,
         * solicita ao usuário que escolha um alvo pelo índice e tenta lançar um míssil
         * no robô escolhido.
         *
         * Regras e comportamentos:
         * - O robô não pode atacar a si mesmo.
         * - Caso não existam robôs disponíveis para ataque, uma mensagem será exibida e
         * a execução será encerrada.
         * - O usuário deve fornecer um índice válido para selecionar o alvo. Caso
         * contrário, uma mensagem de erro será exibida.
         *
         */
        @Override
        public void executar() {
            ArrayList<RoboAereo> robosAlvos = new ArrayList<>();
            int i = 1;

            for (Robo robo : Controlador.getAmbiente().getRobos()) {
                if (robo != this.robo && robo instanceof RoboAereo) { // Não permitir atacar a si mesmo
                    robosAlvos.add((RoboAereo) robo);
                    System.out.printf("\n[%d] %s", i, robo.getNome());
                    i++;
                }
            }

            if (robosAlvos.isEmpty()) {
                System.out.println("\nNão há robôs para atacar.");
                return;
            }

            Scanner scanner = Controlador.getScanner();
            System.out.print("\n\nEscolha o índice do robô para atacar com míssil: ");
            int indice = scanner.nextInt() - 1;

            if (indice < 0 || indice >= robosAlvos.size()) {
                System.out.println("Índice inválido.");
                return;
            }

            RoboAereo alvo = robosAlvos.get(indice);
            try {
                robo.lancarMissil(alvo);
            } catch (IllegalStateException e) {
                System.out.println(e.getMessage());
            }

        }
    }

    /**
     * Classe interna que representa a ação de lançar uma rajada por um RoboJato.
     */
    private class AtirarRajada implements Acao {
        RoboJato robo;

        public AtirarRajada(RoboJato robo) {
            this.robo = robo;
        }

        @Override
        public String getNome() {
            return "Atirar Rajada";
        }

        /**
         * Método que executa a ação de ataque de um robô aéreo utilizando uma rajada.
         * O método apresenta uma lista de robôs disponíveis no ambiente para serem
         * atacados,
         * solicita ao usuário que escolha um alvo pelo índice e tenta lançar um míssil
         * no robô escolhido.
         *
         * Regras e comportamentos:
         * - Caso não existam robôs disponíveis para ataque, uma mensagem será exibida e
         * a execução será encerrada.
         * - O usuário deve fornecer um índice válido para selecionar o alvo. Caso
         * contrário, uma mensagem de erro será exibida.
         *
         */
        @Override
        public void executar() {
            ArrayList<RoboTerrestre> robosAlvos = new ArrayList<>();
            int i = 0;

            for (Robo robo : Controlador.getAmbiente().getRobos()) {
                if (robo instanceof RoboTerrestre) { // Não permitir atacar a si mesmo
                    robosAlvos.add((RoboTerrestre) robo);
                    System.out.printf("[%d] %s\n", (i + 1), robo.getNome());
                    i++;
                }
            }

            if (robosAlvos.isEmpty()) {
                System.out.println("Não há robôs para atacar.");
                return;
            }

            Scanner scanner = Controlador.getScanner();
            System.out.print("Escolha o índice do robô para atacar com rajada: ");
            int indice = scanner.nextInt() - 1;

            scanner.nextLine(); // Consumir \n

            if (indice < 0 || indice >= robosAlvos.size()) {
                System.out.println("Índice inválido.");
                return;
            }

            RoboTerrestre alvo = robosAlvos.get(indice);
            try {
                robo.atirarRajada(alvo);
            } catch (IllegalStateException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}