
package com.itver.solarsystem.view;

import com.itver.solarsystem.controller.EarthCanvas;
import com.itver.solarsystem.controller.MoonCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import javax.swing.JPanel;

/**
 *
 * @author Alayon
 */
public class PanelVistas extends JPanel {
    
    public static FPSAnimator anim1;
    public static FPSAnimator anim2;
    
    public PanelVistas(){
       this.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Otras vistas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12), new java.awt.Color(0, 0, 0))); // NOI18N
       this.setLayout(new java.awt.GridLayout(2, 1, 0, 7));
       this.setBounds(800, 10, 510, 650);
       
       EarthCanvas ec = new EarthCanvas(); //<- TIERRA
       MoonCanvas mc = new MoonCanvas(); //<- LUNA
       anim1 = new FPSAnimator(ec, 60); //<-ANIMADOR TIERRA
       anim2 = new FPSAnimator(mc, 60); //<-ANIMADOR SOL
       anim1.start();
       anim2.start();
       this.add(ec);
       this.add(mc);
    }
}
