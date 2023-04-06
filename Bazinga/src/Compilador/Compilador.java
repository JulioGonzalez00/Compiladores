/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Compilador;

import GUI.Principal;
import java.io.IOException;

/**
 *
 * @author julio
 */
public class Compilador {

    Principal p;
    
    public Compilador(String ruta, Principal p) {
        this.p = p;
        Compilar(ruta);
    }

    private void Compilar(String ruta) {
        try {
            //Aqui se hara el llamado al compilador
            Process p = Runtime.getRuntime().exec("Ruta del compilador");
        } catch (IOException ex) {
            System.err.println("Error: " + ex.getLocalizedMessage());
        }
    }
}
