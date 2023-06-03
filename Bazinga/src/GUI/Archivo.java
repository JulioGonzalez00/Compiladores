/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;

/**
 *
 * @author julio
 */
public class Archivo extends javax.swing.JPanel implements ActionListener {

    //variables para la zona el texto.
    private RSyntaxTextArea textArea;
    private RTextScrollPane sp;
    private JTextField searchField;
    private JTextField replaceField;
    private JCheckBox regexCB;
    private JCheckBox matchCaseCB;
    JToolBar toolBar;

    private String name = "Nuevo archivo";
    private String ruta;
    private boolean finding = false;

    //Variables para abrir y escribir archivos
    FileReader fr = null;
    BufferedReader br = null;
    FileWriter fichero = null;
    PrintWriter pw = null;

    public Archivo() {
        initComponents();
        initPane();
        ruta = null;
    }

    /**
     * Creates new form Archivo
     *
     * @param file
     */
    public Archivo(File file) {
        initComponents();
        initPane();
        name = file.getName();
        abrir(file);
    }

    private void initPane() {
        textArea = new RSyntaxTextArea();
        textArea.setCodeFoldingEnabled(true);
        //textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        try {
            AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance();
            atmf.putMapping("text/sCooper","compilador.LexerColor");
            textArea.setSyntaxEditingStyle("text/sCooper");
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
        }
        sp = new RTextScrollPane();
        textArea.setForeground(Color.white);
        textArea.setBackground(new Color(55, 55, 55));
        textArea.setFont(new Font("Dialog", Font.PLAIN, 17));
        textArea.setCurrentLineHighlightColor(new Color(135, 80, 142));
        sp = new RTextScrollPane(textArea);
        this.add(sp);
        initFind();
    }

    private void initFind() {
        toolBar = new JToolBar();
        searchField = new JTextField(30);
        TextPrompt placeholder = new TextPrompt("Buscar", searchField);
        placeholder.changeAlpha(0.75f);
        placeholder.changeStyle(Font.ITALIC);
        toolBar.add(searchField);
        replaceField = new JTextField(30);

        TextPrompt placeholder2 = new TextPrompt("Remplazar", replaceField);
        placeholder2.changeAlpha(0.75f);
        placeholder2.changeStyle(Font.ITALIC);
        toolBar.add(replaceField);
        final JButton nextButton = new JButton("Buscar siguiente.");
        nextButton.setActionCommand("FindNext");
        nextButton.addActionListener(this);
        toolBar.add(nextButton);
        searchField.addActionListener((ActionEvent e) -> {
            nextButton.doClick(0);
        });
        JButton prevButton = new JButton("Buscar Anterior");
        prevButton.setActionCommand("FindPrev");
        prevButton.addActionListener(this);
        toolBar.add(prevButton);
        final JButton replaceButton = new JButton("Remplazar");
        replaceButton.setActionCommand("Replace");
        replaceButton.addActionListener(this);
        toolBar.add(replaceButton);
        regexCB = new JCheckBox("Regex");
        toolBar.add(regexCB);
        matchCaseCB = new JCheckBox("Match Case");
        toolBar.add(matchCaseCB);
    }

    public String getName() {
        return name;
    }

    public void cerrar() {
        this.textArea.setText("");
        try {
            ruta = null;
            fr.close();
        } catch (IOException ex2) {
            System.err.println("Error: " + ex2.getMessage());
        }
    }

    private void abrir(File file) {
        try {
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            this.textArea.setText("");
            String linea;
            while ((linea = br.readLine()) != null) {
                this.textArea.append(linea + "\n");
            }
            ruta = file.getAbsolutePath();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al abrir el archivo:\n" + ex.getMessage());
            this.textArea.setText("");
            try {
                fr.close();
            } catch (IOException ex2) {
                System.err.println("Error: " + ex2.getMessage());
            }
        }
    }

    public String guardar() {
        String nombre = null;
        try {
            if (ruta == null) {
                JFileChooser KFC = new JFileChooser();
                FileFilter filter = new FileNameExtensionFilter("TXT File", "txt");
                KFC.setFileFilter(filter);
                int ok = KFC.showSaveDialog(this);
                if (ok == JFileChooser.APPROVE_OPTION) {
                    File file = KFC.getSelectedFile();
                    if (file.getName().endsWith(".txt")) {
                        ruta = file.getPath();
                        nombre = file.getName();
                    } else {
                        ruta = file.getPath() + ".txt";
                        nombre = file.getName() + ".txt";
                    }
                }
            }
            fichero = new FileWriter(ruta);
            pw = new PrintWriter(fichero);
            String text = textArea.getText();
            pw.print(text);
            JOptionPane.showMessageDialog(this, "Documento guardado.");
        } catch (IOException ex) {
            System.err.println("Error: " + ex.getMessage());
        } finally {
            try {
                if (null != fichero) {
                    fichero.close();
                }
            } catch (IOException e2) {
                System.err.println("Error: " + e2.getMessage());
            }
        }
        return nombre;
    }

    public void buscar() {
        finding = !finding;
        if (finding) {
            this.add(toolBar, BorderLayout.SOUTH);
            this.validate();
        } else {
            this.remove(toolBar);
            this.validate();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Boolean forward = null;
        switch (e.getActionCommand()) {
            case "FindNext":
                forward = true;
                break;
            case "FindPrev":
                forward = false;
                break;
            case "Replace":
                forward = null;
                break;
        }
        SearchContext context = new SearchContext();
        String text = searchField.getText();
        String text2 = replaceField.getText();
        if (text.length() == 0) {
            return;
        }
        context.setSearchFor(text);
        context.setMatchCase(matchCaseCB.isSelected());
        context.setRegularExpression(regexCB.isSelected());
        context.setWholeWord(false);
        if (forward != null) {
            context.setSearchForward(forward);
            boolean found = SearchEngine.find(textArea, context).wasFound();
            if (!found) {
                JOptionPane.showMessageDialog(this, "Text not found");
            }
        } else {
            if (text2.length() == 0) {
                return;
            }
            context.setReplaceWith(text2);
            boolean found = SearchEngine.replace(textArea, context).wasFound();
            if (!found) {
                JOptionPane.showMessageDialog(this, "Text not found");
            }
        }

    }

    public String getRuta() {
        return this.ruta;
    }

    public String getText() {
        return textArea.getText();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
