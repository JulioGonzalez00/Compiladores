/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Compilador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JList;

/**
 *
 * @author julio
 */
abstract public class AnalizadorLexico {

    public static void main(String[] args) {
        String ruta = "C:/Users/julio/Desktop/Compiladores/Compiladores/Bazinga/src/Compilador/Lexer.flex";
        iniciar(ruta);
    }
    
    public static void iniciar(String ruta){
        File archivo = new File(ruta);
        JFlex.Main.generate(archivo);
    }
    public static void analizar(final String cadena, final JList lista, final JList listaError) {
        try {
            File archivo = new File("lexico.txt");
            try (PrintWriter escribir = new PrintWriter(archivo)) {
                escribir.print(cadena);
            }
            try {
                Reader lector = new BufferedReader(new FileReader("lexico.txt"));
                Lexer lexer = new Lexer(lector);
                ArrayList<String> resultado = new ArrayList();
                while (true) {
                    Tokens token = lexer.yylex();
                    if (token == null) {
                        resultado.add("Fin");
                        break;
                    }   
                    switch (token) {
                        case ERROR ->
                            resultado.add(lexer.lexeme + " el simbolo analizado no existe.");
                        case Identificador, Numero, Reservadas ->
                            resultado.add(lexer.lexeme + ": es un " + token + ".");
                        default ->
                            resultado.add("Token encontrado: " + token);
                    }
                }
                DefaultListModel model = new DefaultListModel();
                for (String string : resultado) {
                    model.addElement(string);
                }
                lista.setModel(model);
            } catch (FileNotFoundException ex) {
                System.err.println("Error en : " + ex.getMessage());
            } catch (IOException ex) {
                System.err.println("Error en : " + ex.getMessage());
            }
        } catch (FileNotFoundException ex) {
            System.err.println("Error en : " + ex.getMessage());
        }
    }
}
