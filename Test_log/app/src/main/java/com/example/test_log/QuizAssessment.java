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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuizAssessment extends AppCompatActivity {

    FirebaseAuth auth;
    Button button;
    private int currentQuestionIndex = 0;
    private int score = 0; // Variable to store the user's score

    TextView question;
    TextView choiceA, choiceB, choiceC, choiceD;
    ArrayList<String> textList;
    ArrayList<String> answer1List;
    ArrayList<String> answer2List;
    ArrayList<String> answer3List;
    ArrayList<String> answer4List;
    ArrayList<String> correctList;

    private String title;

    private DatabaseReference dbRef;

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
        String questionsJsonString = intent.getStringExtra("questions");
        title = intent.getStringExtra("title"); // Assign the value here

        dbRef = FirebaseDatabase.getInstance().getReference().child("QuizScores");

        question = findViewById(R.id.question);
        choiceA = findViewById(R.id.choiceA);
        choiceB = findViewById(R.id.choiceB);
        choiceC = findViewById(R.id.choiceC);
        choiceD = findViewById(R.id.choiceD);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), logpage.class);
                startActivity(intent);
            }
        });

        try {
            JSONArray questionsArray = new JSONArray(questionsJsonString);
            // Initialize ArrayLists to store question data
            textList = new ArrayList<>();
            answer1List = new ArrayList<>();
            answer2List = new ArrayList<>();
            answer3List = new ArrayList<>();
            answer4List = new ArrayList<>();
            correctList = new ArrayList<>();

            for (int i = 0; i < questionsArray.length(); i++) {
                JSONObject questionObject = questionsArray.getJSONObject(i);
                // Extract question text
                String text = questionObject.getString("text");
                textList.add(text);
                // Extract answer options
                String answer1 = questionObject.getString("answer1");
                String answer2 = questionObject.getString("answer2");
                String answer3 = questionObject.getString("answer3");
                String answer4 = questionObject.getString("answer4");
                answer1List.add(answer1);
                answer2List.add(answer2);
                answer3List.add(answer3);
                answer4List.add(answer4);

                String correct = questionObject.getString("correct");
                correctList.add(correct);
            }

            // Display the first question
            displayQuestion();

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
        }

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

    private void displayQuestion() {
        if (textList != null && currentQuestionIndex >= 0 && currentQuestionIndex < textList.size()) {
            question.setText(textList.get(currentQuestionIndex));
            choiceA.setText(answer1List.get(currentQuestionIndex));
            choiceB.setText(answer2List.get(currentQuestionIndex));
            choiceC.setText(answer3List.get(currentQuestionIndex));
            choiceD.setText(answer4List.get(currentQuestionIndex));
        } else {
            // Quiz finished, display score
            Toast.makeText(this, "Quiz finished. Your score: " + score + "/" + textList.size(), Toast.LENGTH_LONG).show();
            storeQuizScoreInDatabase();
            finish();
        }
    }

    private void checkAnswer(String selectedAnswer) {
        if (correctList != null && currentQuestionIndex >= 0 && currentQuestionIndex < correctList.size()) {
            String correctAnswer = correctList.get(currentQuestionIndex);
            if (selectedAnswer.equals(correctAnswer)) {
                Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
                score++; // Increment the score for correct answer
            } else {
                Toast.makeText(this, "Incorrect!", Toast.LENGTH_SHORT).show();
            }
            currentQuestionIndex++;
            displayQuestion();
        }
    }

    private void storeQuizScoreInDatabase() {
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String userUid = currentUser.getUid();
            String userEmail = currentUser.getEmail();

            // Create a new entry under the user's UID in the database
            DatabaseReference userScoreRef = dbRef.child(userUid).push();

            // Store the quiz score data
            Map<String, Object> quizScoreData = new HashMap<>();
            quizScoreData.put("userEmail", userEmail);
            quizScoreData.put("score", score);
            quizScoreData.put("title", title);
            quizScoreData.put("timestamp", System.currentTimeMillis());

            // Set the value in the database
            userScoreRef.setValue(quizScoreData)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Quiz score stored in Realtime Database", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error storing quiz score in Realtime Database", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "Error: User not logged in", Toast.LENGTH_SHORT).show();
        }
    }
}