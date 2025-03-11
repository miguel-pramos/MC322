public class Ambiente {
    private int largura;
    private int altura;

    public Ambiente(int largura, int altura) {
        this.largura = largura;
        this.altura = altura;
    }

    public boolean dentroDosLimites(int x, int y) {
        return x < this.largura && y < this.altura; 
    }

}
