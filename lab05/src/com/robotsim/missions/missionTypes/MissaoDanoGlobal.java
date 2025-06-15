package com.robotsim.missions.missionTypes;

import com.robotsim.environment.Ambiente;
import com.robotsim.missions.Missao;
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
            return;
        }

        if (!(robo instanceof RoboAtacante)) {
            System.out.println("Robô " + robo.getNome() + " não é um RoboAtacante. MissaoDanoGlobal não pode ser executada.");
            return;
        }

        if (robo.getEstado() != EstadoRobo.LIGADO) {
            System.out.println("Robô " + robo.getNome() + " está desligado. MissaoDanoGlobal cancelada.");
            return;
        }
        
        try{
        System.out.println("Robô " + robo.getNome() + " iniciando MissaoDanoGlobal.");
        TimeUnit.MILLISECONDS.sleep(1000);
        RoboAtacante roboAtacante = (RoboAtacante) robo; // Renamed variable to avoid shadowing class name

        // Criar uma cópia da lista de robôs para evitar problemas se a lista original for modificada
        List<Robo> alvosPotenciais = new ArrayList<>(ambiente.getRobos());

        for (Robo alvo : alvosPotenciais) {
            if (roboAtacante.getEstado() != EstadoRobo.LIGADO) {
                System.out.println(roboAtacante.getNome() + " foi desligado. Abortando MissaoDanoGlobal.");
                return;
            }

            if (alvo == roboAtacante || alvo.getEstado() == EstadoRobo.DESLIGADO) {
                // Não atacar a si mesmo ou robôs já desligados/destruídos
                continue;
            }

            System.out.println(roboAtacante.getNome() + " tentando se mover para atacar " + alvo.getNome() + " em (" + alvo.getX() + ", " + alvo.getY() + ").");
            TimeUnit.MILLISECONDS.sleep(1000);
            
            try {
                System.out.println(roboAtacante.getNome() + " chegou em (" + alvo.getX() + ", " + alvo.getY() + ") para atacar " + alvo.getNome());
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupted status
                System.err.println("A espera foi interrompida: " + e.getMessage());
            }

            // Verificar novamente o estado do robô executor antes de atacar
            if (roboAtacante.getEstado() == EstadoRobo.DESLIGADO) {
                System.out.println(roboAtacante.getNome() + " foi desligado antes de poder atacar " + alvo.getNome() + ". Abortando MissaoDanoGlobal.");
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
                alvo.tomarDano(RoboAtacante.getDanoAtacante());
                // A mensagem de sucesso/falha do ataque específico deve vir do método executarAtaque
            } catch (IllegalStateException e) {
                System.out.println(roboAtacante.getNome() + " falhou ao atacar " + alvo.getNome() + ": " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Erro inesperado ao tentar atacar " + alvo.getNome() + " com " + roboAtacante.getNome() + ": " + e.getMessage());
            }
        }

        if (roboAtacante.getEstado() != EstadoRobo.LIGADO) {
            System.out.println(roboAtacante.getNome() + " está desligado antes do movimento final aleatório. MissaoDanoGlobal terminando.");
            return;
        }

        System.out.println(roboAtacante.getNome() + " completou a fase de ataques. Movendo para uma posição aleatória.");
        TimeUnit.MILLISECONDS.sleep(1000);

        int larguraMapa = ambiente.getLargura();
        int alturaMapa = ambiente.getAltura();

        int xAleatorio = random.nextInt(larguraMapa);
        int yAleatorio = random.nextInt(alturaMapa);

        roboAtacante.setX(xAleatorio);
        roboAtacante.setY(yAleatorio);
        System.out.println(roboAtacante.getNome() + " se moveu para a posição final (" + xAleatorio + ", " + yAleatorio + ").");

        System.out.println("MissaoDanoGlobal concluída para " + roboAtacante.getNome() + ".");
    }
    catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
            System.err.println("Ocorreu um erro: " + e.getMessage());
        }
    }
}