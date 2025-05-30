package com.robotsim.robots.terrestrials;

import java.util.ArrayList;
import java.util.Scanner;

import com.robotsim.Controlador;
import com.robotsim.environment.entity.Entidade;
import com.robotsim.etc.Acao;
import com.robotsim.robots.Robo;
import com.robotsim.robots.abilities.Atacante;
import com.robotsim.robots.abilities.Autonomo; // Adicionado import
import com.robotsim.robots.aerials.RoboAereo;
import com.robotsim.util.GeometryMath;

/**
 * Representa um robô terrestre fixo especializado em atacar robôs aéreos.
 * Este robô não se move, possui munição limitada e um alcance específico para
 * seus ataques.
 * Implementa as interfaces {@link Atacante} e {@link Autonomo}.
 *
 * @see RoboTerrestre
 * @see Atacante
 * @see Autonomo
 */
public class RoboAntiAereo extends RoboTerrestre implements Atacante, Autonomo { // Implementa Autonomo
    /** Quantidade de balas restantes para o ataque. */
    private int balasRestantes = 10;
    /** Dano causado por cada ataque bem-sucedido. */
    private int dano = 250;
    /** Alcance máximo dos ataques. */
    private int alcance = 35;
    /** Contador estático para gerar IDs únicos para instâncias de RoboAntiAereo. */
    private static int contador = 0;
    /** Flag que indica se o robô está operando em modo autônomo. */
    private boolean modoAutonomo = false; // Adicionado campo para modo autônomo

    /**
     * Construtor para RoboAntiAereo.
     * Inicializa o robô com nome, posição e HP. A velocidade máxima é definida como
     * 0, pois é um robô fixo.
     *
     * @param nome     Nome do robô.
     * @param posicaoX Posição inicial no eixo X.
     * @param posicaoY Posição inicial no eixo Y.
     */
    public RoboAntiAereo(String nome, int posicaoX, int posicaoY) {
        super(nome, posicaoX, posicaoY, 300);
        this.velocidadeMaxima = 0; // Robo AntiAéreo é fixo.
    }

    /**
     * Retorna uma descrição textual do RoboAntiAereo, incluindo suas estatísticas.
     *
     * @return String contendo a descrição do robô.
     */
    @Override
    public String getDescricao() {
        return String.format(
                "RoboAntiAereo não se move, mas tem um longo alcance pelos céus \nNome: %s, HP: %d, Balas Restantes: %d, Dano: %d, Alcance: %d",
                this.getNome(), this.getHP(), this.balasRestantes, this.dano, this.alcance);
    }

    /**
     * Retorna o caractere que representa o RoboAntiAereo no ambiente do simulador.
     *
     * @return O caractere 'A'.
     */
    @Override
    public char getRepresentacao() {
        return 'A'; // Representação do RoboAntiAereo no ambiente.
    }

    /**
     * Inicializa as ações específicas do RoboAntiAereo.
     * Adiciona as ações "Atirar" e "Alternar Modo Autônomo".
     * Chama o método da superclasse para inicializar ações comuns.
     */
    @Override
    protected void inicializarAcoes() {
        super.inicializarAcoes(); // Chama o inicializarAcoes do RoboTerrestre e Robo
        acoes.add(new Atirar(this));
        acoes.add(new AlternarModoAutonomo(this));
    }

    // Implementação dos métodos da interface Atacante
    /**
     * Executa um ataque contra uma entidade alvo.
     * Verifica se o alvo é um {@link RoboAereo} e se está dentro do alcance.
     * Decrementa a munição a cada tiro.
     *
     * @param alvo A entidade a ser atacada.
     * @throws IllegalStateException Se não houver balas restantes.
     */
    @Override
    public void executarAtaque(Entidade alvo) {
        if (!podeAtacar(alvo)) {
            System.out.println("RoboAntiAereo só pode atacar robôs aéreos.");
            return;
        }
        if (balasRestantes <= 0) {
            throw new IllegalStateException("Nenhuma bala restante");
        }

        if (GeometryMath.distanciaEuclidiana(this, alvo.getX(), alvo.getY()) < this.alcance) {
            ((RoboAereo) alvo).tomarDano(dano);
            this.balasRestantes--;
            System.out.println(this.getNome() + " atingiu " + ((Robo) alvo).getNome() + "!");
        } else {
            System.out.println("O inimigo estava longe demais... " + this.getNome() + " não acertou.");
        }
    }

    /**
     * Verifica se o RoboAntiAereo pode atacar uma determinada entidade.
     * Só pode atacar instâncias de {@link RoboAereo}.
     *
     * @param alvo A entidade a ser verificada.
     * @return true se o alvo for um RoboAereo, false caso contrário.
     */
    @Override
    public boolean podeAtacar(Entidade alvo) {
        return alvo instanceof RoboAereo;
    }

    // Implementação dos métodos da interface Autonomo
    /**
     * Define o modo de operação autônomo do robô.
     * Quando ativado, o robô pode realizar ações (como atacar) sem intervenção
     * direta do usuário.
     *
     * @param ativar true para ativar o modo autônomo, false para desativar.
     */
    @Override
    public void setModoAutonomo(boolean ativar) {
        this.modoAutonomo = ativar;
        if (this.modoAutonomo) {
            System.out.println(this.getNome() + " entrou em modo autônomo.");
            if (Controlador.getAmbiente().getRobos().stream().anyMatch(r -> r instanceof RoboAereo && r != this)) {
                System.out.println(this.getNome() + " detectou alvos aéreos e está pronto para atacar autonomamente.");
            } else {
                System.out.println(this.getNome() + " não detectou alvos aéreos no momento.");
            }
        } else {
            System.out.println(this.getNome() + " saiu do modo autônomo.");
        }
    }

    /**
     * Verifica se o robô está atualmente em modo autônomo.
     *
     * @return true se o modo autônomo estiver ativo, false caso contrário.
     */
    @Override
    public boolean isAutonomo() {
        return this.modoAutonomo;
    }

    /**
     * Classe interna que implementa a ação de atirar para o {@link RoboAntiAereo}.
     * Permite ao robô atacar alvos aéreos selecionados pelo usuário.
     */
    private class Atirar implements Acao {
        /** O robô anti-aéreo que executará a ação de atirar. */
        RoboAntiAereo robo;

        /**
         * Construtor para a ação Atirar.
         * 
         * @param robo O {@link RoboAntiAereo} associado a esta ação.
         */
        public Atirar(RoboAntiAereo robo) {
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
                robo.executarAtaque(alvo); // Modificado para usar o método da interface
            } catch (IllegalStateException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Classe interna que implementa a ação de alternar o modo autônomo do
     * {@link RoboAntiAereo}.
     */
    private class AlternarModoAutonomo implements Acao {
        /** O robô anti-aéreo cujo modo autônomo será alternado. */
        RoboAntiAereo robo;

        /**
         * Construtor para a ação AlternarModoAutonomo.
         * 
         * @param robo O {@link RoboAntiAereo} associado a esta ação.
         */
        public AlternarModoAutonomo(RoboAntiAereo robo) {
            this.robo = robo;
        }

        /**
         * Obtém o nome da ação, indicando o estado atual do modo autônomo (ON/OFF).
         * 
         * @return O nome da ação com o estado do modo autônomo.
         */
        @Override
        public String getNome() {
            return "Alternar Modo Autônomo (" + (robo.isAutonomo() ? "ON" : "OFF") + ")";
        }

        /**
         * Executa a ação de alternar o modo autônomo.
         * Inverte o estado atual do modo autônomo do robô.
         */
        @Override
        public void executar() {
            robo.setModoAutonomo(!robo.isAutonomo());
        }
    }

    /**
     * Obtém o contador para gerar IDs únicos para {@link RoboAntiAereo}.
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
