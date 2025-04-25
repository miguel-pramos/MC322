package com.robotsim.environment;



public enum TipoObstaculo {
    arameFarpado(1, 5, 5, 15),
    minaTerrestre(1, 1, 1, 150),
    bunker(7, 5, 30, 0),
    sobrado(11, 7, 45, 0),
    predio(15, 11, 160, 0);

    protected final int largura;
    protected final int comprimento;
    private final int altura;
    private final int dano;

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
