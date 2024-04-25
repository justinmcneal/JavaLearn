package com.example.test_log;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PeriodicBtn extends AppCompatActivity {
    private FirebaseUser user;
    private FirebaseAuth auth;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_identification);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CardView periodicOne = findViewById(R.id.periodic_one);
        CardView periodicTwo = findViewById(R.id.periodic_two);
        CardView periodicThree = findViewById(R.id.periodic_three);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        textView = findViewById(R.id.user_details);

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), logpage.class);
            startActivity(intent);
            finish();
        } else {
            textView.setText(user.getEmail());
        }

        periodicOne.setOnClickListener(v -> {
            Intent intent = new Intent(PeriodicBtn.this, Identification.class);
            intent.putExtra("difficulty", "easy");
            startActivity(intent);
            MediaPlayer mediaPlayer = MediaPlayer.create(PeriodicBtn.this, R.raw.easymediumhard);
            mediaPlayer.setOnPreparedListener(mp -> mp.start());
        });

        periodicTwo.setOnClickListener(v -> {
            Intent intent = new Intent(PeriodicBtn.this, Identification.class);
            intent.putExtra("difficulty", "medium");
            startActivity(intent);
            MediaPlayer mediaPlayer = MediaPlayer.create(PeriodicBtn.this, R.raw.easymediumhard);
            mediaPlayer.setOnPreparedListener(mp -> mp.start());
        });

        periodicThree.setOnClickListener(v -> {
            Intent intent = new Intent(PeriodicBtn.this, Identification.class);
            intent.putExtra("difficulty", "hard");
            startActivity(intent);
            MediaPlayer mediaPlayer = MediaPlayer.create(PeriodicBtn.this, R.raw.easymediumhard);
            mediaPlayer.setOnPreparedListener(mp -> mp.start());
        });
    }
}