package com.example.jueguito;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Bullet {
    private Bitmap bullet;
    private int x, y;
    private int speed = 20;
    private int directionX = 0, directionY = 0;
    private boolean isActive = false;
    private Rect hitbox;

    public Bullet(Context context, int startX, int startY, int dirX, int dirY, int playerWidth, int playerHeight) {
        this.x = startX + playerWidth / 2 - 25 / 2;
        this.y = startY + playerHeight / 2 - 25 / 2;
        this.directionX = dirX;
        this.directionY = dirY;
        this.isActive = true;

        bullet = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.bala), 25, 25, false);
        hitbox = new Rect(x, y, x + bullet.getWidth(), y + bullet.getHeight());
    }


    public void shoot(int startX, int startY, int dirX, int dirY) {
        if (!isActive) {
            x = startX;
            y = startY;
            directionX = dirX;
            directionY = dirY;
            isActive = true;
        }
    }

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
