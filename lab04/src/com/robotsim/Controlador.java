package com.robotsim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.Random;
import java.lang.reflect.InvocationTargetException;

import com.robotsim.environment.Ambiente;
import com.robotsim.environment.obstacle.Obstaculo;
import com.robotsim.etc.Acao;
import com.robotsim.etc.CatalogoRobos;
import com.robotsim.robots.*;
import com.robotsim.util.TesteColisao;

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
        inicializarSim();

        // Loop principal do simulador
        while (ambiente.getRobos().size() > 1) {
            for (Robo robo : ambiente.getRobos()) {
                if (roboNaoRemovido(robo)) {
                    interagir(robo);
                    imprimirAmbiente(robo);
                }
            }

        }

        System.out.printf("\nParabéns, %s! Você foi o único robô a sobreviver!\n",
                ambiente.getRobos().get(0).getNome());
        scanner.close();
    }

    // EXECUÇÃO NO LOOP

    private static boolean roboNaoRemovido(Robo roboAnalisado){
        for (Robo roboRemovido : ambiente.getRobosRemovidos()){
            if (roboAnalisado == roboRemovido){
                return false;
            }
        }
        return true;
    }

    private static void interagir(Robo robo) {
        System.out.printf("\nSeu HP: %d\n", robo.getHP());
        if (robo instanceof RoboAereo) {
            System.out.printf("Sua posição: (%d, %d, %d)\n", robo.getPosicaoX(), robo.getPosicaoY(), ((RoboAereo) robo).getAltitude());
        } else{
            System.out.printf("Sua posição: (%d, %d, %d)\n", robo.getPosicaoX(), robo.getPosicaoY(), 0);
        }

        System.out.printf("\nAÇÕES DISPONÍVEIS PARA %s:\n", robo.getNome().toUpperCase());
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
    private static void inicializarSim() {
        try {

            final int COMPRIMENTO = 80;
            final int LARGURA = 40;
            ambiente = new Ambiente(COMPRIMENTO, LARGURA);

            // Introdução
            System.out.println("====================== SIMULADOR DE ROBÔS ======================");
            System.out.println("Nesse simulador, você controlará robôs de combate...");
            TimeUnit.MILLISECONDS.sleep(1600);

            System.out.println("O simulador funciona por turnos, nos quais algumas ações serão disponíveis.");
            TimeUnit.MILLISECONDS.sleep(1600);

            // Posicionamento dos obstáculos
            System.out
                    .printf("\n\nO mapa tem tamanho (%d, %d)\n", ambiente.getComprimento(),
                            ambiente.getLargura());
            TimeUnit.MILLISECONDS.sleep(1600);

            Random rand = new Random();
            int numObst = 0;
            for (int loops = 0; loops < 20; loops++) {
                numObst = rand.nextInt(5);
                System.out.print("\rSeu ambiente terá " + numObst + " obstáculos");
                Thread.sleep(85); // Apenas para simular um processo demorado
            }

            // Adição do obstáculo ao ambiente
            for (int i = 0; i < numObst; i++) {
                Obstaculo novoObstaculo = new Obstaculo();
                ambiente.adicionarObstaculo(novoObstaculo);
            }

            TimeUnit.MILLISECONDS.sleep(1600);

            System.out.println("\nAgora, os robôs serão criados:");
            TimeUnit.MILLISECONDS.sleep(300);

            ArrayList<Class<? extends Robo>> todasClasses = CatalogoRobos.getTodasClasses();
            Collections.shuffle(todasClasses);

            Random random = new Random();
            int i = 0;
            while (i < 3) {
                Class<? extends Robo> classeEscolhida = todasClasses.get(i);
                String nome = classeEscolhida.getSimpleName();

                int x = random.nextInt(ambiente.getComprimento());
                int y = random.nextInt(ambiente.getLargura());

                try {
                    Robo novoRobo = classeEscolhida.getConstructor(String.class, int.class, int.class)
                            .newInstance(nome, x, y);

                    String colisao = TesteColisao.tipoDeColisao(novoRobo, x, y);

                    if (!colisao.equals("Nula")) {
                        continue;
                    }

                    ambiente.adicionarRobo(novoRobo);

                    System.out.printf("Robô %s do tipo %s adicionado na posição (%d, %d)\n", nome,
                            classeEscolhida.getSimpleName(), x, y);
                    TimeUnit.MILLISECONDS.sleep(300);
                    i++;
                } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                        | InvocationTargetException e) {
                    System.err.printf("Erro ao criar instância do robô %s: %s\n", nome, e.getMessage());
                    e.printStackTrace();
                }
            }

            TimeUnit.MILLISECONDS.sleep(1600);

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