package com.robotsim.environment.obstacle;

import java.util.Random;
import com.robotsim.Controlador;
import com.robotsim.environment.entity.Entidade;
import com.robotsim.environment.entity.TipoEntidade;
import com.robotsim.exceptions.ColisaoException;

/**
 * A classe Obstaculo representa um obstáculo no ambiente do simulador.
 * Cada obstáculo possui um tipo, uma posição e dimensões específicas.
 *
 * <p>
 * A classe é responsável por gerar obstáculos aleatórios no ambiente,
 * garantindo que eles não colidam com outros obstáculos e estejam dentro dos
 * limites do ambiente.
 */
public class Obstaculo implements Entidade {
    private int posX; // Coordenada X do obstáculo
    private int posY; // Coordenada Y do obstáculo
    protected final TipoObstaculo tipo; // Tipo do obstáculo
    private final TipoObstaculo[] tipos = TipoObstaculo.values(); // Lista de tipos possíveis de obstáculos

    public Obstaculo() {
        Random rand = new Random();
        this.tipo = tipos[rand.nextInt(tipos.length)];

        do {
            this.posX = rand.nextInt(Controlador.getAmbiente().getComprimento());
            this.posY = rand.nextInt(Controlador.getAmbiente().getLargura());
        } while (!boaPosicao(tipo, posX, posY));
    }

    /**
     * Verifica se uma posição é válida para o obstáculo.
     *
     * <p>
     * A posição é considerada válida se o obstáculo estiver dentro dos limites do
     * ambiente
     * e não colidir com outros obstáculos.
     * </p>
     *
     * @param tipo   O tipo do obstáculo.
     * @param testeX A coordenada X a ser testada.
     * @param testeY A coordenada Y a ser testada.
     * @return true se a posição for válida, false caso contrário.
     */
    public static boolean boaPosicao(TipoObstaculo tipo, int testeX, int testeY) {
        int metadeComprimento = (tipo.comprimento - 1) / 2;
        int metadeLargura = (tipo.largura - 1) / 2;

        int superiorX = testeX + metadeComprimento;
        int superiorY = testeY + metadeLargura;
        int inferiorX = testeX - metadeComprimento;
        int inferiorY = testeY - metadeLargura;

        boolean dentroDosLimitesSuperior;
        try {
            dentroDosLimitesSuperior = Controlador.getAmbiente().dentroDosLimites(superiorX, superiorY, 0);
            boolean dentroDosLimitesInferior = Controlador.getAmbiente().dentroDosLimites(inferiorX, inferiorY, 0);

            boolean semColisEntreObst = !testColisEntreObst(superiorX, superiorY, inferiorX, inferiorY);

            return dentroDosLimitesSuperior && dentroDosLimitesInferior && semColisEntreObst;

        } catch (ColisaoException e) {

            return false;
        }

    }

    /**
     * Verifica se há colisão entre o obstáculo atual e outros obstáculos no
     * ambiente.
     *
     * @param ret1SuperiorX Coordenada X superior do obstáculo atual.
     * @param ret1SuperiorY Coordenada Y superior do obstáculo atual.
     * @param ret1InferiorX Coordenada X inferior do obstáculo atual.
     * @param ret1InferiorY Coordenada Y inferior do obstáculo atual.
     * @return true se houver colisão, false caso contrário.
     */
    private static boolean testColisEntreObst(int ret1SuperiorX, int ret1SuperiorY, int ret1InferiorX,
            int ret1InferiorY) {
        for (Obstaculo obstaculo : Controlador.getAmbiente().getObstaculos()) {
            int ret2SuperiorX = obstaculo.getX() + obstaculo.getTipoObstaculo().comprimento;
            int ret2SuperiorY = obstaculo.getY() + obstaculo.getTipoObstaculo().largura;

            int ret2InferiorX = obstaculo.getX() - obstaculo.getTipoObstaculo().comprimento;
            int ret2InferiorY = obstaculo.getY() - obstaculo.getTipoObstaculo().largura;

            // Verifica se um retângulo está à esquerda do outro
            if (ret2SuperiorX < ret1InferiorX || ret1SuperiorX < ret2InferiorX) {
                continue;
            }

            // Verifica se um retângulo está acima do outro
            if (ret1SuperiorY < ret2InferiorY || ret2SuperiorY < ret1InferiorY) {
                continue;
            }

            // Se nenhuma das condições acima for verdadeira, há colisão
            return true;
        }
        return false;
    }

    @Override
    public String getDescricao() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public char getRepresentacao() {
        // TODO Auto-generated method stub
        return 0;
    }

    public TipoEntidade getTipo() {
        return TipoEntidade.OBSTACULO;
    }

    public int getX() {
        return this.posX;
    }

    public int getY() {
        return this.posY;
    }

    public int getZ() {
        return 0;
    }

    public TipoObstaculo getTipoObstaculo() {
        return this.tipo;
    }

    public String getNome() {
        return tipo.toString();
    }
}
