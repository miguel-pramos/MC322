package com.robotsim.etc;

import java.util.ArrayList;
import java.util.HashMap;

import com.robotsim.robots.Robo;

/**
 * A classe CatalogoRobos é responsável por gerenciar um catálogo de classes de
 * robôs
 * organizados por categorias. A ideia é que as classes que herdam de
 * {@link com.robotsim.robots.Robo}
 * possam registrar-se no catálogo automaticamente.
 * <p>
 * Funcionalidades principais:
 * <ul>
 * <li>Registrar classes de robôs em categorias específicas.</li>
 * <li>Obter uma lista de categorias disponíveis.</li>
 * <li>Obter uma lista de robôs registrados em uma categoria específica.</li>
 * <li>Criar instâncias de robôs com base nas classes registradas.</li>
 * </ul>
 */
public class CatalogoRobos {
    // Armazena as classes de robôs por categoria.
    // A chave externa do HashMap é o nome da categoria (String).
    // O valor associado a cada categoria é outro HashMap.
    // Neste HashMap interno, a chave é o nome simples da classe do robô (String)
    // e o valor é a própria classe do robô (Class<? extends Robo>).
    private static final HashMap<String, HashMap<String, Class<? extends Robo>>> catalogo = new HashMap<>();

    /**
     * Registra uma classe de robô em uma categoria específica.
     * Se a categoria não existir, ela será criada.
     * 
     * @param categoria  O nome da categoria onde o robô será registrado (ex:
     *                   "Aéreo", "Terrestre").
     * @param classeRobo A classe do robô a ser registrada (deve estender
     *                   {@link Robo}).
     */
    public static void registrarRobo(String categoria, Class<? extends Robo> classeRobo) {
        // Utiliza computeIfAbsent para obter o HashMap interno da categoria.
        // Se a categoria (chave k) não existir no 'catalogo', um novo HashMap é criado
        // e associado a ela.
        // Em seguida, a classeRobo é adicionada ao HashMap da categoria,
        // usando o nome simples da classe como chave.
        catalogo.computeIfAbsent(categoria, k -> new HashMap<String, Class<? extends Robo>>())
                .put(classeRobo.getSimpleName(), classeRobo);
    }

    /**
     * Obtém uma lista com os nomes de todas as categorias de robôs registradas.
     * 
     * @return Uma {@link ArrayList} de Strings contendo os nomes das categorias.
     */
    public static ArrayList<String> getCategorias() {
        // Retorna uma nova ArrayList contendo todas as chaves (nomes das categorias) do
        // catálogo.
        return new ArrayList<>(catalogo.keySet());
    }

    /**
     * Obtém uma lista com os nomes de todas as classes de robôs registradas em uma
     * categoria específica.
     * 
     * @param categoria O nome da categoria desejada.
     * @return Uma {@link ArrayList} de Strings contendo os nomes simples das
     *         classes de robôs
     *         na categoria especificada. Retorna uma lista vazia se a categoria não
     *         existir.
     */
    public static ArrayList<String> getRobosPorCategoria(String categoria) {
        // Obtém o HashMap de robôs para a categoria especificada.
        // Se a categoria não for encontrada, retorna um HashMap vazio para evitar
        // NullPointerException.
        // Em seguida, retorna uma nova ArrayList contendo as chaves (nomes das classes)
        // desse HashMap.
        return new ArrayList<>(catalogo.getOrDefault(categoria, new HashMap<>()).keySet());
    }

    /**
     * Cria uma nova instância de um robô com base na categoria, nome da classe,
     * nome da instância e posição.
     * 
     * @param categoria  O nome da categoria do robô.
     * @param nomeClasse O nome simples da classe do robô.
     * @param nome       O nome a ser atribuído à instância do robô.
     * @param posicaoX   A coordenada X inicial da instância do robô.
     * @param posicaoY   A coordenada Y inicial da instância do robô.
     * @return Uma nova instância do robô especificado.
     * @throws RuntimeException Se ocorrer um erro durante a criação do robô (ex:
     *                          classe não encontrada,
     *                          problema na instanciação).
     */
    public static Robo criarRobo(String categoria, String nomeClasse, String nome, int posicaoX, int posicaoY) {
        try {
            // Busca a classe do robô no catálogo com base na categoria e no nome da classe.
            Class<? extends Robo> classe = catalogo.get(categoria).get(nomeClasse);
            // Obtém o construtor da classe que aceita String (nome) e dois ints (posicaoX,
            // posicaoY).
            // Cria uma nova instância usando o construtor encontrado e os parâmetros
            // fornecidos.
            return classe.getConstructor(String.class, int.class, int.class)
                    .newInstance(nome, posicaoX, posicaoY);
        } catch (Exception e) {
            // Lança uma RuntimeException se qualquer etapa da criação falhar.
            throw new RuntimeException("Erro ao criar robô: Categoria='" + categoria + "', Classe='" + nomeClasse + "'",
                    e);
        }
    }

    /**
     * Obtém uma lista com todas as classes de robôs registradas em todas as
     * categorias.
     * 
     * @return Uma {@link ArrayList} de {@code Class<? extends Robo>} contendo todas
     *         as classes de robôs.
     */
    public static ArrayList<Class<? extends Robo>> getTodasClasses() {
        ArrayList<Class<? extends Robo>> todasClasses = new ArrayList<>();
        // Itera sobre todos os HashMaps de categorias no catálogo.
        for (HashMap<String, Class<? extends Robo>> categoria : catalogo.values()) {
            // Adiciona todas as classes de robôs da categoria atual à lista 'todasClasses'.
            todasClasses.addAll(categoria.values());
        }
        return todasClasses;
    }
}