# RoboSim - Simulador de Robôs [Lab03]

## Descrição
O **RoboSim** é um simulador de robôs que permite a interação entre diferentes tipos de robôs em um ambiente controlado. Cada robô possui características e habilidades específicas, como movimentação, ataque e defesa. Este projeto foi desenvolvido como parte do laboratório da disciplina **MC322 - Programação Orientada a Objetos**.

## Estrutura do Projeto
O projeto está organizado em pacotes para facilitar a manutenção e a escalabilidade do código:

- **`com.robotsim`**: Contém as classes principais do simulador, como o controlador.
- **`com.robotsim.environment`**: Contém clases que representam o ambiente em que os robôs são inseridos, como a classe `Ambiente`.
- **`com.robotsim.robots`**: Contém as classes que representam os diferentes tipos de robôs, como `RoboTerrestre` e `RoboTanque`.
- **`com.robotsim.robots.sensors`**: Contém as classes que representam sensores e implementam suas funcionalidades.
- **`com.robotsim.etc`**: Contém interfaces e classes auxiliares, como `Acao`.
- **`com.robotsim.util`**: Contém utilitários, como funções matemáticas.

## Funcionalidades adicionadas no Lab 03
- Adição de `Sensor` e implementação do seu uso nos robôs.
- Replanejamento do fluxo de execução, bem como da interação com o usuário.

## Como executar o programa
Execute o script `run.sh` se estiver no Linux ou no Mac e `run.bat` se estiver no Windows. 

O simulador vai indicar as instruções de uso, que se limitam ao controle de robôs já instanciados. 
Para testar o uso de um `Sensor`, espere até que esteja controlando um `RoboDrone`, de forma que 
as funções dos sensores serão listadas no menu de ações.
