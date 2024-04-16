package com.example.test_log;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

public class QuizAssessment extends AppCompatActivity {

    FirebaseAuth auth;
    Button button;
    FirebaseUser user;
    private FirebaseFirestore firestore;
    private int currentQuestionIndex = 0;
    private int score = 0;
    TextView question;
    TextView choiceA, choiceB, choiceC, choiceD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz_assessment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout);
        user = auth.getCurrentUser();
        Intent intent = getIntent();
        String text = intent.getStringExtra("text");
        String answer1 = intent.getStringExtra("answer1");
        String answer2 = intent.getStringExtra("answer2");
        String answer3 = intent.getStringExtra("answer3");
        String answer4 = intent.getStringExtra("answer4");
        String correct = intent.getStringExtra("correct");

        // Find the views in the layout
        question = findViewById(R.id.question);
        choiceA = findViewById(R.id.choiceA);
        choiceB = findViewById(R.id.choiceB);
        choiceC = findViewById(R.id.choiceC);
        choiceD = findViewById(R.id.choiceD);

        // Set the text to the views
        question.setText(text);
        choiceA.setText(answer1);
        choiceB.setText(answer2);
        choiceC.setText(answer3);
        choiceD.setText(answer4);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), logpage.class);
                startActivity(intent);
            }
        });

    }
}