package com.robotsim.missions.missionTypes;

import com.robotsim.environment.Ambiente;
import com.robotsim.environment.obstacle.Obstaculo;
import com.robotsim.missions.Missao;
import com.robotsim.robots.Robo;
import com.robotsim.robots.EstadoRobo;
import com.robotsim.util.GeometryMath; // Certifique-se que esta classe e o método de distância estão acessíveis

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MissaoDestruirObstaculo implements Missao {

    @Override
    public void executar(Robo robo, Ambiente ambiente) {
        if (robo == null || ambiente == null) {
            System.out.println("Robô ou Ambiente nulo. MissaoDestruirObstaculo cancelada.");
            return;
        }

        if (robo.getEstado() != EstadoRobo.LIGADO) {
            System.out.println("Robô " + robo.getNome() + " está desligado. MissaoDestruirObstaculo cancelada.");
            return;
        }

        try {
        System.out.println("Robô " + robo.getNome() + " iniciando MissaoDestruirObstaculo.");
        TimeUnit.MILLISECONDS.sleep(1000);

        List<Obstaculo> obstaculos = ambiente.getObstaculos();

        if (obstaculos.isEmpty()) {
            System.out.println("Nenhum obstáculo encontrado no ambiente. MissaoDestruirObstaculo concluída sem ação.");
            return;
        }

        Obstaculo obstaculoMaisProximo = null;
        double menorDistancia = Double.MAX_VALUE;

        for (Obstaculo obs : obstaculos) {
            // Usando uma forma comum para distância euclidiana.
            // Ajuste se o método em GeometryMath tiver uma assinatura diferente (ex: aceitando Entidades).
            double distancia = GeometryMath.distanciaEuclidiana(robo, obs.getX(), obs.getY());

            if (distancia < menorDistancia) {
                menorDistancia = distancia;
                obstaculoMaisProximo = obs;
            }
        }

        if (obstaculoMaisProximo == null) {
            // Improvável se a lista de obstáculos não estiver vazia, mas é uma verificação de segurança.
            System.out.println("Não foi possível determinar o obstáculo mais próximo. MissaoDestruirObstaculo cancelada.");
            return;
        }

        System.out.println("Obstáculo mais próximo encontrado: " + obstaculoMaisProximo.getNome() +
                           " em (" + obstaculoMaisProximo.getX() + ", " + obstaculoMaisProximo.getY() +
                           ") a uma distância de " + String.format("%.2f", menorDistancia) + ".");
        TimeUnit.MILLISECONDS.sleep(1000);

        int xAlvoObstaculo = obstaculoMaisProximo.getX();
        int yAlvoObstaculo = obstaculoMaisProximo.getY();

        // Verificar se o robô ainda está ligado após o movimento (pode ter colidido e desligado)
        if (robo.getEstado() != EstadoRobo.LIGADO) {
            System.out.println(robo.getNome() + " foi desligado após o movimento. Não pode destruir o obstáculo.");
            System.out.println("MissaoDestruirObstaculo abortada.");
            return;
        }

        System.out.println(robo.getNome() + " tentando destruir o obstáculo " + obstaculoMaisProximo.getNome() + ".");
        ambiente.removerEntidade(obstaculoMaisProximo);
        TimeUnit.MILLISECONDS.sleep(1000);
        System.out.println("Obstáculo " + obstaculoMaisProximo.getNome() + " destruído completamente!");
        TimeUnit.MILLISECONDS.sleep(1000);
        
        robo.setX(xAlvoObstaculo);
        robo.setY(yAlvoObstaculo);
        // O robô finaliza sua ação na posição em que está agora, que idealmente é o antigo centro do obstáculo.
        System.out.println(robo.getNome() + " finalizou a ação na posição (" + robo.getX() + ", " + robo.getY() +
                           "), que era o local do obstáculo " + obstaculoMaisProximo.getNome() + ".");
        TimeUnit.MILLISECONDS.sleep(1000);
        System.out.println("MissaoDestruirObstaculo concluída para " + robo.getNome() + ".");
        TimeUnit.MILLISECONDS.sleep(1000);
        } 
        catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
            System.err.println("A espera foi interrompida: " + e.getMessage());
        }
    }
}