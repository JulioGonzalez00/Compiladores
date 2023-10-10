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
public class NodoArbol {

    private Produccion prod;
    
    private NodoArbol Padre;
    private String Nombre;
    private final ArrayList<NodoArbol> Hijos;

    public NodoArbol(String nombre) {
        Nombre = nombre;
        Hijos = new ArrayList();
    }

    public NodoArbol(String nombre, NodoArbol padre) {
        Nombre = nombre;
        Padre = padre;
        Hijos = new ArrayList();
    }

    public NodoArbol getPadre() {
        return Padre;
    }

    public void setPadre(NodoArbol Padre) {
        this.Padre = Padre;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public void setHijo(NodoArbol aux) {
        this.Hijos.add(aux);
    }

    public ArrayList<NodoArbol> getHijos() {
        return Hijos;
    }

    public Produccion getProd() {
        return prod;
    }

    public void setProd(Produccion prod) {
        this.prod = prod;
    }

}
