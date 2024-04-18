package com.example.test_log;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class QuizAssessment extends AppCompatActivity {

    FirebaseAuth auth;
    Button button;
    private int currentQuestionIndex = 0;
    TextView question;
    TextView choiceA, choiceB, choiceC, choiceD;
    ArrayList<String> textList;
    ArrayList<String> answer1List;
    ArrayList<String> answer2List;
    ArrayList<String> answer3List;
    ArrayList<String> answer4List;
    ArrayList<String> correctList;

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
        Intent intent = getIntent();
        textList = intent.getStringArrayListExtra("text");
        answer1List = intent.getStringArrayListExtra("answer1");
        answer2List = intent.getStringArrayListExtra("answer2");
        answer3List = intent.getStringArrayListExtra("answer3");
        answer4List = intent.getStringArrayListExtra("answer4");
        correctList = intent.getStringArrayListExtra("correct");

        // Initialize views
        question = findViewById(R.id.question);
        choiceA = findViewById(R.id.choiceA);
        choiceB = findViewById(R.id.choiceB);
        choiceC = findViewById(R.id.choiceC);
        choiceD = findViewById(R.id.choiceD);

        // Display the first question
        displayQuestion();

        // Button click listener for logout
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), logpage.class);
                startActivity(intent);
            }
        });

        // Click listeners for choices
        choiceA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(choiceA.getText().toString());
            }
        });

        choiceB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(choiceB.getText().toString());
            }
        });

        choiceC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(choiceC.getText().toString());
            }
        });

        choiceD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(choiceD.getText().toString());
            }
        });
    }

    // Method to display the current question
    private void displayQuestion() {
        if (textList != null && currentQuestionIndex >= 0 && currentQuestionIndex < textList.size()) {
            question.setText(textList.get(currentQuestionIndex));
            choiceA.setText(answer1List.get(currentQuestionIndex));
            choiceB.setText(answer2List.get(currentQuestionIndex));
            choiceC.setText(answer3List.get(currentQuestionIndex));
            choiceD.setText(answer4List.get(currentQuestionIndex));
        } else {
            Toast.makeText(this, "Invalid question index", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to check the user's answer
    private void checkAnswer(String selectedAnswer) {
        if (correctList != null && currentQuestionIndex >= 0 && currentQuestionIndex < correctList.size()) {
            String correctAnswer = correctList.get(currentQuestionIndex);
            if (selectedAnswer.equals(correctAnswer)) {
                Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Incorrect!", Toast.LENGTH_SHORT).show();
            }
            // Move to the next question
            currentQuestionIndex++;
            displayQuestion();
        } else {
            Toast.makeText(this, "Invalid answer list or index", Toast.LENGTH_SHORT).show();
        }
    }
}
