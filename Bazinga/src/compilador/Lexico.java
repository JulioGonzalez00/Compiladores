/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compilador;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 *
 * @author julio
 */
public class Lexico {

    //Vectores que se usan para el lexico
    private static final String[] PalabrasReservadas = {"program", "if", "else", "fi", "do", "until", "while", "read", "write", "float", "int", "bool", "not", "and", "or"};
    private static final Pattern IdentificadorRegex = Pattern.compile("^[a-zA-Z]\\w*$");
    private static final Pattern NumeroRegex = Pattern.compile("^\\d+(\\.\\d+)?$");
    private static final String[] SimbolosEspeciales = {"+", "-", "*", "/", "^", ">=", "<=", "<", ">", "==", "!=", "=", ";", ",", "(", ")", "{", "}"};
    private static final ArrayList<Character> Separadores = new ArrayList();
    private static final String ComentarioBloqueInicio = "/*";
    private static final String ComentarioBloqueFin = "*/";
    private static final String ComentarioLinea = "//";

    private boolean aux;
    private final ArrayList<Token> tokens;
    private String[] lineas;
    private int filaActual;
    private int columnaActual;

    public Lexico() {
        tokens = new ArrayList();
        Separadores.add(' ');
        Separadores.add('\t');
    }

    public ArrayList<Token> AnalizarCodigo(String codigo) {
        InicializarAnalisis(codigo);
        while (!FinDeCodigo()) {
            LeerSiguienteToken();
        }
        return tokens;
    }

    private void InicializarAnalisis(String codigo) {
        tokens.clear();
        String codigoSinComentarios = codigo.replaceAll("//.*|/\\*[\\s\\S]*?\\*/", "");

        lineas = codigoSinComentarios.split("\n");
        filaActual = 0;
        columnaActual = 1;
    }

    private boolean FinDeCodigo() {
        char a = LeerSiguienteCaracter();
        if (aux && a == '$') {
            return true;
        }

        if (filaActual == lineas.length) {
            aux = true;
            return filaActual > lineas.length;
        } else {
            return filaActual > lineas.length;
        }
    }

    private char LeerCaracterActual() {
        if (filaActual < lineas.length) {
            return lineas[filaActual].charAt(columnaActual - 1);
        }
        return '\0';
    }

    private char LeerSiguienteCaracter() {
        if (filaActual <= lineas.length) {
            try {
                if (columnaActual < lineas[filaActual].length()) {
                    return lineas[filaActual].charAt(columnaActual);
                } else if (filaActual + 1 < lineas.length) {
                    return lineas[filaActual + 1].charAt(0);
                }
            } catch (Exception e) {
                return '$';
            }
        }
        return '\0';
    }

    private void AvanzarCaracter() {
        if (filaActual < lineas.length) {
            if (columnaActual < lineas[filaActual].length()) {
                columnaActual++;
            } else {
                filaActual++;
                columnaActual = 1;
            }
        }
    }

    private void AvanzarHastaCaracterNoSeparador() {
        while (!FinDeCodigo() && Separadores.contains(LeerCaracterActual())) {
            AvanzarCaracter();
        }
    }

    private void LeerSiguienteToken() {
        AvanzarHastaCaracterNoSeparador();
        if (FinDeCodigo()) {
            return;
        }

        char caracterActual = LeerCaracterActual();
        char siguienteCaracter = LeerSiguienteCaracter();

        if (Arrays.asList(SimbolosEspeciales).contains(String.valueOf(caracterActual))) {
            if ((caracterActual == '>' || caracterActual == '<' || caracterActual == '=') && siguienteCaracter == '=') {
                ProcesarSimboloEspecial(String.valueOf(caracterActual) + "=");
                AvanzarCaracter();
            } else {
                ProcesarSimboloEspecial(String.valueOf(caracterActual));
            }
        } else if (Character.isLetter(caracterActual) || caracterActual == '_') {
            // Identificador o palabra reservada
            ProcesarIdentificadorOPalabraReservada();
        } else if (Character.isDigit(caracterActual)) {
            // Número
            ProcesarNumero();
        } else {
            // Carácter no reconocido
            AvanzarCaracter();
        }
    }

    private void ProcesarSimboloEspecial(String simbolo) {
        RegistrarToken("Símbolo Especial", simbolo, filaActual, columnaActual);
        AvanzarCaracter();
    }

    private void ProcesarIdentificadorOPalabraReservada() {
        int inicioFila = filaActual;
        int inicioColumna = columnaActual;

        StringBuilder lexema = new StringBuilder();
        lexema.append(LeerCaracterActual());

        AvanzarCaracter();

        while (!FinDeCodigo()) {
            char caracterActual = LeerCaracterActual();
            if (Character.isLetterOrDigit(caracterActual) || caracterActual == '_') {
                lexema.append(caracterActual);
                AvanzarCaracter();
            } else {
                break;
            }
        }

        String lexemaStr = lexema.toString();

        if (Arrays.asList(PalabrasReservadas).contains(lexemaStr.toLowerCase())) {
            RegistrarToken("Palabra Reservada", lexemaStr.toLowerCase(), inicioFila, inicioColumna);
        } else if (lexemaStr.matches("false") || lexemaStr.matches("true")) {
            RegistrarToken("Boolean", lexemaStr, inicioFila, inicioColumna);
        } else if (IdentificadorRegex.matcher(lexemaStr).matches()) {
            RegistrarToken("Identificador", lexemaStr, inicioFila, inicioColumna);
        } else {
            RegistrarToken("Token no válido", lexemaStr, inicioFila, inicioColumna);
        }
    }

    private void ProcesarNumero() {
        int inicioFila = filaActual;
        int inicioColumna = columnaActual;

        StringBuilder lexema = new StringBuilder();
        lexema.append(LeerCaracterActual());

        AvanzarCaracter();

        while (!FinDeCodigo()) {
            char caracterActual = LeerCaracterActual();
            if (Character.isDigit(caracterActual) || caracterActual == '.') {
                lexema.append(caracterActual);
                AvanzarCaracter();
            } else {
                break;
            }
        }

        String lexemaStr = lexema.toString();

        if (NumeroRegex.matcher(lexemaStr).matches()) {
            RegistrarToken("Número", lexemaStr, inicioFila, inicioColumna);
        } else {
            RegistrarToken("Token no válido", lexemaStr, inicioFila, inicioColumna);
        }
    }

    private void RegistrarToken(String clave, String lexema, int fila, int columna) {
        tokens.add(new Token(clave, lexema, fila, columna));
    }
}
