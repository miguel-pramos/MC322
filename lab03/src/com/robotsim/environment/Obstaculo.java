package com.robotsim.environment;

import java.util.Random;
import com.robotsim.environment.TipoObstaculo;
import com.robotsim.Controlador;

public class Obstaculo {
    private int posX;
    private int posY;
    private TipoObstaculo tipo;
    private final TipoObstaculo[] tipos = TipoObstaculo.values()
            ;
    public Obstaculo() {
        Random rand = new Random();
        this.tipo = tipos[rand(5)];
        posX = rand(Controlador.getAmbiente().getComprimento());
        posY = rand(Controlador.getAmbiente().getLargura());

        while (!boaPosicao(tipo, posX, posY)) {
            posX = rand(Controlador.getAmbiente().getComprimento());
            posY = rand(Controlador.getAmbiente().getLargura());
        }

        this.posX = posX;
        this.posY = posY;
    }

    public boolean boaPosicao (TipoObstaculo tipo, int testeX, int testeY){
        int metadeComprimento = (tipo.comprimento - 1) / 2;
        int metadeLargura = (tipo.largura - 1) / 2

        boolean dentroDosLimitesSuperior = Controlador.getAmbiente().dentroDosLimites(
                testeX + metadeComprimento,
                testeY + metadeLargura
        );

        boolean dentroDosLimitesInferior = Controlador.getAmbiente().dentroDosLimites(
                testeX - metadeComprimento,
                testeY - metadeLargura
        );

        return dentroDosLimitesSuperior && dentroDosLimitesInferior;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public TipoObstaculo getTipo() {
        return tipo;
    }

    public String getNome() {
        return tipo.name;
    }
}
