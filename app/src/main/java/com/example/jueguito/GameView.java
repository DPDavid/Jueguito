package com.example.jueguito;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameView extends SurfaceView implements Runnable {
    private volatile boolean playing;
    private Thread gameThread = null;
    private Jugador jugador;
    private Joystick joystick;
    private List<Enemigos> enemigos;
    private List<Bullet> balas;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    private Bitmap background;
    private BotonDisparar botonDisparar;

    public GameView(Context context, int screenX, int screenY) {
        super(context);

        joystick = new Joystick(300, screenY - 500, 200, 100);
        jugador = new Jugador(context, screenX, screenY);
        balas = new ArrayList<>();
        botonDisparar = new BotonDisparar(screenX, screenY);

        enemigos = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            enemigos.add(new Enemigos(context, screenX, screenY));
        }

        surfaceHolder = getHolder();
        paint = new Paint();

        background = BitmapFactory.decodeResource(context.getResources(), R.drawable.fondo);
        background = Bitmap.createScaledBitmap(background, screenX, screenY, false);
    }

    @Override
    public void run() {
        while (playing) {
            update();
            draw();
            control();
        }
    }

    private void update() {
        if (joystick.isPressed()) {
            jugador.setSpeedX(joystick.getDirectionX() * 14);
            jugador.setSpeedY(joystick.getDirectionY() * 14);
        } else {
            jugador.setSpeedX(0);
            jugador.setSpeedY(0);
        }
        jugador.update();

        Iterator<Bullet> bulletIterator = balas.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bala = bulletIterator.next();
            bala.update();
            if (!bala.isActive()) {
                bulletIterator.remove();
            }
        }

        Iterator<Enemigos> enemyIterator = enemigos.iterator();
        while (enemyIterator.hasNext()) {
            Enemigos enemigo = enemyIterator.next();
            enemigo.update(jugador.getX(), jugador.getY(), jugador.getBitmap().getWidth(), jugador.getBitmap().getHeight(), balas);

            for (Bullet bala : balas) {
                if (Rect.intersects(bala.getHitbox(), enemigo.getDetectCollision())) {
                    enemigo.setInactive();
                    bala.deactivate();
                }
            }


            if (Rect.intersects(jugador.getDetectCollision(), enemigo.getDetectCollision())) {
                playing = false;
                return;
            }

            if (!enemigo.isActive()) {
                enemyIterator.remove();
            }
        }
    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);
            canvas.drawBitmap(background, 0, -50, paint);

            joystick.draw(canvas);

            canvas.drawBitmap(jugador.getBitmap(), jugador.getX(), jugador.getY(), paint);

            for (Enemigos enemigo : enemigos) {
                canvas.drawBitmap(enemigo.getBitmap(), enemigo.getX(), enemigo.getY(), paint);
            }

            for (Bullet bala : balas) {
                canvas.drawBitmap(bala.getBitmap(), bala.getX(), bala.getY(), paint);
            }

            botonDisparar.draw(canvas);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void control() {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();


        if (touchX <= getWidth() / 2) {
            joystick.update(event);
        }


        if (botonDisparar.isTouched(touchX, touchY, getWidth())) {
            Bullet nuevaBala = jugador.shoot();
            if (nuevaBala != null) {
                balas.add(nuevaBala);
            }
        }

        return true;
    }
}
