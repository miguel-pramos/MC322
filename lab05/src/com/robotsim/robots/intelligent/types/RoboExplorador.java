package com.robotsim.robots.intelligent.types;

import com.robotsim.environment.Ambiente;
import com.robotsim.robots.intelligent.AgenteInteligente;
import com.robotsim.robots.sensors.SensorObstaculo;

public class RoboExplorador  extends AgenteInteligente {
    private static int contador = 0;

    public RoboExplorador(String nome, int posicaoX, int posicaoY) {
        super(nome, posicaoX, posicaoY, 100); // Inicializando com energia e vida máximas
        this.sensores.add(new SensorObstaculo(10, this));
        this.inicializarSensores();
    }

    @Override
    public void executarMissao(Ambiente a) {
        if (temMissao()) {
            System.out.println("Executando missão exploratória...");
            missao.executar(this, a); // Polimorfismo em ação!
        }
    }

    @Override
    public String getDescricao() {
        return "Robô Explorador: " + getNome();
    }

    @Override
    public char getRepresentacao() {
        return 'E';
    }

    @Override
    public int getContador() {
        contador++;
        return contador;
    }
}