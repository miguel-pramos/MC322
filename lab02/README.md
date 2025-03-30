# RoboSim - Simulador de Robôs [Lab02]

## Descrição
O **RoboSim** é um simulador de robôs que permite a interação entre diferentes tipos de robôs em um ambiente controlado. Cada robô possui características e habilidades específicas, como movimentação, ataque e defesa. Este projeto foi desenvolvido como parte do laboratório da disciplina **MC322 - Programação Orientada a Objetos**.

## Estrutura do Projeto
O projeto está organizado em pacotes para facilitar a manutenção e a escalabilidade do código:

- **`com.robotsim`**: Contém as classes principais do simulador, como o controlador.
- **`com.robotsim.environment`**: Contém clases que representam o ambiente em que os robôs são inseridos, como a classe `Ambiente`.
- **`com.robotsim.robots`**: Contém as classes que representam os diferentes tipos de robôs, como `RoboTerrestre` e `RoboTanque`.
- **`com.robotsim.etc`**: Contém interfaces e classes auxiliares, como `Acao`.
- **`com.robotsim.util`**: Contém utilitários, como funções matemáticas.

## Funcionalidades adicionadas no Lab 02
- Organização da estrutura do simulador em pacotes, para melhor organizar o sistema.
- Implementação de 4 novos tipos de robôs.
- Implementação da interação com o usuário, permitindo maior dinâmica.
- Refatoração de responsabilidades.
- Abstração de ações na classe `Acao`, para permitir interatividade personalizada para cada Robô.
- Implementação de `CatalogoRobos` para gerenciar os robôs disponíveis para simulação.


