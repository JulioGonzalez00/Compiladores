/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compilador;

import JFlex.SilentExit;

/**
 *
 * @author julio
 */
public class ExecuteJFlex {

    public static void main(String[] args) {
        String lexerFile = System.getProperty("user.dir") + "/src/apoyo/Lexer.flex";
        //String lexerColorFile = System.getProperty("user.dir") + "/src/apoyo/LexerColor.flex";
        try {
            JFlex.Main.generate(new String[]{lexerFile});
            //JFlex.Main.generate(new String[]{lexerColorFile});
        } catch (SilentExit ex) {
            System.err.println("Error al compilar los archivos");
        }
    }
}
