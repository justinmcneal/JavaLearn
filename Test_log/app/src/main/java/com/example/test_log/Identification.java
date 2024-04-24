package com.example.test_log;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.database.ServerValue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Identification extends AppCompatActivity {
    private FirebaseAuth auth;
    private Button button;
    private FirebaseUser user;
    TextView textView;
    private MediaPlayer mediaPlayer;
    private final ArrayList<String> questionArray = new ArrayList<>();
    private final ArrayList<String> answerArray = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private DatabaseReference dbRef;
    private String assessmentId; // Unique ID for each assessment attempt
    private int totalScore = 0; // Total score for the assessment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_periodicidentification);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setupLogoutButton();

        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout);
        textView = findViewById(R.id.user_details);
        user = auth.getCurrentUser();

        Button submitButton = findViewById(R.id.submit_btn);
        submitButton.setOnClickListener(v -> submitAnswer());

        String difficulty = getIntent().getStringExtra("difficulty");
        loadQuestions(difficulty);

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), logpage.class);
            startActivity(intent);
            finish();
        } else {
            textView.setText(user.getEmail());
        }

        dbRef = FirebaseDatabase.getInstance().getReference().child("IdentificationScores");
        assessmentId = generateAssessmentId(); // Generate a unique assessment ID
    }

    private void setupLogoutButton() {
        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout);
        button.setOnClickListener(v -> {
            auth.signOut();
            Intent intent = new Intent(this, logpage.class);
            startActivity(intent);
            mediaPlayer = MediaPlayer.create(this, R.raw.logout);
            mediaPlayer.setOnPreparedListener(MediaPlayer::start);
        });

    }

    private void loadQuestions(String difficulty) {
        try {
            JSONObject jsonObjectQuestions = JSONReader.loadJSONObjectFromAsset(this, "identification.json");
            if (jsonObjectQuestions == null) {
                Log.d("elsejson", "Error loading JSON file");
                return;
            }

            JSONArray questionsArray = jsonObjectQuestions.getJSONObject("difficulty").getJSONArray(difficulty);

            questionArray.clear();
            answerArray.clear();

            for (int i = 0; i < questionsArray.length(); i++) {
                JSONObject questionObject = questionsArray.getJSONObject(i);
                String question = questionObject.getString("question");
                String correctAnswer = questionObject.getString("correct");
                questionArray.add(question);
                answerArray.add(correctAnswer);
            }

            if (!questionArray.isEmpty()) {
                TextView questionDisplay = findViewById(R.id.tv_question);
                questionDisplay.setText(questionArray.get(0));
            }
        } catch (JSONException e) {
            Log.e("catch", "Error parsing JSON data", e);
        }
    }

    private void submitAnswer() {
        EditText answerEditText = findViewById(R.id.editText_question);
        String userAnswer = answerEditText.getText().toString().trim();
        String correctAnswer = answerArray.get(currentQuestionIndex);

        if (userAnswer.equalsIgnoreCase(correctAnswer)) {
            Toast.makeText(this, "Correct answer!", Toast.LENGTH_SHORT).show();
            totalScore++; // Increment total score for correct answer
        } else {
            Toast.makeText(this, "Incorrect answer.", Toast.LENGTH_SHORT).show();
        }

        currentQuestionIndex++;

        if (currentQuestionIndex < questionArray.size()) {
            TextView questionDisplay = findViewById(R.id.tv_question);
            questionDisplay.setText(questionArray.get(currentQuestionIndex));
            answerEditText.getText().clear();
        } else {
            Toast.makeText(this, "You have answered all questions.", Toast.LENGTH_SHORT).show();
            String examName = "Assessment_" + assessmentId; // Use assessmentId for the assessment location
            saveUserScoreToDatabase(examName, totalScore, getIntent().getStringExtra("difficulty"));
            finish(); // Finish the activity when there are no more questions left
        }
    }

    private String generateAssessmentId() {
        // Generate a unique assessment ID based on timestamp or any other criteria
        // For simplicity, here we are using current timestamp
        return String.valueOf(System.currentTimeMillis());
    }

    private void saveUserScoreToDatabase(String examName, int totalScore, String difficultyLevel) {
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String userUid = currentUser.getUid();
            DatabaseReference userScoresRef = dbRef.child(userUid).child(examName);

            // Create a new entry under the assessment location in the database
            DatabaseReference userScoreRef = userScoresRef.push();

            // Store the user score data including difficulty level
            Map<String, Object> userScoreData = new HashMap<>();
            userScoreData.put("totalScore", totalScore);
            userScoreData.put("difficultyLevel", difficultyLevel); // Add difficulty level
            userScoreData.put("timestamp", ServerValue.TIMESTAMP);
            userScoreData.put("userEmail", currentUser.getEmail());

            // Set the value in the database
            userScoreRef.setValue(userScoreData)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("Firebase", "User score stored in Realtime Database");
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firebase", "Error storing user score in Realtime Database", e);
                    });
        } else {
            Log.e("Firebase", "Error: User not logged in");
        }
    }
}
