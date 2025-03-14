package com.example.jueguito;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

//Clase que representa una bala disparada por el jugador
public class Bullet {
    private Bitmap bullet;
    private int x, y;
    private int speed = 60;
    private int directionX = 0, directionY = 0;
    private boolean isActive = false;
    private Rect hitbox;

    //Constructor de la bala.
    public Bullet(Context context, int startX, int startY, int dirX, int dirY, int playerWidth, int playerHeight) {
        this.x = startX + playerWidth / 2 - 25 / 2;
        this.y = startY + playerHeight / 2 - 25 / 2;
        this.directionX = dirX;
        this.directionY = dirY;
        this.isActive = true;

        bullet = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.bala), 25, 25, false);
        hitbox = new Rect(x, y, x + bullet.getWidth(), y + bullet.getHeight());
    }

    //Dispara la bala desde una posición y dirección específicas
    public void shoot(int startX, int startY, int dirX, int dirY) {
        if (!isActive) {
            x = startX;
            y = startY;
            directionX = dirX;
            directionY = dirY;
            isActive = true;
        }
    }

    //Actualiza la posición de la bala y verifica si sigue en pantalla
    public void update() {
        if (isActive) {
            x += speed * directionX;
            y += speed * directionY;

            hitbox.set(x, y, x + bullet.getWidth(), y + bullet.getHeight());

            if (x < -50 || x > 2000 || y < -50 || y > 2000) {
                isActive = false;
            }
        }
    }

    //Verifica si la bala ha colisionado con un enemigo
    public boolean checkCollision(Rect enemyHitbox) {
        return isActive && Rect.intersects(hitbox, enemyHitbox);
    }

    public void deactivate() {
        isActive = false;
    }

    public Bitmap getBitmap() {
        return bullet;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isActive() {
        return isActive;
    }

    public Rect getHitbox() {
        return new Rect(x, y, x + bullet.getWidth(), y + bullet.getHeight());
    }
}
