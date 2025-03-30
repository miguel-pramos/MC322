package com.robotsim.robots;

import java.util.Scanner;

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

    public void detectarRobo(RoboAereo alvo) {
        if (GeometryMath.distanciaEuclidiana(alvo.getPosicaoX(), getPosicaoY(), getAltitude()) < alcanceDeteccao) {
            System.out.print("O robo " + alvo.getNome() + " está na posição:");
            System.out.println("(%d, %d, %d)".formatted(alvo.getPosicaoX(), alvo.getPosicaoY(), alvo.getAltitude()));
        }
    }

    public void detectarRobo(RoboTerrestre alvo) {
        if (GeometryMath.distanciaEuclidiana(alvo.getPosicaoX(), getPosicaoY(), 0) < alcanceDeteccao) {
            System.out.print("O robo " + alvo.getNome() + " está na posição:");
            System.out.println("(%d, %d, 0)".formatted(alvo.getPosicaoX(), alvo.getPosicaoY()));
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
            System.out.println("Robôs disponíveis para detecção:");
            int i = 0;

            for (Robo robo : Controlador.ambiente.getRobos()) {
                if (robo != this.robo) { // Não permitir detectar a si mesmo
                    System.out.printf("[%d] %s\n", i, robo.getNome());
                    i++;
                }
            }

            if (i == 0) {
                System.out.println("Não há robôs para detectar.");
                return;
            }

            Scanner scanner = new Scanner(System.in);
            System.out.print("Escolha o índice do robô para detectar: ");
            int indice = scanner.nextInt();

            scanner.close();

            if (indice < 0 || indice >= Controlador.ambiente.getRobos().size()) {
                System.out.println("Índice inválido.");
                return;
            }

            Robo alvo = Controlador.ambiente.getRobos().get(indice);

            if (alvo instanceof RoboAereo) {
                robo.detectarRobo((RoboAereo) alvo);
            } else if (alvo instanceof RoboTerrestre) {
                robo.detectarRobo((RoboTerrestre) alvo);
            } else {
                System.out.println("Tipo de robô desconhecido.");
            }
        }
    }
}