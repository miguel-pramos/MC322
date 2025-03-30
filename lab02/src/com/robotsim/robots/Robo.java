
package com.robotsim.robots;

import java.util.ArrayList;
import java.util.Scanner;

import com.robotsim.Controlador;
import com.robotsim.etc.Acao;

/**
 * Classe abstrata que representa um robô no simulador.
 * Cada robô possui um nome, pontos de vida (HP), posição no ambiente (X, Y)
 * e uma lista de ações que pode executar.
 * <p>
 * Métodos principais incluem movimentação, execução de ações, exibição de
 * posição
 * e manipulação de pontos de vida.
 */
public abstract class Robo {
    private String nome;
    private int HP;
    private int posicaoX;
    private int posicaoY;
    protected ArrayList<Acao> acoes;

    /**
     * Inicializa o robô com um nome e em uma posição. Também inicializa as
     * ações disponíveis da classe.
     * 
     * @param nome     Nome do robô
     * @param posicaoX Posição horizontal inicial
     * @param posicaoY Posição vertical inicial
     */
    public Robo(String nome, int posicaoX, int posicaoY) {
        this.nome = nome;
        this.posicaoX = posicaoX;
        this.posicaoY = posicaoY;
        this.acoes = new ArrayList<>();
        inicializarAcoes();
    }

    /**
     * Método que adiciona as ações de cada classe. Deve ser sobrescrito
     * pelas classes filhas.
     */
    protected void inicializarAcoes() {
        acoes.add(new Mover(this));
    };

    /**
     * Tenta executar uma ação.
     * 
     * @param nomeAcao Nome da ação buscada
     */
    public void executarAcao(Acao acao) {
        try {
            acao.executar();
        } catch (Exception e) {
            System.out.println("Ação indisponível para este robô: " + acao.getNome());
        }
    }

    protected void mover(int deltaX, int deltaY) {
        this.posicaoX += deltaX;
        this.posicaoY += deltaY;
    }

    public void tomarDano(int dano) {
        this.HP -= dano;
        if (this.HP < 0) {
            Controlador.getAmbiente().matarRobo(this);
        }
    }

    public void exibirPosicao() {
        System.out.println(nome + " está na posição (" + this.posicaoX + ", " + this.posicaoY + ")");
    }

    public ArrayList<Acao> getAcoes() {
        return new ArrayList<>(acoes);
    }

    public int getPosicaoX() {
        return posicaoX;
    }

    public int getPosicaoY() {
        return posicaoY;
    }

    public String getNome() {
        return nome;
    }

    private class Mover implements Acao {
        Robo robo;

        public Mover(Robo robo) {
            this.robo = robo;
        }

        @Override
        public String getNome() {
            return "Mover";
        }

        @Override
        public void executar() {
            Scanner scanner = Controlador.getScanner();

            System.out.print("O quento quer andar no eixo X? ");
            int deltaX = scanner.nextInt();

            System.out.print("O quento quer andar no eixo Y? ");
            int deltaY = scanner.nextInt();
            scanner.nextLine(); // Consumir \n

            robo.mover(deltaX, deltaY);
        }
    }
}
