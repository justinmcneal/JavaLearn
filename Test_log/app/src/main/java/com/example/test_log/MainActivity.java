package com.example.test_log;

import android.content.Intent;
import android.media.MediaPlayer;
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

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
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

        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout);
        textView = findViewById(R.id.user_details);
        user = auth.getCurrentUser();

        if(user == null){
            Intent intent = new Intent(getApplicationContext(), logpage.class);
            startActivity(intent);
            finish();
        } else {
            textView.setText(user.getEmail());
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), logpage.class);
                startActivity(intent);
                finish();

                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.logout);
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {

                        mediaPlayer.start();
                    }
                });

            }
        });

        CardView btnEasy = findViewById(R.id.easy_cv);
        CardView btnMedium = findViewById(R.id.medium_cv);
        CardView btnHard = findViewById(R.id.hard_cv);

        btnEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LessonsActivity.class);
                String difficulty = "easy";
                intent.putExtra("difficulty", difficulty);
                startActivity(intent);

                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.easymediumhard);
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer.start();
                    }
                });
            }

        });

        btnMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LessonsActivity.class);

                String difficulty = "medium";
                intent.putExtra("difficulty", difficulty);
                startActivity(intent);

                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.easymediumhard);
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {

                        mediaPlayer.start();
                    }
                });
            }
        });

        btnHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LessonsActivity.class);

                String difficulty = "hard";
                intent.putExtra("difficulty", difficulty);
                startActivity(intent);

                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.easymediumhard);
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





//        String difficulty = intent.getStringExtra("difficulty");

//        if (Objects.equals(difficulty, "easy")) {
//            JSONObject jsonObject = JSONReader.loadJSONObjectFromAsset(this, "lessons.json");
//
//            if (jsonObject != null) {
//                try {
//                    JSONObject easyObject = jsonObject.getJSONObject("difficulty");
//                    JSONArray lessonEasy = easyObject.getJSONArray("easy");
//
//                    for (int i = 0; i <= lessonEasy.length(); i++) {
//                        JSONObject accessTitle = new JSONObject(String.valueOf((lessonEasy.get(i))));
//
//                        JSONArray questionEasy = accessTitle.getJSONArray("questions");
//
//                        for (int j = 0; j <= questionEasy.length(); j++) {
//                            JSONObject question = questionEasy.getJSONObject(j);
//                            String questionText = question.getString("text");
//                            String answer1 = question.getString("answer1");
//                            String answer2 = question.getString("answer2");
//                            String answer3 = question.getString("answer3");
//                            String answer4 = question.getString("answer4");
//                            String correctAnswer = question.getString("correct");
//                            int value = question.getInt("value");
//                        }
//                    }
//
//                } catch (JSONException e) {
//                    Log.d("catch", "error on catch");
//                    e.printStackTrace();
//                }
//            } else {
//                Log.d("elsejson", "error on json else");
//            }
//        } else {
//            Log.d("objectjson", "error on json object");
//        }
