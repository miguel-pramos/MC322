# RoboSim - Simulador de Robôs [Lab05]

## Descrição

O **RoboSim** é um simulador de robôs que permite a interação entre diferentes tipos de robôs em um ambiente controlado. Cada robô possui características e habilidades específicas, como movimentação, ataque e defesa. Este projeto foi desenvolvido como parte do laboratório da disciplina **MC322 - Programação Orientada a Objetos**.

## Estrutura do Projeto

O projeto está organizado em pacotes para facilitar a manutenção e a escalabilidade do código:

-   **`com.robotsim`**: Contém as classes principais do simulador, como o `Controlador` e o enum `GAME_STATUS`.
-   **`com.robotsim.comunicacao`**: Contém classes relacionadas à comunicação entre entidades, como `CentralComunicacao` e `Mensagem`.
-   **`com.robotsim.environment`**: Contém classes que representam o ambiente e suas entidades.
    -   **`com.robotsim.environment.entity`**: Interfaces como `Entidade`, `Comunicavel`, `Sensoreavel` e o enum `TipoEntidade`.
    -   **`com.robotsim.environment.obstacle`**: Classes `Obstaculo` e `TipoObstaculo` para gerenciar obstáculos no ambiente.
-   **`com.robotsim.robots`**: Contém as classes base e abstratas para robôs, bem como suas especializações.
    -   **`com.robotsim.robots.abilities`**: Interfaces que definem capacidades especiais dos robôs, como `Atacante`, `Autonomo`, `Explorador`.
    -   **`com.robotsim.robots.aerials`**: Classes para robôs aéreos como `RoboAereo`, `RoboDrone`, `RoboJato`.
    -   **`com.robotsim.robots.terrestrials`**: Classes para robôs terrestres como `RoboTerrestre`, `RoboTanque`, `RoboAntiAereo`.
    -   **`com.robotsim.robots.intelligent`**: Classes para os novos agentes inteligentes.
    -   **`com.robotsim.robots.sensors`**: Classes de sensores como `Sensor`, `SensorObstaculo`, `SensorRobo`.
-   **`com.robotsim.missions`**: Contém as classes para gerenciar as missões e seus tipos.
-   **`com.robotsim.etc`**: Contém interfaces e classes auxiliares, como `Acao` e `CatalogoRobos`.
-   **`com.robotsim.exceptions`**: Contém classes de exceções customizadas como `ColisaoException`, `ErroComunicacaoException`, `ForasDosLimitesException`, `RoboDesligadoException`.
-   **`com.robotsim.util`**: Contém utilitários, como `GeometryMath` e `RefreshScreen`.

## Funcionalidades adicionadas no Lab 05

-   **Agentes Inteligentes**: Introdução de uma nova categoria de robôs, os `AgenteInteligente`, que operam com base em missões.
    -   Esses agentes são capazes de executar tarefas complexas de forma autônoma, como exploração e destruição de alvos.
-   **Sistema de Missões**: Implementação de um sistema de missões (`com.robotsim.missions`) que permite atribuir objetivos aos agentes inteligentes.
    -   **`MissaoExploracao`**: O robô explora o ambiente de forma autônoma.
    -   **`MissaoDestruirObstaculo`**: O robô procura e destrói um obstáculo específico.
    -   **`MissaoDanoGlobal`**: O robô causa dano a todas as outras entidades no ambiente.
-   **Novos Tipos de Robôs Inteligentes**:
    -   `RoboExplorador`: Especializado em missões de exploração.
    -   `RoboDesconstruido`: Focado em destruir obstáculos.
    -   `RoboAtacante`: Causa dano em área.
-   **Sistema de Log**: Adição de uma classe `Logger` que registra os eventos importantes das missões no arquivo `missao.log`.
-   **Refatoração e Expansão da Estrutura**:
    -   Criação do pacote `com.robotsim.robots.intelligent` para os novos agentes.
    -   Criação do pacote `com.robotsim.missions` para gerenciar as missões e seus tipos.

## Como executar o programa

Execute o script `run.sh` se estiver no Linux ou no Mac e `run.bat` se estiver no Windows.

O simulador vai indicar as instruções de uso. Agora, além de controlar os robôs manualmente, você pode atribuir missões aos novos **Agentes Inteligentes** (`RoboExplorador`, `RoboDesconstruido`, `RoboAtacante`). Selecione um desses robôs e escolha a ação de executar a missão para vê-los operar de forma autônoma. Os resultados e eventos da missão serão registrados no arquivo `missao.log`.
