package com.example.jueguito;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Canvas;

//Clase que representa un botón circular en pantalla para disparar balas
public class BotonDisparar {
    private float botonX, botonY;
    private float radius;
    private Paint botonPaint;

    //Constructor del botón de disparo
    public BotonDisparar(int screenWidth, int screenHeight) {
        botonX = screenWidth - 250;
        botonY = screenHeight - 250;
        radius = 75;

        botonPaint = new Paint();
        botonPaint.setColor(Color.GRAY);
        botonPaint.setAntiAlias(true);
    }

    //Dibuja el botón de disparo en el canvas
    public void draw(Canvas canvas) {
        canvas.drawCircle(botonX, botonY, radius, botonPaint);
    }

    //Verifica si el usuario ha tocado el botón de disparo
    public boolean isTouched(float x, float y, int screenWidth) {
        if (x > screenWidth / 2) {
            float distance = (float) Math.sqrt(Math.pow(x - botonX, 2) + Math.pow(y - botonY, 2));
            return distance <= radius;
        }
        return false;
    }

    public float getX() {
        return botonX;
    }

    public float getY() {
        return botonY;
    }
}
