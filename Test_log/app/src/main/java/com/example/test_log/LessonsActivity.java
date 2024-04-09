package com.example.test_log;

import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

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
import java.util.Objects;

public class LessonsActivity extends AppCompatActivity {
    private  MediaPlayer mediaPlayer;
    ArrayList<String> titleArray = new ArrayList<>(); //since array na ung object sa json nag create tayo array for key-pair value under nung obj here para ma integrate sa function later on
    ArrayList<String> summaryArray = new ArrayList<>();
    FirebaseAuth auth;
    Button button;
    FirebaseUser user;
    ListView listView;

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

        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout);
        user = auth.getCurrentUser();
        Intent intent = getIntent();
        listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter adapter = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, titleArray);
        listView.setAdapter(adapter);
        String difficulty = intent.getStringExtra("difficulty");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), logpage.class);
                startActivity(intent);
                mediaPlayer = MediaPlayer.create(LessonsActivity.this, R.raw.logout);
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {

                        mediaPlayer.start();
                    }
                });
            }
        });

        if (Objects.equals(difficulty, "easy")) {
            JSONObject jsonObject = JSONReader.loadJSONObjectFromAsset(this, "lessons.json");

            if (jsonObject != null) {
                try {
                    JSONObject easyObject = jsonObject.getJSONObject("difficulty");
                    JSONArray lessonEasy = easyObject.getJSONArray("easy");

                    for (int i = 0; i <= lessonEasy.length(); i++) {
                        JSONObject accessTitle = new JSONObject(String.valueOf(lessonEasy.get(i)));
                        String title = accessTitle.getString("title");
                        String summary = accessTitle.getString("summary");
                        titleArray.add(title);
                        summaryArray.add(summary);
                    }
                } catch (JSONException e) {
                    Log.d("catch", "error on catch");
                    e.printStackTrace();
                }
            } else {
                Log.d("elsejson", "error on json else");
            }
        } else {
            Log.d("objectjson", "error on json object");
        }

        if (Objects.equals(difficulty, "medium")) { //same goes but medium
            JSONObject jsonObject = JSONReader.loadJSONObjectFromAsset(this, "lessons.json"); //to be putted
            if (jsonObject != null) {
                try {
                    //access json data
                    JSONObject mediumObject = jsonObject.getJSONObject("difficulty");
                    JSONArray lessonMedium = mediumObject.getJSONArray("medium");

                    for (int i = 0; i <= lessonMedium.length(); i++) {
                        JSONObject accessTitle = new JSONObject(String.valueOf(lessonMedium.get(i)));
                        String title = accessTitle.getString("title");
                        String summary = accessTitle.getString("summary");
                        titleArray.add(title);
                        summaryArray.add(summary);
                    }
                } catch (JSONException e) {
                    Log.d("catch", "error on catch");
                    e.printStackTrace();
                }
            } else {
                Log.d("elsejson", "error on json else");
            }
        } else {
            Log.d("objectjson", "error on json object");
        }

        if (Objects.equals(difficulty, "hard")) { //same goes but hard
            JSONObject jsonObject = JSONReader.loadJSONObjectFromAsset(this, "lessons.json"); //to be putted
            if (jsonObject != null) {
                try {
                    //access json data
                    JSONObject hardObject = jsonObject.getJSONObject("difficulty");
                    JSONArray lessonHard = hardObject.getJSONArray("hard");

                    for (int i = 0; i <= lessonHard.length(); i++) {
                        JSONObject accessTitle = new JSONObject(String.valueOf(lessonHard.get(i)));
                        String title = accessTitle.getString("title");
                        String summary = accessTitle.getString("summary");
                        titleArray.add(title);
                        summaryArray.add(summary);
                    }
                } catch (JSONException e) {
                    Log.d("catch", "error on catch");
                    e.printStackTrace();
                }
            } else {
                Log.d("elsejson", "error on json else");
            }
        } else {
            Log.d("objectjson", "error on json object");
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LessonsActivity.this, TitleSummary.class);

                String title = titleArray.get(position);
                String summary = summaryArray.get(position);
                intent.putExtra("title", title);
                intent.putExtra("summary", summary); //

                mediaPlayer = MediaPlayer.create(LessonsActivity.this, R.raw.lessons);
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer.start();
                    }
                });
                startActivity(intent);
            }
        });
    }


}
