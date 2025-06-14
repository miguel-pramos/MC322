package com.robotsim.robots.intelligent.types;

import com.robotsim.environment.Ambiente;
import com.robotsim.robots.intelligent.AgenteInteligente;

public class RoboAtacante extends AgenteInteligente {
    private static int contador = 0;
    private static int danoAtacante = 10;

    public RoboAtacante(String nome, int posicaoX, int posicaoY) {
        super(nome, posicaoX, posicaoY, 10); // Inicializando com energia e vida máximas
    }

    @Override
    public void executarMissao(Ambiente a) {
        if (temMissao()) {
            System.out.println("Executando missão de ataque...");
            missao.executar(this, a); // Polimorfismo em ação!
        }
    }

    @Override
    public String getDescricao() {
        return "Robô Atacante: " + getNome();
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
    
    public static int getDanoAtacante() {
        return danoAtacante;
    }
}