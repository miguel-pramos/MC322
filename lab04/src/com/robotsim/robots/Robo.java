package com.robotsim.robots;

import java.util.ArrayList;
import java.util.Scanner;

import com.robotsim.Controlador;
import com.robotsim.environment.entity.Comunicavel;
import com.robotsim.util.TesteColisao;
import com.robotsim.etc.Acao;
import com.robotsim.exceptions.ErroComunicacaoException;
import com.robotsim.exceptions.RoboDesligadoException;
import com.robotsim.robots.sensors.Sensor;

/**
 * A classe Robo é a classe base para todos os tipos de robôs no simulador.
 * Ela define propriedades e comportamentos comuns, como posição, nome e ações.
 */
public abstract class Robo implements Comunicavel {
    protected boolean ligado = false;
    protected String nome; // Nome do robô.
    protected int HP; // Pontos de vida do robô.
    protected int posicaoX; // Posição atual no eixo X.
    protected int posicaoY; // Posição atual no eixo Y.
    protected ArrayList<Acao> acoes; // Lista de ações disponíveis para o robô.
    protected ArrayList<Sensor> sensores; // Sensores do robô

    public Robo(String nome, int posicaoX, int posicaoY, int HP) {
        this.nome = nome;
        this.posicaoX = posicaoX;
        this.posicaoY = posicaoY;
        this.HP = HP;
        this.acoes = new ArrayList<>();
        this.sensores = new ArrayList<>();
        inicializarAcoes();
    }

    public boolean alternarEstado() {
        this.ligado = !this.ligado;
        return this.ligado;
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
     * Método responsável por inicializar as ações específicas dos sensores.
     * Este método deve ser chamado por subclasses após os sensores específicos
     * serem adicionados.
     */
    protected void inicializarSensores() {
        for (Sensor sensor : this.sensores)
            acoes.add(sensor.getAcao());
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
        int xFinal = this.posicaoX + deltaX;
        int yFinal = this.posicaoY + deltaY;

        boolean nosLimites = Controlador.getAmbiente()
                .dentroDosLimites(xFinal, yFinal);

        int[] dadosPossivelColisao = TesteColisao.dadosColisao(this, xFinal, yFinal);

        if (nosLimites) {
            this.posicaoX = dadosPossivelColisao[0];
            this.posicaoY = dadosPossivelColisao[1];
            if (TesteColisao.existeColisao(dadosPossivelColisao)) {
                System.out.printf(
                        "Colisão detectada! Parando na posição (%d, %d)\n", posicaoX, posicaoY);
            }
            if (dadosPossivelColisao[2] != 0) {
                this.tomarDano(dadosPossivelColisao[2]);
            }
        } else {
            System.out.println("Você estará fora dos limites do ambiente. Ação cancelada!");
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

    @Override
    public void enviarMensagens(Comunicavel comunicavel, String mensagem)
            throws RoboDesligadoException, ErroComunicacaoException {
        if (!this.ligado)
            throw new RoboDesligadoException("O robô %s está desligado.".formatted(this.nome));

        comunicavel.receberMensagens(mensagem);
        Controlador.getComunicacao().registrarMensagem(this.nome, mensagem);
    }

    @Override
    public void receberMensagens(String mensagem) throws RoboDesligadoException, ErroComunicacaoException {
        if (!this.ligado)
            throw new RoboDesligadoException("O robô %s está desligado.".formatted(this.nome));
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

    public int getHP() {
        return HP;
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
