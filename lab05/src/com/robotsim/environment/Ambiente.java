package com.robotsim.environment;

import java.util.ArrayList;

import com.robotsim.environment.entity.Entidade;
import com.robotsim.environment.entity.TipoEntidade;
import com.robotsim.environment.obstacle.Obstaculo;
import com.robotsim.exceptions.ColisaoException;
import com.robotsim.robots.Robo;

/**
 * A classe Ambiente representa o espaço tridimensional onde as entidades (robôs e obstáculos) interagem.
 * Ela gerencia as dimensões do ambiente, a localização das entidades e fornece
 * métodos para manipulação e visualização do estado do ambiente.
 */
public class Ambiente {
    private int comprimento; // Dimensão do ambiente no eixo X.
    private int largura; // Dimensão do ambiente no eixo Y.
    private int altura; // Dimensão do ambiente no eixo Z.
    private ArrayList<Entidade> entidades = new ArrayList<>(); // Lista de todas as entidades presentes no ambiente.
    private ArrayList<Entidade> entidadesRemovidas = new ArrayList<>(); // Lista de entidades que foram removidas do ambiente (ex: destruídas).
    private TipoEntidade[][][] mapa; // Representação tridimensional do ambiente, armazenando o tipo de entidade em cada coordenada.

    /**
     * Construtor da classe Ambiente.
     * Inicializa as dimensões do ambiente e o mapa tridimensional.
     *
     * @param comprimento A dimensão do ambiente no eixo X.
     * @param largura     A dimensão do ambiente no eixo Y.
     * @param altura      A dimensão do ambiente no eixo Z.
     */
    public Ambiente(int comprimento, int largura, int altura) {
        this.comprimento = comprimento;
        this.largura = largura;
        this.altura = altura;
        this.mapa = new TipoEntidade[comprimento][largura][altura];
        inicializarMapa(); // Preenche o mapa com o tipo VAZIO.
    }

    /**
     * Verifica se uma determinada coordenada (x, y, z) está ocupada por alguma entidade.
     * Este método assume que as coordenadas fornecidas já estão dentro dos limites do ambiente.
     *
     * @param x Coordenada no eixo X.
     * @param y Coordenada no eixo Y.
     * @param z Coordenada no eixo Z.
     * @return true se a posição estiver ocupada, false caso contrário.
     */
    private boolean estaOcupado(int x, int y, int z) {
        // Verifica se as coordenadas estão dentro dos limites físicos do mapa.
        // Esta verificação é uma salvaguarda, pois `dentroDosLimites` deve ser chamada antes.
        if (x < 0 || x >= this.comprimento || y < 0 || y >= this.largura || z < 0 || z >= this.altura) {
            // Considera-se que uma posição fora dos limites não está "VAZIO",
            // prevenindo ArrayIndexOutOfBoundsException se estaOcupado for chamado diretamente com coordenadas inválidas.
            return true; // Ou lançar uma exceção, dependendo da política de erro.
        }
        // Retorna true se a célula do mapa não estiver VAZIA, indicando que está ocupada.
        return this.mapa[x][y][z] != TipoEntidade.VAZIO;
    }

    /**
     * Verifica se uma posição tridimensional (x, y, z) está dentro dos limites físicos do ambiente
     * e se não está ocupada por outra entidade.
     *
     * @param x Coordenada no eixo X.
     * @param y Coordenada no eixo Y.
     * @param z Coordenada no eixo Z.
     * @return true se a posição estiver dentro dos limites físicos e não ocupada (VAZIO).
     * @throws ColisaoException se a posição estiver dentro dos limites físicos, mas já estiver ocupada.
     * @throws IndexOutOfBoundsException se as coordenadas estiverem fora dos limites físicos do mapa (implícito pela lógica de retorno).
     */
    public boolean dentroDosLimites(int x, int y, int z) throws ColisaoException {
        // 1. Primeiro, verificar se as coordenadas estão dentro das dimensões físicas do array/mapa.
        if (!(x < this.comprimento && y < this.largura && z < this.altura &&
                x >= 0 && y >= 0 && z >= 0)) {
            // Se estiver fora dos limites físicos, lança uma exceção.
            // Isso é mais informativo do que simplesmente retornar false, pois indica uma tentativa de acesso inválido.
            throw new IndexOutOfBoundsException(
                    String.format("Posição (%d, %d, %d) está fora dos limites do ambiente [%d, %d, %d].",
                            x, y, z, this.comprimento, this.largura, this.altura));
        }

        // 2. Se estiver dentro dos limites físicos, então verificar se a posição está ocupada.
        if (this.estaOcupado(x, y, z)) { // Acessa o mapa somente após garantir que x,y,z são válidos
            throw new ColisaoException("Posição (" + x + "," + y + "," + z + ") já está ocupada.");
        }

        // Se passou por ambas as verificações, a posição é válida e está vazia.
        return true; // Dentro dos limites e não ocupado (Vazio)
    }

    /**
     * Adiciona uma entidade (robô ou obstáculo) ao ambiente.
     * A entidade é adicionada à lista de entidades e sua posição é marcada no mapa.
     * Se a posição estiver ocupada ou fora dos limites, uma mensagem de erro é impressa.
     *
     * @param entidade A entidade a ser adicionada.
     */
    public void adicionarEntidade(Entidade entidade) {
        int x = entidade.getX();
        int y = entidade.getY();
        int z = entidade.getZ();
        try {
            // Verifica se a posição de destino é válida (dentro dos limites e não ocupada)
            if (dentroDosLimites(x, y, z)) {
                this.mapa[x][y][z] = entidade.getTipo(); // Marca a posição no mapa com o tipo da entidade.
                this.entidades.add(entidade); // Adiciona a entidade à lista de entidades ativas.
            }
        } catch (ColisaoException | IndexOutOfBoundsException e) {
            // Captura exceções de colisão ou acesso fora dos limites e imprime uma mensagem de erro.
            System.err.println("Erro ao adicionar entidade: " + e.getMessage());
        }
    }

    /**
     * Remove uma entidade do ambiente.
     * A posição da entidade no mapa é marcada como VAZIO, e a entidade é movida
     * da lista de entidades ativas para a lista de entidades removidas.
     *
     * @param entidade A entidade a ser removida.
     */
    public void removerEntidade(Entidade entidade) {
        // Marca a posição antiga da entidade como VAZIO no mapa.
        // É importante usar as coordenadas atuais da entidade.
        if (entidade.getX() >= 0 && entidade.getX() < comprimento &&
            entidade.getY() >= 0 && entidade.getY() < largura &&
            entidade.getZ() >= 0 && entidade.getZ() < altura) {
            this.mapa[entidade.getX()][entidade.getY()][entidade.getZ()] = TipoEntidade.VAZIO;
        }
        this.entidades.remove(entidade); // Remove a entidade da lista de entidades ativas.
        this.entidadesRemovidas.add(entidade); // Adiciona a entidade à lista de entidades removidas.
    }

    /**
     * Move uma entidade para uma nova posição (novoX, novoY, novoZ) no ambiente.
     * A posição antiga da entidade no mapa é marcada como VAZIO e a nova posição é marcada com o tipo da entidade.
     * Assume-se que a validade da nova posição (limites e colisão) foi verificada antes de chamar este método.
     *
     * @param entidade A entidade a ser movida.
     * @param novoX    A nova coordenada X da entidade.
     * @param novoY    A nova coordenada Y da entidade.
     * @param novoZ    A nova coordenada Z da entidade.
     */
    public void moverEntidade(Entidade entidade, int novoX, int novoY, int novoZ) {
        // Limpa a posição antiga da entidade no mapa.
        this.mapa[entidade.getX()][entidade.getY()][entidade.getZ()] = TipoEntidade.VAZIO;
        // Marca a nova posição da entidade no mapa.
        this.mapa[novoX][novoY][novoZ] = entidade.getTipo();
        // Atualiza as coordenadas da entidade (esta responsabilidade pode estar na própria entidade também).
        // entidade.setPosicao(novoX, novoY, novoZ); // Se houver um método para isso na classe Entidade.
    }

    /**
     * Inicializa todas as posições do mapa do ambiente como VAZIO.
     * Este método é chamado durante a construção do ambiente.
     */
    void inicializarMapa() {
        for (int i = 0; i < comprimento; i++) {
            for (int j = 0; j < largura; j++) {
                for (int k = 0; k < altura; k++) {
                    this.mapa[i][j][k] = TipoEntidade.VAZIO; // Define cada célula como VAZIO.
                }
            }
        }
    }

    /**
     * Exibe uma representação textual 2D (vista de cima) do ambiente no console.
     * Mostra a representação da entidade que está na maior altitude (Z) em cada coordenada (X, Y).
     * Se não houver entidade em uma coordenada (X,Y) em nenhuma altitude, exibe ".".
     */
    public void visualizarAmbiente() {
        System.out.println("\n--- Visualização do Ambiente (Vista de Cima) ---");
        // Itera sobre a largura (Y) e o comprimento (X) para formar a grade 2D.
        for (int y = 0; y < largura; y++) {
            for (int x = 0; x < comprimento; x++) {
                Entidade entidadeNoTopo = null;
                // Procura a entidade com a maior coordenada Z para a posição (x,y) atual.
                int zMaior = 0; // Começa do topo (maior Z) e desce.
                
                for (Entidade entidade : entidades) {
                    // Verifica se a entidade está na posição (x,y,z) atual.
                    if(entidade instanceof Obstaculo) {
                        if (entidade.getZ() > zMaior && Obstaculo.IsOccupied((Obstaculo) entidade, x, y)){
                            entidadeNoTopo = entidade;
                            zMaior = entidade.getZ(); // Atualiza a maior altura encontrada.         
                        }
                    }
                    else if (entidade.getX() == x && entidade.getY() == y && entidade.getZ() >= zMaior) {
                        entidadeNoTopo = entidade; // Encontrou a entidade mais alta nesta coluna (x,y).
                        zMaior = entidade.getZ(); // Atualiza a maior altura encontrada.
                    }
                }
                
                // Imprime a representação da entidade no topo ou "." se a coluna estiver vazia.
                if (entidadeNoTopo != null) {
                    System.out.print(entidadeNoTopo.getRepresentacao());
                } else {
                    System.out.print("."); // Ponto para indicar uma célula vazia na projeção 2D.
                }
            }
            System.out.println(); // Nova linha para a próxima fileira da grade.
        }
        System.out.println("-------------------------------------------------");
    }

    /**
     * Retorna a largura do ambiente (dimensão Y).
     *
     * @return A largura do ambiente.
     */
    public int getLargura() {
        return largura;
    }

    /**
     * Retorna o comprimento do ambiente (dimensão X).
     *
     * @return O comprimento do ambiente.
     */
    public int getComprimento() {
        return comprimento;
    }

    /**
     * Retorna a altura do ambiente (dimensão Z).
     *
     * @return A altura do ambiente.
     */
    public int getAltura() {
        return altura;
    }

    /**
     * Retorna a lista de todas as entidades atualmente ativas no ambiente.
     *
     * @return Uma {@link ArrayList} contendo todas as entidades ativas.
     */
    public ArrayList<Entidade> getEntidades() {
        return entidades;
    }

    /**
     * Retorna a lista de todas as entidades que foram removidas do ambiente.
     *
     * @return Uma {@link ArrayList} contendo as entidades removidas.
     */
    public ArrayList<Entidade> getEntidadesRemovidas() {
        return entidadesRemovidas;
    }

    /**
     * Retorna uma lista contendo apenas os robôs ativos no ambiente.
     * Filtra a lista de todas as entidades para incluir apenas instâncias de {@link Robo}.
     *
     * @return Uma {@link ArrayList} contendo todos os robôs ativos.
     */
    public ArrayList<Robo> getRobos() {
        ArrayList<Robo> robos = new ArrayList<>();
        for (Entidade entidade : entidades) {
            if (entidade instanceof Robo) { // Verifica se a entidade é uma instância de Robo.
                robos.add((Robo) entidade); // Adiciona à lista de robôs.
            }
        }
        return robos;
    }

    /**
     * Retorna uma lista contendo apenas os obstáculos ativos no ambiente.
     * Filtra a lista de todas as entidades para incluir apenas instâncias de {@link Obstaculo}.
     *
     * @return Uma {@link ArrayList} contendo todos os obstáculos ativos.
     */
    public ArrayList<Obstaculo> getObstaculos() {
        ArrayList<Obstaculo> obstaculos = new ArrayList<>();
        for (Entidade entidade : entidades) {
            if (entidade instanceof Obstaculo) { // Verifica se a entidade é uma instância de Obstaculo.
                obstaculos.add((Obstaculo) entidade); // Adiciona à lista de obstáculos.
            }
        }
        return obstaculos;
    }
}
