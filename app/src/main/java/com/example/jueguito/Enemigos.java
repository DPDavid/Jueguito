package com.example.jueguito;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import java.util.List;
import java.util.Random;

//Clase que representa a los enemigos en el juego
public class Enemigos {
    private Bitmap[] spriteDerecha, spriteIzquierda;
    private Bitmap[] spriteActual;

    private int x, y;
    private int speed = 1;
    private boolean isActive = true;

    private int frameIndex = 0;
    private int frameDelay = 7;
    private int frameCounter = 0;

    private Rect detectCollision;

    //Constructor de un enemigo
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

        Random generator = new Random();

        speed = generator.nextInt(2) + 5;

        int lado = generator.nextInt(4);
        int margin = 50;

        switch (lado) {
            case 0:
                x = -spriteDerecha[0].getWidth() - margin;
                y = generator.nextInt(screenY - spriteDerecha[0].getHeight());
                spriteActual = spriteDerecha;
                break;
            case 1:
                x = screenX + margin;
                y = generator.nextInt(screenY - spriteDerecha[0].getHeight());
                spriteActual = spriteIzquierda;
                break;
            case 2:
                x = generator.nextInt(screenX - spriteDerecha[0].getWidth());
                y = -spriteDerecha[0].getHeight() - margin;
                spriteActual = spriteDerecha;
                break;
            case 3:
                x = generator.nextInt(screenX - spriteDerecha[0].getWidth());
                y = screenY + margin;
                spriteActual = spriteIzquierda;
                break;
        }

        detectCollision = new Rect(x, y, x + spriteDerecha[0].getWidth(), y + spriteDerecha[0].getHeight());
    }

    //Actualiza la posición del enemigo y verifica colisiones
    public void update(int jugadorX, int jugadorY, int jugadorWidth, int jugadorHeight, List<Bullet> balas) {
        if (!isActive) return;

        int targetX = jugadorX + jugadorWidth / 2;
        int targetY = jugadorY + jugadorHeight / 2;

        int deltaX = targetX - x;
        int deltaY = targetY - y;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        if (distance > 0) {
            x += (int) ((deltaX / distance) * speed);
            y += (int) ((deltaY / distance) * speed);
        }

        detectCollision.set(x, y, x + spriteActual[0].getWidth(), y + spriteActual[0].getHeight());

        for (Bullet bala : balas) {
            if (bala.isActive() && Rect.intersects(detectCollision, bala.getHitbox())) {
                isActive = false;
                bala.deactivate();
                break;
            }
        }
    }

    public Bitmap getBitmap() {
        return isActive ? spriteActual[frameIndex] : null;
    }

    public boolean isActive() {
        return isActive;
    }

    public int getX() {
        return x;
    }

    public int getY(){
        return y;
    }

    public Rect getDetectCollision() {
        return detectCollision;
    }

    public void setInactive() {
        isActive = false;
    }

    private Bitmap flipBitmap(Bitmap bitmap) {
        android.graphics.Matrix matrix = new android.graphics.Matrix();
        matrix.preScale(-1, 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}

