package com.example.test_log;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.Iterator;
import java.util.Objects;

public class LessonsActivity extends AppCompatActivity {

    ArrayList<String> titleArray = new ArrayList<>();
    ArrayList<String> summaryArray = new ArrayList<>();

    AssetManager assetManager = getAssets();

    FirebaseAuth auth;
    Button button;
//    TextView textView;
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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), logpage.class);
                startActivity(intent);
                finish();
            }
        });

        Intent intent = getIntent();
        listView = (ListView) findViewById(R.id.listView);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, titleArray);

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);




        // Extract the data
        String difficulty = intent.getStringExtra("difficulty");

        if (Objects.equals(difficulty, "easy")) {
            JSONObject jsonObject = JSONReader.loadJSONObjectFromAsset(this, "lessons.json"); //to be putted
            if (jsonObject != null) {
                try {
                    //access json data
                    JSONObject easyObject = jsonObject.getJSONObject("difficulty");
                    JSONArray lessonEasy = easyObject.getJSONArray("easy");

                    for (int i = 0; i <= lessonEasy.length(); i++){
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

        if (Objects.equals(difficulty, "medium")) {
            JSONObject jsonObject = JSONReader.loadJSONObjectFromAsset(this, "lessons.json"); //to be putted
            if (jsonObject != null) {
                try {
                    //access json data
                    JSONObject mediumObject = jsonObject.getJSONObject("difficulty");
                    JSONArray lessonMedium = mediumObject.getJSONArray("medium");

                    for (int i = 0; i <= lessonMedium.length(); i++){
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


        if (Objects.equals(difficulty, "hard")) {
            JSONObject jsonObject = JSONReader.loadJSONObjectFromAsset(this, "lessons.json"); //to be putted
            if (jsonObject != null) {
                try {
                    //access json data
                    JSONObject hardObject = jsonObject.getJSONObject("difficulty");
                    JSONArray lessonHard = hardObject.getJSONArray("hard");

                    for (int i = 0; i <= lessonHard.length(); i++){
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

//        listView.getOnItemClickListener() {
//            Intent intent = new Intent(LessonsActivity.this, TitleSummary.class);
//
//            String difficulty = "hard";
//            intent.putExtra("difficulty", difficulty);
//            startActivity(intent);
//            finish();
//
//        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LessonsActivity.this, TitleSummary.class);

                String title = titleArray.get(position);
                String summary = summaryArray.get(position);
                intent.putExtra("title", title);
                intent.putExtra("summary", summary);
            startActivity(intent);
            }
        });


    }
}