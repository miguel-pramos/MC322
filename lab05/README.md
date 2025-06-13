# RoboSim - Simulador de Robôs [Lab04]

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
    -   **`com.robotsim.robots.sensors`**: Classes de sensores como `Sensor`, `SensorObstaculo`, `SensorRobo`.
-   **`com.robotsim.etc`**: Contém interfaces e classes auxiliares, como `Acao` e `CatalogoRobos`.
-   **`com.robotsim.exceptions`**: Contém classes de exceções customizadas como `ColisaoException`, `ErroComunicacaoException`, `ForasDosLimitesException`, `RoboDesligadoException`.
-   **`com.robotsim.util`**: Contém utilitários, como `GeometryMath` e `RefreshScreen`.

## Funcionalidades adicionadas no Lab 04

-   **Sistema de Comunicação**: Implementação de um sistema de comunicação entre robôs utilizando uma `CentralComunicacao`.
    -   Robôs agora podem enviar e receber mensagens através da interface `Comunicavel`.
-   **Tratamento de Exceções Aprimorado**: Introdução de um pacote `exceptions` com exceções customizadas para melhor controle de erros e situações específicas do simulador.
-   **Refatoração e Organização de Pacotes**: Melhoria na estrutura de pacotes para maior clareza e manutenibilidade, incluindo:
    -   Separação de entidades do ambiente (`com.robotsim.environment.entity`).
    -   Separação de obstáculos (`com.robotsim.environment.obstacle`).
    -   Categorização mais clara de tipos de robôs (aéreos e terrestres em subpacotes).
    -   Introdução do pacote `com.robotsim.robots.abilities` para definir capacidades especiais dos robôs.
-   **Documentação Abrangente**: Adição de Javadoc e comentários explicativos em todas as classes e métodos importantes do projeto, melhorando a legibilidade e o entendimento do código.
-   **Atualização da Lógica de Sensores e Obstáculos**: Revisão e melhoria na interação dos sensores com o ambiente e na lógica de posicionamento e detecção de obstáculos.

## Como executar o programa

Execute o script `run.sh` se estiver no Linux ou no Mac e `run.bat` se estiver no Windows.

O simulador vai indicar as instruções de uso, que se limitam ao controle de robôs já instanciados.
Para testar o uso de um `Sensor`, espere até que esteja controlando um `RoboDrone`, de forma que
as funções dos sensores serão listadas no menu de ações.
