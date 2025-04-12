package com.robotsim.environment;

public class Obstaculo {
    private int posX;
    private int posY;
    private int posZ;
    private TipoObstaculo tipo;

    public Obstaculo(TipoObstaculo tipo, int posX, int posY, int posZ) {
        this.tipo = tipo;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getPosZ() {
        return posZ;
    }

    public TipoObstaculo getTipo() {
        return tipo;
    }
}
