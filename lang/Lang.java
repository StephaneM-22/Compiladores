package lang;

import java.io.*;
import java_cup.runtime.*;
import lang.ast.Node;
import lang.parser.LangParser;
import lang.parser.LangParserSym;
import lang.parser.LangLexer;
import lang.ast.visitors.Interp;

public class Lang {

      public static void runLexer(LangLexer lex) throws IOException, Exception {
            Symbol tk = lex.nextToken();
            while (tk.sym != LangParserSym.EOF) {
                  System.out.println("(" + tk.left + "," + tk.right + ")" + tk.sym);
                  tk = lex.nextToken();
            }
            System.out.println(tk.toString());
      }

      public static void interpret(LangParser p) throws IOException, Exception {
            Symbol presult = p.parse();
            Node root = (Node) presult.value;
            if (presult != null) {
                  Interp v = new Interp();
                  root.accept(v);
                  System.out.println(v.getStackTop());
            } else {
                  System.out.println("rejected");
            }

      }

      public static void checkSyntax(LangParser p) throws IOException, Exception {
            try {
                  Symbol presult = p.parse();
                  Node root = (Node) presult.value;
                  if (presult != null) {
                        System.out.println("accepted");
                  } else {
                        System.out.println("rejected");
                  }
            } catch (Exception e) {
                  System.out.println("rejected");
            }catch (Error e){
                  System.out.println("rejected");
            }
      }

      public static void main(String args[]) throws IOException, Exception {
            int fname = 0;
            if (args.length < 1 || args.length > 2) {
                  printHelp();
                  System.exit(0);
            } else {
                  if (args.length == 2) {
                        fname = 1;
                  }
                 // System.out.println(args[fname]);
                  // Criando o Lexer
                  LangLexer lex = new LangLexer(new FileReader(args[fname]));
                  // Criando o parser (mais ainda não o executamos);
                  LangParser p = new LangParser(lex);
                  // Testando os argumentos para determinar o que fazer em seguida.
                  if (args.length == 2 && args[0].equals("-lex")) {
                        runLexer(lex);
                        System.exit(0);
                  } else if (args.length == 2 && args[0].equals("-i")) {
                        interpret(p);
                        System.exit(0);
                  } else if (args.length == 2 && args[0].equals("-syn")) {
                        checkSyntax(p);
                        System.exit(0);
                  }
            }
      }

      private static void printHelp() {
            System.out.println("use java lang.Lang [opcao] <nome-de-arquivo>");
            System.out.println("opcao: ");
            System.out.println("   -lex  : Apenas lista os tokens.");
            System.out.println("   -i    : Interpreta o programa.");
            System.out.println("   -syn  : Apenas verifica a sintaxe.");
      }
}