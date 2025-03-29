package com.robotsim.robots;

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
            System.out.println("(" + alvo.getPosicaoX() + "," + alvo.getPosicaoY() + "," + alvo.getAltitude() + ")");
        }
    }

    public void detectarRobo(RoboTerrestre alvo) {
        if (GeometryMath.distanciaEuclidiana(alvo.getPosicaoX(), getPosicaoY(), 0) < alcanceDeteccao) {
            System.out.print("O robo " + alvo.getNome() + " está na posição:");
            System.out.println("(" + alvo.getPosicaoX() + "," + alvo.getPosicaoY() + ",0)");
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
        public void executar(Object... args) {
            if (args.length != 1 || !(args[0] instanceof RoboTerrestre || args[0] instanceof RoboAereo)) {
                throw new IllegalArgumentException("Atirar requer um alvo do tipo RoboTerrestre ou do tipo RoboAereo.");
            }

            if (args[0] instanceof RoboAereo) {
                RoboAereo alvo = (RoboAereo) args[0];
                this.robo.detectarRobo(alvo);
                return;
            }

            RoboTerrestre alvo = (RoboTerrestre) args[0];
            this.robo.detectarRobo(alvo);
        }
    }
}