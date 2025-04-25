package com.robotsim.robots.sensors;

import java.util.concurrent.TimeUnit;

import com.robotsim.Controlador;
import com.robotsim.environment.Obstaculo;
import com.robotsim.robots.Robo;
import com.robotsim.util.GeometryMath;
import com.robotsim.etc.Acao;

/**
 * A classe SensorObstaculo é responsável por detectar obstáculos no ambiente.
 *
 * <p>
 * Quando um obstáculo é detectado, sua posição é exibida no console.
 */
public class SensorObstaculo extends Sensor {

    public SensorObstaculo(double raioDeAlcance, Robo robo) {
        super(raioDeAlcance, robo);
        this.setAcao(new MonitorarAcao(this));
    }

    /**
     * Classe interna que representa a ação de monitorar obstáculos.
     */
    private class MonitorarAcao implements Acao {
        private final SensorObstaculo sensor;

        public MonitorarAcao(SensorObstaculo sensor) {
            this.sensor = sensor;
        }

        @Override
        public String getNome() {
            return "(Sensor) Monitorar Obstáculos";
        }
        
        /**
         * Método sobrescrito que descreve a ação de monitorar obstáculos.
         * Este método é responsável por detectar obstáculos no ambiente e exibir
         * informações sobre eles no console.
         *
         * Regras e comportamentos:
         * - O sensor verifica todos os obstáculos presentes no ambiente.
         * - Caso um obstáculo esteja dentro do raio de alcance do sensor, sua posição
         *   será exibida no console.
         * - Caso nenhum obstáculo seja detectado, uma mensagem informativa será exibida.
         */
        @Override
        public void executar() {
            int counter = 0;
            for (Obstaculo obstaculo : Controlador.getAmbiente().getObstaculos()) {
                System.out.println("Iniciando o processo de detecção...");
                if (GeometryMath.distanciaEuclidiana(this.sensor.getRobo(), obstaculo.getPosX(),
                        obstaculo.getPosY()) <= this.sensor.getRaioDeAlcance()) {
                    counter++;
                    try {
                        TimeUnit.MILLISECONDS.sleep(1600); // Simula o tempo necessário para detecção.
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // Boa prática: reinterromper a thread.
                        System.out.println("A execução foi interrompida.");
                    }

                    System.out.printf("O obstáculo %s está na posição (%d, %d, %d)%n", obstaculo.getNome(),
                            obstaculo.getPosX(), obstaculo.getPosY(), 0);
                }

            }
            if (counter == 0)
                System.out.println("Nenhum obstáculo encontrado.");
        }
    }
}
