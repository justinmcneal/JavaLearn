package com.example.test_log;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class firstpage extends AppCompatActivity {
    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_firstpage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // send and process Message and Runnable objects
        Handler handler = new Handler();

        //delay by 2000 millisecond
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // request an action from this activity to another activity
                Intent intent = new Intent(firstpage.this,logsignpage.class); //purpose .class is para ma execute ng jvm
                startActivity(intent);
                finish(); //para di masyado malaking memory kainin
            }
        }, 2000);
    }
}