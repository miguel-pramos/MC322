package com.robotsim.etc;

/**
 * A interface Acao representa uma ação que pode ser executada.
 * Ela fornece métodos para obter o nome da ação e executá-la
 * com um número variável de argumentos.
 */
public interface Acao {
    /**
     * @return Uma string com o nome da ação.
     */
    String getNome();

    /**
     * Executa a ação.
     * 
     * @param args que dependem da ação específica.
     */
    void executar();
}
