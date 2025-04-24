package com.robotsim.environment;

import java.util.ArrayList;

import com.robotsim.robots.Robo;


/**
 * A classe Ambiente representa o espaço onde os robôs interagem.
 * Define os limites do ambiente e gerencia os robôs presentes nele.
 */
public class Ambiente {
    private int comprimento; // Comprimento do ambiente.
    private int largura; // Largura do ambiente.
    private ArrayList<Robo> robos = new ArrayList<>(); // Lista de robôs no ambiente.
    private ArrayList<Obstaculo> obstaculos = new ArrayList<>(); // Lista de robôs no ambiente.

    public Ambiente(int comprimento, int largura) {
        this.comprimento = comprimento;
        this.largura = largura;
    }

    /**
     * Verifica se uma posição bidimensional está dentro dos limites do ambiente.
     *
     * @param x Coordenada no eixo X.
     * @param y Coordenada no eixo Y.
     * @return true se a posição estiver dentro dos limites, false caso contrário.
     */
    public boolean dentroDosLimites(int x, int y) {
        return (x < this.comprimento && y < this.largura)
                && (x >= 0 && y >= 0);
    }

    /**
     * Verifica se uma posição tridimensional está dentro dos limites do ambiente.
     *
     * @param x            Coordenada no eixo X.
     * @param y            Coordenada no eixo Y.
     * @param z            Coordenada no eixo Z.
     * @param alturaMaxima Coordenada máxima para o eixo Z.
     * @return true se a posição estiver dentro dos limites, false caso contrário.
     */
    public boolean dentroDosLimites(int x, int y, int z, int alturaMaxima) {
        return (x < this.comprimento && y < this.largura && z < alturaMaxima)
                && (x >= 0 && y >= 0 && z >= 0);
    }

    /**
     * Adiciona um robô ao ambiente.
     *
     * @param robo O robô a ser adicionado.
     */
    public void adicionarRobo(Robo robo) {
        this.robos.add(robo);
    }

    /**
     * Remove um robô do ambiente, simulando sua destruição.
     *
     * @param robo O robô a ser removido.
     * @return true se o robô foi removido com sucesso, false caso contrário.
     */
    public void destruirRobo(Robo robo) {
        System.out.printf("O robô %s foi destruído.%n", robo.getNome());
        this.robos.remove(robo);
    }

    /**
     * Adiciona um obstáculo ao ambiente.
     *
     * @param robo O obstáculo a ser adicionado.
     */
    public void adicionarObstaculo(Obstaculo obstaculo) {
        this.obstaculos.add(obstaculo);
    }

    /**
     * Remove um obstáculo do ambiente, simulando sua destruição.
     *
     * @param robo O obstáculo a ser removido.
     * @return true se o obstáculo foi removido com sucesso, false caso contrário.
     */
    public void destruirObstaculo(Obstaculo obstaculo) {
        System.out.printf("O robô %s foi destruído.%n", obstaculo.tipo.toString());
        this.obstaculos.remove(obstaculo);
    }

    public int getLargura() {
        return largura;
    }

    public int getComprimento() {
        return comprimento;
    }

    public ArrayList<Robo> getRobos() {
        return robos;
    }

    public ArrayList<Obstaculo> getObstaculos() {
        return obstaculos;
    }
}
