package com.robotsim.robots;

import java.util.ArrayList;
import java.util.Scanner;

import com.robotsim.Controlador;
import com.robotsim.environment.Obstaculo;
import com.robotsim.environment.TipoObstaculo;
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
        int xIni = this.posicaoX;
        int yIni = this.posicaoY;
        int xFinal = this.posicaoX + deltaX;
        int yFinal = this.posicaoY + deltaY;

        boolean nosLimites = Controlador.getAmbiente()
                .dentroDosLimites(xFinal, yFinal);

        int[] dadosPossivelColisao = dadosColisao(xIni, yIni, xFinal, yFinal);

        if (nosLimites) {
            if (dadosPossivelColisao[0] == -1) {
                this.posicaoX += deltaX;
                this.posicaoY += deltaY;
            }
            else {
                this.posicaoX = dadosPossivelColisao[0] - 1;
                this.posicaoY = dadosPossivelColisao[1] - 1;
                this.tomarDano(dadosPossivelColisao[2]);
            }
        }
        else{
            System.out.println("Você está fora dos limites do ambiente. Ação cancelada!");
        }
    }

    private String tipoDeColisao(int xRobo, int yRobo){
        for(Robo robo : Controlador.getAmbiente().getRobos()){
            if(robo.posicaoX == xRobo && robo.posicaoY == yRobo){
                return "Robo";
            }
        }

        for(Obstaculo obstaculo : Controlador.getAmbiente().getObstaculos()){
            int obsSupX = obstaculo.getPosX() + obstaculo.getTipo().getComprimento();
            int obsSupY = obstaculo.getPosY() + obstaculo.getTipo().getLargura();

            int obsInfX = obstaculo.getPosX() - obstaculo.getTipo().getComprimento();
            int obstInfY = obstaculo.getPosY() - obstaculo.getTipo().getLargura();

            if (xRobo < obsInfX || xRobo > obsSupX) {
                continue;
            }
            if (yRobo < obstInfY || yRobo > obsSupY) {
                continue;
            }

            // Se nenhuma das condições acima for verdadeira, há colisão
            return obstaculo.getNome();
        }

        return "Nula";
    }

    private int[] dadosColisao(int xIni, int yIni, int xFin, int yFin){
        int[] dados = {-1, -1, -1};

        int dx = Math.abs(xFin - xIni);
        int dy = Math.abs(yFin - yIni);

        int sx = xIni < xFin ? 1 : -1;
        int sy = yIni < yFin ? 1 : -1;

        int err = dx - dy;
        int atualX = xIni;
        int atualY = yIni;

        while (true) {
            if (atualX == xFin && atualY == yFin)
                break;

            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                atualX += sx;
            }
            if (e2 < dx) {
                err += dx;
                atualY += sy;
            }

            String tipo = tipoDeColisao(atualX, atualY);
            switch (tipo) {
                case "Nula" -> continue;
                case "Robo" -> {
                    System.out.printf("Colidiu com um robo em %d %d\n", atualX, atualY);
                    dados[0] = atualX;
                    dados[1] = atualY;
                    dados[2] = 0;
                    return dados;
                };
                default -> {
                    TipoObstaculo obstColidido = TipoObstaculo.valueOf(tipo);
                    System.out.printf("Você colidiu com um %s\n", tipo);
                    dados[0] = atualX;
                    dados[1] = atualY;
                    dados[2] = obstColidido.getDano();
                    return dados;
                }
            };
        }

        return dados;
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
