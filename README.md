# Simulador de robôs - Repositório de Labs

Este repositório contém os códigos desenvolvidos durante os laboratórios da disciplina **MC322 - Programação Orientada a Objetos**.

### Diagrama UML do simulador

![Diagrama UML](uml/diagram.png 'Diagrama UML')

## Estrutura do Repositório

Os códigos estão organizados em pastas, onde cada pasta corresponde a um laboratório específico. O nome de cada pasta indica o laboratório específico.

A ideia é que seja possível analisar a evolução do sistema de simulação a cada laboratório.

## Como Navegar

1. Explore as pastas para encontrar o código relacionado a cada laboratório.
2. Cada pasta contém arquivos de código-fonte, testes e outros recursos utilizados no laboratório, além de um README.md indicando os recursos novos implementados e outros detalhes.

## Como executar

Cada laboratório contém, em sua pasta, um REAME.md que indica com mais detalhes como executar o simulador no estágio em que se encontra.
Nele estarão instruções de execução e de interação com o simulador.

## Exemplo de Uso

Para executar o programa, você precisará navegar até o diretório do laboratório específico que deseja rodar. Por exemplo, para o `lab04`:

1.  **Navegue até o diretório do laboratório:**
    ```bash
    cd lab04
    ```
2.  **Execute o script de inicialização:**
    No Linux ou macOS:

    ```bash
    ./run.sh
    ```

    No Windows:

    ```bash
    .\run.bat
    ```

    Este script geralmente compila todos os arquivos Java necessários e inicia o `Controlador` do simulador.

3.  **Interaja com o Simulador:**
    Após a execução, o simulador normalmente apresentará um menu de opções no terminal. Siga as instruções fornecidas para controlar os robôs, visualizar o ambiente, etc.

    Por exemplo, no `lab04`, o simulador indicará as instruções de uso, que se limitam ao controle de robôs já instanciados.

Para detalhes específicos sobre a execução e funcionalidades de cada laboratório, consulte o arquivo `README.md` dentro da respectiva pasta do laboratório (ex: `lab04/README.md`).

## IDEs utilizadas no desenvolvimento do simulador

-   Visual Studio Code (Miguel)
-   IntelliJ (Rafael)

### Versão utilizada do Java

openjdk version "21.0.7"

## Contribuidores

-   **Miguel** (_281335_)
-   **Rafael** (_281359_)
