package com.example.jueguito;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class GameOverActivity extends AppCompatActivity {
    //Muestra la pantalla de "Game Over" durante 3 segundos y luego regresa al menú principal.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(GameOverActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}
