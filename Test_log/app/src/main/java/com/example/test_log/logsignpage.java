package com.example.test_log;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class logsignpage extends AppCompatActivity {

    private Button logpage; //access modifier sya
    private Button signpage; //same goes

    private MediaPlayer mediaPlayer;


    @Override //declares that do inherited class must do something
    protected void onCreate(Bundle savedInstanceState) { //limited access, void is the return type, does not return anything.
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_logsignpage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets; //built in
        }); //built in

        logpage = findViewById(R.id.signinfirst); //kinuha ung logpage sa function sa taas then kinuha ung id sa button from activity_logpage.xml

        logpage.setOnClickListener(new View.OnClickListener() { //sets that this button must be clickable
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(logsignpage.this,logpage.class); // request an action from this activity to another activity
                startActivity(intent);

                mediaPlayer = MediaPlayer.create(logsignpage.this, R.raw.signinout);
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer.start();
                    }
                });
            }
        });

        signpage = findViewById(R.id.signupfirst); //same goes
        signpage.setOnClickListener(new View.OnClickListener() { //same goes
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(logsignpage.this,signpage.class);
                startActivity(intent);

                mediaPlayer = MediaPlayer.create(logsignpage.this, R.raw.signinout);
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer.start();
                    }
                });
            }
        });


    }
}