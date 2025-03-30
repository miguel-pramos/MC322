package com.robotsim.robots;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.robotsim.Controlador;
import com.robotsim.etc.Acao;
import com.robotsim.etc.CatalogoRobos;
import com.robotsim.util.GeometryMath;


public class RoboDrone extends RoboAereo {
    private int bateria = 200;
    private final int alcanceDeteccao = 50;

    static {
        // Registro do robô no catálogo
        CatalogoRobos.registrarRobo("Robo Aéreo", RoboDrone.class);
    }

    public RoboDrone(String nome, int posicaoX, int posicaoY) {
        super(nome, posicaoX, posicaoY);
    }

    protected void detectarRobos(){
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
                distancia = (GeometryMath.distanciaEuclidiana(this, robo.getPosicaoX(),
                        robo.getPosicaoY(), ((RoboAereo) robo).getAltitude()));

                if (distancia < alcanceDeteccao) {
                    System.out.printf("O robô %s está na posição (%d, %d, %d)", robo.getNome(),
                            robo.getPosicaoX(), robo.getPosicaoY(), ((RoboAereo) robo).getAltitude());
                }
            }
            else if (robo instanceof RoboTerrestre) {
                distancia = (GeometryMath.distanciaEuclidiana(this, robo.getPosicaoX(),
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

    protected void mover(int deltaX, int deltaY) {
        this.bateria -= deltaX + deltaY;

        if (this.bateria <= 0) {
            Controlador.ambiente.matarRobo(this);
        } else {
            this.mover(deltaX, deltaY);
            System.out.printf("Sua bateria está em %d%n", this.bateria);
        }
    }

    @Override
    protected void inicializarAcoes() {
        acoes.add(new DetectarRobos(this));
        acoes.add(new Mover(this));
        super.inicializarAcoes();

    }


    private class DetectarRobos implements Acao {
        RoboDrone robo;

        public DetectarRobos(RoboDrone robo) {
            this.robo = robo;
        }

        @Override
        public String getNome() {
            return "Detectar robôs";
        }

        @Override
        public void executar() {
            robo.detectarRobos();
        }
    }

    private class Mover implements Acao {
        RoboDrone robo;

        public Mover(RoboDrone robo) {
            this.robo = robo;
        }

        @Override
        public String getNome() {
            return "Mover";
        }

        @Override
        public void executar() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Quanto você deseja mover em cada direção? Formato: 'x y' ");
            int deltaX = scanner.nextInt();
            int deltaY = scanner.nextInt();
            scanner.close();

            robo.mover(deltaX, deltaY);
        }
    }
}