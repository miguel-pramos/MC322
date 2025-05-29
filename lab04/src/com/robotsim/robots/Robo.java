package com.robotsim.robots;

import java.util.ArrayList;
import java.util.Scanner;

import com.robotsim.Controlador;
import com.robotsim.environment.entity.Comunicavel;
import com.robotsim.environment.entity.Entidade;
import com.robotsim.environment.entity.TipoEntidade;
import com.robotsim.util.TesteColisao;
import com.robotsim.etc.Acao;
import com.robotsim.etc.EstadoRobo;
import com.robotsim.exceptions.ColisaoException;
import com.robotsim.exceptions.ErroComunicacaoException;
import com.robotsim.exceptions.RoboDesligadoException;
import com.robotsim.robots.sensors.Sensor;

/**
 * A classe Robo é a classe base para todos os tipos de robôs no simulador.
 * Ela define propriedades e comportamentos comuns, como posição, nome e ações.
 */
public abstract class Robo implements Comunicavel, Entidade {
    private String nome; // Nome do robô.
    private String id; // Id do robô.
    private TipoEntidade tipo; // Tipo da entidade.
    private EstadoRobo estado; // Ligado ou desligado.
    private int HP; // Pontos de vida do robô.
    private int x; // Posição atual no eixo X.
    private int y; // Posição atual no eixo Y.
    private int z; // Posição atual no eixo Z.
    protected ArrayList<Acao> acoes; // Lista de ações disponíveis para o robô.
    protected ArrayList<Sensor> sensores; // Sensores do robô

    public Robo(String nome, int x, int y, int HP) {
        this(nome, x, y, 0, HP);
    }

    public Robo(String nome, int x, int y, int z, int HP) {
        this.nome = nome;
        this.x = x;
        this.y = y;
        this.HP = HP;
        this.id = criaID(getContador());
        this.tipo = TipoEntidade.ROBO;
        this.estado = EstadoRobo.LIGADO;
        this.acoes = new ArrayList<>();
        this.sensores = new ArrayList<>();
        inicializarAcoes();
    }

    public boolean alternarEstado() {
        if (this.estado == EstadoRobo.LIGADO)
            this.estado = EstadoRobo.DESLIGADO;
        else
            this.estado = EstadoRobo.LIGADO;
        return (this.estado == EstadoRobo.LIGADO);
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
     * @param acao A ação a ser executada.
     * @throws RoboDesligadoException Se o robô estiver desligado.
     */
    public void executarTarefa(Acao acao) throws RoboDesligadoException {
        if (this.estado != EstadoRobo.LIGADO) {
            throw new RoboDesligadoException("O robô " + this.nome + " está desligado e não pode executar ações.");
        }
        try {
            acao.executar();
        } catch (Exception e) { // Captura outras exceções que podem ocorrer durante a execução da ação
            System.out.println("Erro ao executar a ação '" + acao.getNome() + "' para o robô " + this.nome + ": "
                    + e.getMessage());
            // Opcional: imprimir stack trace para depuração
            // e.printStackTrace();
        }
    }

    /**
     * Método responsável por mover o robô para uma nova posição.
     *
     * @param deltaX Deslocamento no eixo X.
     * @param deltaY Deslocamento no eixo Y.
     */

    protected void mover(int deltaX, int deltaY) {
        int xFinal = this.x + deltaX;
        int yFinal = this.y + deltaY;

        try {
            Controlador.getAmbiente()
                    .dentroDosLimites(xFinal, yFinal, 0);

            int[] dadosPossivelColisao = TesteColisao.dadosColisao(this, xFinal, yFinal);

            this.x = dadosPossivelColisao[0];
            this.y = dadosPossivelColisao[1];
            if (TesteColisao.existeColisao(dadosPossivelColisao)) {
                System.out.printf(
                        "Colisão detectada! Parando na posição (%d, %d)\n", x, y);
            }
            if (dadosPossivelColisao[2] != 0) {
                this.tomarDano(dadosPossivelColisao[2]);
            }
        } catch (ColisaoException e) {
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
            Controlador.getAmbiente().removerEntidade(this);
    }

    /**
     * Método que exibe a posição atual do robô.
     */
    public void exibirPosicao() {
        System.out.println(nome + " está na posição (" + this.x + ", " + this.y + ")");
    }

    @Override
    public void enviarMensagens(Comunicavel comunicavel, String mensagem)
            throws RoboDesligadoException, ErroComunicacaoException {
        if (this.estado == EstadoRobo.DESLIGADO)
            throw new RoboDesligadoException("O robô %s está desligado.".formatted(this.nome));

        comunicavel.receberMensagens(mensagem);
        Controlador.getComunicacao().registrarMensagem(this.nome, mensagem);
    }

    @Override
    public void receberMensagens(String mensagem) throws RoboDesligadoException, ErroComunicacaoException {
        if (this.estado == EstadoRobo.DESLIGADO)
            throw new RoboDesligadoException("O robô %s está desligado.".formatted(this.nome));
    }

    public ArrayList<Acao> getAcoes() {
        return new ArrayList<>(acoes);
    }

    public ArrayList<Sensor> getSensores() {
        return new ArrayList<>(sensores);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }

    public String getId() {
        return id;
    }

    public TipoEntidade getTipo() {
        return tipo;
    }

    public EstadoRobo getEstado() {
        return estado;
    }

    public void setEstado(EstadoRobo estado) {
        this.estado = estado;
    }

    public boolean isLigado() {
        return this.estado == EstadoRobo.LIGADO;
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

    protected String criaID(int contador) {
        // Formata o contador para ter dois dígitos, adicionando um zero à esquerda se
        // necessário.
        // Exemplo: contador 1 se torna "01", contador 10 se torna "10".
        String id = String.valueOf(contador);
        if (contador < 10) {
            id = '0' + id;
        }

        Class<?> classeAtual = this.getClass();
        while (true) {
            String specs = classeAtual.getSimpleName();
            id = specs.charAt(4) + id;
            classeAtual = classeAtual.getSuperclass();
            if (classeAtual == Robo.class) {
                id = specs.charAt(0) + id;
                break;
            }
        }

        return id;
    }

    protected abstract int getContador();
}
