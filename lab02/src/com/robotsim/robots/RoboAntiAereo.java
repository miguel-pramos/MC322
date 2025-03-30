package com.robotsim.robots;

import java.util.ArrayList;
import java.util.Scanner;

import com.robotsim.Controlador;
import com.robotsim.etc.Acao;
import com.robotsim.util.GeometryMath;

public class RoboAntiAereo extends RoboTerrestre {
    private int balasRestantes = 10;
    private int dano = 4;
    private int alcance = Controlador.ambiente.getLargura() / 5;

    public RoboAntiAereo(String nome, int posicaoX, int posicaoY) {
        super(nome, posicaoX, posicaoY);
        this.velocidadeMaxima = 0; // Robo AntiAéreo é fixo
    }

    public void atirar(RoboAereo alvo) {
        if (balasRestantes <= 0)
            throw new IllegalStateException("Nenhuma bala restante"); 

        if (GeometryMath.distanciaEuclidiana(this, alvo.getPosicaoX(), alvo.getPosicaoY()) < this.alcance)
            alvo.tomarDano(this.dano);
        this.balasRestantes--;
    }
    
    @Override
    protected void inicializarAcoes() {
        acoes.add(new Atirar(this));
        super.inicializarAcoes();
    }
    
    private class Atirar implements Acao {
        RoboAntiAereo robo;

        public Atirar(RoboAntiAereo robo) {
            this.robo = robo;
        }

        @Override
        public String getNome() {
            return "Atirar";
        }

        @Override
        public void executar() {
            ArrayList<RoboAereo> robosAlvos = new ArrayList<>();
            int i = 0;

            for (Robo robo : Controlador.ambiente.getRobos()) {
                if (robo instanceof RoboAereo) {  // Apenas atacar robôs aéreos
                    robosAlvos.add((RoboAereo) robo);
                    System.out.printf("[%d] %s\n", i, robo.getNome());
                    i++;
                }
            }

            if (robosAlvos.isEmpty()) {
                System.out.println("Não há robôs aéreos para atacar.");
                return;
            }

            Scanner scanner = new Scanner(System.in);
            System.out.print("Escolha o índice do robô aéreo para atacar: ");
            int indice = scanner.nextInt();

            scanner.close();

            if (indice < 0 || indice >= robosAlvos.size()) {
                System.out.println("Índice inválido.");
                return;
            }

            RoboAereo alvo = robosAlvos.get(indice);
            try {
                robo.atirar(alvo);
            } catch (IllegalStateException e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
