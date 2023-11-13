/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compilador;

import java.util.ArrayList;

/**
 *
 * @author julio
 */
public class Informacion {

    private float valor;
    public boolean error;
    public boolean bool;
    String tipo;
    ArrayList<String> lineas = new ArrayList();

    public Informacion(float valor, String tipo, boolean bool, String linea) {
        this.valor = valor;
        this.tipo = tipo;
        this.bool = bool;
        lineas.add(linea);
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean isBool() {
        return bool;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void addLinea(String linea) {
        this.lineas.add(linea);
    }

    public String getLinea() {
        String linea = "";
        for (String aux : lineas) {
            linea += aux + ", ";
        }
        return linea;
    }
}
