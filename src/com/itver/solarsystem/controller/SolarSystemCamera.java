package com.itver.solarsystem.controller;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SolarSystemCamera extends KeyAdapter {

    float azimuth = 0.0f;
    float speed = 0.0f;
    float elevation = 0.0f;

    //posicion de la camara
    float posX = 0.0f;
    float posY = 0.0f;
    float posZ = -40.0f;

    //orientacion de la camara
    float upX = 0.0f;
    float upY = 1.0f;
    float upZ = 0.0f;

    public SolarSystemCamera() { }

    public void setCamera(GL2 gl, GLU glu, float distance) {
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        float widthHeightRatio = (float) SolarSystemCanvas.CANVAS_WIDTH / (float) SolarSystemCanvas.CANVAS_HEIGHT;
        glu.gluPerspective(45, widthHeightRatio, 1, 1000);
        glu.gluLookAt(0, 0, distance, 0, 0, 0, 0, 1, 0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    // control de la camara
    public void moveCamera() {
        float[] tmp = polarToCartesian(azimuth, speed, elevation);
        posX += tmp[0];
        posY += tmp[1];
        posZ += tmp[2];
    }

    public void aimCamera(GL2 gl, GLU glu) {
        gl.glLoadIdentity();
        float[] tmp = polarToCartesian(azimuth, 100.0f, elevation);
        float[] camUp = polarToCartesian(azimuth, 100.0f, elevation + 90);
        upX = camUp[0];
        upY = camUp[1];
        upZ = camUp[2];
        glu.gluLookAt(posX, posY, posZ, posX + tmp[0],
                posY + tmp[1], posZ + tmp[2], upX, upY, upZ);
    }

    private float[] polarToCartesian(float azimuth, float length, float altitude) {
        float[] result = new float[3];
        float x, y, z;

        // Do x-z calculation
        float theta = (float) Math.toRadians(90 - azimuth);
        float tantheta = (float) Math.tan(theta);
        float radian_alt = (float) Math.toRadians(altitude);
        float cospsi = (float) Math.cos(radian_alt);

        x = (float) Math.sqrt((length * length) / (tantheta * tantheta + 1));
        z = tantheta * x;

        x = -x;

        if ((azimuth >= 180.0 && azimuth <= 360.0) || azimuth == 0.0f) {
            x = -x;
            z = -z;
        }

        // Calculate y, and adjust x and z
        y = (float) (Math.sqrt(z * z + x * x) * Math.sin(radian_alt));

        if (length < 0) {
            x = -x;
            z = -z;
            y = -y;
        }

        x = x * cospsi;
        z = z * cospsi;

        result[0] = x;
        result[1] = y;
        result[2] = z;

        return result;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            elevation -= 2.5f;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            elevation += 2.5f;
        }

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            azimuth -= 2.5f;
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            azimuth += 2.5f;
        }
    }

}
