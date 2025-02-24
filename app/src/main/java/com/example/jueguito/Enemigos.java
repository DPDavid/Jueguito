package com.example.jueguito;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Enemigos {
    private Bitmap[] spriteDerecha, spriteIzquierda;
    private Bitmap[] spriteActual;

    private int x;
    private int y;

    private int speed = 1;

    private int maxX;
    private int minX;
    private int maxY;
    private int minY;

    private int mitadX;
    private int mitadY;

    private int frameIndex = 0;
    private int frameDelay = 10;
    private int frameCounter = 0;

    public Enemigos(Context context, int screenX, int screenY) {
        spriteDerecha = new Bitmap[]{
                Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.seta1), 128, 128, false),
                Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.seta2), 128, 128, false),
                Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.seta3), 128, 128, false),
                Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.seta4), 128, 128, false),
                Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.seta5), 128, 128, false),
                Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.seta6), 128, 128, false),
                Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.seta7), 128, 128, false)
        };

        spriteIzquierda = new Bitmap[]{
                flipBitmap(spriteDerecha[0]),
                flipBitmap(spriteDerecha[1]),
                flipBitmap(spriteDerecha[2]),
                flipBitmap(spriteDerecha[3]),
                flipBitmap(spriteDerecha[4]),
                flipBitmap(spriteDerecha[5]),
                flipBitmap(spriteDerecha[6])
        };

        // Limites de la pantalla
        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;

        mitadX = screenX / 2;
        mitadY = screenY / 2;

        Random generator = new Random();
        speed = generator.nextInt(6) + 10;

        int lado = generator.nextInt(4);

        switch (lado) {
            case 0: // Izquierda
                x = minX - spriteDerecha[0].getWidth();
                y = mitadY - spriteDerecha[0].getHeight() / 2;
                spriteActual = spriteDerecha;
                break;

            case 1: // Derecha
                x = maxX;
                y = mitadY - spriteDerecha[0].getHeight() / 2;
                spriteActual = spriteIzquierda;
                break;

            case 2: // Arriba
                x = mitadX - spriteDerecha[0].getWidth() / 2;
                y = minY - spriteDerecha[0].getHeight();
                spriteActual = spriteDerecha;
                break;

            case 3: // Abajo
                x = mitadX - spriteDerecha[0].getWidth() / 2;
                y = maxY;
                spriteActual = spriteIzquierda;
                break;
        }
    }


    public void update(int jugadorX, int jugadorY, int jugadorWidth, int jugadorHeight) {
        // Apunta al centro del sprite del jugador
        int targetX = jugadorX + jugadorWidth / 2;
        int targetY = jugadorY + jugadorHeight / 2;

        // Calcula la dirección hacia el centro del jugador
        int deltaX = targetX - x;
        int deltaY = targetY - y;

        // Normaliza el vector de movimiento
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        if (distance > 0) {
            x += (int) ((deltaX / distance) * speed);
            y += (int) ((deltaY / distance) * speed);
        }

        // Cambia de sprite con retraso
        frameCounter++;
        if (frameCounter >= frameDelay) {
            frameIndex = (frameIndex + 1) % spriteActual.length;
            frameCounter = 0;
        }
    }

    public Bitmap getBitmap() {
        return spriteActual[0];
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpeed() {
        return speed;
    }

    private Bitmap flipBitmap(Bitmap bitmap) {
        android.graphics.Matrix matrix = new android.graphics.Matrix();
        matrix.preScale(-1, 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}
