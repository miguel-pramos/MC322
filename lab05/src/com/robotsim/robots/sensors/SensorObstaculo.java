package com.robotsim.robots.sensors;

import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.List;

import com.robotsim.Controlador;
import com.robotsim.environment.obstacle.Obstaculo;
import com.robotsim.missions.Logger;
import com.robotsim.robots.Robo;
import com.robotsim.util.GeometryMath;
import com.robotsim.etc.Acao;

/**
 * Sensor especializado na detecção de {@link Obstaculo} no ambiente do
 * simulador.
 * Estende a classe {@link Sensor} e implementa uma ação específica para
 * monitorar e reportar obstáculos próximos.
 */
public class SensorObstaculo extends Sensor {

    /**
     * Construtor para SensorObstaculo.
     *
     * @param raioDeAlcance O raio de alcance dentro do qual o sensor pode detectar
     *                      obstáculos.
     * @param robo          O {@link Robo} ao qual este sensor está acoplado.
     */
    public SensorObstaculo(double raioDeAlcance, Robo robo) {
        super(raioDeAlcance, robo);
        this.setAcao(new MonitorarAcao(this));
    }

    /**
     * Classe interna que define a ação de monitoramento de obstáculos.
     * Esta ação, quando executada, varre o ambiente em busca de obstáculos dentro
     * do raio de alcance do sensor.
     */
    private class MonitorarAcao implements Acao {
        /** Referência ao sensor de obstáculo que executa esta ação. */
        private final SensorObstaculo sensor;

        /**
         * Construtor para a ação de monitoramento.
         *
         * @param sensor O {@link SensorObstaculo} associado a esta ação.
         */
        public MonitorarAcao(SensorObstaculo sensor) {
            this.sensor = sensor;
        }

        /**
         * Retorna o nome da ação, que será exibido na interface do usuário ou em logs.
         *
         * @return O nome da ação: "(Sensor) Monitorar Obstáculos".
         */
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
         * será exibida no console.
         * - Caso nenhum obstáculo seja detectado, uma mensagem informativa será
         * exibida.
         */
        @Override
        public void executar() {
            int counter = 0;
            List<Obstaculo> obstaculosDetectados = new ArrayList<>();

            // Registrar ativação do sensor
            Logger.registrarSensorAtivado("SENSOR_ATIVIDADE", this.sensor.getRobo(),
                    "SensorObstaculo", this.sensor.getRaioDeAlcance());

            System.out.println("Iniciando o processo de detecção...");

            for (Obstaculo obstaculo : Controlador.getAmbiente().getObstaculos()) {
                if (GeometryMath.distanciaEuclidiana(this.sensor.getRobo(), obstaculo.getX(),
                        obstaculo.getY()) <= this.sensor.getRaioDeAlcance()) {
                    counter++;
                    obstaculosDetectados.add(obstaculo);

                    try {
                        TimeUnit.MILLISECONDS.sleep(1600); // Simula o tempo necessário para detecção.
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // Boa prática: reinterromper a thread.
                        System.out.println("A execução foi interrompida.");
                    }

                    System.out.printf("O obstáculo %s está na posição (%d, %d, %d)%n", obstaculo.getNome(),
                            obstaculo.getX(), obstaculo.getY(), 0);

                    // Registrar cada obstáculo detectado
                    Logger.registrarObstaculoDetectado("SENSOR_ATIVIDADE", this.sensor.getRobo(), obstaculo);
                }
            }

            // Registrar resultado geral da detecção
            Logger.registrarObstaculosDetectados("SENSOR_ATIVIDADE", this.sensor.getRobo(), obstaculosDetectados);

            if (counter == 0)
                System.out.println("Nenhum obstáculo encontrado.");
        }
    }
}
