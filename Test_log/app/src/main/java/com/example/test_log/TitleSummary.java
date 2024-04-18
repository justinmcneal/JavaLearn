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
                int position = TitleSummary.this.getIntent().getIntExtra("position", -1);

                if (position != -1) {
                    ArrayList<String> text = getIntent().getStringArrayListExtra("text");
                    ArrayList<String> answer1 = getIntent().getStringArrayListExtra("answer1");
                    ArrayList<String> answer2 = getIntent().getStringArrayListExtra("answer2");
                    ArrayList<String> answer3 = getIntent().getStringArrayListExtra("answer3");
                    ArrayList<String> answer4 = getIntent().getStringArrayListExtra("answer4");
                    ArrayList<String> correct = getIntent().getStringArrayListExtra("correct");

                    // Call the method to display the question
                    displayQuestion(position, text, answer1, answer2, answer3, answer4, correct);
                } else {
                    // Handle the case where the position is invalid
                    Toast.makeText(TitleSummary.this, "Invalid position", Toast.LENGTH_SHORT).show();
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

    private void displayQuestion(int position, ArrayList<String> text, ArrayList<String> answer1, ArrayList<String> answer2, ArrayList<String> answer3, ArrayList<String> answer4, ArrayList<String> correct) {
        // Check if the position is within the bounds of the arrays
        if (position >= 0 && position < text.size()) {
            // Get the question and its answers at the specified position
            String question = text.get(position);
            String option1 = answer1.get(position);
            String option2 = answer2.get(position);
            String option3 = answer3.get(position);
            String option4 = answer4.get(position);
            String correctAnswer = correct.get(position);

            // Pass the question and its answers to the next activity
            Intent intent = new Intent(TitleSummary.this, QuizAssessment.class);
            intent.putExtra("question", question);
            intent.putExtra("option1", option1);
            intent.putExtra("option2", option2);
            intent.putExtra("option3", option3);
            intent.putExtra("option4", option4);
            intent.putExtra("correctAnswer", correctAnswer);
            startActivity(intent);
        } else {
            // Handle the case where the position is out of bounds
            Toast.makeText(TitleSummary.this, "Question not found", Toast.LENGTH_SHORT).show();
        }
    }
}
