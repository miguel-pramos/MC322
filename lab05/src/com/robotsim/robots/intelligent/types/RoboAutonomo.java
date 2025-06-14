package com.robotsim.robots.intelligent.types;

import com.robotsim.environment.Ambiente;
import com.robotsim.robots.intelligent.AgenteInteligente;

public class RoboAutonomo extends AgenteInteligente {
    private static int contador = 0;
    
    public RoboAutonomo(String nome, int posicaoX, int posicaoY) {
        super(nome, posicaoX, posicaoY, 50); // Inicializando com energia e vida máximas
    }

    @Override
    public void executarMissao(Ambiente a) {
        if (temMissao()) {
            System.out.println("Executando missão autônoma...");
            missao.executar(this, a); // Polimorfismo em ação!
        }
    }

    @Override
    public String getDescricao() {
        return "Robô Autônomo: " + getNome();
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