package com.example.test_log;

import static android.os.Environment.getExternalStoragePublicDirectory;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.text.LineBreaker;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Objects;

public class TitleSummary extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private Button button;
    private TextView tvTitle;
    private TextView btnStartActivity;
    private TextView downloadPDF;
    private TextView tvSummary;
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
        tvTitle = findViewById(R.id.tv_title);
        tvSummary = findViewById(R.id.tv_summary);
        downloadPDF = findViewById(R.id.downloadPDF);
        tvTitle.setText(title);
        tvSummary.setText(summary);
        downloadPDF.setText(pdf_file);
        button = findViewById(R.id.logout);
        btnStartActivity = findViewById(R.id.startActivity);

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

                Intent intent = new Intent(TitleSummary.this, QuizAssessment.class);
                startActivity(intent);
                mediaPlayer = MediaPlayer.create(TitleSummary.this, R.raw.lessons);
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer.start();
                    }
                });
            }
        });

        firebaseStorage = FirebaseStorage.getInstance();
        final StorageReference storageRef = firebaseStorage.getReferenceFromUrl(pdf_file);

        downloadPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user != null){
                    String filename = "module" + System.currentTimeMillis() + ".pdf";
                    File localFile =new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"JavaLearn");
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

//        String difficulty = intent.getStringExtra("difficulty");

//        if (Objects.equals(difficulty, "easy")) {
//            JSONObject jsonObject = JSONReader.loadJSONObjectFromAsset(this, "lessons.json");
//
//            if (jsonObject != null) {
//                try {
//                    JSONObject easyObject = jsonObject.getJSONObject("difficulty");
//                    JSONArray lessonEasy = easyObject.getJSONArray("easy");
//
//                    for (int i = 0; i <= lessonEasy.length(); i++) {
//                        JSONObject accessTitle = new JSONObject(String.valueOf((lessonEasy.get(i))));
//
//                        JSONArray questionEasy = accessTitle.getJSONArray("questions");
//
//                        for (int j = 0; j <= questionEasy.length(); j++) {
//                            JSONObject question = questionEasy.getJSONObject(j);
//                            String questionText = question.getString("text");
//                            String answer1 = question.getString("answer1");
//                            String answer2 = question.getString("answer2");
//                            String answer3 = question.getString("answer3");
//                            String answer4 = question.getString("answer4");
//                            String correctAnswer = question.getString("correct");
//                            int value = question.getInt("value");
//                        }
//                    }
//
//                } catch (JSONException e) {
//                    Log.d("catch", "error on catch");
//                    e.printStackTrace();
//                }
//            } else {
//                Log.d("elsejson", "error on json else");
//            }
//        } else {
//            Log.d("objectjson", "error on json object");
//        }

    }
}
