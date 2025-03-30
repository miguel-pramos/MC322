package com.robotsim.robots;

import java.util.ArrayList;
import java.util.Scanner;

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

        if (GeometryMath.distanciaEuclidiana(this, alvo.getPosicaoX(), alvo.getPosicaoY(),
                alvo.getAltitude()) < alcanceMissil) {
            alvo.tomarDano(danoMissil);
        }
        misseisRestantes--;
    }

    protected void atirarRajada(RoboTerrestre alvo) {
        if (rajadasRestantes <= 0)
            throw new IllegalStateException("Nenhuma rajada restante"); 

        if (GeometryMath.distanciaEuclidiana(this, alvo.getPosicaoX(), alvo.getPosicaoY(), 0) < alcanceMetralhadora) {
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
        public void executar() {
            ArrayList<RoboAereo> robosAlvos = new ArrayList<>();
            int i = 0;

            for (Robo robo : Controlador.ambiente.getRobos()) {
                if (robo != this.robo) {  // Não permitir atacar a si mesmo
                    robosAlvos.add((RoboAereo) robo);
                    System.out.printf("[%d] %s\n", i, robo.getNome());
                    i++;
                }
            }

            if (robosAlvos.isEmpty()) {
                System.out.println("Não há robôs para atacar.");
                return;
            }

            Scanner scanner = new Scanner(System.in);
            System.out.print("Escolha o índice do robô para atacar com míssil: ");
            int indice = scanner.nextInt();

            scanner.close();

            if (indice < 0 || indice >= robosAlvos.size()) {
                System.out.println("Índice inválido.");
                return;
            }

            RoboAereo alvo = robosAlvos.get(indice);
            try {
                robo.lancarMissil(alvo);
            } catch (IllegalStateException e) {
                System.out.println(e.getMessage());
            }

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
        public void executar() {
            ArrayList<RoboTerrestre> robosAlvos = new ArrayList<>();
            int i = 0;

            for (Robo robo : Controlador.ambiente.getRobos()) {
                if (robo instanceof RoboTerrestre) {  // Não permitir atacar a si mesmo
                    robosAlvos.add((RoboTerrestre) robo);
                    System.out.printf("[%d] %s\n", i, robo.getNome());
                    i++;
                }
            }

            if (robosAlvos.isEmpty()) {
                System.out.println("Não há robôs para atacar.");
                return;
            }

            Scanner scanner = new Scanner(System.in);
            System.out.print("Escolha o índice do robô para atacar com rajada: ");
            int indice = scanner.nextInt();

            scanner.close();

            if (indice < 0 || indice >= robosAlvos.size()) {
                System.out.println("Índice inválido.");
                return;
            }
            

            RoboTerrestre alvo = robosAlvos.get(indice);
            try {
                robo.atirarRajada(alvo);
            } catch (IllegalStateException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}