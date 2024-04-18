package com.example.test_log;

import static android.os.Environment.getExternalStoragePublicDirectory;

import android.content.Intent;
import android.graphics.text.LineBreaker;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class TitleSummary extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    FirebaseUser user;
    FirebaseAuth auth;
    FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_title_summary);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String summary = intent.getStringExtra("summary");
        String pdf_file = intent.getStringExtra("pdf_file");
        String difficulty = intent.getStringExtra("difficulty"); // Retrieve the difficulty here
        TextView tvTitle = findViewById(R.id.tv_title);
        TextView tvSummary = findViewById(R.id.tv_summary);
        TextView downloadPDF = findViewById(R.id.downloadPDF);
        tvTitle.setText(title);
        tvSummary.setText(summary);
        Button button = findViewById(R.id.logout);
        TextView btnStartActivity = findViewById(R.id.startActivity);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tvSummary.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), logpage.class);
                startActivity(intent);

                mediaPlayer = MediaPlayer.create(TitleSummary.this, R.raw.logout);
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer.start();
                    }
                });
            }
        });

        btnStartActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject jsonObject = JSONReader.loadJSONObjectFromAsset(TitleSummary.this, "lessons.json");
                    if (jsonObject == null) {
                        Log.d("elsejson", "Error loading JSON file");
                        return;
                    }

                    JSONObject difficultyObject = jsonObject.getJSONObject("difficulty");
                    JSONArray lessonArray = difficultyObject.getJSONArray(difficulty);

                    for (int i = 0; i < lessonArray.length(); i++) {
                        JSONObject lessonObject = lessonArray.getJSONObject(i);
                        JSONArray questionsArray = lessonObject.getJSONArray("questions");

                        // Loop through the questions array
                        for (int j = 0; j < questionsArray.length(); j++) {
                            JSONObject questionObject = questionsArray.getJSONObject(j);
                            String text = questionObject.getString("text");
                            String answer1 = questionObject.getString("answer1");
                            String answer2 = questionObject.getString("answer2");
                            String answer3 = questionObject.getString("answer3");
                            String answer4 = questionObject.getString("answer4");
                            String correct = questionObject.getString("correct");

                            // Display or use the question data as needed
                            Log.d("Question", "Question: " + text);
                            Log.d("Answer1", "Answer1: " + answer1);
                            Log.d("Answer2", "Answer2: " + answer2);
                            Log.d("Answer3", "Answer3: " + answer3);
                            Log.d("Answer4", "Answer4: " + answer4);
                            Log.d("Correct", "Correct: " + correct);
                        }
                    }
                } catch (JSONException e) {
                    Log.e("catch", "Error parsing JSON data", e);
                }
            }
        });

        firebaseStorage = FirebaseStorage.getInstance();
        final StorageReference storageRef = firebaseStorage.getReferenceFromUrl(pdf_file);

        downloadPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user != null){
                    String filename = title + ".pdf";
                    File localFile =new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),filename);
                    storageRef.getFile(localFile)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(TitleSummary.this, "File downloaded", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(TitleSummary.this, localFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                                    Log.d("catch", localFile.getAbsolutePath());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                    Toast.makeText(TitleSummary.this, "Error downloading file", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(TitleSummary.this, "no user logged in ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
