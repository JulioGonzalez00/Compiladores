/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Vector;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

/**
 *
 * @author julio
 */
public class Principal extends javax.swing.JFrame {

    private DefaultTreeModel tm;
    String rutaC = null;

    Vector<Archivo> archivos = new Vector();
    private final ImageIcon icon = new ImageIcon(getClass().getResource("/recursos/icon/Icono.png"));
    private final ImageIcon tabIcon = new ImageIcon(getClass().getResource("/recursos/iconos/tabIcon.png"));

    /**
     * Creates new form Principal
     */
    public Principal() {
        initComponents();
        this.setTitle("Bazinga!");
        this.setIconImage(icon.getImage());
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        cargar_arbol();
        initPanes();
    }

    private void initPanes() {
        Herramientas.setVisible(false);
        this.jTreeDirectorio.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTreeDirectorio.getLastSelectedPathComponent();
                    if (node != null) {
                        if (node.getChildCount() == 0) {
                            String ruta = "";
                            TreeNode[] path = node.getPath();
                            for (int i = 1; i < path.length; i++) {
                                if (i < path.length - 1) {
                                    ruta += path[i].toString() + "\\";
                                } else {
                                    ruta += path[i].toString();
                                }
                            }
                            try {
                                File archivo = new File(ruta);
                                archivos.add(new Archivo(archivo));
                                jTabbedPaneCentro.add(archivo.getName(), archivos.lastElement());
                            } catch (Exception ex) {
                                System.err.println("Error: " + ex.getMessage());
                            }
                        }
                    }
                }
            }
        });
    }

    private void cargar_arbol() {
        if (rutaC == null) {
            tm = new DefaultTreeModel(new DefaultMutableTreeNode("Ningun directorio abierto."));
            this.jTreeDirectorio.setModel(tm);
        } else {
            String separator = "\\";
            String[] valores = rutaC.split(Pattern.quote(separator));
            DefaultMutableTreeNode nodo1 = new DefaultMutableTreeNode("Ruta");
            DefaultMutableTreeNode nodoi = nodo1;
            for (String valor : valores) {
                DefaultMutableTreeNode nodoaux = new DefaultMutableTreeNode(valor);
                nodoi.add(nodoaux);
                nodoi = nodoaux;
            }
            File carpeta = new File(rutaC);
            if (carpeta.exists()) {
                File[] files = carpeta.listFiles();
                for (File file : files) {
                    String ext = file.getName().substring(file.getName().lastIndexOf('.') + 1);
                    if (ext.matches("txt")) {
                        DefaultMutableTreeNode nodoaux = new DefaultMutableTreeNode(file.getName());
                        nodoi.add(nodoaux);
                    }
                }
            }
            this.jTreeDirectorio.setModel(new DefaultTreeModel(nodo1));
        }
    }

    public void setActive(int index) {
        this.jTabbedPaneCentro.setSelectedIndex(index);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Lienzo = new javax.swing.JPanel();
        Arriba = new javax.swing.JPanel();
        Herramientas = new javax.swing.JPanel();
        Centro = new javax.swing.JPanel();
        jTabbedPaneCentro = new javax.swing.JTabbedPane();
        Abajo = new javax.swing.JPanel();
        jTabbedPaneAbajo = new javax.swing.JTabbedPane();
        jPanelResultados = new javax.swing.JPanel();
        jScrollPaneResultados = new javax.swing.JScrollPane();
        jListResultados = new javax.swing.JList<>();
        jPanelErrores = new javax.swing.JPanel();
        jScrollPaneErrores = new javax.swing.JScrollPane();
        jListErrores = new javax.swing.JList<>();
        Derecha = new javax.swing.JPanel();
        jTabbedPaneDerecha = new javax.swing.JTabbedPane();
        jPanelLexico = new javax.swing.JPanel();
        jScrollPaneLexico = new javax.swing.JScrollPane();
        jListLexico = new javax.swing.JList<>();
        jPanelSintactico = new javax.swing.JPanel();
        jScrollPaneSintactico = new javax.swing.JScrollPane();
        jListSintactico = new javax.swing.JList<>();
        jPanelSemantico = new javax.swing.JPanel();
        jScrollPaneSemantico = new javax.swing.JScrollPane();
        jListSemantico = new javax.swing.JList<>();
        jPanelIntermedio = new javax.swing.JPanel();
        jScrollPaneIntermedio = new javax.swing.JScrollPane();
        jListIntermedio = new javax.swing.JList<>();
        Izquierda = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTreeDirectorio = new javax.swing.JTree();
        MenuBar = new javax.swing.JMenuBar();
        jMenuArchivo = new javax.swing.JMenu();
        jMenuItemNuevo = new javax.swing.JMenuItem();
        jMenuItemAbrir = new javax.swing.JMenuItem();
        jMenuItemCerrar = new javax.swing.JMenuItem();
        jMenuItemAbrirC = new javax.swing.JMenuItem();
        jMenuItemCerrarC = new javax.swing.JMenuItem();
        jMenuItemGuardar = new javax.swing.JMenuItem();
        jMenuEditar = new javax.swing.JMenu();
        jMenuItemFind = new javax.swing.JMenuItem();
        jMenuNavegar = new javax.swing.JMenu();
        jMenuItemArchivo = new javax.swing.JMenuItem();
        jMenuCompilar = new javax.swing.JMenu();
        jMenuItemCorrer = new javax.swing.JMenuItem();
        jMenuItemCompilar = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(800, 500));

        Lienzo.setLayout(new java.awt.BorderLayout());

        Arriba.setMaximumSize(new java.awt.Dimension(2147483647, 200));
        Arriba.setLayout(new java.awt.BorderLayout());

        Herramientas.setMaximumSize(new java.awt.Dimension(32767, 50));
        Herramientas.setMinimumSize(new java.awt.Dimension(0, 50));
        Herramientas.setPreferredSize(new java.awt.Dimension(1200, 50));

        javax.swing.GroupLayout HerramientasLayout = new javax.swing.GroupLayout(Herramientas);
        Herramientas.setLayout(HerramientasLayout);
        HerramientasLayout.setHorizontalGroup(
            HerramientasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1200, Short.MAX_VALUE)
        );
        HerramientasLayout.setVerticalGroup(
            HerramientasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        Arriba.add(Herramientas, java.awt.BorderLayout.CENTER);

        Lienzo.add(Arriba, java.awt.BorderLayout.PAGE_START);

        Centro.setOpaque(false);
        Centro.setLayout(new java.awt.BorderLayout());
        Centro.add(jTabbedPaneCentro, java.awt.BorderLayout.CENTER);

        Lienzo.add(Centro, java.awt.BorderLayout.CENTER);

        Abajo.setMaximumSize(new java.awt.Dimension(32767, 160));
        Abajo.setMinimumSize(new java.awt.Dimension(0, 160));
        Abajo.setPreferredSize(new java.awt.Dimension(1200, 160));
        Abajo.setLayout(new java.awt.BorderLayout());

        jPanelResultados.setLayout(new java.awt.BorderLayout());

        jListResultados.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Resultado 1", "Resultado 2", "Resultado 3", "Conclusion todo jala." };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPaneResultados.setViewportView(jListResultados);

        jPanelResultados.add(jScrollPaneResultados, java.awt.BorderLayout.CENTER);

        jTabbedPaneAbajo.addTab("Resultados", jPanelResultados);

        jPanelErrores.setLayout(new java.awt.BorderLayout());

        jListErrores.setForeground(new java.awt.Color(204, 0, 51));
        jListErrores.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Error 1", "Error 2", "Error 3", " " };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPaneErrores.setViewportView(jListErrores);

        jPanelErrores.add(jScrollPaneErrores, java.awt.BorderLayout.CENTER);

        jTabbedPaneAbajo.addTab("Errores", jPanelErrores);

        Abajo.add(jTabbedPaneAbajo, java.awt.BorderLayout.CENTER);

        Lienzo.add(Abajo, java.awt.BorderLayout.PAGE_END);

        Derecha.setMaximumSize(new java.awt.Dimension(350, 32767));
        Derecha.setMinimumSize(new java.awt.Dimension(350, 0));
        Derecha.setPreferredSize(new java.awt.Dimension(350, 478));
        Derecha.setLayout(new java.awt.BorderLayout());

        jPanelLexico.setLayout(new java.awt.BorderLayout());

        jListLexico.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Lexico 1", "Lexico 2" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPaneLexico.setViewportView(jListLexico);

        jPanelLexico.add(jScrollPaneLexico, java.awt.BorderLayout.CENTER);

        jTabbedPaneDerecha.addTab("Lexico", jPanelLexico);

        jPanelSintactico.setLayout(new java.awt.BorderLayout());

        jListSintactico.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Sintactico 1", "Sintactico 2", "Sintactico 3" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPaneSintactico.setViewportView(jListSintactico);

        jPanelSintactico.add(jScrollPaneSintactico, java.awt.BorderLayout.CENTER);

        jTabbedPaneDerecha.addTab("Sintactico", jPanelSintactico);

        jPanelSemantico.setLayout(new java.awt.BorderLayout());

        jListSemantico.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Semantico 1", "Semantico 2", "Semantico 3", "Semantico 4", " " };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPaneSemantico.setViewportView(jListSemantico);

        jPanelSemantico.add(jScrollPaneSemantico, java.awt.BorderLayout.CENTER);

        jTabbedPaneDerecha.addTab("Semantico", jPanelSemantico);

        jPanelIntermedio.setLayout(new java.awt.BorderLayout());

        jListIntermedio.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Intermedio 1", "Intermedio 2", "Intermedio 3", "Intermedio 4", "Intermedio 5", " " };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPaneIntermedio.setViewportView(jListIntermedio);

        jPanelIntermedio.add(jScrollPaneIntermedio, java.awt.BorderLayout.CENTER);

        jTabbedPaneDerecha.addTab("Codigo Intermedio", jPanelIntermedio);

        Derecha.add(jTabbedPaneDerecha, java.awt.BorderLayout.CENTER);

        Lienzo.add(Derecha, java.awt.BorderLayout.LINE_END);

        Izquierda.setMinimumSize(new java.awt.Dimension(150, 450));
        Izquierda.setPreferredSize(new java.awt.Dimension(150, 450));
        Izquierda.setLayout(new java.awt.BorderLayout());

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        javax.swing.tree.DefaultMutableTreeNode treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("ruta");
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("hola");
        javax.swing.tree.DefaultMutableTreeNode treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("hola");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        jTreeDirectorio.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jScrollPane1.setViewportView(jTreeDirectorio);

        Izquierda.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        Lienzo.add(Izquierda, java.awt.BorderLayout.LINE_START);

        getContentPane().add(Lienzo, java.awt.BorderLayout.CENTER);

        jMenuArchivo.setText("Archivo");

        jMenuItemNuevo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.SHIFT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItemNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/iconos/new_file.png"))); // NOI18N
        jMenuItemNuevo.setText("Nuevo");
        jMenuItemNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemNuevoActionPerformed(evt);
            }
        });
        jMenuArchivo.add(jMenuItemNuevo);

        jMenuItemAbrir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.SHIFT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItemAbrir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/iconos/tabIcon.png"))); // NOI18N
        jMenuItemAbrir.setText("Abrir");
        jMenuItemAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAbrirActionPerformed(evt);
            }
        });
        jMenuArchivo.add(jMenuItemAbrir);

        jMenuItemCerrar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.SHIFT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItemCerrar.setText("Cerrar");
        jMenuItemCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCerrarActionPerformed(evt);
            }
        });
        jMenuArchivo.add(jMenuItemCerrar);

        jMenuItemAbrirC.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.SHIFT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItemAbrirC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/iconos/open_Folder.png"))); // NOI18N
        jMenuItemAbrirC.setText("Abrir Carpeta");
        jMenuItemAbrirC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAbrirCActionPerformed(evt);
            }
        });
        jMenuArchivo.add(jMenuItemAbrirC);

        jMenuItemCerrarC.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.SHIFT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItemCerrarC.setText("Cerrar Carpeta");
        jMenuItemCerrarC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCerrarCActionPerformed(evt);
            }
        });
        jMenuArchivo.add(jMenuItemCerrarC);

        jMenuItemGuardar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItemGuardar.setText("Guardar");
        jMenuItemGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemGuardarActionPerformed(evt);
            }
        });
        jMenuArchivo.add(jMenuItemGuardar);

        MenuBar.add(jMenuArchivo);

        jMenuEditar.setText("Editar");

        jMenuItemFind.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.SHIFT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItemFind.setText("Buscar");
        jMenuItemFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemFindActionPerformed(evt);
            }
        });
        jMenuEditar.add(jMenuItemFind);

        MenuBar.add(jMenuEditar);

        jMenuNavegar.setText("Navegar");
        jMenuNavegar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jMenuItemArchivo.setText("Ir al archivo...");
        jMenuItemArchivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemArchivoActionPerformed(evt);
            }
        });
        jMenuNavegar.add(jMenuItemArchivo);

        MenuBar.add(jMenuNavegar);

        jMenuCompilar.setText("Compilar");

        jMenuItemCorrer.setText("Correr");
        jMenuItemCorrer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCorrerActionPerformed(evt);
            }
        });
        jMenuCompilar.add(jMenuItemCorrer);

        jMenuItemCompilar.setText("Compilar");
        jMenuItemCompilar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCompilarActionPerformed(evt);
            }
        });
        jMenuCompilar.add(jMenuItemCompilar);

        MenuBar.add(jMenuCompilar);

        setJMenuBar(MenuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAbrirActionPerformed
        JFileChooser KFC = new JFileChooser();
        FileFilter filter = new FileNameExtensionFilter("TXT File", "txt");
        KFC.setFileFilter(filter);
        int respuesta = KFC.showOpenDialog(this);
        int band = -1;
        if (respuesta == JFileChooser.APPROVE_OPTION) {
            File file = KFC.getSelectedFile();
            String nombre = file.getName();
            for (int i = 0; i < archivos.size(); i++) {
                if (archivos.get(i).getName().matches(nombre)) {
                    band = i;
                }
            }
            if (band == -1) {
                archivos.add(new Archivo(file));
                this.jTabbedPaneCentro.addTab(KFC.getSelectedFile().getName(), tabIcon, archivos.lastElement());
                this.setActive(archivos.size() - 1);
            } else {
                jTabbedPaneCentro.setSelectedComponent(archivos.get(band));
            }
        }
    }//GEN-LAST:event_jMenuItemAbrirActionPerformed

    private void jMenuItemCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCerrarActionPerformed
        try {
            int sel = this.jTabbedPaneCentro.getSelectedIndex();
            this.jTabbedPaneCentro.remove(sel);
            this.archivos.get(sel).cerrar();
            this.archivos.remove(sel);
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }//GEN-LAST:event_jMenuItemCerrarActionPerformed

    private void jMenuItemGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemGuardarActionPerformed
        try {
            int sel = this.jTabbedPaneCentro.getSelectedIndex();
            String nombre = this.archivos.get(sel).guardar();
            if (nombre != null) {
                this.jTabbedPaneCentro.setTitleAt(sel, nombre);
            }
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }//GEN-LAST:event_jMenuItemGuardarActionPerformed

    private void jMenuItemAbrirCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAbrirCActionPerformed
        JFileChooser KFC = new JFileChooser();
        KFC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int respuesta = KFC.showOpenDialog(this);
        if (respuesta == JFileChooser.APPROVE_OPTION) {
            rutaC = KFC.getSelectedFile().getPath();
            this.cargar_arbol();
            JOptionPane.showMessageDialog(this, "Carpeta abierta.");
        }
    }//GEN-LAST:event_jMenuItemAbrirCActionPerformed

    private void jMenuItemCerrarCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCerrarCActionPerformed
        rutaC = null;
        cargar_arbol();
    }//GEN-LAST:event_jMenuItemCerrarCActionPerformed

    private void jMenuItemFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemFindActionPerformed
        try {
            int sel = this.jTabbedPaneCentro.getSelectedIndex();
            this.archivos.get(sel).buscar();
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }//GEN-LAST:event_jMenuItemFindActionPerformed

    private void jMenuItemNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemNuevoActionPerformed
        archivos.add(new Archivo());
        this.jTabbedPaneCentro.add("Nuevo archivo*", archivos.lastElement());
    }//GEN-LAST:event_jMenuItemNuevoActionPerformed

    private void jMenuItemCorrerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCorrerActionPerformed
        int sel = this.jTabbedPaneCentro.getSelectedIndex();
        //Compilador comp = new Compilador(this.archivos.get(sel).getRuta(),this);
    }//GEN-LAST:event_jMenuItemCorrerActionPerformed

    private void jMenuItemCompilarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCompilarActionPerformed

    }//GEN-LAST:event_jMenuItemCompilarActionPerformed

    private void jMenuItemArchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemArchivoActionPerformed
        DialogoIrArchivo arch = new DialogoIrArchivo(this, archivos);
    }//GEN-LAST:event_jMenuItemArchivoActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Principal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Principal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Principal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Principal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Principal().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Abajo;
    private javax.swing.JPanel Arriba;
    private javax.swing.JPanel Centro;
    private javax.swing.JPanel Derecha;
    private javax.swing.JPanel Herramientas;
    private javax.swing.JPanel Izquierda;
    private javax.swing.JPanel Lienzo;
    private javax.swing.JMenuBar MenuBar;
    private javax.swing.JList<String> jListErrores;
    private javax.swing.JList<String> jListIntermedio;
    private javax.swing.JList<String> jListLexico;
    private javax.swing.JList<String> jListResultados;
    private javax.swing.JList<String> jListSemantico;
    private javax.swing.JList<String> jListSintactico;
    private javax.swing.JMenu jMenuArchivo;
    private javax.swing.JMenu jMenuCompilar;
    private javax.swing.JMenu jMenuEditar;
    private javax.swing.JMenuItem jMenuItemAbrir;
    private javax.swing.JMenuItem jMenuItemAbrirC;
    private javax.swing.JMenuItem jMenuItemArchivo;
    private javax.swing.JMenuItem jMenuItemCerrar;
    private javax.swing.JMenuItem jMenuItemCerrarC;
    private javax.swing.JMenuItem jMenuItemCompilar;
    private javax.swing.JMenuItem jMenuItemCorrer;
    private javax.swing.JMenuItem jMenuItemFind;
    private javax.swing.JMenuItem jMenuItemGuardar;
    private javax.swing.JMenuItem jMenuItemNuevo;
    private javax.swing.JMenu jMenuNavegar;
    private javax.swing.JPanel jPanelErrores;
    private javax.swing.JPanel jPanelIntermedio;
    private javax.swing.JPanel jPanelLexico;
    private javax.swing.JPanel jPanelResultados;
    private javax.swing.JPanel jPanelSemantico;
    private javax.swing.JPanel jPanelSintactico;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPaneErrores;
    private javax.swing.JScrollPane jScrollPaneIntermedio;
    private javax.swing.JScrollPane jScrollPaneLexico;
    private javax.swing.JScrollPane jScrollPaneResultados;
    private javax.swing.JScrollPane jScrollPaneSemantico;
    private javax.swing.JScrollPane jScrollPaneSintactico;
    private javax.swing.JTabbedPane jTabbedPaneAbajo;
    private javax.swing.JTabbedPane jTabbedPaneCentro;
    private javax.swing.JTabbedPane jTabbedPaneDerecha;
    private javax.swing.JTree jTreeDirectorio;
    // End of variables declaration//GEN-END:variables
}
