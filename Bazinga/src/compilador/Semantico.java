/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compilador;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author julio
 */
public class Semantico {

    HashMap<String, Informacion> identificadores = new HashMap();
    ArrayList<String> errores = new ArrayList();

    public void semanticalAnalisys(NodoArbol raiz) {
        analizar(raiz);
        if (!errores.isEmpty()) {
            for (String err : errores) {
                System.out.println(err);
            }
        }
    }

    public void analizar(NodoArbol nodo) {
        String val = nodo.getNombre();
        switch (val) {
            case "Asignación" ->
                asignacion(nodo);
            case "Declaracion" ->
                declaracion(nodo);
            case "If" ->
                sentIf(nodo);
            case "While" ->
                While(nodo);
            case "Write" -> {
            }
            case "Do" ->
                Do(nodo);
            case "Program" -> {
            }
            default ->
                errores.add("Error nodo invalido: " + val);
        }
        for (NodoArbol hijo : nodo.getHijos()) {
            analizar(hijo);
        }
    }

    public void declaracion(NodoArbol nodo) {
        Produccion prod = nodo.getProd();
        String tipo = "";
        for (Token tok : prod.getTokens()) {
            if (tok.getClave().matches("Palabra Reservada")) {
                tipo = tok.getLexema();
            } else if (tok.getClave().matches("Identificador")) {
                if (identificadores.containsKey(tok.getLexema())) {
                    errores.add("Error: Identificador duplicado, " + tok.getLexema()
                            + " [" + (tok.getFila() + 1) + ", " + tok.getColumna() + "]");
                } else {
                    identificadores.put(tok.getLexema(),
                            new Informacion(0, tipo, false));
                }
            }
        }
    }

    public void asignacion(NodoArbol nodo) {
        Produccion prod = nodo.getProd();
        String tipo = "";
        for (Token tok : prod.getTokens()) {
            if (tipo.isEmpty()) {
                if (revisar(tok.getLexema())) {
                    tipo = identificadores.get(tok.getLexema()).getTipo();
                } else {
                    errores.add("Error: identificador no declarado "
                            + tok.getLexema() + " [" + (tok.getFila() + 1) + ", "
                            + tok.getColumna() + "]");
                }
            } else {
                if (!tok.getLexema().matches("=") && !tok.getLexema().matches(";")) {
                    switch (tok.getClave()) {
                        case "Número":
                            if (tipo.matches("int")) {
                                try {
                                    Integer.parseInt(tok.getLexema());
                                } catch (NumberFormatException ex) {
                                    errores.add("Error: asignacion incorrecta, tipos no compatibles "
                                            + tok.getLexema() + " [" + (tok.getFila() + 1) + ", "
                                            + tok.getColumna() + "]");
                                }
                            } else if (tipo.matches("bool")) {
                                errores.add("Error: asignacion incorrecta, tipos no compatibles "
                                        + tok.getLexema() + " [" + (tok.getFila() + 1) + ", "
                                        + tok.getColumna() + "]");
                            }
                            break;
                        case "Boolean":
                            break;
                        case "Identificador":
                            if (revisar(tok.getLexema())) {
                                if (tipo.matches("bool")) {
                                    if (!identificadores.get(tok.getLexema()).getTipo().matches("bool")) {
                                        errores.add("Error: asignacion incorrecta, tipos no compatibles "
                                                + tok.getLexema() + " [" + (tok.getFila() + 1) + ", "
                                                + tok.getColumna() + "]");
                                    }
                                } else {
                                    if (!identificadores.get(tok.getLexema()).getTipo().matches("int")
                                            && !identificadores.get(tok.getLexema()).getTipo().matches("float")) {
                                        errores.add("Error: asignacion incorrecta, tipos no compatibles "
                                                + tok.getLexema() + " [" + (tok.getFila() + 1) + ", "
                                                + tok.getColumna() + "]");
                                    }
                                }
                            } else {
                                errores.add("Error: identificador no declarado "
                                        + tok.getLexema() + " [" + (tok.getFila() + 1) + ", "
                                        + tok.getColumna() + "]");
                            }
                            break;
                        case "Símbolo Especial":
                            if (tipo.matches("bool")) {
                                errores.add("Error: operacion con booleanos es erronea "
                                        + tok.getLexema() + " [" + (tok.getFila() + 1) + ", "
                                        + tok.getColumna() + "]");
                            }
                            break;
                        default:
                            errores.add("Error: tipo incorrecto " + tok.getLexema()
                                    + " [" + (tok.getFila() + 1) + ", " + tok.getColumna() + "]");
                            break;
                    }
                }
            }
        }
    }

    public ArrayList<String> getErrors() {
        return errores;
    }

    private boolean revisar(String ident) {
        return identificadores.containsKey(ident);
    }

    private void sentIf(NodoArbol nodo) {
        Produccion prod = nodo.getProd();
        ArrayList<Token> expBool = new ArrayList();
        int cont = 0;
        for (Token tok : prod.getTokens()) {
            if (tok.getLexema().matches("\\(")) {
                cont++;
            } else if (tok.getLexema().matches("\\)")) {
                cont--;
                if (cont == 0) {
                    expBool.add(tok);
                    break;
                }
            }
            if (cont > 0) {
                expBool.add(tok);
            }
        }
        separarExp(expBool);
    }

    private void separarExp(ArrayList<Token> expBool) {
        ArrayList<Token> aux = new ArrayList();
        for (Token tok : expBool) {
            if (tok.getLexema().matches("and") || tok.getLexema().matches("or")) {
                revisarExp(aux);
                aux = new ArrayList();
            } else {
                aux.add(tok);
            }
        }
        revisarExp(aux);
    }

    private void revisarExp(ArrayList<Token> aux) {
        String tipo = "";
        for (Token tok : aux) {
            if (tipo.isEmpty()) {
                System.out.println(tok.getClave());
                switch (tok.getClave()) {
                    case "Identificador" -> {
                        if (revisar(tok.getLexema())) {
                            switch (identificadores.get(tok.getLexema()).getTipo()) {
                                case "float", "int" ->
                                    tipo = "num";
                                case "bool" ->
                                    tipo = "bool";
                                default -> {
                                    errores.add("Expresion booleana invalida "
                                            + " [" + (tok.getFila() + 1) + ", "
                                            + tok.getColumna() + "]");
                                }

                            }
                        } else {
                            errores.add("Error: identificador no declarado "
                                    + tok.getLexema() + " [" + (tok.getFila() + 1) + ", "
                                    + tok.getColumna() + "]");
                        }
                    }
                    case "Número" ->
                        tipo = "num";
                    case "Boolean" ->
                        tipo = "bool";
                    case "Símbolo Especial" -> {
                    }
                    default ->
                        errores.add("Expresion booleana invalida "
                                + tok.getLexema() + " [" + (tok.getFila() + 1) + ", "
                                + tok.getColumna() + "]");
                }
            } else {
                switch (tok.getClave()) {
                    case "Identificador":
                        if (revisar(tok.getLexema())) {
                            if (tipo.matches("bool")) {
                                if (!identificadores.get(tok.getLexema()).getTipo().matches("bool")) {
                                    errores.add("Tipos comparados incorrectos "
                                            + tok.getLexema() + " [" + (tok.getFila() + 1) + ", "
                                            + tok.getColumna() + "]");
                                }
                            } else {
                                if (identificadores.get(tok.getLexema()).getTipo().matches("bool")) {
                                    errores.add("Tipos comparados incorrectos "
                                            + tok.getLexema() + " [" + (tok.getFila() + 1) + ", "
                                            + tok.getColumna() + "]");
                                }
                            }
                        } else {
                            errores.add("Error: identificador no declarado "
                                    + tok.getLexema() + " [" + (tok.getFila() + 1) + ", "
                                    + tok.getColumna() + "]");
                        }
                        break;
                    case "Número":
                        if (tipo.matches("bool")) {
                            errores.add("Tipos comparados incorrectos "
                                    + tok.getLexema() + " [" + (tok.getFila() + 1) + ", "
                                    + tok.getColumna() + "]");
                        }
                        break;
                    case "Boolean":
                        if (tipo.matches("num")) {
                            errores.add("Tipos comparados incorrectos "
                                    + tok.getLexema() + " [" + (tok.getFila() + 1) + ", "
                                    + tok.getColumna() + "]");
                        }
                        break;
                    case "Símbolo Especial":
                        break;
                    default:
                        errores.add("Expresion booleana invalida"
                                + tok.getLexema() + " [" + (tok.getFila() + 1) + ", "
                                + tok.getColumna() + "]");
                        break;
                }
            }
        }
    }

    private void While(NodoArbol nodo) {
        Produccion prod = nodo.getProd();
        ArrayList<Token> expBool = new ArrayList();
        int cont = 0;
        for (Token tok : prod.getTokens()) {
            if (tok.getLexema().matches("\\(")) {
                cont++;
            } else if (tok.getLexema().matches("\\)")) {
                cont--;
                if (cont == 0) {
                    expBool.add(tok);
                    break;
                }
            }
            if (cont > 0) {
                expBool.add(tok);
            }
        }
        separarExp(expBool);
    }

    private void Do(NodoArbol nodo) {
        Produccion prod = nodo.getProd();
        ArrayList<Token> expBool = new ArrayList();
        int cont = 0;
        boolean band = false;
        for (Token tok : prod.getTokens()) {
            if (tok.getLexema().matches("until")) {
                band = true;
            } else if (band) {
                if (tok.getLexema().matches("\\(")) {
                    cont++;
                } else if (tok.getLexema().matches("\\)")) {
                    cont--;
                    if (cont == 0) {
                        expBool.add(tok);
                        break;
                    }
                }
                if (cont > 0) {
                    expBool.add(tok);
                }
            }
        }
        separarExp(expBool);
    }

}
