package Compilador;
import static Compilador.Tokens.*;
%%
%class Lexer
%type Tokens
L=[a-zA-Z_]+
D=[0-9]+
F=[0-9]+.[0-9]+
espacio=[ ,\t,\r,\n]+
C=[},\],{,\[,(,),;,\",\']
%{
    public String lexeme;
%}
%%
int |
char |
float |
if |
else |
while |
for {lexeme=yytext(); return PalabraReservada;}
{espacio} {/*Ignore*/}
"//".* {/*Ignore*/}
"=" {return Igual;}
"!=" {return Diferente;}
"==" {return IgualIgual;}
"+" {return Suma;}
"-" {return Resta;}
"*" {return Multiplicacion;}
"/" {return Division;}
">" {return MayorQue;}
">=" {return MayorIgual;}
"<" {return MenorQue;}
"<=" {return MenorIgual;}
{C} {lexeme=yytext(); return CaracterEspecial;}
{L}({L}|{D})* {lexeme=yytext(); return Identificador;}
("(-"{D}+")")|{D}+ {lexeme=yytext(); return Numero;}
("(-"{F}+")")|{F}+ {lexeme=yytext(); return Flotante;}
(\'|\"){L}({L}|{D})*(\'|\") {lexeme=yytext(); return Texto;}
 . {lexeme=yytext(); return ERROR;}