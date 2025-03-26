package com.robotsim;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.robotsim.environment.Ambiente;
import com.robotsim.robots.Robo;
import com.robotsim.robots.RoboTanque;

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
            System.out.println("Nesse simulador, você controlará um robô de combate...");
            TimeUnit.MILLISECONDS.sleep(1600);

            System.out.println("O simulador funciona por turnos, nos quais algumas ações serão disponíveis.");
            TimeUnit.MILLISECONDS.sleep(1600);

            System.out.print("\n\nEscolha um nome para o seu robô: ");
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

            ambiente.adicionarRobo(new RoboTanque(nome, x, y));
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
}