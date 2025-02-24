package com.example.jueguito;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.SystemClock;

public class Jugador {
    private Bitmap[] spriteDerecha, spriteIzquierda, spriteArriba, spriteAbajo;
    private Bitmap[] spriteActual;
    private int frameIndex = 0;
    private long lastFrameTime;
    private static final long FRAME_DURATION = 250;

    private int x, y;
    private float speedX = 1, speedY = 1;
    private int maxX, maxY, minX, minY;

    public Jugador(Context context, int screenX, int screenY) {
        x = screenX / 2;
        y = screenY / 2;


        spriteDerecha = new Bitmap[]{
                Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.arthurderecha1), 512, 512, false),
                Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.arthurderecha2), 512, 512, false)
        };
        spriteIzquierda = new Bitmap[]{
                Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.arthurizquierda1), 512, 512, false),
                Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.arthurizquierda2), 512, 512, false)
        };

        spriteArriba = new Bitmap[]{
                Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.arthurarriba1), 512, 512, false),
                Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.arthurarriba2), 512, 512, false)
        };

        spriteAbajo = new Bitmap[]{
                Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.arthurfrente1), 512, 512, false),
                Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.arthurfrente2), 512, 512, false)
        };

        spriteActual = spriteDerecha;


        maxX = screenX - spriteDerecha[0].getWidth();
        maxY = screenY - spriteDerecha[0].getHeight();
        minX = 0;
        minY = 0;

        lastFrameTime = SystemClock.uptimeMillis();
    }

    public void update() {
        x += speedX;
        y += speedY;

        if (x < minX) x = minX;
        if (x > maxX) x = maxX;
        if (y < minY) y = minY;
        if (y > maxY) y = maxY;


        float threshold = 0.1f;

        if (Math.abs(speedX) > Math.abs(speedY)) {
            if (speedX > threshold) {
                spriteActual = spriteDerecha;
            } else if (speedX < -threshold) {
                spriteActual = spriteIzquierda;
            }
        } else {
            if (speedY > threshold) {
                spriteActual = spriteAbajo;
            } else if (speedY < -threshold) {
                spriteActual = spriteArriba;
            }
        }

        long currentTime = SystemClock.uptimeMillis();
        if (currentTime - lastFrameTime >= FRAME_DURATION) {
            frameIndex = (frameIndex + 1) % spriteActual.length;
            lastFrameTime = currentTime;
        }
    }


    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }

    public void setSpeedY(float speedY) {
        this.speedY = speedY;
    }

    public Bitmap getBitmap() {
        return spriteActual[frameIndex];
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
