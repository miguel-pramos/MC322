package com.robotsim.environment;

import java.util.Random;
import com.robotsim.Controlador;

public class Obstaculo {
    private int posX;
    private int posY;
    protected final TipoObstaculo tipo;
    private final TipoObstaculo[] tipos = TipoObstaculo.values();
    
    public Obstaculo() {
        Random rand = new Random();
        this.tipo = tipos[rand.nextInt(tipos.length)];

        do {
            this.posX = rand.nextInt(Controlador.getAmbiente().getComprimento());
            this.posY = rand.nextInt(Controlador.getAmbiente().getLargura());
        } while (!boaPosicao(tipo, posX, posY));
    }

    public static boolean boaPosicao (TipoObstaculo tipo, int testeX, int testeY){
        int metadeComprimento = (tipo.comprimento - 1) / 2;
        int metadeLargura = (tipo.largura - 1) / 2;

        int superiorX = testeX + metadeComprimento;
        int superiorY = testeY + metadeLargura;
        int inferiorX = testeX - metadeComprimento;
        int inferiorY = testeY - metadeLargura;

        boolean dentroDosLimitesSuperior =
                Controlador.getAmbiente().dentroDosLimites(superiorX, superiorY);

        boolean dentroDosLimitesInferior =
                Controlador.getAmbiente().dentroDosLimites(inferiorX, inferiorY);

        boolean semColisEntreObst = !testColisEntreObst(superiorX, superiorY, inferiorX, inferiorY);

        return dentroDosLimitesSuperior && dentroDosLimitesInferior && semColisEntreObst;
    }

    private static boolean testColisEntreObst(int ret1SuperiorX, int ret1SuperiorY, int ret1InferiorX, int ret1InferiorY) {
        for(Obstaculo obstaculo : Controlador.getAmbiente().getObstaculos()) {
            int ret2SuperiorX = obstaculo.getPosX() + obstaculo.getTipo().comprimento;
            int ret2SuperiorY = obstaculo.getPosY() + obstaculo.getTipo().largura;

            int ret2InferiorX = obstaculo.getPosX() - obstaculo.getTipo().comprimento;
            int ret2InferiorY = obstaculo.getPosY() - obstaculo.getTipo().largura;

            // Verifica se um retângulo está à esquerda do outro
            if (ret2SuperiorX < ret1InferiorX || ret1SuperiorX < ret2InferiorX) {
                continue;
            }

            // Verifica se um retângulo está acima do outro
            if (ret1SuperiorY < ret2InferiorY || ret2SuperiorY < ret1InferiorY) {
                continue;
            }

            // Se nenhuma das condições acima for verdadeira, há colisão
            return true;
        }
        return false;
    }

    public int getPosX() {
        return this.posX;
    }

    public int getPosY() {
        return this.posY;
    }

    public TipoObstaculo getTipo() {
        return this.tipo;
    }

    public String getNome() {
        return tipo.toString();
    }
}
