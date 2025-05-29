package com.robotsim.robots;

import java.util.ArrayList;
import java.util.Scanner;

import com.robotsim.Controlador;
import com.robotsim.environment.entity.Entidade;
import com.robotsim.etc.Acao;
import com.robotsim.robots.abilities.Atacante;
import com.robotsim.util.GeometryMath;

/**
 * A classe RoboTanque representa um robô terrestre com a capacidade de atirar
 * em outros robôs terrestres.
 * Este robô possui munição limitada e um alcance específico para seus ataques.
 *
 * @see RoboTerrestre
 */
public class RoboTanque extends RoboTerrestre implements Atacante {
    private int balasRestantes = 10;
    private final int dano = 200;
    private final int alcance = 25;
    private static int contador = 0;

    public RoboTanque(String nome, int posicaoX, int posicaoY) {
        super(nome, posicaoX, posicaoY, 500);
        this.velocidadeMaxima = 15;
    }

    /**
     * Função que atira em um alvo que seja um robô terrestre, causando dano se o
     * alvo estiver
     * dentro do alcance do míssil.
     *
     * @param alvo
     * @throws IllegalStateException
     */
    @Override
    public void executarAtaque(Entidade alvo) {
        if (!podeAtacar(alvo)) {
            System.out.println("RoboTanque não pode atacar este tipo de alvo.");
            return;
        }

        if (this.balasRestantes <= 0)
            throw new IllegalStateException("Nenhuma bala restante");
        else if (GeometryMath.distanciaEuclidiana(this, alvo.getX(), alvo.getY()) < this.alcance) {
            ((RoboTerrestre) alvo).tomarDano(this.dano);
            this.balasRestantes--;
        } else {
            System.out.println("O inimigo estava longe demais... Não acertou");
        }
    }

    @Override
    public boolean podeAtacar(Entidade alvo) {
        return alvo instanceof RoboTerrestre;
    }

    @Override
    public String getDescricao() {
        return String.format(
                "RoboTanque é robusto e leva consigo grande poder de fogo \nNome: %s, HP: %d, Balas Restantes: %d, Dano: %d, Alcance: %d",
                this.nome, this.HP, this.balasRestantes, this.dano, this.alcance);
    }

    @Override
    public char getRepresentacao() {
        return 'T'; // Representação do RoboTanque no ambiente.
    }

    @Override
    protected void inicializarAcoes() {
        acoes.add(new Atirar(this));
        super.inicializarAcoes();
    }

    /**
     * Classe interna que representa a ação de atirar de um RoboTanque.
     */
    private class Atirar implements Acao {
        RoboTanque robo;

        public Atirar(RoboTanque robo) {
            this.robo = robo;
        }

        @Override
        public String getNome() {
            return "Atirar";
        }

        /**
         * Método sobrescrito que executa a ação do RoboTanque.
         * Este método permite ao RoboTanque atacar um robô terrestre presente no
         * ambiente.
         *
         * Regras e comportamentos:
         * - O robô não pode atacar a si mesmo.
         * - Caso não existam robôs disponíveis para ataque, uma mensagem será exibida e
         * a execução será encerrada.
         * - O usuário deve fornecer um índice válido para selecionar o alvo. Caso
         * contrário, uma mensagem de erro será exibida.
         *
         * Caso não haja robôs terrestres no ambiente, ou o índice fornecido seja
         * inválido, mensagens apropriadas serão exibidas ao usuário.
         */
        @Override
        public void executar() {
            ArrayList<RoboTerrestre> robosTerrestres = new ArrayList<>();
            int i = 0;

            for (Robo robo : Controlador.getAmbiente().getRobos()) {
                if (robo instanceof RoboTerrestre && robo != this.robo) {
                    robosTerrestres.add((RoboTerrestre) robo);
                    System.out.printf("[%d] %s\n", (i + 1), robo.getNome());
                    i++;
                }
            }

            if (robosTerrestres.isEmpty()) {
                System.out.println("Não há robôs terrestres para atacar.");
                return;
            }

            Scanner scanner = Controlador.getScanner();
            System.out.print("Escolha o índice do robô terrestre para atacar: ");
            int indice = scanner.nextInt() - 1;
            scanner.nextLine(); // Consumir \n

            if (indice < 0 || indice >= robosTerrestres.size()) {
                System.out.println("Índice inválido.");
                return;
            }

            RoboTerrestre alvo = robosTerrestres.get(indice);
            robo.executarAtaque(alvo);
        }
    }

    @Override
    protected int getContador() {
        contador++;
        return contador;
    }
}
