package com.robotsim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.Random;
import java.lang.reflect.InvocationTargetException;

import com.robotsim.environment.Ambiente;
import com.robotsim.environment.obstacle.Obstaculo;
import com.robotsim.etc.Acao;
import com.robotsim.etc.CatalogoRobos;
import com.robotsim.etc.CentralComunicacao;
import com.robotsim.exceptions.ErroComunicacaoException;
import com.robotsim.exceptions.RoboDesligadoException;
import com.robotsim.robots.*;
import com.robotsim.util.TesteColisao;

/**
 * A classe Controlador é responsável por gerenciar a execução principal do
 * simulador de robôs.
 * Ela inicializa o ambiente, registra as classes de robôs disponíveis no
 * catálogo e permite
 * que o usuário configure e controle os robôs durante a simulação.
 */
public final class Controlador {
    public static final int DELTA_TIME = 1; // Tempo arbitrário de execução em segundos

    private static Ambiente ambiente;
    private static CentralComunicacao comunicacao = new CentralComunicacao();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        inicializarRobos();
        inicializarSim();

        boolean executando = true;
        while (executando && ambiente.getRobos().size() > 1) {
            exibirMenuPrincipal();
            int escolha = lerEscolhaUsuario(5);

            switch (escolha) {
                case 1:
                    listarRobosMenu();
                    break;
                case 2:
                    Robo roboSelecionado = selecionarRoboParaInteracao();
                    if (roboSelecionado != null) {
                        menuInteracaoRobo(roboSelecionado);
                    }
                    break;
                case 3:
                    visualizarStatusAmbiente();
                    break;
                case 4:
                    comunicacao.exibirMensagens();
                    break;
                case 5:
                    executando = false;
                    System.out.println("Encerrando simulação...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }

            // Verifica condição de fim de jogo
            if (ambiente.getRobos().size() <= 1) {
                executando = false;
            }
        }

        if (ambiente.getRobos().size() == 1) {
            System.out.printf("\nParabéns, %s! Você foi o único robô a sobreviver!\n",
                    ambiente.getRobos().get(0).getNome());
        } else if (ambiente.getRobos().isEmpty()) {
            System.out.println("\nTodos os robôs foram destruídos. Não há vencedores.");
        } else {
            System.out.println("\nSimulação encerrada pelo usuário.");
        }
        scanner.close();
    }

    private static void exibirMenuPrincipal() {
        System.out.println("\n=============== MENU PRINCIPAL ===============");
        System.out.println("[1] Listar Robôs");
        System.out.println("[2] Selecionar Robô para Interagir");
        System.out.println("[3] Visualizar Status do Ambiente");
        System.out.println("[4] Listar Mensagens Trocadas");
        System.out.println("[5] Sair da Simulação");
        System.out.print("Escolha uma opção: ");
    }

    private static void listarRobosMenu() {
        System.out.println("\n--- Listar Robôs ---");
        System.out.println("[1] Listar todos");
        System.out.println("[2] Listar por tipo (Aéreo/Terrestre)");
        System.out.println("[3] Listar por estado (Ligado/Desligado)");
        System.out.print("Escolha uma opção de listagem: ");
        int escolha = lerEscolhaUsuario(3);
        switch (escolha) {
            case 1:
                listarRobos(null, null);
                break;
            case 2:
                System.out.print("Digite o tipo (Aereo/Terrestre): ");
                String tipo = scanner.nextLine().trim();
                listarRobos(tipo, null);
                break;
            case 3:
                System.out.print("Digite o estado (Ligado/Desligado): ");
                String estadoStr = scanner.nextLine().trim();
                Boolean ligado = null;
                if ("ligado".equalsIgnoreCase(estadoStr)) {
                    ligado = true;
                } else if ("desligado".equalsIgnoreCase(estadoStr)) {
                    ligado = false;
                }
                listarRobos(null, ligado);
                break;
            default:
                System.out.println("Opção inválida.");
        }
    }

    private static void listarRobos(String tipoFiltro, Boolean estadoLigadoFiltro) {
        System.out.println("\n--- Robôs no Ambiente ---");
        List<Robo> robosFiltrados = ambiente.getRobos().stream()
                .filter(r -> tipoFiltro == null ||
                        ("aereo".equalsIgnoreCase(tipoFiltro) && r instanceof RoboAereo) ||
                        ("terrestre".equalsIgnoreCase(tipoFiltro) && r instanceof RoboTerrestre))
                .filter(r -> estadoLigadoFiltro == null || r.isLigado() == estadoLigadoFiltro)
                .collect(Collectors.toList());

        if (robosFiltrados.isEmpty()) {
            System.out.println("Nenhum robô encontrado com os filtros aplicados.");
            return;
        }
        for (int i = 0; i < robosFiltrados.size(); i++) {
            Robo r = robosFiltrados.get(i);
            System.out.printf("[%d] %s (ID: %s, Tipo: %s, Estado: %s, HP: %d, Pos: (%d,%d,%d))\n",
                    i + 1, r.getNome(), r.getId(), r.getClass().getSimpleName(),
                    r.isLigado() ? "Ligado" : "Desligado", r.getHP(),
                    r.getX(), r.getY(), (r instanceof RoboAereo ? ((RoboAereo) r).getZ() : 0));
        }
    }

    private static Robo selecionarRoboParaInteracao() {
        listarRobos(null, null); // Lista todos os robôs para seleção
        if (ambiente.getRobos().isEmpty()) {
            System.out.println("Não há robôs para selecionar.");
            return null;
        }
        System.out.print("Digite o número do robô para interagir: ");
        int escolha = lerEscolhaUsuario(ambiente.getRobos().size());
        if (escolha > 0 && escolha <= ambiente.getRobos().size()) {
            return ambiente.getRobos().get(escolha - 1);
        }
        System.out.println("Seleção inválida.");
        return null;
    }

    private static void menuInteracaoRobo(Robo robo) {
        boolean continuarInteragindo = true;
        while (continuarInteragindo) {
            System.out.printf("\n--- Menu de Interação: %s ---\n", robo.getNome().toUpperCase());
            visualizarStatusRobo(robo, false); // false para não ser verboso toda vez
            System.out.println("[1] Executar Ação");
            System.out.println("[2] Visualizar Status Completo do Robô");
            System.out.println("[3] Alternar Estado (Ligar/Desligar)");
            System.out.println("[4] Comunicar com outro Robô");
            System.out.println("[5] Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");
            int escolha = lerEscolhaUsuario(5);

            try {
                switch (escolha) {
                    case 1:
                        menuExecutarAcao(robo);
                        break;
                    case 2:
                        visualizarStatusRobo(robo, true); // true para ser verboso
                        break;
                    case 3:
                        alternarEstadoRobo(robo);
                        break;
                    case 4:
                        realizarComunicacao(robo);
                        break;
                    case 5:
                        continuarInteragindo = false;
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (RoboDesligadoException e) {
                System.out.println("Erro: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Ocorreu um erro inesperado: " + e.getMessage());
                e.printStackTrace();
            }
            if (!roboNaoRemovido(robo)) { // Se o robô foi destruído durante a ação
                System.out.println(robo.getNome() + " foi destruído e não pode mais interagir.");
                continuarInteragindo = false;
            }
        }
    }

    private static void visualizarStatusRobo(Robo robo, boolean verboso) {
        if (verboso) {
            System.out.println("\n--- Status Completo: " + robo.getNome() + " ---");
            System.out.println("ID: " + robo.getId());
            System.out.println("Tipo Classe: " + robo.getClass().getName());
            System.out.println("Estado: " + (robo.isLigado() ? "Ligado" : "Desligado"));
            System.out.println("HP: " + robo.getHP());
            if (robo instanceof RoboAereo) {
                RoboAereo ra = (RoboAereo) robo;
                System.out.printf("Posição: (%d, %d, %d)\n", ra.getX(), ra.getY(), ra.getZ());
                System.out.println("Altitude Máxima: " + ra.getAltitudeMaxima());
            } else {
                System.out.printf("Posição: (%d, %d, %d)\n", robo.getX(), robo.getY(), 0); // RoboTerrestre tem Z=0
            }
            // Adicionar mais detalhes específicos se necessário (e.g., balas, bateria)
            System.out.println(robo.getDescricao()); // Usar getDescricao para detalhes específicos
        } else {
            System.out.printf("Status Rápido - %s: HP: %d, Estado: %s, Pos: (%d,%d,%d)\n",
                    robo.getNome(), robo.getHP(), (robo.isLigado() ? "Ligado" : "Desligado"),
                    robo.getX(), robo.getY(), (robo instanceof RoboAereo ? ((RoboAereo) robo).getZ() : 0));
        }
    }

    private static void visualizarStatusAmbiente() {
        System.out.println("\n--- Status do Ambiente ---");
        System.out.println(
                "Dimensões: " + ambiente.getComprimento() + "x" + ambiente.getLargura() + "x" + ambiente.getAltura());
        System.out.println("Número de Robôs Ativos: " + ambiente.getRobos().size());
        System.out.println("Número de Obstáculos: "
                + ambiente.getEntidades().stream().filter(e -> e instanceof Obstaculo).count());
        // Adicionar mais informações se necessário
        imprimirAmbienteGeral();
    }

    private static void alternarEstadoRobo(Robo robo) {
        robo.alternarEstado();
        System.out.println(robo.getNome() + " agora está " + (robo.isLigado() ? "Ligado" : "Desligado") + ".");
    }

    private static void realizarComunicacao(Robo remetente) throws RoboDesligadoException, ErroComunicacaoException {
        if (!remetente.isLigado()) {
            throw new RoboDesligadoException(remetente.getNome() + " está desligado e não pode enviar mensagens.");
        }
        System.out.println("\n--- Comunicar --- ");
        System.out.println("Robôs disponíveis para comunicação:");
        List<Robo> outrosRobos = ambiente.getRobos().stream()
                .filter(r -> r != remetente && r.isLigado())
                .collect(Collectors.toList());
        if (outrosRobos.isEmpty()) {
            System.out.println("Nenhum outro robô ligado disponível para comunicação.");
            return;
        }
        for (int i = 0; i < outrosRobos.size(); i++) {
            System.out.printf("[%d] %s\n", i + 1, outrosRobos.get(i).getNome());
        }
        System.out.print("Escolha o destinatário: ");
        int escolhaDest = lerEscolhaUsuario(outrosRobos.size());
        if (escolhaDest > 0 && escolhaDest <= outrosRobos.size()) {
            Robo destinatario = outrosRobos.get(escolhaDest - 1);
            System.out.print("Digite a mensagem: ");
            String mensagem = scanner.nextLine();
            remetente.enviarMensagens(destinatario, mensagem);
            System.out.println("Mensagem enviada de " + remetente.getNome() + " para " + destinatario.getNome());
        } else {
            System.out.println("Seleção de destinatário inválida.");
        }
    }

    private static void menuExecutarAcao(Robo robo) throws RoboDesligadoException {
        if (!robo.isLigado()) {
            throw new RoboDesligadoException(robo.getNome() + " está desligado e não pode executar ações.");
        }
        System.out.println("\n--- Ações para " + robo.getNome() + " ---");
        ArrayList<Acao> acoes = robo.getAcoes();
        for (int i = 0; i < acoes.size(); i++) {
            Acao acao = acoes.get(i);
            String nomeAcao = acao.getNome();
            // Removida a lógica que renomeia "Mover" para "Mover (Opções Detalhadas)"
            System.out.printf("[%d] %s\n", i + 1, nomeAcao);
        }
        System.out.print("Escolha uma ação: ");
        int escolhaAcao = lerEscolhaUsuario(acoes.size());

        if (escolhaAcao > 0 && escolhaAcao <= acoes.size()) {
            Acao acaoSelecionada = acoes.get(escolhaAcao - 1);
            // Removida a chamada para menuMover. A ação "Mover" será executada diretamente.
            robo.executarTarefa(acaoSelecionada);
        } else {
            System.out.println("Seleção de ação inválida.");
        }
    }

    private static int lerEscolhaUsuario(int maxOpcao) {
        int escolha = -1;
        while (escolha < 1 || escolha > maxOpcao) {
            if (scanner.hasNextInt()) {
                escolha = scanner.nextInt();
                if (escolha < 1 || escolha > maxOpcao) {
                    System.out.println("Escolha inválida. Tente novamente entre 1 e " + maxOpcao + ".");
                }
            } else {
                System.out.println("Entrada inválida. Por favor, insira um número.");
                scanner.next(); // Consumir entrada inválida
            }
        }
        scanner.nextLine(); // Consumir \n
        return escolha;
    }

    private static boolean roboNaoRemovido(Robo roboAnalisado) {
        if (roboAnalisado == null)
            return false;
        // Verifica se o robô ainda está na lista principal do ambiente
        return ambiente.getRobos().contains(roboAnalisado) &&
                !ambiente.getEntidadesRemovidas().contains(roboAnalisado);
    }

    /**
     * Imprime uma representação textual do ambiente, destacando a posição de todos
     * os robôs.
     */
    private static void imprimirAmbienteGeral() {
        // Pausa para melhor visualização
        try {
            TimeUnit.MILLISECONDS.sleep(500); // Reduzido para não ser muito lento
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Pausa interrompida: " + e.getMessage());
        }

        System.out.println("\n\n=============== MAPA DO JOGO ATUAL ===============");
        ambiente.visualizarAmbiente();
        System.out.println("===============================================");
    }

    // Removido imprimirAmbiente(Robo robo) antigo, substituído por
    // imprimirAmbienteGeral()

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

            final int COMPRIMENTO = 60;
            final int LARGURA = 15;
            final int ALTURA = 30;
            ambiente = new Ambiente(COMPRIMENTO, LARGURA, ALTURA);

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
                numObst = rand.nextInt(5) + 1;
                System.out.print("\rSeu ambiente terá " + numObst + " obstáculos");
                Thread.sleep(85); // Apenas para simular um processo demorado
            }

            // Adição do obstáculo ao ambiente
            for (int i = 0; i < numObst; i++) {
                Obstaculo novoObstaculo = new Obstaculo();
                ambiente.adicionarEntidade(novoObstaculo);
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

                    ambiente.adicionarEntidade(novoRobo);

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

    public static CentralComunicacao getComunicacao() {
        return comunicacao;
    }

}