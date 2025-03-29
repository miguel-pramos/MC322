package com.robotsim.robots;

import com.robotsim.Controlador;
import com.robotsim.etc.Acao;
import com.robotsim.etc.CatalogoRobos;
import com.robotsim.util.GeometryMath;

public class RoboTanque extends RoboTerrestre {
    private int balasRestantes = 10;
    private final int dano = 8;
    private final int alcance = Controlador.ambiente.getLargura() / 6;

    static {
        // Registro do robô no catálogo
        CatalogoRobos.registrarRobo("Robo Terrestre", RoboTanque.class);
    }

    public RoboTanque(String nome, int posicaoX, int posicaoY) {
        super(nome, posicaoX, posicaoY);
    }

    public void atirar(RoboTerrestre alvo) {
        if (this.balasRestantes <= 0)
            throw new IllegalStateException("Nenhuma bala restante");
        if (GeometryMath.distanciaEuclidiana(alvo.getPosicaoX(), alvo.getPosicaoY()) < this.alcance)
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
        public void executar(Object... args) {
            if (args.length != 1 || !(args[0] instanceof RoboTerrestre)) {
                throw new IllegalArgumentException("Atirar requer um alvo do tipo RoboTerrestre."); // Estudar necessidade disso
            }

            RoboTerrestre alvo = (RoboTerrestre) args[0];
            robo.atirar(alvo);
        }
    }
}
