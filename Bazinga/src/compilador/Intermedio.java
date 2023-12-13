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
public class Intermedio {

    String codigo = "";
    private NodoArbol raiz;
    ArrayList<String> errores = new ArrayList();
    int actualEntrar = 1;
    int temp = 1;

    Intermedio() {
        this.raiz = null;
    }

    public String generar(NodoArbol raiz) {
        this.raiz = raiz;
        if (this.raiz != null) {
            revisar(this.raiz);
        }
        return codigo;
    }

    public void revisar(NodoArbol nodo) {
        String val = nodo.getNombre();
        switch (val) {
            case "Declaracion" ->
                declaracion(nodo);
            case "Write" ->
                write(nodo);
            case "While" ->
                While(nodo);
            case "Asignación" ->
                asignacion(nodo);
            case "If" ->
                sentIf(nodo);
            case "Do" ->
                Do(nodo);
            case "Program" -> {
                for (NodoArbol hijo : nodo.getHijos()) {
                    revisar(hijo);
                }
            }
            default ->
                errores.add("Error nodo invalido: " + val);
        }
    }

    private void declaracion(NodoArbol nodo) {
        String tipo = "";
        for (Token tok : nodo.getProd().getTokens()) {
            if (tok.getClave().equals("Palabra Reservada")) {
                tipo = tok.getLexema();
            } else if (tok.getClave().matches("Identificador")) {
                codigo += "" + tipo + " " + tok.getLexema() + "\n";
            }
        }
    }

    private void write(NodoArbol nodo) {
        for (Token tok : nodo.getProd().getTokens()) {
            if (tok.getClave().matches("Identificador")) {
                codigo += "write " + tok.getLexema() + "\n";
            }
        }
    }

    private void While(NodoArbol nodo) {
        int inicioWhile = actualEntrar;
        codigo += ("L" + actualEntrar + ":");
        actualEntrar++;
        int salirWhile = actualEntrar;
        actualEntrar++;
        int cont = 0;
        for (Token tok : nodo.getProd().getTokens()) {
            if (tok.getLexema().matches("\\(")) {
                codigo += " if ";
                cont++;
            } else if (tok.getLexema().matches("\\)")) {
                cont--;
                if (cont == 0) {
                    codigo += " goto L" + actualEntrar + "\ngoto L" + salirWhile + "\n";
                    break;
                }
            } else if (cont > 0) {
                if (tok.getLexema().matches("or") || tok.getLexema().matches("and")) {
                    codigo += " goto L" + actualEntrar + "\ngoto L" + salirWhile + "\nL" + actualEntrar + ": ";
                    actualEntrar++;
                } else {
                    codigo += tok.getLexema() + " ";
                }
            }
        }
        boolean bandH = false;
        for (NodoArbol hijo : nodo.getHijos()) {
            if(hijo.getNombre().matches("While") || hijo.getNombre().matches("Do")){
                revisar(hijo);
            }else if(bandH){
                revisar(hijo);
            }else{
                bandH=true;
                codigo += "L" + actualEntrar + ":\n";
                actualEntrar++;
                revisar(hijo);
            }
        }
        codigo += "goto L" + inicioWhile;
        codigo += "\nL" + salirWhile + ":\n";
    }

    private void Do(NodoArbol nodo) {
        int inicioDo = actualEntrar;
        codigo += ("L" + actualEntrar + ":");
        actualEntrar++;
        int salirDo = actualEntrar;
        actualEntrar++;
        Produccion prod = nodo.getProd();
        int cont = 0;
        boolean band = false;
        for (Token tok : prod.getTokens()) {
            if (tok.getLexema().matches("until")) {
int a;
                band = true;
            } else if (band) {
                if (tok.getLexema().matches("\\(")) {
                    codigo += " if ";
                    cont++;
                } else if (tok.getLexema().matches("\\)")) {
                    cont--;
                    if (cont == 0) {
                        codigo += " goto L" + actualEntrar + "\ngoto L" + salirDo + "\n";
                        break;
                    }
                } else if (cont > 0) {
                    if (tok.getLexema().matches("or") || tok.getLexema().matches("and")) {
                        codigo += " goto L" + actualEntrar + "\ngoto L" + salirDo + "\nL" + actualEntrar + ": ";
                        actualEntrar++;
                    } else {
                        codigo += tok.getLexema() + " ";
                    }
                }
            }
        }
        boolean bandH = false;
        for (NodoArbol hijo : nodo.getHijos()) {
            if(hijo.getNombre().matches("While") || hijo.getNombre().matches("Do")){
                revisar(hijo);
            }else if(bandH){
                revisar(hijo);
            }else{
                bandH=true;
                codigo += "L" + actualEntrar + "\n";
                actualEntrar++;
                revisar(hijo);
            }
        }
        codigo += "goto L" + inicioDo;
        codigo += "\nL" + salirDo + ":\n";
    }

    private void sentIf(NodoArbol nodo) {
        codigo += ("L" + actualEntrar + ":");
        actualEntrar++;
        int salirIf = actualEntrar;
        actualEntrar++;
        Produccion prod = nodo.getProd();
        int cont = 0;
        for (Token tok : prod.getTokens()) {
            if (tok.getLexema().matches("\\(")) {
                codigo += " if ";
                cont++;
            } else if (tok.getLexema().matches("\\)")) {
                cont--;
                if (cont == 0) {
                    codigo += " goto L" + actualEntrar + "\ngoto L" + salirIf + "\n";
                    break;
                }
            } else if (cont > 0) {
                if (tok.getLexema().matches("or") || tok.getLexema().matches("and")) {
                    codigo += " goto L" + actualEntrar + "\ngoto L" + salirIf + "\nL" + actualEntrar + ": ";
                    actualEntrar++;
                } else {
                    codigo += tok.getLexema() + " ";
                }
            }
        }
        for (NodoArbol hijo : nodo.getHijos()) {
            revisar(hijo);
        }
        codigo += "L" + salirIf + ":\n";
    }

    private void asignacion(NodoArbol nodo) {
        String codAsign = "";
        String var = "";
        boolean band = false;
        for (Token tok : nodo.getProd().getTokens()) {
            if (!var.isEmpty()) {
                if (!tok.getLexema().matches(";")) {
                    codAsign += tok.getLexema();
                    if (tok.getLexema().matches(var)) {
                        band = true;
                    }
                }
            } else {
                codAsign += tok.getLexema();
                var = tok.getLexema();
            }
        }
        if (band) {
            codAsign = separarAsignacion(nodo.getProd().getTokens());
        }
        codigo += codAsign + "\n";
    }

    private String separarAsignacion(List<Token> tokens) {
        String codAsign = "";
        int number = 0;
        boolean band = false;
        boolean band2 = false;
        codAsign += "t" + temp + " = ";
        for (Token tok : tokens) {
            if (tok.getLexema().matches("=")) {
                band = true;
            } else if (band) {
                if (!tok.getLexema().matches(";")) {
                    codAsign += tok.getLexema();
                    number++;
                    if (number == 3 && !band2) {
                        band2 = true;
                        number = 0;
                    } else if (band2) {
                        temp++;
                        codAsign += "\nt" + temp + " = t" + (temp - 1);
                        band2 = false;
                    } else if (number == 2) {
                        band2 = true;
                    }
                } else {
                    break;
                }
            }
        }
        codAsign += "\n" + tokens.get(0).getLexema() + " = t" + temp;
        temp++;
        return codAsign;
    }

}
