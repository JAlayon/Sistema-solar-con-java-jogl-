package com.itver.solarsystem.view;

import com.itver.solarsystem.controller.SolarSystemCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import java.awt.BorderLayout;
import javax.swing.JPanel;

/**
 *
 * @author Alayon
 */
public class PanelSolar extends JPanel {

    public static FPSAnimator anim;

    public PanelSolar() {
        this.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Sistema solar", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12), new java.awt.Color(0, 0, 0))); // NOI18N
        this.setLayout(new BorderLayout());
        this.setBounds(5, 10, 780, 650);

        SolarSystemCanvas sc = new SolarSystemCanvas(); //<-SISTEMA SOLAR
        anim = new FPSAnimator(sc, 60); //<-ANIMADOR SISTEMA SOLAR
        anim.start();
        this.add(sc, BorderLayout.CENTER);
    }
}
