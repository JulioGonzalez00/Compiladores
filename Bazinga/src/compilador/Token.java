/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compilador;

/**
 *
 * @author julio
 */
public class Token {

    private String clave;
    private String lexema;
    private int fila;
    private int columna;

    public Token(String clave, String lexema, int fila, int columna) {
        this.clave = clave;
        this.lexema = lexema;
        this.fila = fila;
        this.columna = columna;
    }

    
    
    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getLexema() {
        return lexema;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }
    
    
    @Override
    public String toString(){
        String aux = "";
        aux+= "Token { ";
        aux+= clave;
        aux+= ", " + lexema;
        aux+= ", " + fila;
        aux+= ", " + columna + " }";
        
        return aux;
    }
}
