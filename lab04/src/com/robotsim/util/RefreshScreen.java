package com.robotsim.util;

/**
 * A classe {@code RefreshScreen} fornece um método para limpar a tela do
 * console.
 * Funciona em sistemas Windows e outros sistemas baseados em Unix (como Linux e
 * macOS).
 * 
 * <p>
 * Esta classe não deve ser instanciada, pois contém apenas métodos estáticos.
 * </p>
 */
public class RefreshScreen {

    /**
     * Limpa a tela do console.
     * 
     * <p>
     * Este método detecta o sistema operacional e executa o comando apropriado
     * para limpar a tela ("cls" para Windows, "clear" para outros).
     * </p>
     * 
     * @throws Exception Se ocorrer um erro durante a execução do comando de limpeza
     *                   (por exemplo, se o comando não for encontrado ou houver um
     *                   problema de permissão).
     */
    public static void clearScreen() throws Exception {
        if (System.getProperty("os.name").contains("win")) {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } else {
            new ProcessBuilder("clear").inheritIO().start().waitFor();
        }
    }
}