package com.robotsim.robots.aerials;

import com.robotsim.Controlador;
import com.robotsim.environment.entity.Entidade;
import com.robotsim.robots.Robo;
import com.robotsim.robots.abilities.Explorador;
import com.robotsim.robots.sensors.SensorObstaculo;
import com.robotsim.robots.sensors.SensorRobo;
import com.robotsim.util.GeometryMath;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa um robô aéreo do tipo Drone, uma subclasse de {@link RoboAereo}.
 * O RoboDrone é especializado em exploração e reconhecimento, equipado com sensores para detectar
 * outros robôs e obstáculos. Possui uma bateria com capacidade limitada que é consumida durante o movimento.
 * Ao esgotar a bateria, o drone se autodestrói, causando dano a entidades próximas.
 *
 * @see RoboAereo
 * @see Explorador
 */
public class RoboDrone extends RoboAereo implements Explorador {
    /** Nível atual da bateria do drone. */
    private int bateria = 50;
    /** Contador estático para gerar IDs únicos para instâncias de RoboDrone. */
    private static int contador = 0;

    /**
     * Construtor para RoboDrone.
     * Inicializa o drone com nome, posição, HP, altitude inicial, altitude máxima e adiciona sensores.
     *
     * @param nome Nome do drone.
     * @param posicaoX Posição inicial no eixo X.
     * @param posicaoY Posição inicial no eixo Y.
     */
    public RoboDrone(String nome, int posicaoX, int posicaoY) {
        super(nome, posicaoX, posicaoY, 50, 10, 30);
        this.sensores.add(new SensorRobo(30, this));
        this.sensores.add(new SensorObstaculo(30, this));
        this.inicializarSensores();
    }

    /**
     * Retorna uma descrição textual do RoboDrone, incluindo seu HP e nível de bateria.
     *
     * @return String contendo a descrição do drone.
     */
    @Override
    public String getDescricao() {
        return String.format(
                "RoboDrone se move usando sua pequena bateria... Rumores dizem que sua autodestruição é potente \nNome: %s, HP: %d, Bateria: %d",
                this.getNome(), this.getHP(), this.bateria);
    }

    /**
     * Retorna o caractere que representa o RoboDrone no ambiente do simulador.
     *
     * @return O caractere 'D'.
     */
    @Override
    public char getRepresentacao() {
        return 'D'; // Representação do RoboDrone no ambiente.
    }

    /**
     * Move o RoboDrone, consumindo bateria proporcionalmente à distância percorrida.
     * Se a bateria se esgotar (chegar a zero ou menos), o drone é removido do ambiente
     * e causa dano de autodestruição a {@link Entidade} próximas (dentro de um raio de 5 unidades).
     * Caso contrário, o movimento é realizado normalmente e o nível de bateria restante é exibido.
     *
     * @param deltaX Deslocamento desejado no eixo X.
     * @param deltaY Deslocamento desejado no eixo Y.
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

    /**
     * Obtém o contador estático usado para gerar IDs únicos para instâncias de {@link RoboDrone}.
     * Este método é chamado durante a criação do ID do robô na classe base {@link Robo}.
     * Incrementa o contador a cada chamada para garantir a unicidade do próximo ID.
     *
     * @return O valor atualizado do contador específico para RoboDrone.
     */
    @Override
    protected int getContador() {
        contador++;
        return contador;
    }

    /**
     * Realiza uma varredura da área ao redor do drone para detectar outras entidades.
     * Imprime no console as entidades detectadas dentro de um raio de 30 unidades.
     *
     * @return Uma lista de {@link Entidade} detectadas na área de varredura.
     */
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