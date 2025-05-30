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
    /** Nome identificador do robô. */
    private String nome; // Nome do robô.
    /** Identificador único do robô, gerado automaticamente. */
    private String id; // Id do robô.
    /** Categoria da entidade (neste caso, ROBO). */
    private TipoEntidade tipo; // Tipo da entidade.
    /** Estado atual do robô (LIGADO ou DESLIGADO). */
    private EstadoRobo estado; // Ligado ou desligado.
    /** Pontos de vida (Health Points) do robô. */
    private int HP; // Pontos de vida do robô.
    /** Coordenada X da posição atual do robô no ambiente. */
    private int x; // Posição atual no eixo X.
    /** Coordenada Y da posição atual do robô no ambiente. */
    private int y; // Posição atual no eixo Y.
    /**
     * Coordenada Z da posição atual do robô no ambiente (relevante para robôs
     * aéreos).
     */
    private int z; // Posição atual no eixo Z.
    /**
     * Lista de ações que o robô pode executar.
     */
    protected ArrayList<Acao> acoes; // Lista de ações disponíveis para o robô.
    /**
     * Lista de sensores equipados no robô.
     */
    protected ArrayList<Sensor> sensores; // Sensores do robô

    /**
     * Construtor para robôs, inicializando-os em uma posição 2D (z=0).
     *
     * @param nome Nome do robô.
     * @param x    Posição inicial no eixo X.
     * @param y    Posição inicial no eixo Y.
     * @param HP   Pontos de vida iniciais.
     */
    public Robo(String nome, int x, int y, int HP) {
        this(nome, x, y, 0, HP);
    }

    /**
     * Construtor para robôs, inicializando-os em uma posição 3D.
     *
     * @param nome Nome do robô.
     * @param x    Posição inicial no eixo X.
     * @param y    Posição inicial no eixo Y.
     * @param z    Posição inicial no eixo Z.
     * @param HP   Pontos de vida iniciais.
     */
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

    /**
     * Alterna o estado do robô entre LIGADO e DESLIGADO.
     * 
     * @return true se o robô ficou ligado após a operação, false se ficou
     *         desligado.
     */
    public boolean alternarEstado() {
        if (this.estado == EstadoRobo.LIGADO)
            this.estado = EstadoRobo.DESLIGADO;
        else
            this.estado = EstadoRobo.LIGADO;
        return (this.estado == EstadoRobo.LIGADO);
    }

    /**
     * Inicializa as ações básicas do robô. Subclasses devem sobrescrever para
     * adicionar ações específicas.
     */
    protected void inicializarAcoes() {
        acoes.add(new Mover(this));
    }

    /**
     * Inicializa as ações relacionadas aos sensores do robô.
     * Deve ser chamado após adicionar sensores específicos.
     */
    protected void inicializarSensores() {
        for (Sensor sensor : this.sensores)
            acoes.add(sensor.getAcao());
    }

    /**
     * Executa uma ação, caso o robô esteja ligado.
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
     * Move o robô para uma nova posição, considerando colisões e limites do
     * ambiente.
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
     * Aplica dano ao robô. Remove o robô do ambiente se o HP ficar abaixo de zero.
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
     * Exibe a posição atual do robô no ambiente.
     */
    public void exibirPosicao() {
        System.out.println(nome + " está na posição (" + this.x + ", " + this.y + ")");
    }

    /**
     * Envia mensagens para outra entidade comunicável.
     * Registra a mensagem na central de comunicação.
     *
     * @param comunicavel A entidade que receberá a mensagem.
     * @param mensagem    O conteúdo da mensagem a ser enviada.
     * @throws RoboDesligadoException   Se o robô remetente estiver desligado.
     * @throws ErroComunicacaoException Se ocorrer um erro durante a comunicação.
     */
    @Override
    public void enviarMensagens(Comunicavel comunicavel, String mensagem)
            throws RoboDesligadoException, ErroComunicacaoException {
        if (this.estado == EstadoRobo.DESLIGADO)
            throw new RoboDesligadoException("O robô %s está desligado.".formatted(this.nome));

        comunicavel.receberMensagens(mensagem);
        Controlador.getComunicacao().registrarMensagem(this.nome, mensagem);
    }

    /**
     * Recebe mensagens de outra entidade.
     * Atualmente, apenas verifica se o robô está ligado para receber.
     *
     * @param mensagem A mensagem recebida (atualmente não utilizada diretamente
     *                 aqui).
     * @throws RoboDesligadoException   Se o robô destinatário estiver desligado.
     * @throws ErroComunicacaoException Se ocorrer um erro (não implementado
     *                                  especificamente aqui).
     */
    @Override
    public void receberMensagens(String mensagem) throws RoboDesligadoException, ErroComunicacaoException {
        if (this.estado == EstadoRobo.DESLIGADO)
            throw new RoboDesligadoException("O robô %s está desligado.".formatted(this.nome));
    }

    /**
     * Retorna uma cópia da lista de ações disponíveis para o robô.
     *
     * @return Uma nova {@link ArrayList} contendo as ações do robô.
     */
    public ArrayList<Acao> getAcoes() {
        return new ArrayList<>(acoes);
    }

    /**
     * Retorna uma cópia da lista de sensores do robô.
     *
     * @return Uma nova {@link ArrayList} contendo os sensores do robô.
     */
    public ArrayList<Sensor> getSensores() {
        return new ArrayList<>(sensores);
    }

    /**
     * Obtém a coordenada X atual do robô.
     *
     * @return A coordenada X.
     */
    public int getX() {
        return x;
    }

    /**
     * Define a coordenada X do robô.
     *
     * @param x A nova coordenada X.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Obtém a coordenada Y atual do robô.
     *
     * @return A coordenada Y.
     */
    public int getY() {
        return y;
    }

    /**
     * Define a coordenada Y do robô.
     *
     * @param y A nova coordenada Y.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Obtém a coordenada Z atual do robô.
     *
     * @return A coordenada Z.
     */
    public int getZ() {
        return z;
    }

    /**
     * Define a coordenada Z do robô.
     *
     * @param z A nova coordenada Z.
     */
    public void setZ(int z) {
        this.z = z;
    }

    /**
     * Obtém o nome do robô.
     *
     * @return O nome do robô.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do robô.
     *
     * @param nome O novo nome do robô.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Obtém os pontos de vida (HP) atuais do robô.
     *
     * @return O HP atual.
     */
    public int getHP() {
        return HP;
    }

    /**
     * Define os pontos de vida (HP) do robô.
     *
     * @param HP O novo valor de HP.
     */
    public void setHP(int HP) {
        this.HP = HP;
    }

    /**
     * Obtém o ID único do robô.
     *
     * @return O ID do robô.
     */
    public String getId() {
        return id;
    }

    /**
     * Obtém o tipo da entidade (ROBO).
     *
     * @return O {@link TipoEntidade} do robô.
     */
    public TipoEntidade getTipo() {
        return tipo;
    }

    /**
     * Obtém o estado atual do robô (LIGADO/DESLIGADO).
     *
     * @return O {@link EstadoRobo} atual.
     */
    public EstadoRobo getEstado() {
        return estado;
    }

    /**
     * Define o estado do robô.
     *
     * @param estado O novo {@link EstadoRobo}.
     */
    public void setEstado(EstadoRobo estado) {
        this.estado = estado;
    }

    /**
     * Verifica se o robô está ligado.
     *
     * @return true se o robô estiver no estado LIGADO, false caso contrário.
     */
    public boolean isLigado() {
        return this.estado == EstadoRobo.LIGADO;
    }

    /**
     * Classe interna que implementa a ação de mover para o {@link Robo}.
     * Solicita ao usuário os deslocamentos nos eixos X e Y e chama o método
     * {@link Robo#mover(int, int)}.
     */
    private class Mover implements Acao {
        /** O robô que executará a ação de mover. */
        Robo robo;

        /**
         * Construtor para a ação Mover.
         * 
         * @param robo O {@link Robo} associado a esta ação.
         */
        public Mover(Robo robo) {
            this.robo = robo;
        }

        /**
         * Obtém o nome da ação.
         * 
         * @return O nome da ação ("Mover").
         */
        @Override
        public String getNome() {
            return "Mover";
        }

        /**
         * Executa a ação de mover.
         * Solicita ao usuário os deslocamentos deltaX e deltaY e move o robô.
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

    /**
     * Cria um ID único para o robô.
     * O ID é formado por caracteres do nome da classe e suas superclasses,
     * concatenados com um contador formatado.
     *
     * @param contador Um número inteiro usado para diferenciar instâncias da mesma
     *                 subclasse de robô.
     * @return Uma string representando o ID único gerado para o robô.
     */
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

    /**
     * Método abstrato que deve ser implementado pelas subclasses para fornecer
     * um contador específico para a geração de IDs.
     *
     * @return O valor do contador da subclasse.
     */
    protected abstract int getContador();
}
