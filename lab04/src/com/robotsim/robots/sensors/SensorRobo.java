package com.robotsim.robots.sensors;

import java.util.concurrent.TimeUnit;

import com.robotsim.Controlador;
import com.robotsim.etc.Acao;
import com.robotsim.robots.Robo;
import com.robotsim.robots.aerials.RoboAereo;
import com.robotsim.robots.terrestrials.RoboTerrestre;
import com.robotsim.util.GeometryMath;

/**
 * A classe {@code SensorRobo} é uma especialização de {@link Sensor}
 * responsável por detectar
 * outros robôs no ambiente simulado.
 * <p>
 * Este sensor verifica a presença de outros robôs (aéreos ou terrestres) dentro
 * de seu raio de alcance
 * e reporta suas posições. A detecção leva em consideração a altitude para
 * robôs aéreos.
 *
 * @see Sensor
 * @see Robo
 * @see Controlador
 */
public class SensorRobo extends Sensor {

    /**
     * Constrói um novo {@code SensorRobo} associado a um robô específico e com um
     * raio de alcance definido.
     *
     * @param raioDeAlcance O raio de alcance do sensor, em unidades de distância.
     * @param robo          O robô ao qual este sensor está acoplado.
     */
    public SensorRobo(double raioDeAlcance, Robo robo) {
        super(raioDeAlcance, robo); // Chama o construtor da classe pai (Sensor)
        // Define a ação de monitoramento padrão para este sensor
        this.setAcao(new MonitorarAcao(this));
    }

    /**
     * A classe interna {@code MonitorarAcao} implementa a interface {@link Acao} e
     * define
     * o comportamento específico de monitoramento de robôs para o
     * {@link SensorRobo}.
     * <p>
     * Esta ação varre o ambiente em busca de outros robôs, calcula a distância até
     * eles
     * e, se estiverem dentro do raio de alcance do sensor, exibe suas informações.
     */
    private class MonitorarAcao implements Acao {
        private final SensorRobo sensor; // Referência ao sensor que executa esta ação

        /**
         * Constrói uma nova ação de monitoramento associada a um {@link SensorRobo}.
         *
         * @param sensor O {@link SensorRobo} que utilizará esta ação.
         */
        public MonitorarAcao(SensorRobo sensor) {
            this.sensor = sensor;
        }

        /**
         * Retorna o nome da ação de monitoramento.
         *
         * @return Uma string representando o nome da ação: "(Sensor) Monitorar Robôs".
         */
        @Override
        public String getNome() {
            return "(Sensor) Monitorar Robôs";
        }

        /**
         * Executa a lógica de detecção de robôs no ambiente.
         * <p>
         * O método simula um tempo de processamento para a detecção. Em seguida, itera
         * sobre
         * todos os robôs presentes no ambiente (obtidos através do
         * {@link Controlador}).
         * Para cada robô encontrado (excluindo o próprio robô ao qual o sensor está
         * acoplado):
         * <ul>
         * <li>Verifica se é uma instância de {@link RoboAereo} ou
         * {@link RoboTerrestre}.</li>
         * <li>Calcula a distância euclidiana até o robô detectado. Para robôs aéreos,
         * a altitude (coordenada Z) é considerada. Para robôs terrestres,
         * a altitude é considerada como 0.</li>
         * <li>Se o robô detectado estiver dentro do raio de alcance do sensor,
         * sua nome e posição (X, Y, Z) são impressos no console.</li>
         * <li>Se um tipo de robô desconhecido for encontrado, uma mensagem é
         * impressa.</li>
         * </ul>
         * Em caso de interrupção da thread durante a simulação de tempo, a interrupção
         * é
         * registrada e uma mensagem é exibida.
         */
        @Override
        public void executar() {
            System.out.println("Iniciando o processo de detecção de outros robôs...");

            try {
                // Simula o tempo necessário para o sensor realizar a varredura e detecção.
                TimeUnit.MILLISECONDS.sleep(1600);
            } catch (InterruptedException e) {
                // Se a thread for interrompida durante o sleep, restaura o status de
                // interrupção.
                Thread.currentThread().interrupt();
                System.out.println("A varredura do sensor foi interrompida.");
                return; // Encerra a execução se interrompido
            }

            double distancia;
            boolean roboDetectado = false; // Flag para verificar se algum robô foi detectado

            // Itera sobre todos os robôs no ambiente
            for (Robo outro_robo : Controlador.getAmbiente().getRobos()) {
                // Garante que o sensor não detecte o próprio robô ao qual está acoplado
                if (outro_robo == this.sensor.getRobo()) {
                    continue; // Pula para o próximo robô na lista
                }

                if (outro_robo instanceof RoboAereo) {
                    RoboAereo roboAereoDetectado = (RoboAereo) outro_robo;
                    // Calcula a distância euclidiana considerando a altitude do robô aéreo.
                    distancia = GeometryMath.distanciaEuclidiana(this.sensor.getRobo(), roboAereoDetectado.getX(),
                            roboAereoDetectado.getY(), roboAereoDetectado.getZ());

                    if (distancia < this.sensor.getRaioDeAlcance()) {
                        System.out.printf("Robô AÉREO detectado: %s na posição (%d, %d, %d)%n",
                                roboAereoDetectado.getNome(), roboAereoDetectado.getX(), roboAereoDetectado.getY(),
                                roboAereoDetectado.getZ());
                        roboDetectado = true;
                    }

                } else if (outro_robo instanceof RoboTerrestre) {
                    RoboTerrestre roboTerrestreDetectado = (RoboTerrestre) outro_robo;
                    // Calcula a distância euclidiana. Para robôs terrestres, a altitude (Z) é 0.
                    distancia = GeometryMath.distanciaEuclidiana(this.sensor.getRobo(), roboTerrestreDetectado.getX(),
                            roboTerrestreDetectado.getY(), 0); // Z é 0 para robôs terrestres

                    if (distancia < this.sensor.getRaioDeAlcance()) {
                        System.out.printf("Robô TERRESTRE detectado: %s na posição (%d, %d, %d)%n",
                                roboTerrestreDetectado.getNome(), roboTerrestreDetectado.getX(),
                                roboTerrestreDetectado.getY(), 0);
                        roboDetectado = true;
                    }

                } else {
                    // Caso o tipo do robô não seja aéreo nem terrestre (e não seja o próprio robô)
                    System.out.printf(
                            "Robô de tipo desconhecido ('%s') detectado na posição (%d, %d). Verificação de alcance não aplicável sem tipo definido.%n",
                            outro_robo.getNome(), outro_robo.getX(), outro_robo.getY());
                    // Não podemos calcular distância de forma genérica sem saber se é aéreo ou
                    // terrestre para considerar Z.
                }
            }

            if (!roboDetectado) {
                System.out.println("Nenhum outro robô detectado dentro do raio de alcance.");
            }
            System.out.println("Processo de detecção de outros robôs finalizado.");
        }
    }
}
