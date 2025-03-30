package com.robotsim.robots;

import java.util.Scanner;

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
        public void executar() {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Digite a altitude a descer: ");
            int deltaZ = scanner.nextInt();

            scanner.close();

            if (deltaZ <= 0) {
                System.out.println("A altitude deve ser um valor positivo.");
                return;
            }

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
        public void executar() {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Digite a altitude a subir: ");
            int deltaZ = scanner.nextInt();

            scanner.nextLine(); // Consumir \n

            scanner.close();

            if (deltaZ <= 0) {
                System.out.println("A altitude deve ser um valor positivo.");
                return;
            }

            robo.subir(deltaZ);
        }
    }

}
