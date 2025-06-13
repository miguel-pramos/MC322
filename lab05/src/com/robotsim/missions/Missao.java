package com.robotsim.missions;

import com.robotsim.robots.Robo;
import com.robotsim.environment.Ambiente;

public interface Missao {
    void executar(Robo r, Ambiente a);
}