package com.robotsim.robots.intelligent.types;

import com.robotsim.environment.Ambiente;
import com.robotsim.etc.Acao;
import com.robotsim.missions.missionTypes.MissaoDestruirObstaculo;
import com.robotsim.robots.intelligent.AgenteInteligente;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.robotsim.Controlador;

public class RoboDesconstruido extends AgenteInteligente {
    private static int contador = 0;
    
    public RoboDesconstruido(String nome, int posicaoX, int posicaoY) {
        super(nome, posicaoX, posicaoY, 50); // Inicializando com energia e vida máximas
    }

    @Override
    public void executarMissao(Ambiente a) {
        if (temMissao()) {
            System.out.println("Executando missão autônoma...");
            missao.executar(this, a); // Polimorfismo em ação!
        }
    }

    @Override
    public String getDescricao() {
        return "Robô Autônomo: " + getNome();
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
    
    @Override
    protected void inicializarAcoes() {
        super.inicializarAcoes();
        acoes.add(new FazerMissao(this));
    }

    private class FazerMissao implements Acao {
        /** O robô autônomo que executará a ação de fazer missão. */
        RoboDesconstruido robo;

        /**
         * Construtor para a ação FazerMissao.
         *
         * @param robo O {@link RoboDesconstruido} que realizará esta ação.
         */
        public FazerMissao(RoboDesconstruido robo) {
            this.robo = robo;
        }

        /**
         * Obtém o nome da ação, que será exibido ao usuário.
         *
         * @return O nome da ação ("Fazer Missão").
         */
        @Override
        public String getNome() {
            return "Verificar missões";
        }

        @Override
        public void executar() {
            System.out.println("As ações disponíveis para o robô " + robo.getNome() + " são:");
            Scanner scanner = Controlador.getScanner();

            if(robo.missao == null) {
                System.out.println("[1] Definir Missão de Destruir Obstáculo");
                System.out.println("[2] Pular rodada");

                boolean acaoInvalida = true;
                while(acaoInvalida) {
                    System.out.print("Escolha a opção que deseja: ");
                    int indice = scanner.nextInt();
                    scanner.nextLine(); // Consumir \n

                    if (indice == 1) {
                        System.out.println("Definindo missão de destruir obstáculo...");
                        robo.definirMissao(new MissaoDestruirObstaculo());
                        try {
                            TimeUnit.MILLISECONDS.sleep(1000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt(); // Restore interrupted status
                            System.err.println("A espera foi interrompida: " + e.getMessage());
                        }
                        System.out.println("Missão de destruir obstáculo definida com sucesso!");
                        acaoInvalida = false;
                    } else if (indice == 2) {
                        System.out.println("Rodada pulada.");
                        acaoInvalida = false;
                    } else {
                        System.out.println("Opção inválida. Nenhuma ação realizada.");
                    }
                }
        } else {
            System.out.println("[1] Executar Missão de Destruir Obstáculo");
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