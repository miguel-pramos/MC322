package com.robotsim;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.robotsim.environment.Ambiente;
import com.robotsim.etc.Acao;
import com.robotsim.etc.CatalogoRobos;
import com.robotsim.robots.*;

enum GAME_STATUS {
    GAMEON,
    GAMEOVER
}

/**
 * A classe Controlador é responsável por gerenciar a execução principal do
 * simulador de robôs.
 * Ela inicializa o ambiente, registra as classes de robôs disponíveis no
 * catálogo e permite
 * que o usuário configure e controle os robôs durante a simulação.
 * <p>
 * A classe Controlador possui o <i>entry point</i> do simulador, no método Main
 * e gerencia
 * o loop principal da execução deste.
 * <p>
 * O simulador funciona em turnos, onde o usuário pode interagir com o sistema
 * para
 * controlar os robôs e visualizar o progresso da simulação.
 */
public class Controlador {
    public static final int DELTA_TIME = 1; // Tempo arbitrário de execução em segundos

    private static Ambiente ambiente;

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        inicializarRobos();
        inicialzizarSim();

        // Loop principal do simulador
        while (ambiente.getRobos().size() > 1) {
            for (Robo robo : ambiente.getRobos()) {
                interagir(robo);
                imprimirAmbiente(robo);
            }

        }

        System.out.printf("\nParabéns, %s! Você foi o único robô a sobreviver!\n",
                ambiente.getRobos().get(0).getNome());
        scanner.close();
    }

    // EXECUÇÃO NO LOOP

    private static void interagir(Robo robo) {
        System.out.printf("\n\nAÇÕES DISPONÍVEIS PARA %s:\n", robo.getNome().toUpperCase());
        int i = 1;
        for (Acao acao : robo.getAcoes()) {
            System.out.printf("[%d] %s\n", i, acao.getNome());
            i++;
        }

        int escolha = -1;
        while (escolha < 1 || escolha > robo.getAcoes().size()) {
            System.out.printf("O que %s deseja fazer? (Escolha entre 1 e %d): ", robo.getNome(),
                    robo.getAcoes().size());
            if (scanner.hasNextInt()) {
                escolha = scanner.nextInt();
                if (escolha < 1 || escolha > robo.getAcoes().size()) {
                    System.out.println("Escolha inválida. Tente novamente.");
                }
            } else {
                System.out.println("Entrada inválida. Por favor, insira um número.");
                scanner.next(); // Consumir entrada inválida
            }
        }
        scanner.nextLine(); // Consumir \n
        robo.executarAcao(robo.getAcoes().get(escolha - 1));

    }

    /**
     * TODO: documentar
     */
    private static void imprimirAmbiente(Robo robo) {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\n\n=============== MAPA DO JOGO ===============");
        for (int i = 0; i < ambiente.getLargura(); i++) {
            for (int j = 0; j < ambiente.getComprimento(); j++) {
                if (robo.getPosicaoX() == j && robo.getPosicaoY() == i)
                    System.out.print(robo.getNome().charAt(0));
                else
                    System.out.print(".");
            }
            System.out.println("");
        }
    }

    // INICIALIZAÇÃO

    /**
     * Método responsável por inicializar a simulação do simulador de robôs.
     * Este método configura o ambiente, solicita ao usuário a quantidade de robôs
     * que deseja controlar, permite a escolha de tipos, categorias, classes e nomes
     * para os robôs, e define suas posições iniciais no mapa.
     * <p>
     * Observação:
     * - O método utiliza pausas (TimeUnit.MILLISECONDS.sleep) para melhorar a
     * experiência
     * do usuário com mensagens exibidas de forma gradual.
     */
    private static void inicialzizarSim() {
        try {

            final int COMPRIMENTO = 50;
            final int LARGURA = 10;
            ambiente = new Ambiente(COMPRIMENTO, LARGURA);

            // Introdução
            System.out.println("====================== SIMULADOR DE ROBÔS ======================");
            System.out.println("Nesse simulador, você controlará robôs de combate...");
            TimeUnit.MILLISECONDS.sleep(1600);

            System.out.println("O simulador funciona por turnos, nos quais algumas ações serão disponíveis.");
            TimeUnit.MILLISECONDS.sleep(1600);

            // Seleção de quantidade de robôs
            int quantidade = -1;
            while (quantidade > 10 || quantidade <= 1) {
                System.out.print("\nQuantos robôs você deseja controlar? ");
                quantidade = scanner.nextInt();

                if (quantidade > 10)
                    System.out.println("O número escolhido é muito grande!");

                else if (quantidade <= 1)
                    System.out.println("O número escolhido é muito pequeno!");

            }
            TimeUnit.MILLISECONDS.sleep(1600);

            // Loop de configuração de cada robô
            for (int i = 0; i < quantidade; i++) {

                // ====== Seleção de categoria ======
                System.out.println("\n======= CATEGORIAS DE ROBÔS =======");

                ArrayList<String> categorias = CatalogoRobos.getCategorias();
                for (int j = 1; j <= categorias.size(); j++)
                    System.out.printf("[%d] %s\n", j, categorias.get(j - 1));

                System.out.printf("\nQual será a categoria escolhido para o seu robô {%d}? ", i + 1);

                String categoriaEscolhida = categorias.get(scanner.nextInt() - 1);
                TimeUnit.MILLISECONDS.sleep(1600);

                // ====== Seleção de tipo de robô ======
                System.out.printf("\n======= TIPOS DE %s =======\n", categoriaEscolhida.toUpperCase());

                ArrayList<String> classes = CatalogoRobos.getRobosPorCategoria(categoriaEscolhida);
                for (int j = 1; j <= classes.size(); j++)
                    System.out.printf("[%d] %s\n", j, classes.get(j - 1));

                System.out.printf("\nQual será o tipo escolhido para o seu robô {%d}? ", i + 1);

                String classeEscolhida = classes.get(scanner.nextInt() - 1);
                TimeUnit.MILLISECONDS.sleep(1600);

                // Escolha do nome do robô
                System.out.printf("\n\nEscolha um nome para o seu robô %d do tipo %s: ",
                        i + 1, classeEscolhida);

                scanner.nextLine(); // Consumir quebra de linha deixada pelo nextInt()
                String nome = scanner.nextLine();
                TimeUnit.MILLISECONDS.sleep(1600);

                // Posicionamento do robô
                System.out
                        .printf("\n\nO mapa tem tamanho (%d, %d)\n", ambiente.getComprimento(),
                                ambiente.getLargura());
                TimeUnit.MILLISECONDS.sleep(1600);

                System.out.printf("Escolha uma posição inicial para %s no formato [x y]: ", nome);
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                System.out.println();

                // Adição do robô ao ambiente
                Robo novoRobo = CatalogoRobos.criarRobo(categoriaEscolhida, classeEscolhida, nome, x, y);
                ambiente.adicionarRobo(novoRobo);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * Método responsável por inicializar os robôs no catálogo.
     * <p>
     * Robôs registrados:
     * <ul>
     * <li>"Robô Aéreo" associado às classes RoboJato e RoboDrone.
     * <li>"Robô Terrestre" associado às classes RoboTanque e RoboAntiAereo.
     * <ul>
     */
    private static void inicializarRobos() {
        CatalogoRobos.registrarRobo("Robô Aéreo", RoboJato.class);
        CatalogoRobos.registrarRobo("Robô Aéreo", RoboDrone.class);
        CatalogoRobos.registrarRobo("Robô Terrestre", RoboTanque.class);
        CatalogoRobos.registrarRobo("Robô Terrestre", RoboAntiAereo.class);
    }

    public static Scanner getScanner() {
        return scanner;
    }

    public static Ambiente getAmbiente() {
        return ambiente;
    }

}