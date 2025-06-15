package com.robotsim.robots.intelligent.types;

import com.robotsim.environment.Ambiente;
import com.robotsim.robots.intelligent.AgenteInteligente;
import com.robotsim.etc.Acao;
import com.robotsim.missions.missionTypes.MissaoDanoGlobal;

import java.util.Scanner;

import com.robotsim.Controlador;

public class RoboAtacante extends AgenteInteligente {
    private static int contador = 0;
    private static int danoAtacante = 10;

    public RoboAtacante(String nome, int posicaoX, int posicaoY) {
        super(nome, posicaoX, posicaoY, 10); // Inicializando com energia e vida máximas
    }

    @Override
    public void executarMissao(Ambiente a) {
        if (temMissao()) {
            System.out.println("Executando missão de ataque...");
            missao.executar(this, a); // Polimorfismo em ação!
        }
    }

    @Override
    public String getDescricao() {
        return "Robô Atacante: " + getNome();
    }

    @Override
    public char getRepresentacao() {
        return 'E';
    }

    @Override
    public int getContador() {
        contador++;
        return contador;
    }

    public static int getDanoAtacante() {
        return danoAtacante;
    }

    @Override
    protected void inicializarAcoes() {
        super.inicializarAcoes();
        acoes.add(new FazerMissao(this));
    }

    private class FazerMissao implements Acao {
        /** O robô autônomo que executará a ação de fazer missão. */
        RoboAtacante robo;

        /**
         * Construtor para a ação FazerMissao.
         *
         * @param robo O {@link RoboAtacante} que realizará esta ação.
         */
        public FazerMissao(RoboAtacante robo) {
            this.robo = robo;
        }

        /**
         * Obtém o nome da ação, que será exibido ao usuário.
         *
         * @return O nome da ação ("Fazer Missão").
         */
        @Override
        public String getNome() {
            return "FazerMissao (Dano Global)";
        }

        @Override
        public void executar() {
            System.out.println("As ações disponíveis para o robô " + robo.getNome() + " são:");
            Scanner scanner = Controlador.getScanner();

            if(robo.missao == null) {
                System.out.println("[1] Definir Missão de Dano Global");
                System.out.println("[2] Pular rodada");                
                

                boolean acaoInvalida = true;
                while(acaoInvalida) {
                    System.out.print("Escolha a opção que deseja: ");
                    int indice = scanner.nextInt();
                    scanner.nextLine(); // Consumir \n

                    if (indice == 1) {
                        System.out.println("Definindo missão de dano global...");
                        robo.definirMissao(new MissaoDanoGlobal());
                        System.out.println("Missão de dano global definida com sucesso!");
                        acaoInvalida = false;
                    } else if (indice == 2) {
                        System.out.println("Rodada pulada.");
                        acaoInvalida = false;
                    } else {
                        System.out.println("Opção inválida. Nenhuma ação realizada.");
                    }
                }
        } else {
            System.out.println("[1] Executar Missão de Dano Global");
            System.out.println("[2] Pular rodada");  

            boolean acaoInvalida = true;
            while(acaoInvalida) {
                System.out.print("Escolha a opção que deseja: ");
                int indice = scanner.nextInt();
                scanner.nextLine(); // Consumir \n

                if (indice == 1) {
                    robo.executarMissao(Controlador.getAmbiente());
                    acaoInvalida = false;
                } else if (indice == 2) {
                    System.out.println("Rodada pulada.");
                    acaoInvalida = false;
                } else {
                    System.out.println("Opção inválida. Nenhuma ação realizada.");
                }
            }
        }
    }
    }
}