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
        // Esta verificação agora é redundante aqui se dentroDosLimites for chamada
        // primeiro,
        // mas é seguro mantê-la se estaOcupado puder ser chamado de outros lugares.
        // No entanto, a lógica principal de verificação de limites deve estar em
        // dentroDosLimites.
        if (x < 0 || x >= this.comprimento || y < 0 || y >= this.largura || z < 0 || z >= this.altura) {
            // Se chamado com coordenadas fora dos limites, considera-se "ocupado" no
            // sentido de inválido.
            // Ou poderia lançar uma exceção, dependendo da semântica desejada.
            // Para o contexto atual de dentroDosLimites, esta condição não deveria ser
            // atingida se a ordem for corrigida.
            return false; // Ou true, dependendo de como "ocupado" é interpretado para posições inválidas.
                          // Vamos assumir que uma posição fora dos limites não está "VAZIO".
        }
        return this.mapa[x][y][z] != TipoEntidade.VAZIO;
    }

    /**
     * Verifica se uma posição tridimensional está dentro dos limites do ambiente.
     *
     * @param x Coordenada no eixo X.
     * @param y Coordenada no eixo Y.
     * @param z Coordenada no eixo Z.
     * @return true se a posição estiver dentro dos limites E não ocupada, false se
     *         estiver fora dos limites físicos.
     * @throws ColisaoException se a posição estiver dentro dos limites físicos MAS
     *                          ocupada (não vazia).
     */
    public boolean dentroDosLimites(int x, int y, int z) throws ColisaoException {
        // 1. Primeiro, verificar se as coordenadas estão dentro das dimensões do array
        if (!(x < this.comprimento && y < this.largura && z < this.altura &&
                x >= 0 && y >= 0 && z >= 0)) {
            return false; // Fora dos limites físicos do mapa
        }

        // 2. Se estiver dentro dos limites físicos, então verificar se está ocupado
        if (this.estaOcupado(x, y, z)) { // Acessa o mapa somente após garantir que x,y,z são válidos
            throw new ColisaoException("Posição (" + x + "," + y + "," + z + ") já está ocupada.");
        }

        return true; // Dentro dos limites e não ocupado (Vazio)
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
        try {
            if (dentroDosLimites(x, y, z)) {
                this.mapa[x][y][z] = entidade.getTipo();
                this.entidades.add(entidade);
            }
        } catch (ColisaoException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Remove um robô do ambiente, simulando sua destruição.
     *
     * @param robo O robô a ser removido.
     * @return true se o robô foi removido com sucesso, false caso contrário.
     */
    public void removerEntidade(Entidade entidade) {
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

    public int getAltura() {
        return altura;
    }

    public ArrayList<Entidade> getEntidades() {
        return entidades;
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
