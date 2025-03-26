package com.robotsim.robots;

import com.robotsim.util.GeometryMath;

public class RoboJato extends RoboAereo {
    private int misseisRestantes = 4;
    private int rajadasRestantes = 10;
    private int HP = 100;

    public RoboJato(String nome, int posicaoX, int posicaoY, int altitude, int altitudeMaxima) {

    }

    public void lancarMissil(RoboAereo alvo){
        if (GeometryMath.distanciaEuclidiana(alvo.getPosicaoX(), getPosicaoY(), getPosicaoZ()));
    }

    public void lancarMissil(RoboTerrestre alvo){
        if(GeometryMath.distanciaEuclidiana){
            if (GeometryMath.distanciaEuclidiana(alvo.getPosicaoX(), getPosicaoY(), 0));
        }
}