package com.robotsim.robots.sensors;

import com.robotsim.Controlador;
import com.robotsim.environment.Obstaculo;

public class SensorObstaculo extends Sensor {


    
    public SensorObstaculo(double raioDeAlcance) {
        super(raioDeAlcance);
    }

    @Override
    public void monitorar() {
        for (Obstaculo obstaculo : Controlador.getAmbiente().getObstaculos()) {
            
        }
    }
    
}