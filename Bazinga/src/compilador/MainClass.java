package compilador;

import compilerTools.Directory;
import compilerTools.ErrorLSSL;
import compilerTools.Functions;
import compilerTools.Grammar;
import compilerTools.Production;
import compilerTools.Token;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author julio
 */
public class MainClass {

    private String title;
    private Directory Directorio;
    private ArrayList<Token> tokens;
    private ArrayList<ErrorLSSL> errors;
    private Timer timer;
    private ArrayList<Production> identProd;
    private HashMap<String, String> identificadores;
    private boolean codeHasBeenCompiled = false;

    public MainClass() {
        tokens = new ArrayList<>();
        errors = new ArrayList<>();
        identProd = new ArrayList<>();
        identificadores = new HashMap();
    }

    public void compile(String codigo, JTable lexica, JList listaErrors) {
        lexicalAnalysis(codigo, lexica);
        syntacticAnalysis(listaErrors);
        semanticAnalysis();
    }

    private void lexicalAnalysis(String codigo, JTable lexica) {
        Lexer lexer;
        File code = new File("code.encrypter");
        try {
            FileOutputStream output = new FileOutputStream(code);
            byte[] bytesText = codigo.getBytes();
            output.write(bytesText);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(new FileInputStream(code), "UTF-8"));
            lexer = new Lexer(entrada);
            while (true) {
                Token token = lexer.yylex();
                if (token == null) {
                    break;
                }
                tokens.add(token);
            }

        } catch (FileNotFoundException ex) {
            System.err.println("Error: " + ex.getMessage());
        } catch (IOException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
        DefaultTableModel modelo = (DefaultTableModel) lexica.getModel();
        int a = modelo.getRowCount();
        for (int i = 0; i < a; i++) {
            modelo.removeRow(0);
        }
        Object[] rowData = new Object[6];
        for (Token p : tokens) {
            rowData[0] = p.getLexeme();
            rowData[1] = p.getLexicalComp();
            rowData[2] = p.getLine() + ", " + p.getColumn();
            modelo.addRow(rowData);
        }
        lexica.setModel(modelo);
    }

    private void syntacticAnalysis(JList listaErrores) {
        Grammar gramatica = new Grammar(tokens, errors);

        /*Eliminacion de los errores*/
        gramatica.delete(new String[]{"ERROR", "Error_numerico", "Error_identificador"});

        /* Agrupacion de valores logicos */
        gramatica.group("Logico", "(True | False)", true);

        /*Agrupacion de valores*/
        gramatica.group("Valor", "(Numero | Logico)", true);

        /* Agrupacion de los tipos de dato */
        gramatica.group("Tipo_dato", "(Float | Int | Boolean)", true);

        /*Declaracion de variable*/
        gramatica.group("Variable", "Tipo_dato Identificador Igual Valor", true);
        gramatica.group("Variable", "Tipo_dato Igual Valor", true,
                2, "Error sintactico {}: Falta el identificador en la variable [#, %]");
        gramatica.group("Variable", "Tipo_dato Identificador Valor", true,
                3, "Error sintactico {}: Falta el asignador en la variable [#, %]");
        gramatica.group("Variable", "Identificador Igual Valor", true,
                3, "Error sintactico {}: Falta el tipo de dato en la variable [#, %]");

        gramatica.finalLineColumn();

        gramatica.group("Variable", "Tipo_dato Identificador Igual", true,
                4, "Error sintactico {}: Falta el valor en la declaracion [#, %]");

        gramatica.initialLineColumn();

        /* Eliminacion de tipos de datos y operadores de asignacion*/
        gramatica.delete("Tipo_dato", 5,
                "Error sintactico {}: El tipo de dato no esta en una dclaracion [#, %]");
        gramatica.delete("Igual", 6,
                "Error sintactico {}: El asignador no esta en una declaracion [#, %]");

        /*Agrupar identificadores y parametros*/
        gramatica.group("Valor", "Identificador", true);
        gramatica.group("Parametros", "Valor (Coma Valor)+");

        /*Agrupacion de funciones*/
        gramatica.group("Palabra_reservada",
                "(Program | Read | Write)", true);

        gramatica.group("Funcion", "Palabra_reservada", true);
        gramatica.group("Funcion_comp", "Funcion Abre_llave (Valor | Parametros)? Cierra_llave");
        gramatica.group("Funcion_comp", "Funcion (Valor | Parametros)? Cierra_llave", 7,
                "Error sintactico {}: Falta el parentensis que abre [#, %]");

        gramatica.finalLineColumn();

        gramatica.group("Funcion_comp", "Funcion Abre_parentesis (Valor | Parametros)", 8,
                "Error sintactico {}: Falta el parentesis que cierra [#, %]");

        gramatica.initialLineColumn();

        /* Eliminacion de funciones incompletas*/
        gramatica.delete("Funcion", 9,
                "Error sintactico {}: La funcion no se declaro de manera adecuada");

        /* Operadores logicos */
        gramatica.group("Operador_logico",
                "(Menor | Mayor | MenorIgual | MayorIgual | IgualIgual | Diferente)", true);

        gramatica.loopForFunExecUntilChangeNotDetected(() -> {
            gramatica.group("Expresion_logica", "(Funcion_comp | Expresion_logica) (Operador_logico (Expresion_logica | Funcion_comp))+");
            gramatica.group("Expresion_logica", "Abre_parentesis Expresion_logica Cierra_parentesis");
        });

        /* Eliminacion de operadores logicos incompletos */
        gramatica.delete("Operador_logico", 10, "Error sintactico {}: El operador logico no esta en ninguna expresion.");

        /* Agrupacion de expreciones logicas como valor y parametros */
        gramatica.group("Valor", "Expresion_logica");
        gramatica.group("Parametros", "Valor (Coma Valor)+");

        /* Sentencias */
        gramatica.group("Sentencia", "(Variable_pc | Funcion_comp_pc)");

        /* Agrupacion de estructuras de control */
        gramatica.group("Estructura_control", "(While | If | Done | Else | Until | Then)");
        gramatica.group("Estructura_control", "Estructura_control Abre_llave Cierra_llave");
        gramatica.group("Estructura_control_comp", "Estructura_control Abre_llave (Sentencia)? Cierra_llave");
        gramatica.group("Estructura_control_comp", "Estructura_control Abre_parentesis (Valor | Parametros) Cierra_parentesis");
        gramatica.group("Estructura_control_comp", "Estructura_control Abre_parentesis (Valor | Parametros) Cierra_parentesis Estructura_control");

        /* Eliminacion de estructuras de control incompletas */
        gramatica.delete("Estructura_control", 11, "Error sintactico {}: La estructura no se declaro correctamente [#, %]");

        /* Eliminacion de parentesis */
        gramatica.delete(new String[]{"Abre_parentesis", "Cierra_parentesis"}, 12,
                "Error sintactico {}: El parentesis [] no esta contenido en una agrupacion [#, %] ");

        gramatica.finalLineColumn();

        /* Verificacion de punto y coma al final de una sentencia */
        //Identificadores de variables
        gramatica.group("Variable_PC", "Variable Punto_y_coma");
        gramatica.group("Variable_PC", "Variable", true, 13,
                "Error sintactico {}: Falta el punto y coma al final del avariable [#, %]");

        //Funciones
        gramatica.group("Funcion_comp_pc", "Funcion_comp Punto_y_coma");

        gramatica.initialLineColumn();

        /* Eliminacion del punto y coma */
        gramatica.delete("Punto_y_coma", 14, "Error sintactico {}: El punto y coma no esta al final de una sentencia [#, %]");

        gramatica.loopForFunExecUntilChangeNotDetected(() -> {
            gramatica.group("Estructura_control_comp_LASLC", "Estructura_control_comp Abre_llave (Sentencia)? Cierra_llave", true);
            gramatica.group("Sentencia", "(Sentencia | Estructura_control_comp_LASLC)+");
        });

        /* Estructuras de control incompletas */
        gramatica.loopForFunExecUntilChangeNotDetected(() -> {
            gramatica.initialLineColumn();
            gramatica.group("Estructura_control_comp_LASLC", "Estructura_control_completa (Sentencia)? Cierra_llave", true,
                    15, "Error sintactico {}: Falta la llave que abre la estructura de control [#, %]");
            gramatica.finalLineColumn();
            gramatica.group("Estructura_control_comp_LASLC", "Estructura_control_completa Abre_llave (Sentencia)", true,
                    16, "Error sintactico {}: Falta la llave que cierra la estructura de control [#, %]");
            gramatica.group("Sentencia", "(Sentencia | Estructura_control_comp_LASLC)");
        });

        gramatica.delete(new String[]{"Abre_llave", "Cierra_llave"}, 17,
                "Error sintactico {}: La llave [] no esta contenido en una agrupacion [#, %]");

        DefaultListModel<String> lista = new DefaultListModel<>();
        int sizeErrors = errors.size();
        if (sizeErrors > 0) {
            Functions.sortErrorsByLineAndColumn(errors);
            for (ErrorLSSL error : errors) {
                lista.addElement(String.valueOf(error));
            }
        } else {

        }
        listaErrores.setModel(lista);
        System.out.println("Hola");
    }

    private void semanticAnalysis() {

    }

}
