package com.robotsim.robots;

import java.util.ArrayList;
import java.util.Scanner;

import com.robotsim.Controlador;
import com.robotsim.environment.entity.Entidade;
import com.robotsim.etc.Acao;
import com.robotsim.robots.abilities.Atacante;
import com.robotsim.robots.abilities.Autonomo; // Adicionado import
import com.robotsim.util.GeometryMath;

/**
 * A classe RoboAntiAereo representa um robô terrestre fixo especializado em
 * atacar robôs aéreos. Este robô possui munição limitada e um alcance
 * específico
 * para seus ataques.
 *
 * @see RoboTerrestre
 */
public class RoboAntiAereo extends RoboTerrestre implements Atacante, Autonomo { // Implementa Autonomo
    private int balasRestantes = 10;
    private int dano = 250;
    private int alcance = 35;
    private static int contador = 0;
    private boolean modoAutonomo = false; // Adicionado campo para modo autônomo

    public RoboAntiAereo(String nome, int posicaoX, int posicaoY) {
        super(nome, posicaoX, posicaoY, 300);
        this.velocidadeMaxima = 0; // Robo AntiAéreo é fixo.
    }

    @Override
    public String getDescricao() {
        return String.format(
                "RoboAntiAereo não se move, mas tem um longo alcance pelos céus \nNome: %s, HP: %d, Balas Restantes: %d, Dano: %d, Alcance: %d",
                this.getNome(), this.getHP(), this.balasRestantes, this.dano, this.alcance);
    }

    @Override
    public char getRepresentacao() {
        return 'A'; // Representação do RoboAntiAereo no ambiente.
    }

    /**
     * Método responsável por inicializar as ações do RoboAntiAereo.
     * Adiciona a ação de "Atirar" à lista de ações e chama o método
     * da superclasse para inicializar outras ações.
     */
    @Override
    protected void inicializarAcoes() {
        super.inicializarAcoes(); // Chama o inicializarAcoes do RoboTerrestre e Robo
        acoes.add(new Atirar(this));
        acoes.add(new AlternarModoAutonomo(this));
    }

    // Implementação dos métodos da interface Atacante
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

    @Override
    public boolean podeAtacar(Entidade alvo) {
        return alvo instanceof RoboAereo;
    }

    // Implementação dos métodos da interface Autonomo
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

    @Override
    public boolean isAutonomo() {
        return this.modoAutonomo;
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
                robo.executarAtaque(alvo); // Modificado para usar o método da interface
            } catch (IllegalStateException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private class AlternarModoAutonomo implements Acao {
        RoboAntiAereo robo;

        public AlternarModoAutonomo(RoboAntiAereo robo) {
            this.robo = robo;
        }

        @Override
        public String getNome() {
            return "Alternar Modo Autônomo (" + (robo.isAutonomo() ? "ON" : "OFF") + ")";
        }

        @Override
        public void executar() {
            robo.setModoAutonomo(!robo.isAutonomo());
        }
    }

    @Override
    protected int getContador() {
        contador++;
        return contador;
    }
}
