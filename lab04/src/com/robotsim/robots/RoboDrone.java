package com.robotsim.robots;

import com.robotsim.Controlador;
import com.robotsim.environment.entity.Entidade;
import com.robotsim.robots.abilities.Explorador;
import com.robotsim.robots.sensors.SensorObstaculo;
import com.robotsim.robots.sensors.SensorRobo;
import com.robotsim.util.GeometryMath;

import java.util.ArrayList;
import java.util.List;

/**
 * A classe RoboDrone representa um robô aéreo com a capacidade de detectar
 * outros robôs
 * no ambiente. Este robô possui uma bateria limitada e um alcance específico
 * para detecção.
 *
 * @see RoboAereo
 */
public class RoboDrone extends RoboAereo implements Explorador {
    private int bateria = 50;
    private static int contador = 0;

    public RoboDrone(String nome, int posicaoX, int posicaoY) {
        super(nome, posicaoX, posicaoY, 50, 10, 30);
        this.sensores.add(new SensorRobo(30, this));
        this.sensores.add(new SensorObstaculo(30, this));
        this.inicializarSensores();
    }

    @Override
    public String getDescricao() {
        return String.format(
                "RoboDrone se move usando sua pequena bateria... Rumores dizem que sua autodestruição é potente \nNome: %s, HP: %d, Bateria: %d",
                this.getNome(), this.getHP(), this.bateria);
    }

    @Override
    public char getRepresentacao() {
        return 'D'; // Representação do RoboDrone no ambiente.
    }

    /**
     * Método responsável por mover o RoboDrone.
     * <p>
     * A cada movimento, a bateria é reduzida proporcionalmente ao deslocamento.
     * Caso a bateria chegue a zero ou menos, o robô é removido do ambiente.
     *
     * @param deltaX Deslocamento no eixo X.
     * @param deltaY Deslocamento no eixo Y.
     */
    protected void mover(int deltaX, int deltaY) {
        this.bateria -= Math.abs(deltaX) + Math.abs(deltaY);

        if (this.bateria <= 0) {
            System.out.println("Bateria esgotada! RoboDrone não pode mais exitir... Destruindo RoboDrone...");
            for (Entidade entidade : Controlador.getAmbiente().getEntidades()) {
                if (entidade instanceof Robo &&
                        GeometryMath.distanciaEuclidiana((Robo) this, entidade.getX(), entidade.getY()) <= 5) {
                    System.out.println(((Robo) entidade).getNome() + " está próximo demais! Tomou muito dano!!");
                    ((Robo) entidade).tomarDano(100); // Causa dano ao robô próximo.
                }
            }
            Controlador.getAmbiente().removerEntidade(this); // Remove o robô do ambiente.
        } else {
            super.mover(deltaX, deltaY); // Chama o método da superclasse para movimentação.
            System.out.printf("Sua bateria está em %d%n", this.bateria);
        }
    }

    @Override
    protected int getContador() {
        contador++;
        return contador;
    }

    @Override
    public List<Entidade> escanearArea() {
        System.out.println(this.getNome() + " escaneando área...");
        ArrayList<Entidade> entidadesDetectadas = new ArrayList<>();
        for (Entidade entidade : Controlador.getAmbiente().getEntidades()) {
            if (entidade != this && GeometryMath.distanciaEuclidiana(this, entidade.getX(), entidade.getY()) <= 30) {
                entidadesDetectadas.add(entidade);
                System.out.println("Entidade detectada: " + entidade.getClass().getSimpleName() + " em ("
                        + entidade.getX() + ", " + entidade.getY() + ")");
            }
        }
        if (entidadesDetectadas.isEmpty()) {
            System.out.println("Nenhuma entidade detectada na área.");
        }
        return entidadesDetectadas;
    }
}