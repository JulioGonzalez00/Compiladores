/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bazinga;

import GUI.Principal;
import com.formdev.flatlaf.*;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author julio
 */
public class Bazinga {

    public static void main(String[] args) {
        Principal p = new Principal();
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            System.out.println("Error Kitsume: \n" + ex.getMessage());
        }
        //p.setLocationRelativeTo(null);
        //p.setVisible(true);
    }

}
