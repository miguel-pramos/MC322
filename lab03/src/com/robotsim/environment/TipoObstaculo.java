package com.robotsim.environment;



public enum TipoObstaculo {
    arameFarpado(3, 7, 5, 15),
    minaTerrestre(2, 2, 1, 150),
    bunker(10, 8, 30, 0);


    private final int largura;
    private final int comprimento;
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
