/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compilador;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author julio
 */

public class Produccion {

    private String Nombre;
    private List<Token> Tokens;
    private int Fila;
    private int Columna;

    public Produccion(String Nombre) {
        this.Nombre = Nombre;
        this.Tokens = new ArrayList<>();
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public List<Token> getTokens() {
        return Tokens;
    }

    public void setTokens(List<Token> Tokens) {
        this.Tokens = Tokens;
    }

    public int getFila() {
        return Fila;
    }

    public void setFila(int Fila) {
        this.Fila = Fila;
    }

    public int getColumna() {
        return Columna;
    }

    public void setColumna(int Columna) {
        this.Columna = Columna;
    }

    @Override
    public String toString() {
        StringBuilder ex = new StringBuilder(Nombre + " { ");
        for (Token item : Tokens) {
            ex.append(item.toString()).append(" \n");
        }
        return ex.append(" }").toString();
    }
}
