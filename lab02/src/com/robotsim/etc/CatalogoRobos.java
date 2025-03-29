package com.robotsim.etc;

import java.util.ArrayList;
import java.util.HashMap;

import com.robotsim.robots.Robo;

public class CatalogoRobos {
    // Armazena as classes de robôs por categoria
    private static final HashMap<String, HashMap<String, Class<? extends Robo>>> catalogo = new HashMap<>();

    /**
     * Registra uma classe de robô em uma categoria
     * 
     * @param categoria  Categoria do robô (ex: "Aéreo", "Terrestre")
     * @param classeRobo Classe do robô a ser registrada
     */
    public static void registrarRobo(String categoria, Class<? extends Robo> classeRobo) {
        catalogo.computeIfAbsent(categoria, _ -> new HashMap<String, Class<? extends Robo>>())
                .put(classeRobo.getSimpleName(), classeRobo);
    }

    /**
     * Obtém todas as categorias disponíveis
     * 
     * @return Lista de categorias
     */
    public static ArrayList<String> getCategorias() {
        return new ArrayList<>(catalogo.keySet());
    }

    /**
     * Obtém todos os robôs de uma categoria
     * 
     * @param categoria Categoria desejada
     * @return Lista de classes de robôs
     */
    public static ArrayList<String> getRobosPorCategoria(String categoria) {
        return new ArrayList<>(catalogo.getOrDefault(categoria, new HashMap<>()).keySet());
    }

    /**
     * Cria uma instância de um robô
     * 
     * @param categoria  Categoria do robô
     * @param nomeClasse Nome da classe do robo
     * @param nome       Nome da instância
     * @param posicaoX   Posição X
     * @param posicaoY   Posição Y
     * @return Nova instância do robô
     */
    public static Robo criarRobo(String categoria, String nomeClasse, String nome, int posicaoX, int posicaoY) {
        try {
            Class<? extends Robo> classe = catalogo.get(categoria).get(nomeClasse);
            return classe.getConstructor(String.class, int.class, int.class, int.class)
                    .newInstance(nome, posicaoX, posicaoY);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar robô", e);
        }
    }
}