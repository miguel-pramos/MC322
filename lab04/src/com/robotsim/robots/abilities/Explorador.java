package com.robotsim.robots.abilities;

import com.robotsim.environment.entity.Entidade;
import java.awt.Point;
import java.util.List; // Adicionado import para List

/**
 * Interface para robôs com capacidade de exploração autônoma.
 */
public interface Explorador {
    /**
     * Define um novo ponto de destino para exploração.
     * @param ponto O novo ponto de destino (coordenadas X, Y).
     */
    void definirPontoDeExploracao(Point ponto);

    /**
     * Executa uma varredura da área ao redor do robô para identificar entidades ou características do terreno.
     * @return Uma lista de entidades ou informações detectadas.
     */
    List<Entidade> escanearArea();
}
