package com.robotsim.missions.missionTypes;

import com.robotsim.environment.Ambiente;
import com.robotsim.missions.Missao;
import com.robotsim.missions.Logger;
import com.robotsim.robots.Robo;
import com.robotsim.robots.EstadoRobo;
import com.robotsim.robots.intelligent.types.RoboAtacante;

import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MissaoDanoGlobal implements Missao {

    private static final Random random = new Random();

    @Override
    public void executar(Robo robo, Ambiente ambiente) {
        if (robo == null || ambiente == null) {
            System.out.println("Robô ou Ambiente nulo. MissaoDanoGlobal cancelada.");
            com.robotsim.missions.Logger.registrar("MISSAO_DANO_GLOBAL", "Missão cancelada - Robô ou Ambiente nulo");
            return;
        }

        if (!(robo instanceof RoboAtacante)) {
            System.out.println(
                    "Robô " + robo.getNome() + " não é um RoboAtacante. MissaoDanoGlobal não pode ser executada.");
            com.robotsim.missions.Logger.registrar("MISSAO_DANO_GLOBAL",
                    "Missão cancelada - Robô não é RoboAtacante: " + robo.getClass().getSimpleName());
            return;
        }

        if (robo.getEstado() != EstadoRobo.LIGADO) {
            System.out.println("Robô " + robo.getNome() + " está desligado. MissaoDanoGlobal cancelada.");
            com.robotsim.missions.Logger.registrarMudancaEstado("MISSAO_DANO_GLOBAL", robo, "DESLIGADO", "CANCELADO");
            return;
        }

        try {
            // Registrar início da missão
            com.robotsim.missions.Logger.iniciarMissao("MISSAO_DANO_GLOBAL", robo);

            System.out.println("Robô " + robo.getNome() + " iniciando MissaoDanoGlobal.");
            TimeUnit.MILLISECONDS.sleep(1000);
            RoboAtacante roboAtacante = (RoboAtacante) robo; // Renamed variable to avoid shadowing class name

            // Criar uma cópia da lista de robôs para evitar problemas se a lista original
            // for modificada
            List<Robo> alvosPotenciais = new ArrayList<>(ambiente.getRobos());

            for (Robo alvo : alvosPotenciais) {
                if (roboAtacante.getEstado() != EstadoRobo.LIGADO) {
                    System.out.println(roboAtacante.getNome() + " foi desligado. Abortando MissaoDanoGlobal.");
                    com.robotsim.missions.Logger.finalizarMissao("MISSAO_DANO_GLOBAL", roboAtacante,
                            "Falha - Robô desligado durante execução");
                    return;
                }

                if (alvo == roboAtacante || alvo.getEstado() == EstadoRobo.DESLIGADO) {
                    // Não atacar a si mesmo ou robôs já desligados/destruídos
                    continue;
                }

                // Registrar detecção de alvo
                com.robotsim.missions.Logger.registrarRoboDetectado("MISSAO_DANO_GLOBAL", roboAtacante, alvo);

                System.out.println(roboAtacante.getNome() + " tentando se mover para atacar " + alvo.getNome() + " em ("
                        + alvo.getX() + ", " + alvo.getY() + ").");
                TimeUnit.MILLISECONDS.sleep(1000);

                try {
                    System.out.println(roboAtacante.getNome() + " chegou em (" + alvo.getX() + ", " + alvo.getY()
                            + ") para atacar " + alvo.getNome());
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restore interrupted status
                    System.err.println("A espera foi interrompida: " + e.getMessage());
                }

                // Verificar novamente o estado do robô executor antes de atacar
                if (roboAtacante.getEstado() == EstadoRobo.DESLIGADO) {
                    System.out.println(roboAtacante.getNome() + " foi desligado antes de poder atacar " + alvo.getNome()
                            + ". Abortando MissaoDanoGlobal.");
                    com.robotsim.missions.Logger.finalizarMissao("MISSAO_DANO_GLOBAL", roboAtacante,
                            "Falha - Robô desligado antes de atacar");
                    return;
                }
                // Verificar estado do alvo também, pois pode ter sido destruído por outra coisa
                if (alvo.getEstado() == EstadoRobo.DESLIGADO) {
                    System.out.println("Alvo " + alvo.getNome() + " não está mais ligado. Pulando ataque.");
                    continue;
                }

                System.out.println(roboAtacante.getNome() + " tentando atacar " + alvo.getNome() + ".");
                TimeUnit.MILLISECONDS.sleep(1000);
                try {
                    int dano = RoboAtacante.getDanoAtacante();
                    alvo.tomarDano(dano);

                    // Registrar ataque realizado
                    com.robotsim.missions.Logger.registrarAtaque("MISSAO_DANO_GLOBAL", roboAtacante, alvo, dano);

                    // A mensagem de sucesso/falha do ataque específico deve vir do método
                    // executarAtaque
                } catch (IllegalStateException e) {
                    System.out.println(
                            roboAtacante.getNome() + " falhou ao atacar " + alvo.getNome() + ": " + e.getMessage());
                    com.robotsim.missions.Logger.registrar("MISSAO_DANO_GLOBAL", "Falha no ataque: "
                            + roboAtacante.getNome() + " -> " + alvo.getNome() + " - " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Erro inesperado ao tentar atacar " + alvo.getNome() + " com "
                            + roboAtacante.getNome() + ": " + e.getMessage());
                    com.robotsim.missions.Logger.registrar("MISSAO_DANO_GLOBAL",
                            "Erro inesperado no ataque: " + e.getMessage());
                }
            }

            if (roboAtacante.getEstado() != EstadoRobo.LIGADO) {
                System.out.println(roboAtacante.getNome()
                        + " está desligado antes do movimento final aleatório. MissaoDanoGlobal terminando.");
                com.robotsim.missions.Logger.finalizarMissao("MISSAO_DANO_GLOBAL", roboAtacante,
                        "Falha - Robô desligado antes do movimento final");
                return;
            }

            System.out.println(
                    roboAtacante.getNome() + " completou a fase de ataques. Movendo para uma posição aleatória.");
            TimeUnit.MILLISECONDS.sleep(1000);

            int larguraMapa = ambiente.getLargura();
            int alturaMapa = ambiente.getAltura();

            int xAleatorio = random.nextInt(larguraMapa);
            int yAleatorio = random.nextInt(alturaMapa);

            // Registrar movimento final
            com.robotsim.missions.Logger.registrarMovimento("MISSAO_DANO_GLOBAL", roboAtacante, roboAtacante.getX(),
                    roboAtacante.getY(), roboAtacante.getZ());

            roboAtacante.setX(xAleatorio);
            roboAtacante.setY(yAleatorio);
            System.out.println(roboAtacante.getNome() + " se moveu para a posição final (" + xAleatorio + ", "
                    + yAleatorio + ").");

            System.out.println("MissaoDanoGlobal concluída para " + roboAtacante.getNome() + ".");

            // Registrar finalização da missão
            com.robotsim.missions.Logger.finalizarMissao("MISSAO_DANO_GLOBAL", roboAtacante,
                    "Sucesso - Ataques globais completados");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
            System.err.println("Ocorreu um erro: " + e.getMessage());
            com.robotsim.missions.Logger.finalizarMissao("MISSAO_DANO_GLOBAL", robo,
                    "Falha - Interrupção durante execução: " + e.getMessage());
        }
    }
}