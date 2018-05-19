package com.itver.solarsystem.controller;


import com.itver.solarsystem.models.Planet;
import com.itver.solarsystem.models.Sun;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.fixedfunc.GLPointerFunc;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import static com.itver.solarsystem.models.IPlanets.*;
import com.jogamp.opengl.util.texture.Texture;
import java.util.ArrayList;

/**
 *
 * @author Alayon
 */
public class SolarSystemCanvas extends GLCanvas implements GLEventListener {

    private GLU glu;

    //DIMENSIONES DEL CANVAS
    public static final int CANVAS_WIDTH = 600;
    public static final int CANVAS_HEIGHT = 600;

    //LISTA DE PLANETAS {MERCURIO, VENUS, TIERRA...}
    private static ArrayList<Planet> planets;
    //CAMARA
    private SolarSystemCamera camera;


    private float Angle = 0;
    private float earthAngle = 0;
    private float systemAngle = 0;
    private Sun sun;

    private Texture earthTexture;
    private Texture cloudTexture;
    private Texture moonTexture;
    private Texture skyTexture;

    //CONSTRUCTOR
    public SolarSystemCanvas() {
        super(SetupGL.capabilities);
        this.addGLEventListener(this);
        this.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);

        //iniciar los objetos
        planets = new ArrayList<>();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        //CONFIGURACION INICIAL
        GL2 gl = drawable.getGL().getGL2();
        glu = new GLU();
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glDepthFunc(GL.GL_LEQUAL);
        gl.glDisable(GL.GL_DEPTH_TEST);
        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glClearColor(0f, 0f, 0f, 0f);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);

        //inciar los planetas con sus texturas
        PlanetTextures.loadTextures();
        loadPlanets(gl);
        
        //iniciar la configuracion de la camara
        camera = new SolarSystemCamera();
        this.addKeyListener(camera);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glViewport(0, 0, width, height);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        //LUCES Y CAMARAS
        camera.setCamera(gl, glu, 300);
        camera.aimCamera(gl, glu);
        camera.moveCamera();
        SolarSystemLights.setLights(gl);

        //dibujar sol, planetas y luna  
        sun.draw();
        drawEarthAndMoon(gl);
        for (Planet p : planets) {
            p.display();
        }

        skyTexture.bind(gl);
        skyTexture.enable(gl);

        // skybox
        drawCube(gl);
    }

    private void loadPlanets(GL2 gl) {
        //MERC, VEN, TIE, MAR, JUP, SAT, URA, NEPT
        planets.add(new Planet(gl, glu, PlanetTextures.getTexture("mercurio"), 
                    1.2f, SUN_RADIUS + 2f, 2.56f));
        
        planets.add(new Planet(gl, glu, PlanetTextures.getTexture("venus"), 
                    0.7f, SUN_RADIUS + 12f, 3.56f));
        //tierra
        earthTexture = PlanetTextures.getTexture("tierra");
        moonTexture = PlanetTextures.getTexture("luna");
        skyTexture = PlanetTextures.getTexture("espacio");
        cloudTexture = PlanetTextures.getTexture("cielo");
        sun = new Sun(gl, glu, PlanetTextures.getTexture("sol"));

        planets.add(new Planet(gl, glu, PlanetTextures.getTexture("marte"), 
                    0.3f, SUN_RADIUS + 50f, 3.56f));
        
        planets.add(new Planet(gl, glu, PlanetTextures.getTexture("jupiter"), 
                    0.25f, SUN_RADIUS + 65f, 8.56f));
        
        planets.add(new Planet(gl, glu, PlanetTextures.getTexture("saturno"), 
                   0.3f, SUN_RADIUS + 90f, 7.56f));
        
        planets.add(new Planet(gl, glu, PlanetTextures.getTexture("urano"), 
                    0.25f, SUN_RADIUS + 105f, 6.56f));
        
        planets.add(new Planet(gl, glu, PlanetTextures.getTexture("neptuno"), 
                    0.275f, SUN_RADIUS + 120f, 5.56f));

    }

    private void drawEarthAndMoon(GL2 gl) {
        gl.glPushMatrix();
        systemAngle = (systemAngle + 0.4f) % 360f;

        final float distance = EARTH_DISTANCE;
        final float x = (float) Math.sin(Math.toRadians(systemAngle)) * distance;
        final float y = (float) Math.cos(Math.toRadians(systemAngle)) * distance;
        final float z = 0;
        gl.glTranslatef(x, y, z);

        drawEarth(gl);
        drawMoon(gl);
        gl.glPopMatrix();

    }

    private void drawEarth(GL2 gl) {

        float[] rgba = {1f, 1f, 1f};
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, rgba, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, rgba, 0);
        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 0.5f);

        gl.glPushName(4);
        earthAngle = (earthAngle + 0.1f) % 360f;
        cloudTexture.enable(gl);
        cloudTexture.bind(gl);

        gl.glPushMatrix();
        gl.glRotatef(earthAngle, 0.2f, 0.1f, 0);
        final float radius = EARTH_DIAM;
        final int slices = 25;
        final int stacks = 25;
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_COLOR, GL.GL_DST_ALPHA);
       
        
        // clouds above the earth using a bigger sphere and applying blend to it
        GLUquadric clouds = glu.gluNewQuadric();
        glu.gluQuadricOrientation(clouds, GLU.GLU_OUTSIDE);
        glu.gluQuadricTexture(clouds, true);
        glu.gluSphere(clouds, 7, slices, stacks);
        
        
        
        earthTexture.enable(gl);
        earthTexture.bind(gl);
        gl.glDisable(GL.GL_BLEND);

        GLUquadric earth = glu.gluNewQuadric();
        glu.gluQuadricTexture(earth, true);
        glu.gluQuadricDrawStyle(earth, GLU.GLU_FILL);
        glu.gluQuadricNormals(earth, GLU.GLU_FLAT);
        glu.gluQuadricOrientation(earth, GLU.GLU_OUTSIDE);

        glu.gluSphere(earth, radius, slices, stacks);

        gl.glPopName();

        glu.gluDeleteQuadric(earth);
        glu.gluDeleteQuadric(clouds);

        gl.glPopMatrix();
    }

    // luna
    private void drawMoon(GL2 gl) {

        gl.glPushMatrix();
        moonTexture.enable(gl);
        moonTexture.bind(gl);
        gl.glPushName(5);
        Angle = (Angle + 1f) % 360f;
        final float distance = 12.000f;
        final float x = (float) Math.sin(Math.toRadians(Angle)) * distance;
        final int y = (int) ((float) Math.cos(Math.toRadians(Angle)) * distance);
        final float z = 0;
        gl.glTranslatef(x, y, z);
        gl.glRotatef(Angle, 0, 0, -1);
        gl.glRotatef(45f, 0, 1, 0);

        final float radius = 3.378f;
        final int slices = 16;
        final int stacks = 16;
        GLUquadric moon = glu.gluNewQuadric();
        glu.gluQuadricTexture(moon, true);
        glu.gluQuadricDrawStyle(moon, GLU.GLU_FILL);
        glu.gluQuadricNormals(moon, GLU.GLU_FLAT);
        glu.gluQuadricOrientation(moon, GLU.GLU_INSIDE);
        glu.gluSphere(moon, radius, slices, stacks);

        gl.glPopMatrix();
        gl.glPopName();
    }

    private void drawCube(GL gl) {

        skyTexture.enable(gl);
        skyTexture.bind(gl);

        ((GLPointerFunc) gl).glDisableClientState(GL2.GL_VERTEX_ARRAY);
        final float radius = 150f;
        final int slices = 25;
        final int stacks = 25;
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_COLOR, GL.GL_DST_ALPHA);
        GLUquadric sky = glu.gluNewQuadric();
        glu.gluQuadricTexture(sky, true);
        glu.gluQuadricDrawStyle(sky, GLU.GLU_FILL);
        glu.gluQuadricNormals(sky, GLU.GLU_FLAT);
        glu.gluQuadricOrientation(sky, GLU.GLU_INSIDE);
        glu.gluSphere(sky, radius*2, slices, stacks);

        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_COLOR, GL.GL_DST_ALPHA);
    }

    private static final float SUN_RADIUS = 12f;
}
