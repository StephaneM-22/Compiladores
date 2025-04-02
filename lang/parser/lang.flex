/*
 * This lexer implementation contains Arrays as tokens.
 * The students are encouraged to argue why this is a bad ideia !
 *
 * lang.Lang lexer specification
 * Bernardo Lucas de Araujo Dias
 * Ronaldo Luiz de Freitas Santos
 */

package lang.parser;

import java.util.ArrayList;
import java_cup.runtime.Symbol;

%%

%public
%function nextToken
%type Symbol
%class LangLexer


%line
%column

%unicode

%eofval{
   return new Symbol(LangParserSym.EOF, yyline + 1, yycolumn + 1);
%eofval}

%{
    private ArrayList arr;

    private int toInt(String s){
        try{
            return Integer.parseInt(yytext());
        }catch(NumberFormatException e){
            System.out.println("erro de conversao: " + s + " para inteiro");
            return 0;
        }
    }

    private float toFloat(String s){
        try{
            return Float.parseFloat(yytext());
        }catch(NumberFormatException e){
            System.out.println("erro de conversao:  " + s + " para ponto flutuante");
            return 0;
        }
    }

    private char escapeToChar(String s) {
        switch (s.charAt(2)) { // Pega o segundo caractere após a barra invertida
            case 'n': return '\n';
            case 't': return '\t';
            case 'b': return '\b';
            case 'r': return '\r';
            case '\'': return '\'';
            case '\"': return '\"';
            case '\\': return '\\';
            default: throw new Error("Caractere de escape inválido: " + s);
        }
    }


    private char ascIIToChar(String s) {
        String decimalValue = s.substring(2, s.length() - 1);
        try {
            int decimal = Integer.parseInt(decimalValue);
            return (char) decimal;
        } catch (NumberFormatException e) {
            throw new Error("Erro ao converter o valor inteiro '" + s + "' em um caractere ASCII.");
        }
    }   

    private Symbol mkSymbol(int symCode, Object obj){
        return new Symbol(symCode, yyline + 1, yycolumn + 1, obj);
    }

    private Symbol mkSymbol(int symCode){
        return mkSymbol(symCode,null);
    }

%}


identifier = [a-z]+
tyid = [A-Z][a-zA-Z0-9_]*
integer = [0-9]+
float = [0-9]*\.[0-9]+
white =  [ \n\t\r]+
char = "'" [^'\\] "'" 
ascII = "'" \\[0-9]{3} "'" 
escape = "'" \\[ntbr\\\\e'\"] "'"

%%
<YYINITIAL>{

// Tipos primitivos
"Int"         { return mkSymbol(LangParserSym.TYINT); }  // Tipo Int
"Char"        { return mkSymbol(LangParserSym.TYCHAR); } // Tipo Char
"Bool"        { return mkSymbol(LangParserSym.TYBOOL); } // Tipo Bool
"Float"       { return mkSymbol(LangParserSym.TYFLOAT); } // Tipo Float

"true"         { return mkSymbol(LangParserSym.TRUE, true);    }
"false"        { return mkSymbol(LangParserSym.FALSE, false);  }

"new"          { return mkSymbol(LangParserSym.NEW);}
"if"           { return mkSymbol(LangParserSym.IF);  }
"else"         { return mkSymbol(LangParserSym.ELSE);  }
"data"         { return mkSymbol(LangParserSym.DATA);  }
"iterate"      { return mkSymbol(LangParserSym.ITERATE);  }
"read"         { return mkSymbol(LangParserSym.READ);  }
"print"        { return mkSymbol(LangParserSym.PRINT);  }
"return"       { return mkSymbol(LangParserSym.RETURN);  }
"null"         { return mkSymbol(LangParserSym.NULL, null);  }

"+"            { return mkSymbol(LangParserSym.PLUS);  }
"*"            { return mkSymbol(LangParserSym.MULT);  }
"-"            { return mkSymbol(LangParserSym.SUB);  }
"/"            { return mkSymbol(LangParserSym.DIV);  }
"%"            { return mkSymbol(LangParserSym.MOD);  }
">"            { return mkSymbol(LangParserSym.GT);  }
"<"            { return mkSymbol(LangParserSym.LT);  }
"=="           { return mkSymbol(LangParserSym.EQ);  }
"!="           { return mkSymbol(LangParserSym.NEQ);  }
"&&"           { return mkSymbol(LangParserSym.AND);  }
"!"            { return mkSymbol(LangParserSym.NOT);  }
"("            { return mkSymbol(LangParserSym.OPEN_PARENTHESIS);  }
")"            { return mkSymbol(LangParserSym.CLOSE_PARENTHESIS);  }
"["            { return mkSymbol(LangParserSym.LBRACKET);  }
"]"            { return mkSymbol(LangParserSym.RBRACKET);  }
"{"            { return mkSymbol(LangParserSym.LBRACE);  }
"}"            { return mkSymbol(LangParserSym.RBRACE);  }
"::"           { return mkSymbol(LangParserSym.DOUBLE_COLON);  }
";"            { return mkSymbol(LangParserSym.SEMICOLON);  }
":"            { return mkSymbol(LangParserSym.COLON);  }
"."            { return mkSymbol(LangParserSym.DOT);  }
","            { return mkSymbol(LangParserSym.COMMA);  }
"="            { return mkSymbol(LangParserSym.ATBR);  }

{identifier}   { return mkSymbol(LangParserSym.ID,  yytext()); }
{tyid}         { return mkSymbol(LangParserSym.TYID,  yytext()); }
{integer}      { return mkSymbol(LangParserSym.INT_LITERAL, toInt(yytext()));   }
{float}        { return mkSymbol(LangParserSym.FLOAT_LITERAL, toFloat(yytext()));   }
{char}        { return mkSymbol(LangParserSym.CHAR_LITERAL, yytext().charAt(1));   }
{escape}        { return mkSymbol(LangParserSym.CHAR_LITERAL, escapeToChar(yytext()));   }
{ascII}        { return mkSymbol(LangParserSym.CHAR_LITERAL, ascIIToChar(yytext()));   }

{white}        {/* While reading whites do nothing*/ }
[^]            {/* Matches any char form the input*/
                throw new Error("Illegal character <"+ yytext()+">"); }
}

