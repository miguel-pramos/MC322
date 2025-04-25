package com.robotsim.robots.sensors;

import java.util.concurrent.TimeUnit;

import com.robotsim.Controlador;
import com.robotsim.etc.Acao;
import com.robotsim.robots.Robo;
import com.robotsim.robots.RoboAereo;
import com.robotsim.robots.RoboTerrestre;
import com.robotsim.util.GeometryMath;

public class SensorRobo extends Sensor {

    public SensorRobo(double raioDeAlcance, Robo robo) {
        super(raioDeAlcance, robo);
        this.setAcao(new MonitorarAcao(this));
    }

    private class MonitorarAcao implements Acao {
        private final SensorRobo sensor;

        public MonitorarAcao(SensorRobo sensor) {
            this.sensor = sensor;
        }

        @Override
        public String getNome() {
            return "(Sensor) Monitorar Robôs";
        }

        @Override
        public void executar() {
            System.out.println("Iniciando o processo de detecção...");

            try {
                TimeUnit.MILLISECONDS.sleep(1600); // Simula o tempo necessário para detecção.
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Boa prática: reinterromper a thread.
                System.out.println("A execução foi interrompida.");
            }

            double distancia;
            for (Robo outro_robo : Controlador.getAmbiente().getRobos()) {
                if (outro_robo instanceof RoboAereo && outro_robo != this.sensor.getRobo()) {
                    // Calcula a distância considerando a altitude.
                    distancia = GeometryMath.distanciaEuclidiana(this.sensor.getRobo(), outro_robo.getPosicaoX(),
                            outro_robo.getPosicaoY(), ((RoboAereo) outro_robo).getAltitude());

                    if (distancia < this.sensor.getRaioDeAlcance()) {
                        System.out.printf("O robô %s está na posição (%d, %d, %d)%n", outro_robo.getNome(),
                                outro_robo.getPosicaoX(), outro_robo.getPosicaoY(),
                                ((RoboAereo) outro_robo).getAltitude());
                    }
                } else if (outro_robo instanceof RoboTerrestre) {
                    // Calcula a distância considerando altitude zero para robôs terrestres.
                    distancia = GeometryMath.distanciaEuclidiana(this.sensor.getRobo(), outro_robo.getPosicaoX(),
                            outro_robo.getPosicaoY(), 0);

                    if (distancia < this.sensor.getRaioDeAlcance()) {
                        System.out.printf("O robô %s está na posição (%d, %d, %d)%n", outro_robo.getNome(),
                                outro_robo.getPosicaoX(), outro_robo.getPosicaoY(), 0);
                    }
                } else {
                    System.out.println("Tipo desconhecido");
                }
            }
        }
    }
}
