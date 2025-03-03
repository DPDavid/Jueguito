package com.example.jueguito;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
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
    private Rect detectCollision;

    private static final int COLLISION_WIDTH = 60;
    private static final int COLLISION_HEIGHT = 60;

    private Bullet bullet;
    private int directionX = 1, directionY = 0;

    private boolean canShoot = true;
    private Context context;

    public Jugador(Context context, int screenX, int screenY) {
        this.context = context;

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

        int centerX = x + spriteDerecha[0].getWidth() / 2 - COLLISION_WIDTH / 2;
        int centerY = y + spriteDerecha[0].getHeight() / 2 - COLLISION_HEIGHT / 2;
        detectCollision = new Rect(centerX, centerY, centerX + COLLISION_WIDTH, centerY + COLLISION_HEIGHT);

        maxX = screenX + 200 - spriteDerecha[0].getWidth();
        maxY = screenY + 100 - spriteDerecha[0].getHeight();
        minX = -200;
        minY = -200;

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
                directionX = 1;
                directionY = 0;
            } else if (speedX < -threshold) {
                spriteActual = spriteIzquierda;
                directionX = -1;
                directionY = 0;
            }
        } else {
            if (speedY > threshold) {
                spriteActual = spriteAbajo;
                directionX = 0;
                directionY = 1;
            } else if (speedY < -threshold) {
                spriteActual = spriteArriba;
                directionX = 0;
                directionY = -1;
            }
        }

        long currentTime = SystemClock.uptimeMillis();
        if (currentTime - lastFrameTime >= FRAME_DURATION) {
            frameIndex = (frameIndex + 1) % spriteActual.length;
            lastFrameTime = currentTime;
        }

        int centerX = x + spriteDerecha[0].getWidth() / 2 - COLLISION_WIDTH / 2;
        int centerY = y + spriteDerecha[0].getHeight() / 2 - COLLISION_HEIGHT / 2;
        detectCollision.set(centerX, centerY, centerX + COLLISION_WIDTH, centerY + COLLISION_HEIGHT);
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

    public Rect getDetectCollision() {
        return detectCollision;
    }

    public Bullet shoot() {
        if (canShoot) {
            canShoot = false;
            int bulletX = 0;
            int bulletY=0;

            if(spriteActual==spriteArriba){
                bulletY = detectCollision.centerY()-290;
                bulletX= detectCollision.centerX()-300;
            }
            if (spriteActual==spriteAbajo){
                bulletY= detectCollision.centerY()-240;
                bulletX= detectCollision.centerX()-210;
            }
            if (spriteActual==spriteDerecha){
                bulletY = detectCollision.centerY()-240;
                bulletX= detectCollision.centerX()-190;
            }
            if (spriteActual==spriteIzquierda){
                bulletY = detectCollision.centerY()-240;
                bulletX= detectCollision.centerX()-320;
            }



            // Crear la bala en la posición calculada y con la dirección actual del jugador
            Bullet nuevaBala = new Bullet(context, bulletX, bulletY, directionX, directionY, spriteActual[0].getWidth(), spriteActual[0].getHeight());

            // Hilo para manejar el retraso de disparo
            new Thread(() -> {
                try {
                    Thread.sleep(500);
                    canShoot = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

            return nuevaBala;
        }
        return null;
    }




    public Bullet getBullet() {
        return bullet;
    }
}
