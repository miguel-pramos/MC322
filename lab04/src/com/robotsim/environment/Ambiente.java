package com.robotsim.environment;

import java.util.ArrayList;

import com.robotsim.environment.entity.Entidade;
import com.robotsim.environment.entity.TipoEntidade;
import com.robotsim.environment.obstacle.Obstaculo;
import com.robotsim.exceptions.ColisaoException;
import com.robotsim.robots.Robo;

/**
 * A classe Ambiente representa o espaço onde os robôs interagem.
 * Define os limites do ambiente e gerencia os robôs presentes nele.
 */
public class Ambiente {
    private int comprimento, largura, altura; // Comprimento do ambiente.
    private ArrayList<Entidade> entidades = new ArrayList<>();
    private ArrayList<Entidade> entidadesRemovidas = new ArrayList<>();
    private TipoEntidade[][][] mapa;

    public Ambiente(int comprimento, int largura, int altura) {
        this.comprimento = comprimento;
        this.largura = largura;
        this.altura = altura;
        this.mapa = new TipoEntidade[comprimento][largura][altura];
        inicializarMapa();
    }

    private boolean estaOcupado(int x, int y, int z) {
        return this.mapa[x][y][z] == TipoEntidade.VAZIO;
    }

    /**
     * Verifica se uma posição tridimensional está dentro dos limites do ambiente.
     *
     * @param x            Coordenada no eixo X.
     * @param y            Coordenada no eixo Y.
     * @param z            Coordenada no eixo Z.
     * @param alturaMaxima Coordenada máxima para o eixo Z.
     * @return true se a posição estiver dentro dos limites, false caso contrário.
     */
    public boolean dentroDosLimites(int x, int y, int z) throws ColisaoException {
        if (estaOcupado(x, y, z))
            throw new ColisaoException();
        return (x < this.comprimento && y < this.largura && z < this.altura)
                && (x >= 0 && y >= 0 && z >= 0);
    }

    /**
     * Adiciona um robô ao ambiente.
     *
     * @param robo O robô a ser adicionado.
     */
    public void adicionarEntidade(Entidade entidade) {
        int x = entidade.getX();
        int y = entidade.getY();
        int z = entidade.getZ();
        if (dentroDosLimites(x, y, z)) {
            this.mapa[x][y][z] = entidade.getTipo();
            this.entidades.add(entidade);
        } else {
            throw new IllegalArgumentException("Entidade fora dos limites do ambiente.");
        }
    }

    /**
     * Remove um robô do ambiente, simulando sua destruição.
     *
     * @param robo O robô a ser removido.
     * @return true se o robô foi removido com sucesso, false caso contrário.
     */
    public void destruirEntidade(Entidade entidade) {
        this.mapa[0][0][0] = TipoEntidade.VAZIO;
        this.entidades.remove(entidade);
        entidadesRemovidas.add(entidade);
    }

    public void moverEntidade(Entidade entidade, int novoX, int novoY, int novoZ) {
        this.mapa[entidade.getX()][entidade.getY()][entidade.getZ()] = TipoEntidade.VAZIO;
        this.mapa[novoX][novoY][novoZ] = entidade.getTipo();
    }

    void inicializarMapa() {
        for (int i = 0; i < comprimento; i++)
            for (int j = 0; j < largura; j++)
                for (int k = 0; k < altura; k++)
                    this.mapa[i][j][k] = TipoEntidade.VAZIO;

    }

    public void visualizarAmbiente() {
        for (int y = 0; y < largura; y++) {
            for (int x = 0; x < comprimento; x++) {
                Entidade entidadeNoTopo = null;
                for (int z = altura - 1; z >= 0; z--) {
                    for (Entidade entidade : entidades)
                        if (entidade.getX() == x && entidade.getY() == y && entidade.getZ() == z) {
                            entidadeNoTopo = entidade;
                            break;
                        }

                    if (entidadeNoTopo != null)
                        break;
                }
                if (entidadeNoTopo != null)
                    System.out.print(entidadeNoTopo.getRepresentacao());
                else
                    System.out.print(".");

                System.out.print(" ");
            }
            System.out.println();
        }
    }

    public int getLargura() {
        return largura;
    }

    public int getComprimento() {
        return comprimento;
    }

    public ArrayList<Entidade> getEntidadesRemovidas() {
        return entidadesRemovidas;
    }

    public ArrayList<Robo> getRobos() {
        ArrayList<Robo> robos = new ArrayList<>();
        for (Entidade entidade : entidades) {
            if (entidade instanceof Robo) {
                robos.add((Robo) entidade);
            }
        }
        return robos;
    }

    public ArrayList<Obstaculo> getObstaculos() {
        ArrayList<Obstaculo> obstaculos = new ArrayList<>();
        for (Entidade entidade : entidades) {
            if (entidade instanceof Obstaculo) {
                obstaculos.add((Obstaculo) entidade);
            }
        }
        return obstaculos;
    }
}
