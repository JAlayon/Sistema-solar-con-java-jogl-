package com.itver.solarsystem.controller;

import static com.itver.solarsystem.controller.SetupGL.profile;
import static com.itver.solarsystem.controller.SetupGL.texturePlanets;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author Alayon
 */
//CLASE PARA PROVEEER FUNCIONALIDADES EXTERNAS
public class PlanetTextures{

    //ruta de la carpeta que tiene las texturas
    private static final String path = "res\\textures";
    private static String[] namesImg = {"cielo", "espacio", "jupiter", "luna",
                                 "marte", "mercurio", "neptuno", "saturno",
                                 "sol", "tierra", "urano", "venus"};
    
    private static HashMap<String, Texture> t = new HashMap();
    
    public static void loadTextures() {
        File f = new File(path);
        if (f.exists()) {
            File[] imgs = f.listFiles();
            int i=0;
            for (File img : imgs) {
                t.put(namesImg[i], getTexture(img));
                i++;
            }
        } else {
            System.err.println("No se ha encontrado el archivo: " + path);
        }
    }

    private static  Texture getTexture(File im) {
        try {
            String name = im.getName();
            String extension = name.substring(name.lastIndexOf('.'));
            TextureData td = TextureIO.newTextureData(profile, im, false, extension);
            return TextureIO.newTexture(td);
        } catch (IOException e) {
            System.err.println("Error en la lectura de la imagen");
        }

        return null;
    }
    
    public static Texture getTexture(String key){
        return t.get(key);
    }

}
