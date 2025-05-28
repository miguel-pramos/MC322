package com.robotsim.environment.obstacle;

/**
 * O enum TipoObstaculo define os diferentes tipos de obstáculos que podem existir no ambiente.
 * Cada tipo de obstáculo possui dimensões específicas (largura, comprimento e altura) e um valor de dano.
 *
 * <p>
 * Este enum é utilizado para representar obstáculos como arame farpado, minas terrestres, bunkers, sobrados e prédios.
 */
public enum TipoObstaculo {
    arameFarpado(1, 5, 5, 15), // Obstáculo pequeno que causa dano moderado.
    minaTerrestre(1, 1, 1, 150), // Obstáculo pequeno que causa alto dano.
    bunker(7, 5, 30, 0), // Obstáculo médio que não causa dano.
    sobrado(11, 7, 45, 0), // Obstáculo grande que não causa dano.
    predio(15, 11, 160, 0); // Obstáculo muito grande que não causa dano.

    protected final int largura; // Largura do obstáculo.
    protected final int comprimento; // Comprimento do obstáculo.
    private final int altura; // Altura do obstáculo.
    private final int dano; // Dano causado pelo obstáculo.

    /**
     * Construtor do enum TipoObstaculo.
     *
     * @param largura Largura do obstáculo.
     * @param comprimento Comprimento do obstáculo.
     * @param altura Altura do obstáculo.
     * @param dano Dano causado pelo obstáculo.
     */
    TipoObstaculo(int largura, int comprimento, int altura, int dano) {
        this.largura = largura;
        this.comprimento = comprimento;
        this.altura = altura;
        this.dano = dano;
    }

    public int getLargura() {
        return largura;
    }

    public int getComprimento() {
        return comprimento;
    }

    public int getAltura() {
        return altura;
    }

    public int getDano() {
        return dano;
    }
}
