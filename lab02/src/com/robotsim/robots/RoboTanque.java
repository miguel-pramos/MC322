package com.robotsim.robots;

import java.util.ArrayList;
import java.util.Scanner;

import com.robotsim.Controlador;
import com.robotsim.etc.Acao;
import com.robotsim.util.GeometryMath;

public class RoboTanque extends RoboTerrestre {
    private int balasRestantes = 10;
    private final int dano = 8;
    private final int alcance = Controlador.ambiente.getLargura() / 6;

    public RoboTanque(String nome, int posicaoX, int posicaoY) {
        super(nome, posicaoX, posicaoY);
    }

    public void atirar(RoboTerrestre alvo) {
        if (this.balasRestantes <= 0)
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
        RoboTanque robo;

        public Atirar(RoboTanque robo) {
            this.robo = robo;
        }

        @Override
        public String getNome() {
            return "Atirar";
        }

        @Override
        public void executar() {
            ArrayList<RoboTerrestre> robosTerrestres = new ArrayList<>();
            int i = 0;

            for (Robo robo : Controlador.ambiente.getRobos()) {
                if (robo instanceof RoboTerrestre) {
                    robosTerrestres.add((RoboTerrestre) robo);
                    System.out.printf("[%d] %s\n", i, robo.getNome());
                    i++;
                }
            }

            if (robosTerrestres.isEmpty()) {
                System.out.println("Não há robôs terrestres para atacar.");
                return;
            }

            Scanner scanner = new Scanner(System.in);
            System.out.print("Escolha o índice do robô terrestre para atacar: ");
            int indice = scanner.nextInt();
            scanner.close();

            if (indice < 0 || indice >= robosTerrestres.size()) {
                System.out.println("Índice inválido.");
                return;
            }

            RoboTerrestre alvo = robosTerrestres.get(indice);
            robo.atirar(alvo);
        }
    }
}
