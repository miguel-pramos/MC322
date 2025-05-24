package com.robotsim.robots;

import java.util.ArrayList;
import java.util.Scanner;

import com.robotsim.Controlador;
import com.robotsim.etc.Acao;
import com.robotsim.util.GeometryMath;

/**
 * A classe RoboAntiAereo representa um robô terrestre fixo especializado em
 * atacar robôs aéreos. Este robô possui munição limitada e um alcance
 * específico
 * para seus ataques.
 *
 * @see RoboTerrestre
 */
public class RoboAntiAereo extends RoboTerrestre {
    private int balasRestantes = 10;
    private int dano = 250;
    private int alcance = 35;

    public RoboAntiAereo(String nome, int posicaoX, int posicaoY) {
        super(nome, posicaoX, posicaoY, 300);
        this.velocidadeMaxima = 0; // Robo AntiAéreo é fixo.
    }

    /**
     * Método responsável por atirar em um alvo aéreo.
     * <p>
     * O ataque só é realizado se houver balas restantes e se o alvo estiver dentro
     * do alcance.
     *
     * @param alvo O alvo aéreo (RoboAereo) que será atacado.
     * @throws IllegalStateException Se não houver balas restantes.
     */
    public void atirar(RoboAereo alvo) {
        if (balasRestantes <= 0)
            throw new IllegalStateException("Nenhuma bala restante");

        if (GeometryMath.distanciaEuclidiana(this, alvo.getX(), alvo.getPosicaoY()) < this.alcance) {
            alvo.tomarDano(dano);
            this.balasRestantes--;
        } else {
            System.out.println("O inimigo estava longe demais... Não acertou");
        }
    }

    /**
     * Método responsável por inicializar as ações do RoboAntiAereo.
     * Adiciona a ação de "Atirar" à lista de ações e chama o método
     * da superclasse para inicializar outras ações.
     */
    @Override
    protected void inicializarAcoes() {
        acoes.add(new Atirar(this));
    }

    /**
     * Classe interna que representa a ação de atirar de um RoboAntiAereo.
     * Implementa a interface Acao, permitindo que o robô execute a ação de ataque
     * contra robôs aéreos no ambiente.
     */
    private class Atirar implements Acao {
        RoboAntiAereo robo;

        public Atirar(RoboAntiAereo robo) {
            this.robo = robo;
        }

        @Override
        public String getNome() {
            return "Atirar";
        }

        /**
         * Método sobrescrito que executa a ação do RoboAntiAereo.
         * Este método permite ao RoboTanque atacar um robô terrestre presente no
         * ambiente.
         *
         * Regras e comportamentos:
         * - Caso não existam robôs disponíveis para ataque, uma mensagem será exibida e
         * a execução será encerrada.
         * - O usuário deve fornecer um índice válido para selecionar o alvo. Caso
         * contrário, uma mensagem de erro será exibida.
         *
         * Caso não haja robôs aéreos no ambiente, ou o índice fornecido seja
         * inválido, mensagens apropriadas serão exibidas ao usuário.
         */
        @Override
        public void executar() {
            ArrayList<RoboAereo> robosAlvos = new ArrayList<>();
            int i = 0;

            for (Robo robo : Controlador.getAmbiente().getRobos()) {
                if (robo instanceof RoboAereo) { // Apenas atacar robôs aéreos
                    robosAlvos.add((RoboAereo) robo);
                    System.out.printf("[%d] %s\n", (i + 1), robo.getNome());
                    i++;
                }
            }

            if (robosAlvos.isEmpty()) {
                System.out.println("Não há robôs aéreos para atacar.");
                return;
            }

            Scanner scanner = Controlador.getScanner();
            System.out.print("Escolha o índice do robô aéreo para atacar: ");
            int indice = scanner.nextInt() - 1;
            scanner.nextLine(); // Consumir \n

            if (indice < 0 || indice >= robosAlvos.size()) {
                System.out.println("Índice inválido.");
                return;
            }

            RoboAereo alvo = robosAlvos.get(indice);
            try {
                robo.atirar(alvo);
            } catch (IllegalStateException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
