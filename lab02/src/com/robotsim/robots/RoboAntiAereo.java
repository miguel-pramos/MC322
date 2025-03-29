package com.robotsim.robots;

import com.robotsim.Controlador;
import com.robotsim.etc.Acao;
import com.robotsim.etc.CatalogoRobos;
import com.robotsim.util.GeometryMath;

public class RoboAntiAereo extends RoboTerrestre {
    private int balasRestantes = 10;
    private int dano = 4;
    private int alcance = Controlador.ambiente.getLargura() / 5;
    
    static {
        // Registro do robô no catálogo
        CatalogoRobos.registrarRobo("Robo Terrestre", RoboAntiAereo.class);
    }


    public RoboAntiAereo(String nome, int posicaoX, int posicaoY) {
        super(nome, posicaoX, posicaoY);
        this.velocidadeMaxima = 0; // Robo AntiAéreo é fixo
    }

    public void atirar(RoboAereo alvo) {
        if (balasRestantes <= 0)
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
        RoboAntiAereo robo;

        public Atirar(RoboAntiAereo robo) {
            this.robo = robo;
        }

        @Override
        public String getNome() {
            return "Atirar";
        }

        @Override
        public void executar(Object... args) {
            if (args.length != 1 || !(args[0] instanceof RoboAereo)) {
                throw new IllegalArgumentException("Atirar requer um alvo do tipo RoboAereo."); // Estudar necessidade disso
            }
            RoboAereo alvo = (RoboAereo) args[0];
            robo.atirar(alvo);
        }
    }

}
