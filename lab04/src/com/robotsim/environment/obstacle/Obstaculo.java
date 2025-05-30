package com.robotsim.environment.obstacle;

import java.util.Random;
import com.robotsim.Controlador;
import com.robotsim.environment.entity.Entidade;
import com.robotsim.environment.entity.TipoEntidade;
import com.robotsim.exceptions.ColisaoException;

/**
k * Representa um obstáculo no ambiente de simulação.
 * <p>
 * Cada obstáculo é caracterizado por um {@link TipoObstaculo}, uma posição
 * (coordenadas X e Y)
 * e dimensões (comprimento e largura) definidas pelo seu tipo.
 * Os obstáculos são gerados aleatoriamente no ambiente, garantindo que
 * não se sobreponham a outros obstáculos e que estejam dentro dos limites do
 * ambiente.
 *
 * @see Entidade
 * @see TipoObstaculo
 * @see Controlador
 */
public class Obstaculo implements Entidade {
    private int posX; // Coordenada X da posição central do obstáculo
    private int posY; // Coordenada Y da posição central do obstáculo
    protected final TipoObstaculo tipo; // O tipo específico deste obstáculo (e.g., PEDRA, ARVORE)
    private final TipoObstaculo[] tipos = TipoObstaculo.values(); // Cache dos tipos de obstáculos possíveis para
                                                                  // seleção aleatória

    /**
     * Constrói um novo obstáculo com um tipo e posição aleatórios.
     * <p>
     * O tipo do obstáculo é selecionado aleatoriamente dentre os definidos em
     * {@link TipoObstaculo}.
     * A posição (posX, posY) é determinada aleatoriamente dentro dos limites do
     * ambiente,
     * garantindo que a posição seja válida
     * ({@link #boaPosicao(TipoObstaculo, int, int)})
     * para evitar colisões com outros obstáculos existentes ou posicionamento fora
     * dos limites.
     */
    public Obstaculo() {
        Random rand = new Random();
        // Seleciona um tipo de obstáculo aleatoriamente
        this.tipo = tipos[rand.nextInt(tipos.length)];

        // Loop para encontrar uma posição válida para o obstáculo
        do {
            // Gera coordenadas X e Y aleatórias dentro das dimensões do ambiente
            this.posX = rand.nextInt(Controlador.getAmbiente().getComprimento());
            this.posY = rand.nextInt(Controlador.getAmbiente().getLargura());
        } while (!boaPosicao(tipo, posX, posY)); // Continua até encontrar uma posição que não cause colisão e esteja
                                                 // nos limites
    }

    /**
     * Verifica se uma determinada posição (testeX, testeY) é válida para um
     * obstáculo de um dado tipo.
     * <p>
     * Uma posição é considerada válida se o obstáculo, quando colocado nela:
     * <ul>
     * <li>Estiver completamente dentro dos limites do ambiente.</li>
     * <li>Não colidir com nenhum outro obstáculo já existente no ambiente.</li>
     * </ul>
     * A verificação de colisão e limites leva em conta as dimensões (comprimento e
     * largura) do tipo de obstáculo.
     *
     * @param tipo   O {@link TipoObstaculo} para o qual a posição está sendo
     *               testada.
     * @param testeX A coordenada X central da posição a ser testada.
     * @param testeY A coordenada Y central da posição a ser testada.
     * @return {@code true} se a posição for válida; {@code false} caso contrário.
     */
    public static boolean boaPosicao(TipoObstaculo tipo, int testeX, int testeY) {
        // Calcula a metade do comprimento e da largura para determinar os limites do
        // obstáculo a partir do centro
        int metadeComprimento = (tipo.comprimento - 1) / 2;
        int metadeLargura = (tipo.largura - 1) / 2;

        // Calcula as coordenadas do canto superior direito e inferior esquerdo do
        // obstáculo
        int superiorX = testeX + metadeComprimento;
        int superiorY = testeY + metadeLargura;
        int inferiorX = testeX - metadeComprimento;
        int inferiorY = testeY - metadeLargura;

        try {
            // Verifica se os cantos superior e inferior do obstáculo estão dentro dos
            // limites do ambiente
            boolean dentroDosLimitesSuperior = Controlador.getAmbiente().dentroDosLimites(superiorX, superiorY, 0); // Z=0
                                                                                                                    // para
                                                                                                                    // obstáculos
            boolean dentroDosLimitesInferior = Controlador.getAmbiente().dentroDosLimites(inferiorX, inferiorY, 0); // Z=0
                                                                                                                    // para
                                                                                                                    // obstáculos

            // Verifica se não há colisão com outros obstáculos existentes
            boolean semColisEntreObst = !testColisEntreObst(superiorX, superiorY, inferiorX, inferiorY);

            // A posição é boa se estiver dentro dos limites e não colidir com outros
            // obstáculos
            return dentroDosLimitesSuperior && dentroDosLimitesInferior && semColisEntreObst;

        } catch (ColisaoException e) {
            // Se dentroDosLimites lançar ColisaoException (o que não deveria ocorrer aqui,
            // mas por segurança)
            // ou se ocorrer qualquer outro problema inesperado que resulte em exceção,
            // considera a posição inválida.
            // Nota: A lógica de ColisaoException em dentroDosLimites pode precisar ser
            // revisada se for para colisões com robôs.
            // Aqui, o foco é a validade da posição do obstáculo em si.
            return false;
        } catch (IndexOutOfBoundsException e) { // Adicionado para capturar exceções de limites
            // Se ocorrer uma IndexOutOfBoundsException (por exemplo, ao chamar dentroDosLimites),
            // significa que a posição calculada para o obstáculo (considerando seu tamanho)
            // está fora dos limites físicos do ambiente.
            // System.err.println("IndexOutOfBoundsException em boaPosicao: " + e.getMessage()); // Opcional: para debug
            return false; // Posição é inválida se causa esta exceção.
        }
    }

    /**
     * Verifica se um retângulo definido por suas coordenadas de canto colide com
     * algum obstáculo existente no ambiente.
     * <p>
     * Este método é usado para garantir que um novo obstáculo (ret1) não se
     * sobreponha a obstáculos já posicionados (ret2).
     * A colisão é detectada usando o algoritmo de Axis-Aligned Bounding Box (AABB).
     *
     * @param ret1SuperiorX Coordenada X do canto superior direito do primeiro
     *                      retângulo (novo obstáculo).
     * @param ret1SuperiorY Coordenada Y do canto superior direito do primeiro
     *                      retângulo.
     * @param ret1InferiorX Coordenada X do canto inferior esquerdo do primeiro
     *                      retângulo.
     * @param ret1InferiorY Coordenada Y do canto inferior esquerdo do primeiro
     *                      retângulo.
     * @return {@code true} se houver colisão com algum obstáculo existente;
     *         {@code false} caso contrário.
     */
    private static boolean testColisEntreObst(int ret1SuperiorX, int ret1SuperiorY, int ret1InferiorX,
            int ret1InferiorY) {
        // Itera sobre todos os obstáculos já presentes no ambiente
        for (Obstaculo obstaculoExistente : Controlador.getAmbiente().getObstaculos()) {
            // Calcula as coordenadas dos cantos do obstáculo existente (ret2)
            // Ajuste: as dimensões devem ser usadas para calcular os limites a partir do
            // centro (posX, posY)
            int metadeCompExistente = (obstaculoExistente.getTipoObstaculo().comprimento - 1) / 2;
            int metadeLargExistente = (obstaculoExistente.getTipoObstaculo().largura - 1) / 2;

            int ret2SuperiorX = obstaculoExistente.getX() + metadeCompExistente;
            int ret2SuperiorY = obstaculoExistente.getY() + metadeLargExistente;
            int ret2InferiorX = obstaculoExistente.getX() - metadeCompExistente;
            int ret2InferiorY = obstaculoExistente.getY() - metadeLargExistente;

            // Verifica a não colisão (separação) nos eixos X e Y
            // Se ret1 está à esquerda de ret2 OU ret2 está à esquerda de ret1, não há
            // colisão no eixo X.
            boolean semSobreposicaoX = ret1SuperiorX < ret2InferiorX || ret2SuperiorX < ret1InferiorX;
            // Se ret1 está abaixo de ret2 OU ret2 está abaixo de ret1, não há colisão no
            // eixo Y.
            boolean semSobreposicaoY = ret1SuperiorY < ret2InferiorY || ret2SuperiorY < ret1InferiorY;

            // Se não há sobreposição em X OU não há sobreposição em Y, então não há colisão
            // com este obstáculo.
            if (semSobreposicaoX || semSobreposicaoY) {
                continue; // Passa para o próximo obstáculo
            }

            // Se chegou aqui, significa que há sobreposição em ambos os eixos, logo, há
            // colisão.
            return true;
        }
        // Se o loop terminar sem encontrar colisões, retorna false.
        return false;
    }

    /**
     * Retorna uma descrição textual do obstáculo, incluindo seu tipo e posição.
     *
     * @return Uma string formatada descrevendo o obstáculo.
     */
    @Override
    public String getDescricao() {
        return String.format("Obstáculo: %s na posição (%d, %d)", tipo.toString(), posX, posY);
    }

    /**
     * Retorna o caractere que representa visualmente este obstáculo no ambiente.
     * A representação é obtida a partir do {@link TipoObstaculo}.
     *
     * @return O caractere de representação do obstáculo.
     */
    @Override
    public char getRepresentacao() {
        return tipo.getRepresentacao();
    }

    /**
     * Retorna o tipo genérico da entidade, que para esta classe é sempre
     * {@link TipoEntidade#OBSTACULO}.
     *
     * @return {@link TipoEntidade#OBSTACULO}.
     */
    @Override
    public TipoEntidade getTipo() {
        return TipoEntidade.OBSTACULO;
    }

    /**
     * Retorna a coordenada X da posição central do obstáculo.
     *
     * @return A coordenada X.
     */
    @Override
    public int getX() {
        return this.posX;
    }

    /**
     * Retorna a coordenada Y da posição central do obstáculo.
     *
     * @return A coordenada Y.
     */
    @Override
    public int getY() {
        return this.posY;
    }

    /**
     * Retorna a coordenada Z (altitude) do obstáculo.
     * Para obstáculos, a altitude é sempre 0.
     *
     * @return 0.
     */
    @Override
    public int getZ() {
        return 0; // Obstáculos são sempre terrestres, no nível Z = 0
    }

    /**
     * Retorna o tipo específico do obstáculo.
     *
     * @return O {@link TipoObstaculo} deste obstáculo.
     */
    public TipoObstaculo getTipoObstaculo() {
        return this.tipo;
    }

    /**
     * Retorna o nome do tipo do obstáculo.
     *
     * @return O nome do tipo do obstáculo como uma string.
     */
    public String getNome() {
        return tipo.toString(); // Retorna o nome do enum como string
    }
}
