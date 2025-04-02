# Trabalho2-Compiladores
#Guilherme Lima
#Stephane Matos

Analisador Léxico e Sintático para a Linguagem Lang
Este projeto é uma implementação de compilador para a linguagem fictícia "Lang", composta por um Analisador Léxico, um Analisador Sintático, a construção da Árvore Sintática Abstrata (AST) e um Interpretador para execução do código. O compilador analisa e processa o código-fonte em Lang, reconhecendo seus tokens e construindo a estrutura sintática.

Estrutura do Projeto
1. Analisador Léxico
A primeira parte do processo envolve a análise léxica, onde o código-fonte é dividido em tokens que representam as unidades básicas da linguagem.

lang_lexer.flex: Arquivo fonte do JFlex que contém as expressões regulares para identificar os tokens da linguagem Lang.
Lang_lexer.java: Arquivo gerado automaticamente pelo JFlex, que realiza o processamento dos tokens definidos em lang_lexer.flex.
TK.java: Enumeração com os tipos de tokens reconhecidos pela linguagem Lang (como palavras-chave, identificadores, literais, operadores, etc.).
Token.java: Representação de um token, incluindo seu tipo e informações adicionais (como valor e posição no código-fonte).
Main.java: Arquivo principal que executa o lexer, processando o código e imprimindo os tokens identificados.
Makefile: Automação de tarefas de compilação e execução do projeto.
input.txt: Arquivo de entrada contendo o código-fonte que será analisado.
2. Analisador Sintático e Árvore Sintática Abstrata (AST)
A segunda parte do compilador realiza a análise sintática, onde a estrutura do código-fonte é verificada conforme a gramática da linguagem Lang. Além disso, a AST é construída.

Parser.java: Implementação do analisador sintático que verifica a conformidade do código com a gramática de Lang.
Visitor.java: Implementação do padrão Visitor para percorrer a AST e realizar operações sobre ela.
Interp.java: Implementação do Interpretador, que executa o código a partir da AST gerada.
ASTNode.java: Classe base para os nós da AST, representando os elementos da sintaxe da linguagem Lang, como expressões, declarações e comandos.
3. Tipos e Nós Representados
Os seguintes tipos e estruturas são representados como nós na AST:

Instruções de Controle: if, iterate (laços), print.
Operadores Binários: Operadores aritméticos, lógicos e relacionais.
Funções: Declarações e chamadas de funções.
Tipos de Dados: int, bool, char, userDataType (tipos definidos pelo usuário).
Arrays e Bind de Tipos: Nós que representam declarações de tipo e associações entre variáveis e tipos.
Atribuições: Nós para representar a atribuição de valores a variáveis.

4. Como Executar o Projeto
Pré-requisitos
Java: Certifique-se de que o JDK está instalado e configurado no seu sistema.
JFlex: O arquivo jflex.jar precisa estar disponível para a geração do lexer a partir do arquivo .flex.
## Passos para Execução
Compilação e Execução do Analisador Léxico Para rodar o analisador léxico e gerar os tokens a partir de um arquivo de entrada, siga os seguintes passos:

make

Isso executa o processo completo, incluindo a geração do lexer, a compilação dos arquivos Java e a execução do programa que imprime os tokens reconhecidos no código-fonte.


Limpeza de Arquivos Gerados Para remover os arquivos temporários e compilados, use o seguinte comando:

make clean
Isso limpa todos os arquivos compilados, gerados ou temporários.

Exemplo de Entrada e Saída
Arquivo de Entrada (input.txt):
java
Copiar
Editar
int x = 10;
if (x > 5) {
    print(x);
}
Saída Esperada do Analisador Léxico:
yaml
Copiar
Editar
(0,0) TK: INT
(0,4) TK: IDENTIFIER  val: x
(0,6) TK: ASSIGN
(0,8) TK: INT_LITERAL  val: 10
(0,10) TK: SEMICOLON
(1,0) TK: IF
(1,3) TK: OPEN_PARENTHESIS
(1,4) TK: IDENTIFIER  val: x
(1,6) TK: GREATER
(1,8) TK: INT_LITERAL  val: 5
(1,9) TK: CLOSE_PARENTHESIS
(1,11) TK: OPEN_BRACES
(2,4) TK: PRINT
(2,9) TK: OPEN_PARENTHESIS
(2,10) TK: IDENTIFIER  val: x
(2,11) TK: CLOSE_PARENTHESIS
(2,12) TK: SEMICOLON
(3,0) TK: CLOSE_BRACES
Token: (4,0) TK: EOF
Saída Esperada do Analisador Sintático (AST):
A estrutura da AST gerada pode ser visualizada em um formato hierárquico no terminal, mostrando os nós representando cada parte do código analisado.
Desenvolvimento e Expansão
Construção dos Nós da AST
Para adicionar novos nós à AST ou modificar a estrutura existente, siga os seguintes passos:

Criação de Nó: Adicione a classe do nó correspondente no pacote correto (por exemplo, IfNode.java, PrintNode.java, etc.).
Visitor: Registre o novo nó em Visitor.java, implementando a visitação adequada.
Interpretador: Atualize Interp.java para que o interpretador saiba como executar o novo nó.
Esses passos são fundamentais para a expansão e manutenção da linguagem Lang, além de garantir que a AST seja corretamente interpretada e executada.

>>>>>>> 45c352a (Adiciona código do projeto Versão 1 da Parte 3)
