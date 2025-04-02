# Instrunções para Eexecutar os Testes

O arquivo conf.txt deve ser preenchido com os comandos para executar o compilador.
Cada comando deve ser esepcificado em uma linha, são esperados dois comandos, o primeiro
para executar os testes sintáticos e o segundo para executar os testes semânticos.

## Exemplo

Suponha que seu main esteja na classe Langcc.java e que ele esteja dentro da pasta lang que está na raiz do
seu projeto. Após compilar seu trabalho, você deve copiar a pasta testes para a raiz do seu projeto.  Note que você terá que referenciar o caminho relativo  com, origem na pasta testes, para o seu comando de execução da sintaxe e da semântica. Assim nossa árvore de diretorias estaria assim:

     Raiz_Do_Projeto
     |
     |-- + lang
     |   | Langcc.java
     |   | Langcc.class
     |   |--+ tools
     |      | java-cup-11b-runtime.jar
     |      | ...
     |--+ testes
        | -- - langtester
        | -- - semantica
        | -- - sintaxe
        | cont.txt
        | makefile
        | Intrucoes_Teste.md

Deste modo o arquivo conf.txt deve ficar assim:

    java -cp ..:../lang/tools/java-cup-11b-runtime.jar lang/Langcc -syn
    java -cp ..:../lang/tools/java-cup-11b-runtime.jar lang/Langcc -i
