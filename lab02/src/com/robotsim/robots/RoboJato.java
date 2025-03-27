package com.robotsim.robots;

import com.robotsim.Controlador;
import com.robotsim.util.GeometryMath;

public class RoboJato extends RoboAereo{
    private int HP = 100;
    private int misseisRestantes = 2;
    private int rajadasRestantes = 10;
    private final int alcanceMissil = 1;
    private final int alcanceMetralhadora = 1;
    private final int danoMissil = 200;
    private final int danoMetralhadora = 20;

    public RoboJato(String nome, int posicaoX, int posicaoY, int posicaoZ, int HP, int altitudeMaxima, int misseisRestantes, int rajadasRestantes) {
        super(nome, posicaoX, posicaoY, posicaoZ, HP, altitudeMaxima);
        this.HP = HP;
        this.misseisRestantes = misseisRestantes;
        this.rajadasRestantes = rajadasRestantes;
    }

    public void lancarMissil(RoboAereo alvo){
        if (GeometryMath.distanciaEuclidiana(alvo.getPosicaoX(), getPosicaoY(), getPosicaoZ()) < alcanceMissil){
            alvo.tomarDano(danoMissil);
        }
        misseisRestantes --;
    }

    public void atirarRajada(RoboTerrestre alvo){
        if(GeometryMath.distanciaEuclidiana(alvo.getPosicaoX(), alvo.getPosicaoY(), 0) < alcanceMetralhadora){
            alvo.tomarDano(danoMetralhadora);
        }
        rajadasRestantes --;
    }
}