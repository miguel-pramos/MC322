package com.robotsim.robots.aerials;

import java.util.ArrayList;
import java.util.Scanner;

import com.robotsim.Controlador;
import com.robotsim.environment.entity.Entidade;
import com.robotsim.etc.Acao;
import com.robotsim.robots.Robo;
import com.robotsim.robots.abilities.Atacante;
import com.robotsim.robots.terrestrials.RoboTerrestre;
import com.robotsim.util.GeometryMath;

/**
 * Representa um robô aéreo do tipo Jato, uma subclasse de {@link RoboAereo}.
 * O RoboJato é especializado em combate aéreo e terrestre, capaz de lançar
 * mísseis contra alvos aéreos
 * e atirar rajadas de metralhadora contra alvos terrestres. Possui munição
 * limitada para ambas as armas
 * e alcances de ataque específicos.
 *
 * @see RoboAereo
 * @see Atacante
 */
public class RoboJato extends RoboAereo implements Atacante {
    /** Quantidade de mísseis restantes. */
    private int misseisRestantes = 4;
    /** Quantidade de rajadas de metralhadora restantes. */
    private int rajadasRestantes = 10;
    /** Alcance máximo dos mísseis. */
    private final int alcanceMissil = 20;
    /** Alcance máximo da metralhadora. */
    private final int alcanceMetralhadora = 15;
    /** Dano causado por cada míssil. */
    private final int danoMissil = 250;
    /** Dano causado por cada rajada de metralhadora. */
    private final int danoMetralhadora = 180;
    /** Contador estático para gerar IDs únicos para instâncias de RoboJato. */
    private static int contador = 0;

    /**
     * Construtor para RoboJato.
     * Inicializa o robô com nome, posição, HP, altitude inicial e altitude máxima.
     *
     * @param nome     Nome do robô.
     * @param posicaoX Posição inicial no eixo X.
     * @param posicaoY Posição inicial no eixo Y.
     */
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
                alvo.getZ()) < alcanceMissil) {
            alvo.tomarDano(danoMissil);
            this.misseisRestantes--;
            System.out.println(this.getNome() + " lançou um míssil em " + alvo.getNome() + "!");
        } else {
            System.out.println("O inimigo estava longe demais... " + this.getNome() + " errou o míssil.");
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
            System.out.println(this.getNome() + " atirou uma rajada em " + alvo.getNome() + "!");
        } else {
            System.out.println("O inimigo estava longe demais... " + this.getNome() + " errou a rajada.");
        }
    }

    /**
     * Retorna uma descrição textual do RoboJato, incluindo suas estatísticas de
     * combate como HP, mísseis e rajadas restantes.
     *
     * @return String contendo a descrição detalhada do robô.
     */
    @Override
    public String getDescricao() {
        return String.format(
                "RoboJato é rápido e perigoso. Especializado em ataques aéreos \nNome: %s, HP: %d, Mísseis Restantes: %d, Rajadas Restantes: %d",
                this.getNome(), this.getHP(), this.misseisRestantes, this.rajadasRestantes);
    }

    /**
     * Retorna o caractere que representa o RoboJato no ambiente do simulador.
     *
     * @return O caractere 'J'.
     */
    @Override
    public char getRepresentacao() {
        return 'J'; // Representação do RoboJato no ambiente.
    }

    /**
     * Inicializa as ações específicas do RoboJato.
     * Adiciona a ação genérica "Atacar (Jato)" à lista de ações do robô.
     * Chama o método da superclasse para inicializar ações comuns herdadas.
     */
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
    /**
     * Executa um ataque contra uma entidade alvo.
     * O método determina qual arma utilizar (míssil ou metralhadora) com base no
     * tipo do alvo.
     * Lança mísseis contra {@link RoboAereo} e atira rajadas contra
     * {@link RoboTerrestre}.
     *
     * @param alvo A entidade (robô) a ser atacada.
     */
    @Override
    public void executarAtaque(Entidade alvo) {
        if (!podeAtacar(alvo)) {
            System.out.println(this.getNome() + " não pode atacar este tipo de alvo.");
            return;
        }

        if (alvo instanceof RoboAereo) {
            System.out.println(this.getNome() + " vai tentar lançar um míssil em " + ((Robo) alvo).getNome());
            lancarMissil((RoboAereo) alvo);
        } else if (alvo instanceof RoboTerrestre) {
            System.out.println(this.getNome() + " vai tentar atirar uma rajada em " + ((Robo) alvo).getNome());
            atirarRajada((RoboTerrestre) alvo);
        } else {
            System.out.println("Tipo de alvo não suportado para ataque por RoboJato.");
        }
    }

    /**
     * Verifica se o RoboJato pode atacar uma determinada entidade.
     * O RoboJato pode atacar outras instâncias de {@link RoboAereo} ou
     * {@link RoboTerrestre}.
     *
     * @param alvo A entidade a ser verificada como possível alvo.
     * @return true se o alvo for um RoboAereo ou RoboTerrestre, false caso
     *         contrário.
     */
    @Override
    public boolean podeAtacar(Entidade alvo) {
        return alvo instanceof RoboAereo || alvo instanceof RoboTerrestre;
    }

    /**
     * Classe interna que implementa a ação genérica de atacar para o
     * {@link RoboJato}.
     * Esta ação permite ao usuário selecionar um alvo dentre os robôs inimigos
     * (aéreos ou terrestres)
     * detectados no ambiente e, em seguida, comanda o RoboJato para executar o
     * ataque apropriado.
     */
    private class Atacar implements Acao {
        /** O robô jato que executará a ação de atacar. */
        RoboJato robo;

        /**
         * Construtor para a ação Atacar.
         * 
         * @param robo O {@link RoboJato} que realizará esta ação.
         */
        public Atacar(RoboJato robo) {
            this.robo = robo;
        }

        /**
         * Obtém o nome da ação, que será exibido ao usuário.
         * 
         * @return O nome da ação ("Atacar (Jato)").
         */
        @Override
        public String getNome() {
            return "Atacar (Jato)";
        }

        /**
         * Executa a ação de atacar.
         * Primeiramente, lista todos os robôs inimigos (aéreos e terrestres) que podem
         * ser atacados pelo RoboJato.
         * Em seguida, solicita ao usuário que escolha um alvo da lista.
         * Finalmente, chama o método {@link RoboJato#executarAtaque(Entidade)} para
         * realizar o ataque contra o alvo selecionado.
         * Trata possíveis exceções {@link IllegalStateException} que podem ocorrer
         * durante o ataque (ex: falta de munição).
         */
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

    /**
     * Obtém o contador estático usado para gerar IDs únicos para instâncias de
     * {@link RoboJato}.
     * Este método é chamado durante a criação do ID do robô na classe base
     * {@link Robo}.
     * Incrementa o contador a cada chamada para garantir a unicidade do próximo ID.
     *
     * @return O valor atualizado do contador específico para RoboJato.
     */
    @Override
    protected int getContador() {
        contador++;
        return contador;
    }
}