package com.robotsim.robots.sensors;

import com.robotsim.etc.Acao;
import com.robotsim.robots.Robo;

/**
 * A classe Sensor é uma abstração para sensores utilizados por robôs no
 * simulador.
 * Cada sensor possui um raio de alcance e está associado a um robô.
 * Além disso, cada sensor pode executar uma ação específica definida pela
 * interface {@link Acao}.
 *
 * <p>
 * Esta classe deve ser herdada para criar sensores específicos, como sensores
 * de robôs ou de obstáculos.
 */
public abstract class Sensor {
    /**
     * O raio de alcance efetivo do sensor. Entidades fora deste raio não são
     * detectadas.
     */
    private final double raioDeAlcance;
    /**
     * A ação que pode ser executada com base na detecção do sensor (ex: "Detectar
     * Obstáculo").
     */
    private Acao acao;
    /** O robô ao qual este sensor está fisicamente conectado. */
    private Robo robo;

    /**
     * Construtor para a classe Sensor.
     *
     * @param raioDeAlcance O raio de alcance do sensor.
     * @param robo          O robô ao qual o sensor pertence.
     */
    public Sensor(double raioDeAlcance, Robo robo) {
        this.raioDeAlcance = raioDeAlcance;
        this.robo = robo;
    }

    /**
     * Obtém o robô ao qual este sensor está associado.
     *
     * @return O {@link Robo} proprietário do sensor.
     */
    public Robo getRobo() {
        return robo;
    }

    /**
     * Obtém a ação associada a este sensor.
     * Esta ação é tipicamente adicionada à lista de ações do robô e pode ser
     * invocada pelo usuário.
     *
     * @return A {@link Acao} vinculada ao sensor.
     */
    public Acao getAcao() {
        return acao;
    }

    /**
     * Define a ação associada a este sensor.
     * Este método é geralmente chamado pelas subclasses durante sua inicialização
     * para configurar a ação específica do sensor.
     *
     * @param acao A {@link Acao} a ser vinculada ao sensor.
     */
    public void setAcao(Acao acao) {
        this.acao = acao;
    }

    /**
     * Obtém o raio de alcance deste sensor.
     *
     * @return O valor double representando o raio de alcance.
     */
    public double getRaioDeAlcance() {
        return raioDeAlcance;
    }
}