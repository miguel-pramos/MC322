package com.robotsim.robots.abilities;

import com.robotsim.environment.entity.Entidade;
import java.util.List; // Adicionado import para List

/**
 * Interface para robôs com capacidade de exploração autônoma.
 */
public interface Explorador {
    /**
     * Executa uma varredura da área ao redor do robô para identificar entidades ou características do terreno.
     * @return Uma lista de entidades ou informações detectadas.
     */
    List<Entidade> escanearArea();
}
