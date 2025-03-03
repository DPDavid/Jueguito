package com.example.jueguito;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

public class Joystick {
    private float centerX, centerY, baseRadius, hatRadius;
    private float joystickX, joystickY;
    private boolean isPressed = false;
    private float touchX, touchY;

    public Joystick(float centerX, float centerY, float baseRadius, float hatRadius) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.baseRadius = baseRadius;
        this.hatRadius = hatRadius;
        this.joystickX = centerX;
        this.joystickY = centerY;
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setAlpha(100);
        canvas.drawCircle(centerX, centerY, baseRadius, paint);

        paint.setColor(Color.WHITE);
        canvas.drawCircle(joystickX, joystickY, hatRadius, paint);
    }

    public void update(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                // Calcular la distancia entre el toque y el centro del joystick
                float dx = event.getX() - centerX;
                float dy = event.getY() - centerY;
                double distance = Math.sqrt(dx * dx + dy * dy);

                // Si el toque está dentro de la base, mover el joystick
                if (distance < baseRadius) {
                    joystickX = event.getX();
                    joystickY = event.getY();
                } else {
                    // Si el toque está fuera de la base, restringir el movimiento del joystick
                    joystickX = (float) (centerX + (dx / distance) * baseRadius);
                    joystickY = (float) (centerY + (dy / distance) * baseRadius);
                }
                isPressed = true;
                break;

            case MotionEvent.ACTION_UP:
                // Al soltar el toque, restablecer la posición del joystick al centro
                joystickX = centerX;
                joystickY = centerY;
                isPressed = false;
                break;
        }
    }


    public float getDirectionX() {
        return (joystickX - centerX) / baseRadius;
    }

    public float getDirectionY() {
        return (joystickY - centerY) / baseRadius;
    }

    public boolean isPressed() {
        return isPressed;
    }
}
