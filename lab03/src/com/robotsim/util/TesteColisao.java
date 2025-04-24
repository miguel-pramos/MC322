package com.robotsim.util;

import com.robotsim.Controlador;
import com.robotsim.environment.Obstaculo;
import com.robotsim.environment.TipoObstaculo;
import com.robotsim.robots.Robo;
import com.robotsim.robots.RoboTerrestre;

public class TesteColisao {
    private static final int VAZIO = -1;

    public static boolean semColisao(int[] dadosPossivelColisao) {
        return dadosPossivelColisao[0] == VAZIO;
    }

    public static int[] dadosColisao(int xIni, int yIni, int xFin, int yFin){
        int dx = Math.abs(xFin - xIni);
        int dy = Math.abs(yFin - yIni);

        int sx = xIni < xFin ? 1 : -1;
        int sy = yIni < yFin ? 1 : -1;

        int err = dx - dy;
        int atualX = xIni;
        int atualY = yIni;

        while (true) {
            if (atualX == xFin && atualY == yFin)
                break;

            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                atualX += sx;
            }
            if (e2 < dx) {
                err += dx;
                atualY += sy;
            }

            String tipo = tipoDeColisao(atualX, atualY);
            switch (tipo) {
                case "Nula": continue;
                case "Robo":
                    System.out.printf("Colidiu com um robo em %d %d\n", atualX, atualY);
                    return new int[]{atualX, atualY, 0};
                default:
                    TipoObstaculo obstColidido = TipoObstaculo.valueOf(tipo);
                    System.out.printf("Você colidiu com um %s\n", tipo);
                    return new int[]{atualX, atualY, obstColidido.getDano()};
            }
        }

        int[] dados = {VAZIO, VAZIO, VAZIO};
        return dados;
    }

    protected static String tipoDeColisao(int xRobo, int yRobo){
        for(Robo robo : Controlador.getAmbiente().getRobos()){
            if(robo instanceof RoboTerrestre &&
                    robo.getPosicaoX() == xRobo && robo.getPosicaoY() == yRobo){
                return "Robo";
            }
        }

        for(Obstaculo obstaculo : Controlador.getAmbiente().getObstaculos()){
            int obsSupX = obstaculo.getPosX() + obstaculo.getTipo().getComprimento();
            int obsSupY = obstaculo.getPosY() + obstaculo.getTipo().getLargura();

            int obsInfX = obstaculo.getPosX() - obstaculo.getTipo().getComprimento();
            int obstInfY = obstaculo.getPosY() - obstaculo.getTipo().getLargura();

            if (xRobo < obsInfX || xRobo > obsSupX) {
                continue;
            }
            if (yRobo < obstInfY || yRobo > obsSupY) {
                continue;
            }

            // Se nenhuma das condições acima for verdadeira, há colisão
            return obstaculo.getNome();
        }

        return "Nula";
    }
}
