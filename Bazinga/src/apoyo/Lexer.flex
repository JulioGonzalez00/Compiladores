import compilerTools.Token;

%%
%class Lexer
%type Token
%line
%column
%{
    private Token token(String lexeme, String lexicalComp, int line, int column){
        return new Token(lexeme,lexicalComp,line+1,column+1);
    }

%}
    /*Variables basicas para comentarios y espacios*/
    TerminadorDeLinea = \r|\n|\r\n
    EntradaDeCaracter = [^\r\n]
    EspacioEnBlanco     = {TerminadorDeLinea} | [ \t\f]

    /*Comentarios*/
    Comentario = {ComentarioTradicional} | {FinDeLineaDeComentario} | {ComentarioDeDocumentacion}

    ComentarioTradicional   = "/*" [^*] ~"*/" | "/*" "*"+ "/"

    FinDeLineaDeComentario     = "//" {EntradaDeCaracter}* {TerminadorDeLinea}?
    ComentarioDeDocumentacion = "/**" {ContenidoDeComentario} "*"+ "/"
    ContenidoDeComentario       = ( [^*] | \*+ [^/*] )*

    /*Identificador*/
    Letra=[A-Za-zÑñ_ÁÉÍÓÚáéíóú]
    Digito=[0-9]
    Identificador={Letra}({Letra}|{Digito})*

    /*Numero*/
    Numero = {Digito}{Digito}*(.{Digito}|{Digito})?
%%

    {Comentario}|{EspacioEnBlanco} { /* Ignorar */ }

    /* Valores */
    {Numero} {return token(yytext(), "Numero", yyline, yycolumn);}
    "true" {return token(yytext(), "Verdadero", yyline, yycolumn);}
    "false" {return token(yytext(), "Falso", yyline, yycolumn);}

    /* Operadores de agrupacion */
    "(" {return token(yytext(), "Abre_parentesis", yyline, yycolumn);}
    ")" {return token(yytext(), "Cierra_parentesis", yyline, yycolumn);}
    "{" {return token(yytext(), "Abre_llave", yyline, yycolumn);}
    "}" {return token(yytext(), "Cierra_llave", yyline, yycolumn);}

    /* Signos de puntuacion */
    "," {return token(yytext(), "Coma", yyline, yycolumn);}
    ";" {return token(yytext(), "Punto_y_coma", yyline, yycolumn);}

    /* Operadores */
    "=" {return token(yytext(), "Igual", yyline, yycolumn);}
    "+" {return token(yytext(), "Suma", yyline, yycolumn);}
    "-" {return token(yytext(), "Menos", yyline, yycolumn);}
    "*" {return token(yytext(), "Multiplicacion", yyline, yycolumn);}
    "/" {return token(yytext(), "Division", yyline, yycolumn);}
    "^" {return token(yytext(), "Exponente", yyline, yycolumn);}
    "<" {return token(yytext(), "Menor", yyline, yycolumn);}
    ">" {return token(yytext(), "Mayor", yyline, yycolumn);}
    "<=" {return token(yytext(), "MenorIgual", yyline, yycolumn);}
    ">=" {return token(yytext(), "MayorIgual", yyline, yycolumn);}
    "==" {return token(yytext(), "IgualIgual", yyline, yycolumn);}
    "!=" {return token(yytext(), "Diferente", yyline, yycolumn);}

    /* Palabras reservadas */
    "program" {return token(yytext(), "Program", yyline, yycolumn);}
    "if" {return token(yytext(), "If", yyline, yycolumn);}
    "else" {return token(yytext(), "Else", yyline, yycolumn);}
    "fi" {return token(yytext(), "Fi", yyline, yycolumn);}
    "do" {return token(yytext(), "Do", yyline, yycolumn);}
    "until" {return token(yytext(), "Until", yyline, yycolumn);}
    "while" {return token(yytext(), "While", yyline, yycolumn);}
    "read" {return token(yytext(), "Read", yyline, yycolumn);}
    "write" {return token(yytext(), "Write", yyline, yycolumn);}
    "not" {return token(yytext(), "Not", yyline, yycolumn);}
    "and" {return token(yytext(), "And", yyline, yycolumn);}
    "or" {return token(yytext(), "Or", yyline, yycolumn);}
    "then" {return token(yytext(), "Then", yyline, yycolumn);}

    /* Tipos de datos */

    "float" {return token(yytext(), "Float", yyline, yycolumn);}
    "int" {return token(yytext(), "Int", yyline, yycolumn);}
    "bool" {return token(yytext(), "Boolean", yyline, yycolumn);}

    /* Identificador */
    {Identificador} {return token(yytext(), "Identificador", yyline, yycolumn);}

    /* Errores */
    0{Numero} {return token(yytext(), "Error_numerico", yyline, yycolumn);} //Error de enteros
    
    . { return token(yytext(), "ERROR", yyline, yycolumn);}