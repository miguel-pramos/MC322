package com.robotsim.robots.sensors;

public abstract class Sensor {
    private double raioDeAlcance;

    public Sensor(double raioDeAlcance) {
        this.raioDeAlcance = raioDeAlcance;
    }

    public abstract void monitorar();
}