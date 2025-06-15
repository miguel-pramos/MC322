package com.robotsim.missions.missionTypes;

import com.robotsim.environment.Ambiente;
import com.robotsim.environment.obstacle.Obstaculo;
import com.robotsim.environment.obstacle.TipoObstaculo;
import com.robotsim.missions.Missao;
import com.robotsim.missions.Logger;
import com.robotsim.robots.Robo;
// TesteColisao não será usado para parar o robô antes, mas o robo.mover() internamente o utiliza.

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Missão que faz o robô tentar andar em uma direção aleatória até um ponto
 * final,
 * ignorando colisões para o planejamento do movimento final (o robô ainda
 * sofrerá as consequências da colisão).
 * Lista todos os obstáculos que tocariam a reta do trajeto.
 */
public class MissaoExploracao implements Missao {

    private static final int DISTANCIA_MAX_EXPLORACAO = 20; // Distância máxima da tentativa de exploração
    private static final Random random = new Random();

    @Override
    public void executar(Robo robo, Ambiente ambiente) {
        if (robo == null || ambiente == null) {
            System.out.println("Robô ou Ambiente nulo. Missão cancelada.");
            Logger.registrar("MISSAO_EXPLORACAO", "Missão cancelada - Robô ou Ambiente nulo");
            return;
        }

        try {
            // Registrar início da missão
            Logger.iniciarMissao("MISSAO_EXPLORACAO", robo);

            System.out.println(
                    "Robô " + robo.getNome() + " iniciando Missão de Exploração Direta com Lista de Obstáculos.");
            TimeUnit.MILLISECONDS.sleep(1000);

            double anguloAleatorio = random.nextDouble() * 2 * Math.PI; // Ângulo em radianos

            int xInicial = robo.getX();
            int yInicial = robo.getY();

            // Ponto final desejado, independentemente de obstáculos no caminho
            int xDestinoFinal = xInicial + (int) (DISTANCIA_MAX_EXPLORACAO * Math.cos(anguloAleatorio));
            int yDestinoFinal = yInicial + (int) (DISTANCIA_MAX_EXPLORACAO * Math.sin(anguloAleatorio));

            // Garante não ultrapassar os limites do ambiente
            xDestinoFinal = Math.max(0, Math.min(xDestinoFinal, ambiente.getComprimento()));
            yDestinoFinal = Math.max(0, Math.min(yDestinoFinal, ambiente.getLargura()));

            System.out.println("Trajetória reta planejada de (" + xInicial + "," + yInicial + ") para (" + xDestinoFinal
                    + "," + yDestinoFinal + ").");

            // Registrar planejamento de rota
            String origem = "(" + xInicial + "," + yInicial + ")";
            String destino = "(" + xDestinoFinal + "," + yDestinoFinal + ")";

            TimeUnit.MILLISECONDS.sleep(1000);

            // 1. Listar obstáculos na reta da direção aleatória (até
            // DISTANCIA_MAX_EXPLORACAO)
            System.out.println("Verificando obstáculos na trajetória reta...");
            TimeUnit.MILLISECONDS.sleep(1000);
            List<Obstaculo> obstaculosNaReta = new ArrayList<>();
            Set<Obstaculo> obstaculosJaAdicionados = new HashSet<>();

            // Lógica de varredura de reta (similar a Bresenham)
            int atualX = xInicial;
            int atualY = yInicial;
            int dx_abs = Math.abs(xDestinoFinal - xInicial);
            int dy_abs = Math.abs(yDestinoFinal - yInicial);
            int sx = xInicial < xDestinoFinal ? 1 : -1;
            int sy = yInicial < yDestinoFinal ? 1 : -1;
            int err = dx_abs - dy_abs;

            while (true) {
                for (Obstaculo obs : ambiente.getObstaculos()) {
                    if (pontoEstaNoObstaculo(atualX, atualY, obs, robo) && !obstaculosJaAdicionados.contains(obs)) {
                        obstaculosNaReta.add(obs);
                        obstaculosJaAdicionados.add(obs);
                        // Registrar obstáculo detectado durante planejamento
                        Logger.registrarObstaculoDetectado("MISSAO_EXPLORACAO", robo, obs);
                    }
                }

                if (atualX == xDestinoFinal && atualY == yDestinoFinal) {
                    break;
                }

                if (dx_abs == 0 && dy_abs == 0)
                    break;

                int e2 = 2 * err;
                boolean moved = false;
                if (e2 > -dy_abs) {
                    if (atualX == xDestinoFinal) {
                        // No X de destino
                    } else {
                        err -= dy_abs;
                        atualX += sx;
                        moved = true;
                    }
                }
                if (e2 < dx_abs) {
                    if (atualY == yDestinoFinal) {
                        // No Y de destino
                    } else {
                        err += dx_abs;
                        atualY += sy;
                        moved = true;
                    }
                }
                if (!moved && (atualX != xDestinoFinal || atualY != yDestinoFinal)) {
                    if (atualX != xDestinoFinal)
                        atualX += sx;
                    else if (atualY != yDestinoFinal)
                        atualY += sy;
                    else
                        break;
                }
            }

            if (obstaculosNaReta.isEmpty()) {
                System.out.println("Nenhum obstáculo interceptaria a trajetória reta.");
                TimeUnit.MILLISECONDS.sleep(1000);
            } else {
                System.out.println("Obstáculos que interceptariam a trajetória reta:");
                TimeUnit.MILLISECONDS.sleep(1000);
                for (Obstaculo obs : obstaculosNaReta) {
                    System.out.println("- Obstáculo: " + obs.getNome() + " (" + obs.getTipoObstaculo().name() + ") em ("
                            + obs.getX() + ", " + obs.getY() + ")");
                }
                TimeUnit.MILLISECONDS.sleep(1000);
            }

            // Registrar planejamento de rota com obstáculos encontrados
            Logger.registrarPlanejamentoRota("MISSAO_EXPLORACAO", robo, origem, destino, obstaculosNaReta);

            // 2. Mover o robô para o destino final, ignorando colisões no planejamento da
            // missão.
            // O método robo.mover() ainda lidará com as consequências da colisão (dano,
            // parada).
            if (xDestinoFinal == xInicial && yDestinoFinal == yInicial) {
                System.out.println("Robô " + robo.getNome() + " já está no destino.");
            } else {
                // Registrar movimento
                Logger.registrarMovimento("MISSAO_EXPLORACAO", robo, xInicial, yInicial, robo.getZ());

                System.out.println("O Robô Explorador " + robo.getNome() + " está viajando de (" + xInicial + ","
                        + yInicial + ") para o destino ideal (" + xDestinoFinal + ", " + yDestinoFinal + ").");
                robo.setX(xDestinoFinal);
                robo.setY(yDestinoFinal);
            }
            TimeUnit.MILLISECONDS.sleep(1000);

            System.out.println("Missão de Exploração concluída para " + robo.getNome() + ".");

            // Registrar finalização da missão
            Logger.finalizarMissao("MISSAO_EXPLORACAO", robo, "Sucesso - Exploração completada");

            TimeUnit.MILLISECONDS.sleep(1000);
            System.out.println("Posição final do robô: (" + robo.getX() + ", " + robo.getY() + ")");
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (Exception e) {
            System.err.println("Erro ao executar Missão de Exploração: " + e.getMessage());
            Logger.finalizarMissao("MISSAO_EXPLORACAO", robo, "Falha - Erro durante execução: " + e.getMessage());
        }
    }

    /**
     * Verifica se um ponto (pontoX, pontoY) está dentro dos limites de um
     * obstáculo,
     * considerando a altura para robôs aéreos.
     * Esta é uma cópia da função em MissaoExplorar.java e pode ser refatorada para
     * uma classe utilitária.
     */
    private boolean pontoEstaNoObstaculo(int pontoX, int pontoY, Obstaculo obstaculo, Robo robo) {
        TipoObstaculo tipoObs = obstaculo.getTipoObstaculo();
        int obsX = obstaculo.getX();
        int obsY = obstaculo.getY();

        int obsSupX = obsX + (tipoObs.getComprimento() - 1) / 2;
        int obsSupY = obsY + (tipoObs.getLargura() - 1) / 2;
        int obsInfX = obsX - (tipoObs.getComprimento() - 1) / 2;
        int obsInfY = obsY - (tipoObs.getLargura() - 1) / 2;

        boolean colisao2D = (pontoX >= obsInfX && pontoX <= obsSupX &&
                pontoY >= obsInfY && pontoY <= obsSupY);

        if (!colisao2D) {
            return false;
        }
        return true; // Colisão (Robô terrestre ou RoboAereo na altura do obstáculo)
    }
}