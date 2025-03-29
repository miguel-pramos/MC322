
package com.robotsim.robots;

import java.util.ArrayList;

import com.robotsim.Controlador;
import com.robotsim.etc.Acao;

/**
 * Classe abstrata que representa um robô no simulador.
 * Cada robô possui um nome, pontos de vida (HP), posição no ambiente (X, Y) 
 * e uma lista de ações que pode executar.
 * <p>
 * Métodos principais incluem movimentação, execução de ações, exibição de posição 
 * e manipulação de pontos de vida.
 */
public abstract class Robo {
    private String nome;
    private int HP;
    private int posicaoX;
    private int posicaoY;
    protected ArrayList<Acao> acoes;

    public Robo(String nome, int posicaoX, int posicaoY) {
        this.nome = nome;
        this.posicaoX = posicaoX;
        this.posicaoY = posicaoY;
        this.acoes = new ArrayList<>();
        inicializarAcoes();
    }

    protected void inicializarAcoes() {
        acoes.add(new Mover(this));
    };

    public void executarAcao(String nomeAcao) {
        for (Acao acao : acoes) {
            if (acao.getNome().equalsIgnoreCase(nomeAcao)) {
                acao.executar();
                return;
            }
        }
        System.out.println("Ação indisponível para este robô: " + nomeAcao);
    }

    public void mover(int deltaX, int deltaY) {
        this.posicaoX += deltaX;
        this.posicaoY += deltaY;
    }

    public void tomarDano(int dano) {
        this.HP -= dano;
        if (this.HP < 0) {
            Controlador.ambiente.matarRobo(this);
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
        public void executar(Object... args) {
            if (args.length != 1 || !(args[0] instanceof Integer && args[1] instanceof Integer)) {
                throw new IllegalArgumentException("Mover requer dois inteiros."); // Estudar necessidade disso
            }
            int deltaX = (int) args[0];
            int deltaY = (int) args[1];
            robo.mover(deltaX, deltaY);
        }
    }
}
