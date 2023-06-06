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

    public void compile(String codigo, JTable lexica, JList listaErrors, JList Resultados) {
        lexicalAnalysis(codigo, lexica);
        syntacticAnalysis(listaErrors, Resultados);
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

    private void syntacticAnalysis(JList listaErrores, JList Resultados) {

        Grammar gramatica = new Grammar(tokens, errors);
        gramatica.disableMessages();
        /*Eliminacion de los errores*/
        gramatica.delete(new String[]{"ERROR", "Error_numerico", "Error_identificador"});

        gramatica.group("Valor", "(Numero | Verdadero | Falso)");
        gramatica.group("Valor", "Identificador Menos Identificador");
        gramatica.group("Valor", "Identificador Suma Identificador");
        gramatica.group("Valor", "Identificador Menos Valor");
        gramatica.group("Valor", "Identificador Suma Valor");
        gramatica.group("Valor", "Identificador Multiplicacion Valor");
        gramatica.group("Valor", "Identificador Multiplicacion Identificador");
        gramatica.group("Valor", "Identificador Division Valor");
        gramatica.group("Valor", "Identificador Division Identificador");

        gramatica.group("Tipo_dato", "(Float | Int | Boolean)");
        gramatica.group("Declaracion", "Tipo_dato Identificador (Coma Identificador)? Punto_y_coma");

        gramatica.loopForFunExecUntilChangeNotDetected(() -> {
            gramatica.group("Lista_decl", "Declaracion");
            gramatica.group("Lista_decl", "Lista_decl Declaracion");
            gramatica.group("Lista_decl", "Lista_decl Lista_decl");
        });

        gramatica.loopForFunExecUntilChangeNotDetected(() -> {
            gramatica.group("Op_logico", "(IgualIgual | Mayor | Menor | MenorIgual | MayorIgual | Diferente)");
            gramatica.group("Exp_logica", "Identificador Op_logico Valor");
            gramatica.group("Exp_logica", "Identificador Op_logico Identificador");
            gramatica.group("Exp_logica", "Exp_logica (And | Or) Exp_logica");
        });

        gramatica.loopForFunExecUntilChangeNotDetected(() -> {
            //gramatica.group("Suma", "Identificador  (Suma | Resta) Valor");
            gramatica.group("Sent_asig", "Identificador Igual Valor Punto_y_coma");

            gramatica.group("Sent_write", "Write Identificador Punto_y_coma");
            gramatica.group("Sent_while", "While Abre_parentesis Exp_logica Cierra_parentesis Bloque");
            gramatica.group("Sent_if", "If Abre_parentesis Exp_logica Cierra_parentesis Then Bloque (Else Bloque)? Fi");
            gramatica.group("Sent_do", "Do Bloque Until Abre_parentesis Exp_logica Cierra_parentesis Punto_y_coma");
            gramatica.group("Bloque", "Abre_llave (Lista_sent) Cierra_llave");

            gramatica.group("Sentencia", "(Sent_write | Sent_asig | Sent_if | Sent_while | Sent_do)");

            gramatica.group("Lista_sent", "Sentencia");
            gramatica.group("Lista_sent", "Lista_sent Sentencia");
            gramatica.group("Lista_sent", "Lista_sent Lista_sent");

        });

        gramatica.group("Program", "Program Abre_llave (Lista_decl)? (Lista_sent)? Cierra_llave");
        int num = 0;
        /* Declaracion de los errores */
        gramatica.initialLineColumn();

        gramatica.group("Program", "Program (Lista_decl)? (Lista_sent)? Cierra_llave", num++,
                "Error sintactico {}: Falta la llave que abre[#, %]");
        gramatica.group("Declaracion", "Identificador (Coma Identificador)? Punto_y_coma", num++,
                "Error sintactico {}: Falta el tipo de dato [#, %}");
        gramatica.group("Exp_logica", "Op_logico Identificador", num++,
                "Error sintactico {}: Falta el primer valor de operacion [#, %]");
        gramatica.group("Exp_logica", "(And | Or) Exp_logica", num++,
                "Error sintactico {}: Falta la primer expresion logica a evaluar [#, %]");
        gramatica.group("Sent_asig", "Igual Valor Punto_y_coma", num++,
                "Error sintactico {}: Falta el identificador [#, %]");
        gramatica.group("Sent_asig", "Identificador Valor Punto_y_coma", num++,
                "Error sintactico {}: Falta el operador de asignacion [#, %]");
        gramatica.group("Sent_asig", "Identificador Igual Punto_y_coma", num++,
                "Error sintactico {}: Falta el valor [#, %]");
        gramatica.group("Sent_while", "Abre_parentesis Exp_logica Cierra_parentesis Bloque", num++,
                "Error sintactico {}: Falta la palabra reservada while [#, %]");
        gramatica.group("Sent_while", "While Exp_logica Cierra_parentesis Bloque", num++,
                "Error sintactico {}: Falta el parentesis que abre [#, %]");
        gramatica.group("Sent_while", "While Abre_parentesis Cierra_parentesis Bloque", num++,
                "Error sintactico {}: Falta la operacion logica [#, %]");
        gramatica.group("Sent_while", "While Abre_parentesis Exp_logica Bloque", num++,
                "Error sintactico {}: Falta el parentesis que cierra [#, %]");
        gramatica.group("Sent_if", "Abre_parentesis Exp_logica Cierra_parentesis Then Bloque (Else Bloque)? Fi", num++,
                "Error sintactico {}: Falta la palabra reservada if [#, %]");
        gramatica.group("Sent_if", "If Exp_logica Cierra_parentesis Then Bloque (Else Bloque)? Fi", num++,
                "Error sintactico {}: Falta el parentesis que abre [#, %]");
        gramatica.group("Sent_if", "If Abre_parentesis Cierra_parentesis Then Bloque (Else Bloque)? Fi", num++,
                "Error sintactico {}: Falta la expresion logica [#, %]");
        gramatica.group("Sent_if", "If Abre_parentesis Exp_logica Then Bloque (Else Bloque)? Fi", num++,
                "Error sintactico {}: Falta el parentesis que cierra [#, %]");
        gramatica.group("Sent_if", "If Abre_parentesis Exp_logica Cierra_parentesis Bloque (Else Bloque)? Fi", num++,
                "Error sintactico {}: Falta la palabra reservada then [#, %]");
        gramatica.group("Sent_if", "If Abre_parentesis Exp_logica Cierra_parentesis Then (Else Bloque)? Fi", num++,
                "Error sintactico {}: Falta el bloque a ejecutar [#, %]");
        gramatica.group("Sent_if", "If Abre_parentesis Exp_logica Cierra_parentesis Then Bloque (Bloque)? Fi", num++,
                "Error sintactico {}: Falta la palabra reservada Else [#, %]");
        gramatica.group("Sent_if", "If Abre_parentesis Exp_logica Cierra_parentesis Then Bloque (Else )? Fi", num++,
                "Error sintactico {}: Falta el bloque a ejecutar en el else [#, %]");
        gramatica.group("Sent_do", "Bloque Until Abre_parentesis Exp_logica Cierra_parentesis Punto_y_coma", num++,
                "Error sintactico {}: Falta la palabra reservada do [#, %]");
        gramatica.group("Sent_do", "Do Until Abre_parentesis Exp_logica Cierra_parentesis Punto_y_coma", num++,
                "Error sintactico {}: Falta el bloque a ejecutar [#, %]");
        gramatica.group("Sent_do", "Do Bloque Abre_parentesis Exp_logica Cierra_parentesis Punto_y_coma", num++,
                "Error sintactico {}: Falta la palabra reservada until [#, %]");
        gramatica.group("Sent_do", "Do Bloque Until Exp_logica Cierra_parentesis Punto_y_coma", num++,
                "Error sintactico {}: Falta el parentesis que abre [#, %]");
        gramatica.group("Sent_do", "Do Bloque Until Abre_parentesis Cierra_parentesis Punto_y_coma", num++,
                "Error sintactico {}: Falta la expresion logica [#, %]");
        gramatica.group("Sent_do", "Do Bloque Until Abre_parentesis Exp_logica Punto_y_coma", num++,
                "Error sintactico {}: Falta el parentesis que cierra [#, %]");
        gramatica.group("Bloque", "(Lista_sent) Cierra_llave", num++,
                "Error sintactico {}: Falta el parentesis que abre [#, %]");
        gramatica.group("Bloque", "Abre_llave Cierra_llave", num++,
                "Error sintactico {}: Falta el bloque de ejecucion [#, %]");

        gramatica.finalLineColumn();

        gramatica.group("Program", "Program Abre_llave (Lista_decl)? (Lista_sent)? ", num++,
                "Error sintactico {}: Falta la llave que cierra [#, %]");
        gramatica.group("Declaracion", "Tipo_dato Identificador (Coma Identificador)?", num++,
                "Error sintactico {}: Falta el punto y coma [#, %}");
        gramatica.group("Exp_logica", "Identificador Op_logico ", num++,
                "Error sintactico {}: Falta el segundo valor de operacion [#, %]");
        gramatica.group("Exp_logica", "Exp_logica (And | Or)", num++,
                "Error sintactico {}: Falta la segunda expresion logica a evaluar [#, %]");
        gramatica.group("Sent_asig", "Identificador Igual Valor", num++,
                "Error sintactico {}: Falta el punto y coma [#, %]");
        gramatica.group("Sent_while", "While Abre_parentesis Exp_logica Cierra_parentesis", num++,
                "Error sintactico {}: Falta el bloque a ejecutar[#, %]");
        gramatica.group("Sent_if", "If Abre_parentesis Exp_logica Cierra_parentesis Then Bloque (Else Bloque)?", num++,
                "Error sintactico {}: Falta la palabra reservada fi[#, %]");
        gramatica.group("Sent_do", "Do Bloque Until Abre_parentesis Exp_logica Cierra_parentesis", num++,
                "Error sintactico {}: Falta el punto y coma [#, %]");   
        gramatica.group("Bloque", "Abre_llave (Lista_sent)", num++,
                "Error sintactico {}: Falta el parentesis que cierra [#, %]");

        //gramatica.show();
        DefaultListModel<String> lista = new DefaultListModel<>();
        DefaultListModel<String> listaR = new DefaultListModel<>();
        int sizeErrors = errors.size();
        if (sizeErrors > 0) {
            listaR.addElement("Compilacion abortada.");
            listaR.addElement("Revisar panel de errores.");
            Functions.sortErrorsByLineAndColumn(errors);
            for (ErrorLSSL error : errors) {
                lista.addElement(String.valueOf(error));
            }
        } else {
            listaR.addElement("Analisis sintactico terminado.");
            listaR.addElement("El codigo es valido.");
        }
        listaErrores.setModel(lista);
        Resultados.setModel(listaR);
    }

    private void semanticAnalysis() {

    }

}
