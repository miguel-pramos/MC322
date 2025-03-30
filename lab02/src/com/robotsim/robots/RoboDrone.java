package com.robotsim.robots;

import java.util.concurrent.TimeUnit;

import com.robotsim.Controlador;
import com.robotsim.etc.Acao;
import com.robotsim.etc.CatalogoRobos;
import com.robotsim.util.GeometryMath;

public class RoboDrone extends RoboAereo {
    private int bateria = 200;
    private final int alcanceDeteccao = 50;

    public RoboDrone(String nome, int posicaoX, int posicaoY) {
        super(nome, posicaoX, posicaoY);
    }

    @Override
    protected void inicializarAcoes() {
        super.inicializarAcoes();
        acoes.add(new DetectarRobo(this));
    }

    public void mover(int deltaX, int deltaY, int deltaZ) {
        bateria -= deltaX + deltaY + deltaZ;

        if (bateria <= 0) {
            Controlador.ambiente.matarRobo(this);
        } else {
            this.mover(deltaX, deltaY);
            this.altitude += deltaZ;

            System.out.println("Sua bateria está em " + bateria);
        }
    }


    private class DetectarRobo implements Acao {
        RoboDrone robo;

        public DetectarRobo(RoboDrone robo) {
            this.robo = robo;
        }

        @Override
        public String getNome() {
            return "Detectar robô";
        }

        @Override
        public void executar() {
            System.out.println("Iniciando o processo de detecção...");

            try {
                TimeUnit.MILLISECONDS.sleep(1600);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Boa prática: reinterromper a thread
                System.out.println("A execução foi interrompida.");
            }
            double distancia;
            for (Robo robo : Controlador.ambiente.getRobos()) {
                if (robo instanceof RoboAereo){
                    distancia = (GeometryMath.distanciaEuclidiana(robo.getPosicaoX(),
                            robo.getPosicaoY(), ((RoboAereo) robo).getAltitude()));

                    if (distancia < alcanceDeteccao) {
                        System.out.printf("O robô %s está na posição (%d, %d, %d)", robo.getNome(),
                                robo.getPosicaoX(), robo.getPosicaoY(), ((RoboAereo) robo).getAltitude());
                    }
                }else if (robo instanceof RoboTerrestre) {
                    distancia = (GeometryMath.distanciaEuclidiana(robo.getPosicaoX(),
                            robo.getPosicaoY(), 0));

                    if (distancia < alcanceDeteccao) {
                        System.out.printf("O robô %s está na posição (%d, %d, %d)", robo.getNome(),
                                robo.getPosicaoX(), robo.getPosicaoY(), 0);
                    }

                    }
                else {
                    System.out.println("Tipo desconhecido");
                }
            }
        }
    }
}