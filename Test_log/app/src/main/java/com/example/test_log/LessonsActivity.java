package com.example.test_log;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LessonsActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private Button button;
    private FirebaseUser user;
    TextView textView;
    private MediaPlayer mediaPlayer;
    private final ArrayList<String> titleArray = new ArrayList<>();
    private final ArrayList<String> summaryArray = new ArrayList<>();
    private final ArrayList<String> pdfArray = new ArrayList<>();
    private final ArrayList<String> textArray = new ArrayList<>();
    private final ArrayList<String> answer1Array= new ArrayList<>();
    private final ArrayList<String> answer2Array= new ArrayList<>();
    private final ArrayList<String> answer3Array= new ArrayList<>();
    private final ArrayList<String> answer4Array= new ArrayList<>();
    private final ArrayList<String> correctArray= new ArrayList<>();
    private final ArrayList<Integer> valueArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lessons);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupLogoutButton();
        setupListView();
        parseLessonsData(getIntent().getStringExtra("difficulty"));

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



    private void parseLessonsData(String difficulty) {
        if (!Objects.equals(difficulty, "easy") && !Objects.equals(difficulty, "medium") && !Objects.equals(difficulty, "hard")) {
            Log.d("objectjson", "Invalid difficulty level");
            return;
        }

        try {
            JSONObject jsonObject = JSONReader.loadJSONObjectFromAsset(this, "lessons.json");
            if (jsonObject == null) {
                Log.d("elsejson", "Error loading JSON file");
                return;
            }

            JSONObject difficultyObject = jsonObject.getJSONObject("difficulty");
            JSONArray lessonArray = difficultyObject.getJSONArray(difficulty);

            for (int i = 0; i < lessonArray.length(); i++) {
                JSONObject lessonObject = new JSONObject(String.valueOf(lessonArray.get(i)));
                String title = lessonObject.getString("title");
                String summary = lessonObject.getString("summary");
                String pdf_file = lessonObject.getString("pdf_file");
                titleArray.add(title);
                summaryArray.add(summary);
                pdfArray.add(pdf_file);

                // Access and parse the questions array
                JSONArray questionsArray = lessonObject.getJSONArray("questions");
                for (int j = 0; j < questionsArray.length(); j++) {
                    JSONObject questionObject = questionsArray.getJSONObject(j);
                    String text = questionObject.getString("text");
                    String answer1 = questionObject.getString("answer1");
                    String answer2 = questionObject.getString("answer2");
                    String answer3 = questionObject.getString("answer3");
                    String answer4 = questionObject.getString("answer4");
                    String correct = questionObject.getString("correct");

                    textArray.add(text);
                    answer1Array.add(answer1);
                    answer2Array.add(answer2);
                    answer3Array.add(answer3);
                    answer4Array.add(answer4);
                    correctArray.add(correct);
                }
            }

        } catch (JSONException e) {
            Log.e("catch", "Error parsing JSON data", e);
        }
    }

    private void setupListView() {
        user = auth.getCurrentUser();
        ListView listView = findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, titleArray);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(LessonsActivity.this, TitleSummary.class);
            intent.putExtra("title", titleArray.get(position));
            intent.putExtra("summary", summaryArray.get(position));
            intent.putExtra("pdf_file", pdfArray.get(position));
            intent.putExtra("position", position); // Pass the position here

            intent.putExtra("text", textArray.get(position));
            intent.putExtra("answer1", answer1Array.get(position));
            intent.putExtra("answer2", answer2Array.get(position));
            intent.putExtra("answer3", answer3Array.get(position));
            intent.putExtra("answer4", answer4Array.get(position));
            intent.putExtra("correct", correctArray.get(position));
            Log.d("Position", "Position in LessonsActivity: " + position);


            mediaPlayer = MediaPlayer.create(this, R.raw.lessons);
            mediaPlayer.setOnPreparedListener(MediaPlayer::start);
            startActivity(intent);
        });
    }
}