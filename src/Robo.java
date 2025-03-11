public class Robo {
    private String nome;
    private int posicaoX;
    private int posicaoY;

    public Robo(String nome, int posicaoX, int posicaoY)
    {
        this.nome = nome;
        this.posicaoX = posicaoX;
        this.posicaoY = posicaoY;
    }

    public void mover(int deltaX, int deltaY)
    {
        this.posicaoX += deltaX;
        this.posicaoY += deltaY;
    }

    public int exibirPosicao()
    {
        return this.posicaoX;
        return this.posicaoY;
    }
}
