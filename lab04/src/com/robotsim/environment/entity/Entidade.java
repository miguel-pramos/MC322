package com.robotsim.environment.entity;

/**
 * Interface que representa uma entidade genérica no ambiente de simulação.
 * <p>
 * Todas as entidades no ambiente, como robôs e obstáculos, devem implementar
 * esta interface.
 * Ela define métodos básicos para obter informações sobre a posição, tipo,
 * descrição
 * e representação visual da entidade.
 *
 * @see TipoEntidade
 */
public interface Entidade {
    /**
     * Retorna a coordenada X da posição da entidade no ambiente.
     *
     * @return A coordenada X da entidade.
     */
    int getX();

    /**
     * Retorna a coordenada Y da posição da entidade no ambiente.
     *
     * @return A coordenada Y da entidade.
     */
    int getY();

    /**
     * Retorna a coordenada Z da posição da entidade no ambiente (altitude).
     * Para entidades terrestres, este valor pode ser 0.
     *
     * @return A coordenada Z da entidade.
     */
    int getZ();

    /**
     * Retorna o tipo da entidade.
     *
     * @return O {@link TipoEntidade} que classifica esta entidade.
     */
    TipoEntidade getTipo();

    /**
     * Retorna uma descrição textual da entidade.
     *
     * @return Uma string contendo a descrição da entidade.
     */
    String getDescricao();

    /**
     * Retorna um caractere que representa visualmente a entidade no ambiente (por
     * exemplo, em um mapa textual).
     *
     * @return O caractere de representação da entidade.
     */
    char getRepresentacao();
}
