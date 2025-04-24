package com.robotsim.environment;



public enum TipoObstaculo {
    arameFarpado(3, 7, 5, 15),
    minaTerrestre(3, 3, 1, 150),
    bunker(9, 7, 30, 0),
    sobrado(15, 9, 45, 0),
    predio(21, 17, 160, 0);

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
