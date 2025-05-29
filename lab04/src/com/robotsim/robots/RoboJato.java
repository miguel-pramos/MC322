package com.robotsim.robots;

import java.util.ArrayList;
import java.util.Scanner;

import com.robotsim.Controlador;
import com.robotsim.environment.entity.Entidade;
import com.robotsim.etc.Acao;
import com.robotsim.robots.abilities.Atacante;

import com.robotsim.util.GeometryMath;

/**
 * A classe RoboJato representa um robô aéreo especializado que possui a
 * capacidade
 * de lançar mísseis e atirar rajadas de metralhadora, a depender do tipo de
 * alvo.
 *
 * @see RoboAereo
 */
public class RoboJato extends RoboAereo implements Atacante {
    private int misseisRestantes = 4;
    private int rajadasRestantes = 10;
    private final int alcanceMissil = 20;
    private final int alcanceMetralhadora = 15;
    private final int danoMissil = 250;
    private final int danoMetralhadora = 180;
    private static int contador = 0;

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

        // A verificação de podeAtacar já garante que alvo é RoboAereo
        if (GeometryMath.distanciaEuclidiana(this, alvo.getX(), alvo.getY(),
                alvo.getAltitude()) < alcanceMissil) {
            alvo.tomarDano(danoMissil);
            this.misseisRestantes--;
            System.out.println(this.nome + " lançou um míssil em " + alvo.getNome() + "!");
        } else {
            System.out.println("O inimigo estava longe demais... " + this.nome + " errou o míssil.");
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

        // A verificação de podeAtacar já garante que alvo é RoboTerrestre
        if (GeometryMath.distanciaEuclidiana(this, alvo.getX(), alvo.getY(), 0) < alcanceMetralhadora) {
            alvo.tomarDano(danoMetralhadora);
            this.rajadasRestantes--;
            System.out.println(this.nome + " atirou uma rajada em " + alvo.getNome() + "!");
        } else {
            System.out.println("O inimigo estava longe demais... " + this.nome + " errou a rajada.");
        }
    }

    @Override
    public String getDescricao() {
        return String.format(
                "RoboJato é rápido e perigoso. Especializado em ataques aéreos \nNome: %s, HP: %d, Mísseis Restantes: %d, Rajadas Restantes: %d",
                this.nome, this.HP, this.misseisRestantes, this.rajadasRestantes);
    }

    @Override
    public char getRepresentacao() {
        return 'J'; // Representação do RoboJato no ambiente.
    }

    @Override
    protected void inicializarAcoes() {
        super.inicializarAcoes();
        // As ações de ataque agora são gerenciadas pela interface Atacante e sua
        // implementação em RoboJato
        // Ações específicas como "Lançar Míssil" e "Atirar Rajada" podem ser mantidas
        // se
        // a intenção é ter comandos separados para cada tipo de ataque.
        // Se a ideia é ter um único comando "Atacar" que decide qual arma usar,
        // então essas ações específicas podem ser removidas ou modificadas.
        // Por ora, manterei as ações específicas, mas a lógica de ataque principal
        // estará em executarAtaque.
        acoes.add(new Atacar(this)); // Adiciona a ação genérica de atacar
    }

    // Implementação dos métodos da interface Atacante
    @Override
    public void executarAtaque(Entidade alvo) {
        if (!podeAtacar(alvo)) {
            System.out.println(this.nome + " não pode atacar este tipo de alvo.");
            return;
        }

        if (alvo instanceof RoboAereo) {
            System.out.println(this.nome + " vai tentar lançar um míssil em " + ((Robo) alvo).getNome());
            lancarMissil((RoboAereo) alvo);
        } else if (alvo instanceof RoboTerrestre) {
            System.out.println(this.nome + " vai tentar atirar uma rajada em " + ((Robo) alvo).getNome());
            atirarRajada((RoboTerrestre) alvo);
        } else {
            System.out.println("Tipo de alvo não suportado para ataque por RoboJato.");
        }
    }

    @Override
    public boolean podeAtacar(Entidade alvo) {
        return alvo instanceof RoboAereo || alvo instanceof RoboTerrestre;
    }

    /**
     * Classe interna que representa a ação genérica de Atacar para RoboJato.
     * Esta classe pode listar todos os alvos possíveis (aéreos e terrestres)
     * e então chamar o método executarAtaque apropriado.
     */
    private class Atacar implements Acao {
        RoboJato robo;

        public Atacar(RoboJato robo) {
            this.robo = robo;
        }

        @Override
        public String getNome() {
            return "Atacar (Jato)";
        }

        @Override
        public void executar() {
            ArrayList<Robo> alvosPossiveis = new ArrayList<>();
            System.out.println("Alvos disponíveis para " + robo.getNome() + ":");
            int i = 1;
            for (Robo r : Controlador.getAmbiente().getRobos()) {
                if (r != robo && robo.podeAtacar(r)) {
                    alvosPossiveis.add(r);
                    System.out.printf("[%d] %s (%s)\n", i, r.getNome(), r instanceof RoboAereo ? "Aéreo" : "Terrestre");
                    i++;
                }
            }

            if (alvosPossiveis.isEmpty()) {
                System.out.println("Nenhum alvo disponível para ataque.");
                return;
            }

            Scanner scanner = Controlador.getScanner();
            System.out.print("Escolha o índice do robô para atacar: ");
            int indice = scanner.nextInt() - 1;
            scanner.nextLine(); // Consumir \n
            if (indice < 0 || indice >= alvosPossiveis.size()) {
                System.out.println("Índice inválido.");
                return;
            }

            Robo alvoSelecionado = alvosPossiveis.get(indice);
            try {
                robo.executarAtaque(alvoSelecionado);
            } catch (IllegalStateException e) {
                System.out.println("Erro ao atacar: " + e.getMessage());
            }
        }
    }

    @Override
    protected int getContador() {
        contador++;
        return contador;
    }
}