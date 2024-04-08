package com.example.test_log;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button button;
    TextView textView;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        auth = FirebaseAuth.getInstance(); // yes
        button = findViewById(R.id.logout);
//        textView = findViewById(R.id.user_details);
//        user = auth.getCurrentUser();
//
//        if(user == null){
//            Intent intent = new Intent(getApplicationContext(), logpage.class);
//            startActivity(intent);
//            finish();
//        } else {
//            textView.setText(user.getEmail());
//        }

        button.setOnClickListener(new View.OnClickListener() { //do something
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut(); //kukunin ung data tas isisignout sa app
                Intent intent = new Intent(getApplicationContext(), logpage.class);
                startActivity(intent);

            }
        });

        CardView btnEasy = findViewById(R.id.easy_cv); //kinuha mga id for the 3 cardview recyclerview sana but 3 lang so this is easier
        CardView btnMedium = findViewById(R.id.medium_cv);
        CardView btnHard = findViewById(R.id.hard_cv);

        btnEasy.setOnClickListener(new View.OnClickListener() { //function in easy
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LessonsActivity.class); //move to next activity

                String difficulty = "easy"; //call
                intent.putExtra("difficulty", difficulty); //call to get extra in next activity
                startActivity(intent);
            }

        });

        btnMedium.setOnClickListener(new View.OnClickListener() { //same goes
            @Override
            public void onClick(View v) { //samegoes
                Intent intent = new Intent(MainActivity.this, LessonsActivity.class);

                String difficulty = "medium"; //same goes
                intent.putExtra("difficulty", difficulty); //same goes
                startActivity(intent);
            }
        });

        btnHard.setOnClickListener(new View.OnClickListener() { //same goes
            @Override
            public void onClick(View v) { //same goes
                Intent intent = new Intent(MainActivity.this, LessonsActivity.class);

                String difficulty = "hard"; //same goes
                intent.putExtra("difficulty", difficulty);
                startActivity(intent);

            }
        });



    }
}