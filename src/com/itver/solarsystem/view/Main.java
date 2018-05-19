package com.itver.solarsystem.view;

import com.itver.solarsystem.controller.EarthCanvas;
import com.itver.solarsystem.controller.SolarSystemCanvas;
import com.itver.solarsystem.controller.SetupGL;
import javax.swing.*;


import java.awt.Color;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {

    public static void main(String[] args) {
        JFrame f = new JFrame("Sistema Solar");
        PanelSolar panelSolar = new PanelSolar(); //<- PANEL SOLAR
        PanelVistas panelVistas = new PanelVistas(); //<-PANEL VISTAS
        MenuBar menu = new MenuBar(); //<- BARRA DE MENU
        
        //setup frame
        f.getContentPane().setBackground(Color.WHITE);
        f.setLayout(null);
        f.setJMenuBar(menu);
        f.setSize(SetupGL.screenSize.width, SetupGL.screenSize.height);
        f.add(panelSolar);
        f.add(panelVistas);
        
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new Thread() {
                    @Override
                    public void run() {
                        if (PanelSolar.anim.isAnimating())
                            PanelSolar.anim.stop();
                        System.exit(0);
                    }
                }.start();
            }
        });

    }

    
}
