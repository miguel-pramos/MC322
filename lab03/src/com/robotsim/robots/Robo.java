package com.robotsim.robots;

import java.util.ArrayList;
import java.util.Scanner;

import com.robotsim.Controlador;
import com.robotsim.etc.Acao;

/**
 * A classe Robo é a classe base para todos os tipos de robôs no simulador.
 * Ela define propriedades e comportamentos comuns, como posição, nome e ações.
 */
public abstract class Robo {
    protected String nome; // Nome do robô.
    protected int HP; // Pontos de vida do robô.
    protected int posicaoX; // Posição atual no eixo X.
    protected int posicaoY; // Posição atual no eixo Y.
    protected ArrayList<Acao> acoes; // Lista de ações disponíveis para o robô.

    public Robo(String nome, int posicaoX, int posicaoY, int HP) {
        this.nome = nome;
        this.posicaoX = posicaoX;
        this.posicaoY = posicaoY;
        this.HP = HP;
        this.acoes = new ArrayList<>();
        inicializarAcoes();
    }

    /**
     * Método responsável por inicializar as ações do robô.
     * Este método deve ser sobrescrito pelas subclasses para adicionar ações
     * específicas.
     */
    protected void inicializarAcoes() {
        acoes.add(new Mover(this));
    }

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

    /**
     * Método responsável por mover o robô para uma nova posição.
     *
     * @param deltaX Deslocamento no eixo X.
     * @param deltaY Deslocamento no eixo Y.
     */
    protected void mover(int deltaX, int deltaY) {
        boolean nosLimites = Controlador.getAmbiente()
                .dentroDosLimites(this.posicaoX + deltaX, this.posicaoY + deltaY)

        boolean semColisao

        if (nosLimites) {
            this.posicaoX += deltaX;
            this.posicaoY += deltaY;
        }
        else{
            System.out.println("Você está fora dos limites do ambiente. Ação cancelada!");
        }
    }

    private semColisao(){
        private List<Posicao> calcularCaminho(Posicao inicio, Posicao fim) {
            List<Posicao> caminho = new ArrayList<>();

            int x1 = inicio.x;
            int y1 = inicio.y;
            int x2 = fim.x;
            int y2 = fim.y;

            int dx = Math.abs(x2 - x1);
            int dy = Math.abs(y2 - y1);

            int sx = x1 < x2 ? 1 : -1;
            int sy = y1 < y2 ? 1 : -1;

            int err = dx - dy;
            int currentX = x1;
            int currentY = y1;

            while (true) {
                caminho.add(new Posicao(currentX, currentY));
                if (currentX == x2 && currentY == y2) break;

                int e2 = 2 * err;
                if (e2 > -dy) {
                    err -= dy;
                    currentX += sx;
                }
                if (e2 < dx) {
                    err += dx;
                    currentY += sy;
                }
            }
            return caminho;
        }
    }
    /**
     * Método que aplica dano ao robô.
     * Este método pode ser sobrescrito por subclasses para implementar
     * comportamentos específicos.
     *
     * @param dano Quantidade de dano a ser aplicada.
     */
    public void tomarDano(int dano) {
        this.HP -= dano;
        System.out.printf("O robo %s foi atingido com sucesso!\n", this.nome);
        if (this.HP < 0)
            Controlador.getAmbiente().destruirRobo(this);
    }

    /**
     * Método que exibe a posição atual do robô.
     */
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

    /**
     * Classe interna que representa a ação de mover o robô.
     */
    private class Mover implements Acao {
        Robo robo;

        public Mover(Robo robo) {
            this.robo = robo;
        }

        @Override
        public String getNome() {
            return "Mover";
        }

        /**
         * Método que executa a ação de mover o robô.
         */
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
