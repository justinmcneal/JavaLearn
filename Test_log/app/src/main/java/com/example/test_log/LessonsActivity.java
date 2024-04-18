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


    public void parseLessonsData(String difficulty) {
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
                JSONObject lessonObject = lessonArray.getJSONObject(i);
                String title = lessonObject.getString("title");
                String summary = lessonObject.getString("summary");
                String pdf_file = lessonObject.getString("pdf_file");
                titleArray.add(title);
                summaryArray.add(summary);
                pdfArray.add(pdf_file);

                ArrayList<String> lessonTextArray = new ArrayList<>();
                ArrayList<String> lessonAnswer1Array = new ArrayList<>();
                ArrayList<String> lessonAnswer2Array = new ArrayList<>();
                ArrayList<String> lessonAnswer3Array = new ArrayList<>();
                ArrayList<String> lessonAnswer4Array = new ArrayList<>();
                ArrayList<String> lessonCorrectArray = new ArrayList<>();

                JSONArray questionsArray = lessonObject.getJSONArray("questions");
                for (int j = 0; j < Math.min(questionsArray.length(), 10); j++) {
                    JSONObject questionObject = questionsArray.getJSONObject(j);
                    String text = questionObject.getString("text");
                    String answer1 = questionObject.getString("answer1");
                    String answer2 = questionObject.getString("answer2");
                    String answer3 = questionObject.getString("answer3");
                    String answer4 = questionObject.getString("answer4");
                    String correct = questionObject.getString("correct");

                    lessonTextArray.add(text);
                    lessonAnswer1Array.add(answer1);
                    lessonAnswer2Array.add(answer2);
                    lessonAnswer3Array.add(answer3);
                    lessonAnswer4Array.add(answer4);
                    lessonCorrectArray.add(correct);
                }

                textArray.add(lessonTextArray.toString());
                answer1Array.add(lessonAnswer1Array.toString());
                answer2Array.add(lessonAnswer2Array.toString());
                answer3Array.add(lessonAnswer3Array.toString());
                answer4Array.add(lessonAnswer4Array.toString());
                correctArray.add(lessonCorrectArray.toString());
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

            // pass lists of questions and answers for the selected lesson
            intent.putStringArrayListExtra("text", textArray);
            intent.putStringArrayListExtra("answer1", answer1Array);
            intent.putStringArrayListExtra("answer2", answer2Array);
            intent.putStringArrayListExtra("answer3", answer3Array);
            intent.putStringArrayListExtra("answer4", answer4Array);
            intent.putStringArrayListExtra("correct", correctArray);

            Log.d("Position", "Position in LessonsActivity: " + position);

            mediaPlayer = MediaPlayer.create(this, R.raw.lessons);
            mediaPlayer.setOnPreparedListener(MediaPlayer::start);
            startActivity(intent);
        });
    }


}