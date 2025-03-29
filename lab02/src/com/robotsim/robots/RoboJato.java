package com.robotsim.robots;

import com.robotsim.Controlador;
import com.robotsim.etc.Acao;
import com.robotsim.etc.CatalogoRobos;
import com.robotsim.util.GeometryMath;

public class RoboJato extends RoboAereo {
    private int misseisRestantes = 2;
    private int rajadasRestantes = 10;
    private final int alcanceMissil = 1;
    private final int alcanceMetralhadora = Controlador.ambiente.getComprimento() / 12;
    private final int danoMissil = 200;
    private final int danoMetralhadora = 20;

    static {
        // Registro do robô no catálogo
        altitudeMaxima = 10;
        CatalogoRobos.registrarRobo("Robo Aéreo", RoboJato.class);
    }

    @Override
    protected void inicializarAcoes() {
        super.inicializarAcoes();
        acoes.add(new AtirarRajada(this));
        acoes.add(new LancarMissil(this));
    }

    public RoboJato(String nome, int posicaoX, int posicaoY) {
        super(nome, posicaoX, posicaoY);
        this.altitude = 2;
    }

    protected void lancarMissil(RoboAereo alvo) {
        if (misseisRestantes <= 0)
            throw new IllegalStateException("Nenhum míssil restante");

        if (GeometryMath.distanciaEuclidiana(alvo.getPosicaoX(), alvo.getPosicaoY(),
                alvo.getAltitude()) < alcanceMissil) {
            alvo.tomarDano(danoMissil);
        }
        misseisRestantes--;
    }

    protected void atirarRajada(RoboTerrestre alvo) {
        if (rajadasRestantes <= 0)
            throw new IllegalStateException("Nenhuma rajada restante"); 

        if (GeometryMath.distanciaEuclidiana(alvo.getPosicaoX(), alvo.getPosicaoY(), 0) < alcanceMetralhadora) {
            alvo.tomarDano(danoMetralhadora);
        }
        rajadasRestantes--;
    }

    private class LancarMissil implements Acao {
        RoboJato robo;

        public LancarMissil(RoboJato robo) {
            this.robo = robo;
        }

        @Override
        public String getNome() {
            return "Lançar míssil";
        }

        @Override
        public void executar(Object... args) {
            if (args.length != 1 || !(args[0] instanceof RoboAereo)) {
                throw new IllegalArgumentException("Atirar requer um alvo do tipo RoboAereo.");
            }

            RoboAereo alvo = (RoboAereo) args[0];
            robo.lancarMissil(alvo);
        }
    }
    
    private class AtirarRajada implements Acao {
        RoboJato robo;

        public AtirarRajada(RoboJato robo) {
            this.robo = robo;
        }

        @Override
        public String getNome() {
            return "Atirar Rajada";
        }

        @Override
        public void executar(Object... args) {
            if (args.length != 1 || !(args[0] instanceof RoboTerrestre)) {
                throw new IllegalArgumentException("Atirar requer um alvo do tipo RoboTerrestre.");
            }
            
            RoboTerrestre alvo = (RoboTerrestre) args[0];
            robo.atirarRajada(alvo);
        }
    }
}