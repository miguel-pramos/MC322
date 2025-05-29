package com.robotsim.util;

import com.robotsim.Controlador;
import com.robotsim.environment.obstacle.Obstaculo;
import com.robotsim.environment.obstacle.TipoObstaculo;
import com.robotsim.robots.Robo;
import com.robotsim.robots.RoboAereo;
import com.robotsim.robots.RoboTerrestre;

/**
 * A classe {@code TesteColisao} é responsável por detectar colisões entre robôs
 * e obstáculos
 * ou entre robôs no ambiente de simulação.
 * 
 * <p>
 * Ela utiliza o algoritmo de Bresenham para verificar colisões ao longo da
 * trajetória de movimento
 * de um robô.
 * </p>
 */
public class TesteColisao {
    // Constantes para representar verdadeiro e falso, usadas na detecção de
    // colisão.
    // TODO: Considerar substituir por booleanos diretamente para maior clareza.
    public static int True = 1;
    public static int False = 0;

    /**
     * Verifica se houve uma colisão com base nos dados retornados por
     * {@link #dadosColisao(Robo, int, int)}.
     *
     * @param dadosColisao Um array de inteiros onde o quarto elemento (índice 3)
     *                     indica se ocorreu uma colisão.
     * @return {@code true} se uma colisão ocorreu, {@code false} caso contrário.
     */
    public static boolean existeColisao(int[] dadosColisao) {
        if (dadosColisao[3] == True) {
            return true;
        }
        return false;
    }

    /**
     * Calcula a trajetória de um robô de uma posição inicial para uma final e
     * detecta colisões.
     * Utiliza o algoritmo de Bresenham para determinar os pontos intermediários da
     * trajetória.
     *
     * @param roboMovendo O robô que está se movendo.
     * @param xFin        A coordenada X final do movimento.
     * @param yFin        A coordenada Y final do movimento.
     * @return Um array de inteiros contendo:
     *         <ul>
     *         <li>Índice 0: A coordenada X da última posição segura antes da
     *         colisão (ou a posição final se não houver colisão).</li>
     *         <li>Índice 1: A coordenada Y da última posição segura antes da
     *         colisão (ou a posição final se não houver colisão).</li>
     *         <li>Índice 2: O dano causado pelo obstáculo, se a colisão foi com um
     *         obstáculo (0 caso contrário).</li>
     *         <li>Índice 3: {@code True} (1) se ocorreu uma colisão, {@code False}
     *         (0) caso contrário.</li>
     *         </ul>
     */
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
                case "Nula": // Nenhuma colisão no ponto atual, continua a verificação.
                    continue;
                case "Robo": // Colisão com outro robô.
                    System.out.printf("Colidiu com um robo em %d %d\n", atualX, atualY);
                    return new int[] { antigoX, antigoY, 0, True }; // Retorna a posição anterior e indica colisão.
                default: // Colisão com um obstáculo.
                    TipoObstaculo obstColidido = TipoObstaculo.valueOf(tipo);
                    System.out.printf("Você colidiu com um %s\n", tipo);
                    // Retorna a posição anterior, o dano do obstáculo e indica colisão.
                    return new int[] { antigoX, antigoY, obstColidido.getDano(), True };
            }
        }

        // Se o loop terminar sem colisões, retorna a posição final e indica ausência de
        // colisão.
        int[] dados = { atualX, atualY, 0, False };
        return dados;
    }

    /**
     * Determina o tipo de colisão em uma coordenada específica para um dado robô.
     * Verifica colisões com outros robôs (do mesmo tipo, aéreo ou terrestre) e com
     * obstáculos.
     *
     * @param roboMovendo O robô para o qual a colisão está sendo verificada.
     * @param xRobo       A coordenada X a ser verificada.
     * @param yRobo       A coordenada Y a ser verificada.
     * @return Uma string indicando o tipo de colisão:
     *         <ul>
     *         <li>"Robo": Se colidiu com outro robô.</li>
     *         <li>Nome do {@link TipoObstaculo}: Se colidiu com um obstáculo (e.g.,
     *         "PEDRA", "ARVORE").</li>
     *         <li>"Nula": Se não houve colisão.</li>
     *         </ul>
     */
    public static String tipoDeColisao(Robo roboMovendo, int xRobo, int yRobo) {

        // Verifica colisão com outros robôs terrestres
        if (roboMovendo instanceof RoboTerrestre) {
            for (Robo robo : Controlador.getAmbiente().getRobos()) {
                if (robo instanceof RoboTerrestre && robo != roboMovendo &&
                        robo.getX() == xRobo && robo.getY() == yRobo) {
                    return "Robo";
                }
            }
        } else if (roboMovendo instanceof RoboAereo) {
            // Verifica colisão com outros robôs aéreos na mesma altitude
            int zRobo = ((RoboAereo) roboMovendo).getZ();

            for (Robo robo : Controlador.getAmbiente().getRobos()) {
                if (robo instanceof RoboAereo &&
                        robo != roboMovendo &&
                        robo.getX() == xRobo &&
                        robo.getY() == yRobo &&
                        ((RoboAereo) robo).getZ() == zRobo)
                    return "Robo";
            }
        }

        // Verifica colisão com obstáculos
        for (Obstaculo obstaculo : Controlador.getAmbiente().getObstaculos()) {
            // Calcula os limites do obstáculo
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
                    ((RoboAereo) roboMovendo).getZ() > obstaculo.getTipoObstaculo().getAltura()) {
                continue;
            }

            // Se nenhuma das condições acima for verdadeira, há colisão
            return obstaculo.getNome();
        }

        return "Nula";
    }
}
