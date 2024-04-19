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

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

public class TitleSummary extends AppCompatActivity {
    private FirebaseAuth auth;
    private Button button;
    private int currentQuestionIndex = 0;
    private TextView question;
    private TextView choiceA, choiceB, choiceC, choiceD;
    private ArrayList<String> questionTextArray;
    private ArrayList<String> answer1Array;
    private ArrayList<String> answer2Array;
    private ArrayList<String> answer3Array;
    private ArrayList<String> answer4Array;
    private ArrayList<String> correctList;
    FirebaseUser user;

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

        auth = FirebaseAuth.getInstance(); // Initialize auth here
        user = auth.getCurrentUser();
        button = findViewById(R.id.logout);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String summary = intent.getStringExtra("summary");
        String pdf_file = intent.getStringExtra("pdf_file");
        String difficulty = intent.getStringExtra("difficulty");

        TextView tvTitle = findViewById(R.id.tv_title);
        TextView tvSummary = findViewById(R.id.tv_summary);
        TextView downloadPDF = findViewById(R.id.downloadPDF);
        TextView btnStartActivity = findViewById(R.id.startActivity);

        tvTitle.setText(title);
        tvSummary.setText(summary);

        button.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent logoutIntent = new Intent(getApplicationContext(), logpage.class);
            startActivity(logoutIntent);
        });

        questionTextArray = intent.getStringArrayListExtra("questionTextArray");
        answer1Array = intent.getStringArrayListExtra("answer1Array");
        answer2Array = intent.getStringArrayListExtra("answer2Array");
        answer3Array = intent.getStringArrayListExtra("answer3Array");
        answer4Array = intent.getStringArrayListExtra("answer4Array");
        correctList = intent.getStringArrayListExtra("correct");

        btnStartActivity.setOnClickListener(v -> {
            Intent quizIntent = new Intent(TitleSummary.this, QuizAssessment.class);
            quizIntent.putStringArrayListExtra("text", questionTextArray);
            quizIntent.putStringArrayListExtra("answer1", answer1Array);
            quizIntent.putStringArrayListExtra("answer2", answer2Array);
            quizIntent.putStringArrayListExtra("answer3", answer3Array);
            quizIntent.putStringArrayListExtra("answer4", answer4Array);
            quizIntent.putStringArrayListExtra("correct", correctList);
            startActivity(quizIntent);
        });

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
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

