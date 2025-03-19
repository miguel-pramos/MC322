import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

package com.robotsim.robots;

public class RoboJato extends RoboAereo {

    public RoboJato(String nome, int posicaoX, int posicaoY, int altitude, int altitudeMaxima) {
        super(nome, posicaoX, posicaoY, altitude, altitudeMaxima);
        char answer;

        public void subir(char answer, int deltaAltitude){
            if (this.altitude + deltaAltitude > this.altitudeMaxima){
                while (1)`{
                    System.out.println("Essa altitude ultrapassará os limites do jato. Deseja mesmo continuar? Y/N");
                    try {
                        this.answer = System.in.read();
                    } catch (IOException e) {
                        System.out.println("Ocorreu um erro com a entrada.");
                        e.printStackTrace();
                        continue;
                    }

                    switch (answer) {
                        case 'y': {
                            System.out.println("Você subiu... e subiu... até não aguentar mais");
                            gameStatus = GAME_STATUS.GAMEOVER;
                            break;
                        }
                        case 'n': {
                            System.out.println("Você subiu para a posição (" + posicaoX + ", " + posicaoY + ", " + altitude + ").");
                            break;
                        }
                        default:
                            System.out.println("Ocorreu um erro com a entrada.");
                    }
                }
            }
        }

    }
}