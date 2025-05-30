package com.robotsim.environment.obstacle;

/**
 * Enumeração que define os diferentes tipos de obstáculos que podem existir no
 * ambiente de simulação.
 * <p>
 * Cada tipo de obstáculo é caracterizado por suas dimensões (largura,
 * comprimento, altura),
 * o dano que pode causar ao contato, e uma representação visual (um caractere).
 * Estes atributos são utilizados para configurar e interagir com os obstáculos
 * na simulação.
 * </p>
 */
public enum TipoObstaculo {
    /**
     * Arame farpado: um obstáculo terrestre de pequenas dimensões que causa dano
     * moderado.
     * Largura: 1, Comprimento: 5, Altura: 5, Dano: 15, Representação: '#'.
     */
    arameFarpado(1, 5, 5, 15, '#'),
    /**
     * Mina terrestre: um obstáculo pequeno, quase imperceptível, que causa dano
     * muito alto ao ser ativado.
     * Largura: 1, Comprimento: 1, Altura: 1, Dano: 150, Representação: '*'.
     */
    minaTerrestre(1, 1, 1, 150, '*'),
    /**
     * Bunker: uma estrutura fortificada de tamanho médio, oferece cobertura e não
     * causa dano por si só.
     * Largura: 7, Comprimento: 5, Altura: 30, Dano: 0, Representação: 'B'.
     */
    bunker(7, 5, 30, 0, 'B'),
    /**
     * Sobrado: uma edificação de tamanho grande, não causa dano.
     * Largura: 11, Comprimento: 7, Altura: 45, Dano: 0, Representação: 'S'.
     */
    sobrado(11, 7, 45, 0, 'S'),
    /**
     * Prédio: uma estrutura muito grande, não causa dano.
     * Largura: 15, Comprimento: 11, Altura: 160, Dano: 0, Representação: 'P'.
     */
    predio(15, 11, 160, 0, 'P');

    protected final int largura; // Largura do obstáculo em unidades do ambiente.
    protected final int comprimento; // Comprimento do obstáculo em unidades do ambiente.
    private final int altura; // Altura do obstáculo em unidades do ambiente.
    private final int dano; // Quantidade de dano que o obstáculo causa ao contato.
    private final char representacao; // Caractere usado para exibir o obstáculo em representações textuais do
                                      // ambiente.

    /**
     * Construtor para cada tipo de obstáculo, definindo seus atributos.
     *
     * @param largura       A largura do obstáculo.
     * @param comprimento   O comprimento do obstáculo.
     * @param altura        A altura do obstáculo.
     * @param dano          O valor de dano infligido pelo obstáculo.
     * @param representacao O caractere que representa o obstáculo visualmente.
     */
    TipoObstaculo(int largura, int comprimento, int altura, int dano, char representacao) {
        this.largura = largura;
        this.comprimento = comprimento;
        this.altura = altura;
        this.dano = dano;
        this.representacao = representacao;
    }

    /**
     * Retorna a largura do obstáculo.
     *
     * @return A largura do obstáculo.
     */
    public int getLargura() {
        return largura;
    }

    /**
     * Retorna o comprimento do obstáculo.
     *
     * @return O comprimento do obstáculo.
     */
    public int getComprimento() {
        return comprimento;
    }

    /**
     * Retorna a altura do obstáculo.
     *
     * @return A altura do obstáculo.
     */
    public int getAltura() {
        return altura;
    }

    /**
     * Retorna o valor de dano que este tipo de obstáculo causa.
     *
     * @return O dano causado pelo obstáculo.
     */
    public int getDano() {
        return dano;
    }

    /**
     * Retorna o caractere de representação visual do obstáculo.
     *
     * @return O caractere de representação.
     */
    public char getRepresentacao() {
        return representacao;
    }
}
