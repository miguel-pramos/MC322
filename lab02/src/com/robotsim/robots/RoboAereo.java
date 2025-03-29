package com.robotsim.robots;

import com.robotsim.etc.Acao;

public abstract class RoboAereo extends Robo {
    protected static int altitudeMaxima;
    protected int altitude;

    public RoboAereo(String nome, int posicaoX, int posicaoY) {
        super(nome, posicaoX, posicaoY);
    }

    @Override
    protected void inicializarAcoes() {
        super.inicializarAcoes();
        acoes.add(new Subir(this));
        acoes.add(new Descer(this));
    }

    public void subir(int deltaZ) {
        if (this.altitude + deltaZ < altitudeMaxima) {
            this.altitude += deltaZ;
        } else {
            this.altitude = altitudeMaxima;
        }
    }

    public void descer(int deltaZ) {
        if (this.altitude - deltaZ > 0) {
            this.altitude -= deltaZ;
        } else {
            this.altitude = 0;
        }
    }

    public int getAltitude() {
        return (altitude);
    }

    private class Descer implements Acao {
        RoboAereo robo;

        public Descer(RoboAereo robo) {
            this.robo = robo;
        }

        @Override
        public String getNome() {
            return "Descer";
        }

        @Override
        public void executar(Object... args) {
            if (args.length != 1 || !(args[0] instanceof Integer)) {
                throw new IllegalArgumentException("Descer requer um inteiro representando a altura a descer."); // Estudar necessidade disso
            }
            int deltaZ = (int) args[0];
            robo.descer(deltaZ);
        }
    }
    
    private class Subir implements Acao {
        RoboAereo robo;

        public Subir(RoboAereo robo) {
            this.robo = robo;
        }

        @Override
        public String getNome() {
            return "Subir";
        }
        
        @Override
        public void executar(Object... args) {
            if (args.length != 1 || !(args[0] instanceof Integer)) {
                throw new IllegalArgumentException("Subir requer um inteiro representando a altura a subir."); // Estudar necessidade disso
            }
            int deltaZ = (int) args[0];
            robo.subir(deltaZ);
        }
    }

}
