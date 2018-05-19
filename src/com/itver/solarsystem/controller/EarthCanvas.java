package com.itver.solarsystem.controller;

import static com.itver.solarsystem.models.IPlanets.EARTH_DIAM;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.fixedfunc.GLPointerFunc;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.texture.Texture;

/**
 *
 * @author Alayon
 */
public class EarthCanvas extends GLCanvas implements GLEventListener {

    public static final int CANVAS_WIDTH = 600;
    public static final int CANVAS_HEIGHT = 600;

    private GLU glu;

    //TEXTURAS
    private Texture textureEarth;
    private Texture spaceTexture;
    private Texture cloudTexture;

    private float rotationY = 0;

    //CAMARA
    private SolarSystemCamera camera;

    public EarthCanvas() {
        super(SetupGL.capabilities);
        this.addGLEventListener(this);
        this.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
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

        PlanetTextures.loadTextures();
        //recuperar la textura
        textureEarth = PlanetTextures.getTexture("tierra");
        cloudTexture = PlanetTextures.getTexture("cielo");
        spaceTexture = PlanetTextures.getTexture("espacio");

        //iniciar la configuracion de la camara
        camera = new SolarSystemCamera();
        this.addKeyListener(camera);

    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        //LUCES Y CAMARAS
        camera.setCamera(gl, glu, 0.5f);
        camera.aimCamera(gl, glu);
        camera.moveCamera();
        SolarSystemLights.setLights(gl);

        rotationY = (rotationY + 0.2f) % 360f;
        
        //dibujar tierra
        gl.glPushMatrix();
                    float[] rgba = {1f, 1f, 1f};
                    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, rgba, 0);
                    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, rgba, 0);
                    gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 0.5f);

                     gl.glPushName(4);
                     cloudTexture.enable(gl);
                     cloudTexture.bind(gl);
                     gl.glPushMatrix();
                     gl.glRotatef(rotationY, 0f, 1f, 0);

                     final int slices = 45;
                     final int stacks = 45;
                     gl.glEnable(GL.GL_BLEND);
                     gl.glBlendFunc(GL.GL_SRC_COLOR, GL.GL_DST_ALPHA);
                     
                    // clouds above the earth using a bigger sphere and applying blend to it
                    GLUquadric clouds = glu.gluNewQuadric();
                    glu.gluQuadricOrientation(clouds, GLU.GLU_OUTSIDE);
                    glu.gluQuadricTexture(clouds, true);
                    glu.gluSphere(clouds, 7, slices, stacks);

                    textureEarth.enable(gl);
                    textureEarth.bind(gl);
                    gl.glDisable(GL.GL_BLEND);

                   GLUquadric planet = glu.gluNewQuadric();
                   glu.gluQuadricTexture(planet, true);
                   glu.gluQuadricDrawStyle(planet, GLU.GLU_FILL);
                   glu.gluQuadricNormals(planet, GLU.GLU_FLAT);
                   glu.gluQuadricOrientation(planet, GLU.GLU_OUTSIDE);
                   glu.gluSphere(planet, EARTH_DIAM, slices, stacks);
                   gl.glPopName();

        glu.gluDeleteQuadric(planet);
        glu.gluDeleteQuadric(clouds);

        gl.glPopMatrix();

        spaceTexture.bind(gl);
        spaceTexture.enable(gl);
        drawCube(gl);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glViewport(0, 0, width, height);
    }

    public void drawCube(GL2 gl) {
        spaceTexture.enable(gl);
        spaceTexture.bind(gl);

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
        glu.gluSphere(sky, radius * 2, slices, stacks);

        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_COLOR, GL.GL_DST_ALPHA);
    }

}
