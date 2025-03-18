
public class Main {
    public static void main(String[] args) {
        Robo robson = new Robo("Robson", 5, 5);
        Ambiente ambiente = new Ambiente(10, 10);


        int deltaX = Integer.parseInt(System.console().readLine("Digite o quanto " + robson.getNome() + " deve se mover no eixo X: "));
        int deltaY = Integer.parseInt(System.console().readLine("Digite o quanto " + robson.getNome() + " deve se mover no eixo Y: "));
        
        if (ambiente.dentroDosLimites(robson.getPosicaoX() + deltaX, robson.getPosicaoY() + deltaY)) {
            robson.mover(deltaX, deltaY);
            System.out.println(robson.getNome() + " se moveu com sucesso!");
        }
        else {
            System.out.println(robson.getNome() + " não pode se mover para fora dos limites do ambiente.");
        }

        robson.exibirPosicao();
    }
}