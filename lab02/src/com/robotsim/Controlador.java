package com.robotsim;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.robotsim.environment.Ambiente;
import com.robotsim.etc.CatalogoRobos;
import com.robotsim.robots.*;


enum GAME_STATUS {
    GAMEON,
    GAMEOVER
}

public class Controlador {
    public static final int deltaTime = 1; // Tempo arbitrário de execução em segundos
    public static GAME_STATUS gameStatus = GAME_STATUS.GAMEON;

    public static Ambiente ambiente;

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        inicializarRobos();
        inicialzizarSim();

        while (gameStatus != GAME_STATUS.GAMEOVER) {
            imprimirAmbiente();

            System.out.print("Digite 1 para continuar: ");
            scanner.nextInt();
        }

        System.out.println("============= GAME OVER =============");

    }

    private static void inicialzizarSim() {
        try {

            final int COMPRIMENTO = 50;
            final int LARGURA = 10;
            ambiente = new Ambiente(COMPRIMENTO, LARGURA);

            System.out.println("====================== SIMULADOR DE ROBÔS ======================");
            System.out.println("Nesse simulador, você controlará robôs de combate...");
            TimeUnit.MILLISECONDS.sleep(1600);

            System.out.println("O simulador funciona por turnos, nos quais algumas ações serão disponíveis.");
            TimeUnit.MILLISECONDS.sleep(1600);

            int quantidade;
            do {
                System.out.print("\nQuantos robôs você deseja controlar? ");
                quantidade = scanner.nextInt();

                if (quantidade > 10) {
                    System.out.println("O número escolhido é muito grande!");
                }
            } while (quantidade > 10);
            TimeUnit.MILLISECONDS.sleep(1600);

            for (int i = 0; i < quantidade; i++) {
                System.out.print("\n\nQual será o tipo escolhido para o seu robô " + (i + 1) + "? ");
                TimeUnit.MILLISECONDS.sleep(1600);
                
                ArrayList<String> categorias = CatalogoRobos.getCategorias();
                System.out.print(categorias);
                
                for (int j = 1; j <= categorias.size(); j++) {
                    System.out.print(j + " " + categorias.get(j - 1) + "   ");
                }

                String tipo = scanner.nextLine();

                int idxCategoria = scanner.nextInt() - 1;
                String categoriaEscolhida = categorias.get(idxCategoria);
                TimeUnit.MILLISECONDS.sleep(1600);

                ArrayList<String> classes = CatalogoRobos.getRobosPorCategoria(categorias.get(idxCategoria));
                for (int j = 1; j <= classes.size(); j++) {
                    System.out.print(j + " " + classes.get(j) + "    ");
                }
                int idxClasse = scanner.nextInt() - 1;
                String classeEscolhida = classes.get(idxClasse);
                TimeUnit.MILLISECONDS.sleep(1600);

                System.out.print("\n\nEscolha um nome para o seu robô " + (i + 1) +
                        " do tipo" + classes.get(idxClasse) + ": ");
                String nome = scanner.nextLine();
                TimeUnit.MILLISECONDS.sleep(1600);


                System.out
                        .println("\nO mapa tem tamanho (%d, %d)".formatted(ambiente.getComprimento(),
                                ambiente.getLargura()));
                TimeUnit.MILLISECONDS.sleep(1600);

                System.out.print("Escolha uma posição inicial para %s no formato [x y]: ".formatted(nome));
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                System.out.println();

                Robo novoRobo = CatalogoRobos.criarRobo(categoriaEscolhida, classeEscolhida, nome, x, y);
                ambiente.adicionarRobo(novoRobo);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private static void imprimirAmbiente() {
        for (int i = 0; i < ambiente.getLargura(); i++) {
            for (int j = 0; j < ambiente.getComprimento(); j++) {
                for (Robo robo : ambiente.getRobos())
                    if (robo.getPosicaoX() == j && robo.getPosicaoY() == i)
                        System.out.print(robo.getNome().charAt(0));
                    else
                        System.out.print(".");
            }
            System.out.println("");
        }
    }

    private static void inicializarRobos() {
        Class<?>[] robos = {
            RoboAereo.class,
            RoboAntiAereo.class,
            RoboTanque.class,
            RoboJato.class
        };
    }
}