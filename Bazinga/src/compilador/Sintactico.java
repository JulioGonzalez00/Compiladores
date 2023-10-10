/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compilador;

/**
 *
 * @author julio
 */
import java.util.ArrayList;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class Sintactico {

    private final JTree Arbol;
    private NodoArbol raiz;
    private NodoArbol nodoActual;
    private ArrayList<Token> tokens;
    private int currentTokenIndex;
    private final ArrayList<Produccion> arrayProduccion = new ArrayList<>();
    private final ArrayList<Produccion> arrayProduccionCompletadas = new ArrayList<>();

    public Sintactico(JTree Arbol) {
        this.Arbol = Arbol;
    }

    public ArrayList<Produccion> parseProgram(ArrayList<Token> tokens) {
        this.tokens = tokens;
        this.raiz = new NodoArbol("Program");
        this.nodoActual = raiz;
        this.currentTokenIndex = 0;
        arrayProduccion.add(new Produccion("Program"));
        match("program");
        match("{");
        parseListDecls();
        parseListSents();
        match("}");
        arrayProduccionCompletadas.add(arrayProduccion.get(arrayProduccion.size() - 1));
        nodoActual.setProd(arrayProduccion.get(arrayProduccion.size() - 1));
        arrayProduccion.remove(arrayProduccion.size() - 1);
        construirArbol();
        return arrayProduccionCompletadas;
    }

    private void parseListDecls() {
        while (currentTokenIndex < tokens.size()
                && (tokens.get(currentTokenIndex).getLexema().equals("int")
                || tokens.get(currentTokenIndex).getLexema().equals("float")
                || tokens.get(currentTokenIndex).getLexema().equals("bool"))) {
            parseDecl();
        }
    }

    private void parseDecl() {
        NodoArbol aux = new NodoArbol("Declaracion", nodoActual);
        nodoActual.setHijo(aux);
        nodoActual = aux;
        arrayProduccion.add(new Produccion("Declaracion"));
        parseTipo();
        parseListaId();
        match(";");
        arrayProduccionCompletadas.add(arrayProduccion.get(arrayProduccion.size() - 1));
        nodoActual.setProd(arrayProduccion.get(arrayProduccion.size() - 1));
        arrayProduccion.remove(arrayProduccion.size() - 1);
        nodoActual = nodoActual.getPadre();
    }

    private void parseTipo() {
        if (tokens.get(currentTokenIndex).getLexema().equals("int")
                || tokens.get(currentTokenIndex).getLexema().equals("float")
                || tokens.get(currentTokenIndex).getLexema().equals("bool")) {
            for (Produccion item : arrayProduccion) {
                item.getTokens().add(tokens.get(currentTokenIndex));
            }
            currentTokenIndex++;
        } else {
            throw new RuntimeException("Error de sintaxis: se esperaba un tipo");
        }
    }

    private void parseListaId() {
        parseId();
        while (currentTokenIndex < tokens.size() && tokens.get(currentTokenIndex).getLexema().equals(",")) {
            match(",");
            parseId();
        }
    }

    private void parseId() {
        if (currentTokenIndex < tokens.size() && !isKeyword(tokens.get(currentTokenIndex).getLexema())) {
            for (Produccion item : arrayProduccion) {
                item.getTokens().add(tokens.get(currentTokenIndex));
            }
            currentTokenIndex++;
        } else {
            throw new RuntimeException("Error de sintaxis: se esperaba un identificador");
        }
    }

    private boolean isKeyword(String token) {
        return token.equals("int") || token.equals("float") || token.equals("bool")
                || token.equals("if") || token.equals("else") || token.equals("while")
                || token.equals("do") || token.equals("until") || token.equals("read")
                || token.equals("write") || token.equals("break") || token.equals("true")
                || token.equals("false") || token.equals("not") || token.equals("or")
                || token.equals("and") || token.equals("==") || token.equals("!=")
                || token.equals("<") || token.equals("<=") || token.equals(">") || token.equals(">=")
                || token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/")
                || token.equals("(") || token.equals(")") || token.equals("{") || token.equals("}")
                || token.equals(";") || token.equals("=");
    }

    private void parseListSents() {
        while (currentTokenIndex < tokens.size() && (tokens.get(currentTokenIndex).getLexema().equals("if")
                || tokens.get(currentTokenIndex).getLexema().equals("while")
                || tokens.get(currentTokenIndex).getLexema().equals("do")
                || tokens.get(currentTokenIndex).getLexema().equals("read")
                || tokens.get(currentTokenIndex).getLexema().equals("write")
                || tokens.get(currentTokenIndex).getLexema().equals("{")
                || !isKeyword(tokens.get(currentTokenIndex).getLexema()))) {
            parseSent();
        }
    }

    private void parseSent() {
        if (tokens.get(currentTokenIndex).getLexema().equals("if")) {
            parseSentIf();
        } else if (tokens.get(currentTokenIndex).getLexema().equals("while")) {
            parseSentWhile();
        } else if (tokens.get(currentTokenIndex).getLexema().equals("do")) {
            parseSentDo();
        } else if (tokens.get(currentTokenIndex).getLexema().equals("write")) {
            parseSentWrite();
        } else if (tokens.get(currentTokenIndex).getLexema().equals("{")) {
            parseBloque();
        } else if (!isKeyword(tokens.get(currentTokenIndex).getLexema())) {
            parseSentAssign();
        } else {
            throw new RuntimeException("Error de sintaxis: sentencia no válida");
        }
    }

    private void parseSentIf() {
        NodoArbol aux = new NodoArbol("If", nodoActual);
        nodoActual.setHijo(aux);
        nodoActual = aux;
        arrayProduccion.add(new Produccion("If"));
        match("if");
        match("(");
        parseExpBool();
        match(")");
        match("then");
        parseBloque();
        if (currentTokenIndex < tokens.size() && tokens.get(currentTokenIndex).getLexema().equals("else")) {
            match("else");
            parseBloque();
        }
        match("fi");
        arrayProduccionCompletadas.add(arrayProduccion.get(arrayProduccion.size() - 1));
        nodoActual.setProd(arrayProduccion.get(arrayProduccion.size() - 1));
        arrayProduccion.remove(arrayProduccion.size() - 1);
        nodoActual = nodoActual.getPadre();
    }

    private void parseSentWhile() {
        NodoArbol aux = new NodoArbol("While", nodoActual);
        nodoActual.setHijo(aux);
        nodoActual = aux;
        arrayProduccion.add(new Produccion("While"));
        match("while");
        match("(");
        parseExpBool();
        match(")");
        parseBloque();
        arrayProduccionCompletadas.add(arrayProduccion.get(arrayProduccion.size() - 1));
        nodoActual.setProd(arrayProduccion.get(arrayProduccion.size() - 1));
        arrayProduccion.remove(arrayProduccion.size() - 1);
        nodoActual = nodoActual.getPadre();
    }

    private void parseSentDo() {
        NodoArbol aux = new NodoArbol("Do", nodoActual);
        nodoActual.setHijo(aux);
        nodoActual = aux;
        arrayProduccion.add(new Produccion("Do"));
        match("do");
        parseBloque();
        match("until");
        match("(");
        parseExpBool();
        match(")");
        match(";");
        arrayProduccionCompletadas.add(arrayProduccion.get(arrayProduccion.size() - 1));
        nodoActual.setProd(arrayProduccion.get(arrayProduccion.size() - 1));
        arrayProduccion.remove(arrayProduccion.size() - 1);
        nodoActual = nodoActual.getPadre();
    }

    private void parseSentWrite() {
        NodoArbol aux = new NodoArbol("Write", nodoActual);
        nodoActual.setHijo(aux);
        nodoActual = aux;
        arrayProduccion.add(new Produccion("Write"));
        match("write");
        parseExpBool();
        match(";");
        arrayProduccionCompletadas.add(arrayProduccion.get(arrayProduccion.size() - 1));
        nodoActual.setProd(arrayProduccion.get(arrayProduccion.size() - 1));
        arrayProduccion.remove(arrayProduccion.size() - 1);
        nodoActual = nodoActual.getPadre();
    }

    private void parseBloque() {
        match("{");
        parseListSents();
        match("}");
    }

    private void parseSentAssign() {
        NodoArbol aux = new NodoArbol("Asignación", nodoActual);
        nodoActual.setHijo(aux);
        nodoActual = aux;
        arrayProduccion.add(new Produccion("Asignación"));
        parseId();
        match("=");
        parseExpBool();
        match(";");
        arrayProduccionCompletadas.add(arrayProduccion.get(arrayProduccion.size() - 1));
        nodoActual.setProd(arrayProduccion.get(arrayProduccion.size() - 1));
        arrayProduccion.remove(arrayProduccion.size() - 1);
        nodoActual = nodoActual.getPadre();
    }

    private void parseExpBool() {
        parseComb();
        while (currentTokenIndex < tokens.size() && tokens.get(currentTokenIndex).getLexema().equals("or")) {
            match("or");
            parseComb();
        }
    }

    private void parseComb() {
        parseIgualdad();
        while (currentTokenIndex < tokens.size() && tokens.get(currentTokenIndex).getLexema().equals("and")) {
            match("and");
            parseIgualdad();
        }
    }

    private void parseIgualdad() {
        parseRel();
        while (currentTokenIndex < tokens.size() && (tokens.get(currentTokenIndex).getLexema().equals("==") || tokens.get(currentTokenIndex).getLexema().equals("!="))) {
            if (tokens.get(currentTokenIndex).getLexema().equals("==")) {
                match("==");
            } else {
                match("!=");
            }
            parseRel();
        }
    }

    private void parseRel() {
        parseExpr();
        while (currentTokenIndex < tokens.size() && (tokens.get(currentTokenIndex).getLexema().equals("<") || tokens.get(currentTokenIndex).getLexema().equals(">=") || tokens.get(currentTokenIndex).getLexema().equals("<=")
                || tokens.get(currentTokenIndex).getLexema().equals(">") || tokens.get(currentTokenIndex).getLexema().equals("=="))) {
            match(tokens.get(currentTokenIndex).getLexema());
            parseExpr();
        }
    }

    private void parseExpr() {
        parseTerm();
        while (currentTokenIndex < tokens.size() && (tokens.get(currentTokenIndex).getLexema().equals("+") || tokens.get(currentTokenIndex).getLexema().equals("-"))) {
            if (tokens.get(currentTokenIndex).getLexema().equals("+")) {
                match("+");
            } else {
                match("-");
            }
            parseTerm();
        }
    }

    private void parseTerm() {
        parseUnario();
        while (currentTokenIndex < tokens.size() && (tokens.get(currentTokenIndex).getLexema().equals("*") || tokens.get(currentTokenIndex).getLexema().equals("/"))) {
            if (tokens.get(currentTokenIndex).getLexema().equals("*")) {
                match("*");
            } else {
                match("/");
            }
            parseUnario();
        }
    }

    private void parseUnario() {
        if (tokens.get(currentTokenIndex).getLexema().equals("not") || tokens.get(currentTokenIndex).getLexema().equals("-")) {
            match(tokens.get(currentTokenIndex).getLexema());
            parseUnario();
        } else {
            parseFactor();
        }
    }

    private void parseFactor() {
        if (tokens.get(currentTokenIndex).getLexema().equals("(")) {
            match("(");
            parseExpBool();
            match(")");
        } else {
            parseIdOrLiteral();
        }
    }

    private void parseIdOrLiteral() {
        if (!isKeyword(tokens.get(currentTokenIndex).getLexema())) {
            parseId();
        } else if (tokens.get(currentTokenIndex).getLexema().equals("true") || tokens.get(currentTokenIndex).getLexema().equals("false")) {
            for (Produccion item : arrayProduccion) {
                item.getTokens().add(tokens.get(currentTokenIndex));
            }
            currentTokenIndex++;
        } else {
            throw new RuntimeException("Error de sintaxis: se esperaba un identificador o literal");
        }
    }

    private void match(String expectedToken) {
        if (currentTokenIndex < tokens.size() && tokens.get(currentTokenIndex).getLexema().equals(expectedToken)) {
            for (Produccion item : arrayProduccion) {
                item.getTokens().add(tokens.get(currentTokenIndex));
            }
            currentTokenIndex++;
        } else {
            throw new RuntimeException("Error de sintaxis: se esperaba '" + expectedToken + "'");
        }
    }

    private void construirArbol() {
        DefaultMutableTreeNode nodo1 = new DefaultMutableTreeNode(this.raiz.getNombre());
        agregarHijos(nodo1, raiz);
        this.Arbol.setModel(new DefaultTreeModel(nodo1));
    }

    private void agregarHijos(DefaultMutableTreeNode nodo, NodoArbol padre) {
        ArrayList<NodoArbol> hijos = padre.getHijos();
        for (NodoArbol aux : hijos) {
            DefaultMutableTreeNode nodoi = new DefaultMutableTreeNode(aux.getNombre());
            agregarHijos(nodoi, aux);
            nodo.add(nodoi);
        }
    }

    NodoArbol getArbol() {
        return raiz;
    }
}
