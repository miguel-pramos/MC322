package com.robotsim.missions;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.robotsim.environment.obstacle.Obstaculo;
import com.robotsim.robots.Robo;

/**
 * Logger especializado para registrar atividades das missões de robôs.
 * Registra detalhes como posições visitadas, sensores ativados, obstáculos
 * detectados
 * e outros eventos importantes durante a execução das missões.
 */
public class Logger {
    private static final String LOG_FILE = "missao.log";
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Registra uma mensagem geral no log.
     * 
     * @param mission  Nome da missão
     * @param mensagem Mensagem a ser registrada
     */
    public static void registrar(String mission, String mensagem) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
                PrintWriter pw = new PrintWriter(fw)) {
            String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
            pw.println(String.format("[%s] [%s] %s", timestamp, mission, mensagem));
        } catch (IOException e) {
            System.err.println("Erro ao escrever no log: " + e.getMessage());
        }
    }

    /**
     * Registra o início de uma missão com detalhes do robô.
     * 
     * @param missaoTipo Tipo da missão sendo iniciada
     * @param robo       Robô executando a missão
     */
    public static void iniciarMissao(String missaoTipo, Robo robo) {
        String mensagem = String.format("INÍCIO - Robô %s (%s) iniciou missão na posição (%d, %d, %d)",
                robo.getNome(), robo.getClass().getSimpleName(),
                robo.getX(), robo.getY(), robo.getZ());
        registrar(missaoTipo, mensagem);
    }

    /**
     * Registra uma mudança de posição do robô.
     * 
     * @param missaoTipo       Tipo da missão
     * @param robo             Robô que se moveu
     * @param posicaoAnteriorX Posição anterior X
     * @param posicaoAnteriorY Posição anterior Y
     * @param posicaoAnteriorZ Posição anterior Z
     */
    public static void registrarMovimento(String missaoTipo, Robo robo,
            int posicaoAnteriorX, int posicaoAnteriorY, int posicaoAnteriorZ) {
        String mensagem = String.format("MOVIMENTO - Robô %s moveu de (%d, %d, %d) para (%d, %d, %d)",
                robo.getNome(), posicaoAnteriorX, posicaoAnteriorY, posicaoAnteriorZ,
                robo.getX(), robo.getY(), robo.getZ());
        registrar(missaoTipo, mensagem);
    }

    /**
     * Registra a ativação de um sensor.
     * 
     * @param missaoTipo  Tipo da missão
     * @param robo        Robô que ativou o sensor
     * @param tipoSensor  Tipo do sensor ativado
     * @param raioAlcance Raio de alcance do sensor
     */
    public static void registrarSensorAtivado(String missaoTipo, Robo robo, String tipoSensor, double raioAlcance) {
        String mensagem = String.format(
                "SENSOR ATIVADO - Robô %s ativou sensor %s com raio %.2f na posição (%d, %d, %d)",
                robo.getNome(), tipoSensor, raioAlcance, robo.getX(), robo.getY(), robo.getZ());
        registrar(missaoTipo, mensagem);
    }

    /**
     * Registra a detecção de obstáculos.
     * 
     * @param missaoTipo           Tipo da missão
     * @param robo                 Robô que detectou
     * @param obstaculosDetectados Lista de obstáculos detectados
     */
    public static void registrarObstaculosDetectados(String missaoTipo, Robo robo,
            List<Obstaculo> obstaculosDetectados) {
        if (obstaculosDetectados.isEmpty()) {
            String mensagem = String.format("DETECÇÃO - Robô %s não detectou obstáculos na posição (%d, %d, %d)",
                    robo.getNome(), robo.getX(), robo.getY(), robo.getZ());
            registrar(missaoTipo, mensagem);
        } else {
            String mensagem = String.format("DETECÇÃO - Robô %s detectou %d obstáculo(s) na posição (%d, %d, %d):",
                    robo.getNome(), obstaculosDetectados.size(), robo.getX(), robo.getY(), robo.getZ());
            registrar(missaoTipo, mensagem);

            for (Obstaculo obstaculo : obstaculosDetectados) {
                String detalhes = String.format("  - Obstáculo: %s (%s) em (%d, %d) - Distância: %.2f",
                        obstaculo.getNome(), obstaculo.getTipoObstaculo().name(),
                        obstaculo.getX(), obstaculo.getY(),
                        calcularDistancia(robo.getX(), robo.getY(), obstaculo.getX(), obstaculo.getY()));
                registrar(missaoTipo, detalhes);
            }
        }
    }

    /**
     * Registra a detecção de um único obstáculo.
     * 
     * @param missaoTipo Tipo da missão
     * @param robo       Robô que detectou
     * @param obstaculo  Obstáculo detectado
     */
    public static void registrarObstaculoDetectado(String missaoTipo, Robo robo, Obstaculo obstaculo) {
        double distancia = calcularDistancia(robo.getX(), robo.getY(), obstaculo.getX(), obstaculo.getY());
        String mensagem = String.format("OBSTÁCULO DETECTADO - Robô %s detectou %s (%s) em (%d, %d) - Distância: %.2f",
                robo.getNome(), obstaculo.getNome(), obstaculo.getTipoObstaculo().name(),
                obstaculo.getX(), obstaculo.getY(), distancia);
        registrar(missaoTipo, mensagem);
    }

    /**
     * Registra a detecção de outros robôs.
     * 
     * @param missaoTipo    Tipo da missão
     * @param roboDetector  Robô que fez a detecção
     * @param roboDetectado Robô detectado
     */
    public static void registrarRoboDetectado(String missaoTipo, Robo roboDetector, Robo roboDetectado) {
        double distancia = calcularDistancia(roboDetector.getX(), roboDetector.getY(),
                roboDetectado.getX(), roboDetectado.getY());
        String mensagem = String.format("ROBÔ DETECTADO - Robô %s detectou %s (%s) em (%d, %d, %d) - Distância: %.2f",
                roboDetector.getNome(), roboDetectado.getNome(), roboDetectado.getClass().getSimpleName(),
                roboDetectado.getX(), roboDetectado.getY(), roboDetectado.getZ(), distancia);
        registrar(missaoTipo, mensagem);
    }

    /**
     * Registra um evento de colisão.
     * 
     * @param missaoTipo  Tipo da missão
     * @param robo        Robô que colidiu
     * @param tipoColisao Tipo da colisão (obstáculo, robô, limite)
     * @param detalhes    Detalhes adicionais da colisão
     */
    public static void registrarColisao(String missaoTipo, Robo robo, String tipoColisao, String detalhes) {
        String mensagem = String.format("COLISÃO - Robô %s colidiu com %s na posição (%d, %d, %d) - %s",
                robo.getNome(), tipoColisao, robo.getX(), robo.getY(), robo.getZ(), detalhes);
        registrar(missaoTipo, mensagem);
    }

    /**
     * Registra o final de uma missão.
     * 
     * @param missaoTipo Tipo da missão
     * @param robo       Robô que concluiu a missão
     * @param resultado  Resultado da missão (sucesso, falha, etc.)
     */
    public static void finalizarMissao(String missaoTipo, Robo robo, String resultado) {
        String mensagem = String.format("FIM - Robô %s finalizou missão na posição (%d, %d, %d) - Resultado: %s",
                robo.getNome(), robo.getX(), robo.getY(), robo.getZ(), resultado);
        registrar(missaoTipo, mensagem);
    }

    /**
     * Registra uma ação específica de ataque durante missões de combate.
     * 
     * @param missaoTipo Tipo da missão
     * @param atacante   Robô atacante
     * @param alvo       Robô alvo
     * @param dano       Dano aplicado
     */
    public static void registrarAtaque(String missaoTipo, Robo atacante, Robo alvo, int dano) {
        String mensagem = String.format("ATAQUE - Robô %s atacou %s causando %d de dano na posição (%d, %d, %d)",
                atacante.getNome(), alvo.getNome(), dano, alvo.getX(), alvo.getY(), alvo.getZ());
        registrar(missaoTipo, mensagem);
    }

    /**
     * Registra a destruição de um obstáculo.
     * 
     * @param missaoTipo Tipo da missão
     * @param robo       Robô que destruiu o obstáculo
     * @param obstaculo  Obstáculo destruído
     */
    public static void registrarDestruicaoObstaculo(String missaoTipo, Robo robo, Obstaculo obstaculo) {
        String mensagem = String.format("DESTRUIÇÃO - Robô %s destruiu obstáculo %s (%s) na posição (%d, %d)",
                robo.getNome(), obstaculo.getNome(), obstaculo.getTipoObstaculo().name(),
                obstaculo.getX(), obstaculo.getY());
        registrar(missaoTipo, mensagem);
    }

    /**
     * Registra informações sobre o planejamento de rota.
     * 
     * @param missaoTipo           Tipo da missão
     * @param robo                 Robô que está planejando
     * @param origem               Posição de origem
     * @param destino              Posição de destino
     * @param obstaculosNoPercurso Lista de obstáculos no percurso planejado
     */
    public static void registrarPlanejamentoRota(String missaoTipo, Robo robo, String origem, String destino,
            List<Obstaculo> obstaculosNoPercurso) {
        String mensagem = String.format("PLANEJAMENTO - Robô %s planeja rota de %s para %s",
                robo.getNome(), origem, destino);
        registrar(missaoTipo, mensagem);

        if (!obstaculosNoPercurso.isEmpty()) {
            String obstaculos = String.format("  - %d obstáculos detectados no percurso:", obstaculosNoPercurso.size());
            registrar(missaoTipo, obstaculos);

            for (Obstaculo obs : obstaculosNoPercurso) {
                String detalhe = String.format("    * %s (%s) em (%d, %d)",
                        obs.getNome(), obs.getTipoObstaculo().name(),
                        obs.getX(), obs.getY());
                registrar(missaoTipo, detalhe);
            }
        }
    }

    /**
     * Registra mudanças no estado do robô.
     * 
     * @param missaoTipo     Tipo da missão
     * @param robo           Robô que mudou de estado
     * @param estadoAnterior Estado anterior
     * @param novoEstado     Novo estado
     */
    public static void registrarMudancaEstado(String missaoTipo, Robo robo, String estadoAnterior, String novoEstado) {
        String mensagem = String.format("ESTADO - Robô %s mudou estado de %s para %s na posição (%d, %d, %d)",
                robo.getNome(), estadoAnterior, novoEstado, robo.getX(), robo.getY(), robo.getZ());
        registrar(missaoTipo, mensagem);
    }

    /**
     * Calcula a distância euclidiana entre duas posições 2D.
     * 
     * @param x1 Coordenada X do primeiro ponto
     * @param y1 Coordenada Y do primeiro ponto
     * @param x2 Coordenada X do segundo ponto
     * @param y2 Coordenada Y do segundo ponto
     * @return Distância euclidiana
     */
    private static double calcularDistancia(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}