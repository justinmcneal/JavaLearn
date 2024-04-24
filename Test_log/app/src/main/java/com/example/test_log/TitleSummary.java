package com.example.test_log;

import static android.os.Environment.getExternalStoragePublicDirectory;

import android.content.Intent;
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

public class TitleSummary extends AppCompatActivity {
    FirebaseUser user;
    private FirebaseAuth auth;
    private Button button;

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
        button = findViewById(R.id.logout);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String summary = intent.getStringExtra("summary");
        String pdf_file = intent.getStringExtra("pdf_file");

        TextView tvTitle = findViewById(R.id.tv_title);
        TextView tvSummary = findViewById(R.id.tv_summary);
        TextView downloadPDF = findViewById(R.id.downloadPDF);
        TextView btnStartActivity = findViewById(R.id.startActivity);

        tvTitle.setText(title);
        tvSummary.setText(summary);

        btnStartActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject lessonObject = JSONReader.loadJSONObjectFromAsset(TitleSummary.this, "lessons.json");
                    String difficulty = getIntent().getStringExtra("difficulty");
                    int position = getIntent().getIntExtra("position", -1);

                    JSONObject difficultyObject = lessonObject.getJSONObject("difficulty");
                    JSONArray lessonArray = difficultyObject.getJSONArray(difficulty);

                    // Check if the position is valid
                    if (position >= 0 && position < lessonArray.length()) {
                        JSONObject lesson = lessonArray.getJSONObject(position);
                        JSONArray questionsArray = lesson.getJSONArray("questions");

                        Intent intent1 = new Intent(TitleSummary.this, QuizAssessment.class);
                        intent1.putExtra("questions", questionsArray.toString());
                        intent1.putExtra("title", title);
                        startActivity(intent1);
                    } else {
                        Toast.makeText(TitleSummary.this, "Invalid position", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(TitleSummary.this, "Error loading questions", Toast.LENGTH_SHORT).show();
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent logoutIntent = new Intent(getApplicationContext(), logpage.class);
                startActivity(logoutIntent);
            }
        });

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        final StorageReference storageRef = firebaseStorage.getReferenceFromUrl(pdf_file);

        downloadPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user != null) {
                    String filename = title + ".pdf";
                    File localFile = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);
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

