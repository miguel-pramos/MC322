package com.robotsim.util;

import com.robotsim.Controlador;
import com.robotsim.environment.obstacle.Obstaculo;
import com.robotsim.environment.obstacle.TipoObstaculo;
import com.robotsim.robots.Robo;
import com.robotsim.robots.RoboAereo;
import com.robotsim.robots.RoboTerrestre;

public class TesteColisao {
    public static int True = 1;
    public static int False = 0;

    public static boolean existeColisao(int[] dadosColisao) {
        if (dadosColisao[3] == True) {
            return true;
        }
        return false;
    }

    public static int[] dadosColisao(Robo roboMovendo, int xFin, int yFin) {
        int xIni = roboMovendo.getX();
        int yIni = roboMovendo.getY();

        int dx = Math.abs(xFin - xIni);
        int dy = Math.abs(yFin - yIni);

        int sx = xIni < xFin ? 1 : -1;
        int sy = yIni < yFin ? 1 : -1;

        int err = dx - dy;
        int atualX = xIni;
        int atualY = yIni;
        int antigoX = atualX;
        int antigoY = atualY;

        while (true) {
            if (atualX == xFin && atualY == yFin)
                break;

            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                antigoX = atualX;
                atualX += sx;
            }
            if (e2 < dx) {
                err += dx;
                antigoY = atualY;
                atualY += sy;
            }

            String tipo = tipoDeColisao(roboMovendo, atualX, atualY);
            switch (tipo) {
                case "Nula":
                    continue;
                case "Robo":
                    System.out.printf("Colidiu com um robo em %d %d\n", atualX, atualY);
                    return new int[] { antigoX, antigoY, 0, True };
                default:
                    TipoObstaculo obstColidido = TipoObstaculo.valueOf(tipo);
                    System.out.printf("Você colidiu com um %s\n", tipo);
                    return new int[] { antigoX, antigoY, obstColidido.getDano(), True };
            }
        }

        int[] dados = { atualX, atualY, 0, False };
        return dados;
    }

    public static String tipoDeColisao(Robo roboMovendo, int xRobo, int yRobo) {

        if (roboMovendo instanceof RoboTerrestre) {
            for (Robo robo : Controlador.getAmbiente().getRobos()) {
                if (robo instanceof RoboTerrestre && robo != roboMovendo &&
                        robo.getX() == xRobo && robo.getY() == yRobo) {
                    return "Robo";
                }
            }
        } else if (roboMovendo instanceof RoboAereo) {
            int zRobo = ((RoboAereo) roboMovendo).getAltitude();

            for (Robo robo : Controlador.getAmbiente().getRobos()) {
                if (robo instanceof RoboAereo &&
                        robo != roboMovendo &&
                        robo.getX() == xRobo &&
                        robo.getY() == yRobo &&
                        ((RoboAereo) robo).getAltitude() == zRobo)
                    return "Robo";
            }
        }

        for (Obstaculo obstaculo : Controlador.getAmbiente().getObstaculos()) {
            int obsSupX = obstaculo.getX() + (obstaculo.getTipoObstaculo().getComprimento() - 1) / 2;
            int obsSupY = obstaculo.getY() + (obstaculo.getTipoObstaculo().getLargura() - 1) / 2;

            int obsInfX = obstaculo.getX() - (obstaculo.getTipoObstaculo().getComprimento() - 1) / 2;
            int obstInfY = obstaculo.getY() - (obstaculo.getTipoObstaculo().getLargura() - 1) / 2;

            if (xRobo < obsInfX || xRobo > obsSupX) {
                continue;
            }
            if (yRobo < obstInfY || yRobo > obsSupY) {
                continue;
            }

            if (roboMovendo instanceof RoboAereo &&
                    ((RoboAereo) roboMovendo).getAltitude() > obstaculo.getTipoObstaculo().getAltura()) {
                continue;
            }

            // Se nenhuma das condições acima for verdadeira, há colisão
            return obstaculo.getNome();
        }

        return "Nula";
    }
}
