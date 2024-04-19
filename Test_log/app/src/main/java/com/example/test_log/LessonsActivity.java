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

public class LessonsActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private Button button;
    private FirebaseUser user;
    TextView textView;
    private MediaPlayer mediaPlayer;
    private final ArrayList<String> titleArray = new ArrayList<>();
    private final ArrayList<String> summaryArray = new ArrayList<>();
    private final ArrayList<String> pdfArray = new ArrayList<>();

    private final ArrayList<String> questionTextArray = new ArrayList<>();
    private final ArrayList<String> answer1Array = new ArrayList<>();
    private final ArrayList<String> answer2Array = new ArrayList<>();
    private final ArrayList<String> answer3Array = new ArrayList<>();
    private final ArrayList<String> answer4Array = new ArrayList<>();

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
                JSONArray lessons = lessonObject.getJSONArray("lesson");

                for (int j = 0; j < lessons.length(); j++) {
                    JSONObject singleLesson = lessons.getJSONObject(j);
                    String title = singleLesson.getString("title");
                    String summary = singleLesson.getString("summary");
                    String pdf_file = singleLesson.getString("pdf_file");
                    titleArray.add(title);
                    summaryArray.add(summary);
                    pdfArray.add(pdf_file);

                    JSONArray questionsArray = singleLesson.getJSONArray("questions");

                    for (int k = 0; k < questionsArray.length(); k++) {
                        JSONObject questionObject = questionsArray.getJSONObject(k);
                        String questionText = questionObject.getString("text");
                        String answer1 = questionObject.getString("answer1");
                        String answer2 = questionObject.getString("answer2");
                        String answer3 = questionObject.getString("answer3");
                        String answer4 = questionObject.getString("answer4");

                        questionTextArray.add(questionText);
                        answer1Array.add(answer1);
                        answer2Array.add(answer2);
                        answer3Array.add(answer3);
                        answer4Array.add(answer4);

                    }
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
            intent.putExtra("difficulty", getIntent().getStringExtra("difficulty")); // Retrieve difficulty from the intent
            intent.putExtra("position", position); // Pass the position here

            intent.putStringArrayListExtra("questionTextArray", questionTextArray);
            intent.putStringArrayListExtra("answer1Array", answer1Array);
            intent.putStringArrayListExtra("answer2Array", answer2Array);
            intent.putStringArrayListExtra("answer3Array", answer3Array);
            intent.putStringArrayListExtra("answer4Array", answer4Array);

            Log.d("Position", "Position in LessonsActivity: " + position);

            mediaPlayer = MediaPlayer.create(this, R.raw.lessons);
            mediaPlayer.setOnPreparedListener(MediaPlayer::start);
            startActivity(intent);
        });
    }
}