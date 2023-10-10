package compilador;

import apoyo.CustomOutput;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.table.DefaultTableModel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author julio
 */
public class MainClass {

    private NodoArbol raiz;
    private ArrayList<Token> tokens;
    PrintStream output;
    private final Sintactico sintax;
    private final Lexico lexical = new Lexico();
    private final Semantico semantic = new Semantico();
    private ArrayList<Produccion> producciones = new ArrayList();
    private boolean error = false;

    public MainClass(JTree Arbol) {
        this.sintax = new Sintactico(Arbol);
        tokens = new ArrayList();
    }

    public void compile(String codigo, JTable lexica, JList listaErrors, JList Resultados, JTextArea sintactica) {
        output = new PrintStream(new CustomOutput(sintactica));
        lexicalAnalysis(codigo, lexica);
        syntacticAnalysis(listaErrors, Resultados, sintactica);
        if (!error) {
            semanticAnalysis(listaErrors);
        }
    }

    private void lexicalAnalysis(String codigo, JTable lexica) {
        tokens = lexical.AnalizarCodigo(codigo);
        DefaultTableModel modelo = (DefaultTableModel) lexica.getModel();
        int a = modelo.getRowCount();
        for (int i = 0; i < a; i++) {
            modelo.removeRow(0);
        }
        Object[] rowData = new Object[6];
        for (Token p : tokens) {
            rowData[0] = p.getLexema();
            rowData[1] = p.getClave();
            rowData[2] = (p.getFila() + 1) + ", " + p.getColumna();
            modelo.addRow(rowData);
        }
        lexica.setModel(modelo);

    }

    private void syntacticAnalysis(JList listaErrores, JList Resultados, JTextArea sintactica) {
        DefaultListModel<String> modelE = new DefaultListModel();
        DefaultListModel<String> modelR = new DefaultListModel();
        try {

            producciones = sintax.parseProgram(tokens);
            raiz=sintax.getArbol();
            modelR.addElement("Producciones realizadas correctamente.");
            modelR.addElement("Procediendo con el analisis semantico...");
            modelE.addElement("");
            for (Produccion aux : producciones) {
                sintactica.append("..................................\n");
                sintactica.append(aux.toString() + "\n");
            }
        } catch (Exception ex) {
            modelE.addElement("Error: " + ex.getMessage());
            modelR.addElement("Errores en las producciones, revisar el panel de errores");
            modelR.addElement("Imposible realizar el analisis semantico...");
            modelR.addElement("Finalizando ejecucion");
            error = true;
        }
        Resultados.setModel(modelR);
        listaErrores.setModel(modelE);
    }

    private void semanticAnalysis(JList listaE) {
        semantic.semanticalAnalisys(raiz);
        DefaultListModel<String> model = (DefaultListModel)listaE.getModel();
        for(String err: semantic.getErrors()){
            model.addElement(err);
        }
    }
}
