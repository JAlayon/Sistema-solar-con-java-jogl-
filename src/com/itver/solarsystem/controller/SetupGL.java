
package com.itver.solarsystem.controller;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.swing.JFrame;

public class SetupGL {
    
  
    //configuracion de la pantalla
    public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static boolean fullscreen=false;
    
    //setup openGL version 2
    public static final GLProfile profile = GLProfile.get(GLProfile.GL2);
    public static final GLCapabilities capabilities = new GLCapabilities(profile);
    
    //texturas de los planetas
    public static HashMap<Integer,Texture> texturePlanets = new HashMap<>();
    
    protected static void  fullScreen(JFrame f){
        if (!fullscreen) {
            f.dispose();
            f.setUndecorated(true);
            f.setVisible(true);
            f.setResizable(false);
            
        }
    }
    
  
    

    

}
