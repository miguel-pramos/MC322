package com.robotsim.robots.terrestrials;

import java.util.ArrayList;
import java.util.Scanner;

import com.robotsim.Controlador;
import com.robotsim.environment.entity.Entidade;
import com.robotsim.etc.Acao;
import com.robotsim.robots.Robo;
import com.robotsim.robots.abilities.Atacante;
import com.robotsim.util.GeometryMath;

/**
 * Representa um robô do tipo tanque, uma subclasse de {@link RoboTerrestre}.
 * O RoboTanque é especializado em combate terrestre, possuindo a capacidade de
 * atirar
 * em outros robôs terrestres. Ele tem munição limitada e um alcance de ataque
 * específico.
 *
 * @see RoboTerrestre
 * @see Atacante
 */
public class RoboTanque extends RoboTerrestre implements Atacante {
    /** Quantidade de balas restantes para o ataque. */
    private int balasRestantes = 10;
    /** Dano causado por cada ataque bem-sucedido. */
    private final int dano = 200;
    /** Alcance máximo dos ataques. */
    private final int alcance = 25;
    /** Contador estático para gerar IDs únicos para instâncias de RoboTanque. */
    private static int contador = 0;

    /**
     * Construtor para RoboTanque.
     * Inicializa o robô com nome, posição, HP e define sua velocidade máxima.
     *
     * @param nome     Nome do robô.
     * @param posicaoX Posição inicial no eixo X.
     * @param posicaoY Posição inicial no eixo Y.
     */
    public RoboTanque(String nome, int posicaoX, int posicaoY) {
        super(nome, posicaoX, posicaoY, 500);
        this.velocidadeMaxima = 15;
    }

    /**
     * Função que atira em um alvo que seja um robô terrestre, causando dano se o
     * alvo estiver
     * dentro do alcance do míssil.
     *
     * @param alvo A entidade a ser atacada.
     * @throws IllegalStateException Se não houver balas restantes.
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

    /**
     * Verifica se o RoboTanque pode atacar uma determinada entidade.
     * Só pode atacar instâncias de {@link RoboTerrestre}.
     *
     * @param alvo A entidade a ser verificada.
     * @return true se o alvo for um RoboTerrestre, false caso contrário.
     */
    @Override
    public boolean podeAtacar(Entidade alvo) {
        return alvo instanceof RoboTerrestre;
    }

    /**
     * Retorna uma descrição textual do RoboTanque, incluindo suas estatísticas.
     *
     * @return String contendo a descrição do robô.
     */
    @Override
    public String getDescricao() {
        return String.format(
                "RoboTanque é robusto e leva consigo grande poder de fogo \nNome: %s, HP: %d, Balas Restantes: %d, Dano: %d, Alcance: %d",
                this.getNome(), this.getHP(), this.balasRestantes, this.dano, this.alcance);
    }

    /**
     * Retorna o caractere que representa o RoboTanque no ambiente do simulador.
     *
     * @return O caractere 'T'.
     */
    @Override
    public char getRepresentacao() {
        return 'T'; // Representação do RoboTanque no ambiente.
    }

    /**
     * Inicializa as ações específicas do RoboTanque.
     * Adiciona a ação "Atirar" e chama o método da superclasse para inicializar
     * ações comuns.
     */
    @Override
    protected void inicializarAcoes() {
        acoes.add(new Atirar(this));
        super.inicializarAcoes();
    }

    /**
     * Classe interna que implementa a ação de atirar para o {@link RoboTanque}.
     * Permite ao robô atacar alvos terrestres selecionados pelo usuário.
     */
    private class Atirar implements Acao {
        /** O robô tanque que executará a ação de atirar. */
        RoboTanque robo;

        /**
         * Construtor para a ação Atirar.
         * 
         * @param robo O {@link RoboTanque} associado a esta ação.
         */
        public Atirar(RoboTanque robo) {
            this.robo = robo;
        }

        /**
         * Obtém o nome da ação.
         * 
         * @return O nome da ação ("Atirar").
         */
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

    /**
     * Obtém o contador para gerar IDs únicos para {@link RoboTanque}.
     * Incrementa o contador a cada chamada.
     *
     * @return O valor atualizado do contador.
     */
    @Override
    protected int getContador() {
        contador++;
        return contador;
    }
}
