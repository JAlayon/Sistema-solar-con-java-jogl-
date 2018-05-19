package com.itver.solarsystem.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 *
 * @author Alayon
 */
public class MenuBar extends JMenuBar {

    private final String tittle = "Desarrolladores";
    private final String message = "Jair Alayón Sauceda"
            + "\nJose Antonio Villegas Mendoza"
            + "\n\n"
            + "Materia:Graficación por computadora\n"
            + "Maestro: Genaro Méndez López";

    public MenuBar() {
        JMenu help = new JMenu("Ayuda");
        JMenuItem about = new JMenuItem("Acerca de");
        help.setMnemonic('A');
        help.add(about);
        about.addActionListener((ActionEvent e) -> {
            JOptionPane.showMessageDialog(null, message, tittle, JOptionPane.INFORMATION_MESSAGE);
        });
        
        this.add(help);
    }

}
