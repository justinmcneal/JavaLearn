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
import java.util.Objects;


public class TitleSummary extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    FirebaseUser user;
    FirebaseAuth auth;
    FirebaseStorage firebaseStorage;
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
                // Get the position of the item clicked
                int position = getIntent().getIntExtra("position", -1);

                if (position != -1) {
                    Intent intent = new Intent(TitleSummary.this, QuizAssessment.class);
                    // Pass the data including the correct answer
                    intent.putExtra("text", textArray.get(position));
                    intent.putExtra("answer1", answer1Array.get(position));
                    intent.putExtra("answer2", answer2Array.get(position));
                    intent.putExtra("answer3", answer3Array.get(position));
                    intent.putExtra("answer4", answer4Array.get(position));
                    intent.putExtra("correct", correctArray.get(position));
                    Log.d("Position", "Position in TitleSummary: " + position);
                    Log.d("Array", "TextArray: " + textArray);
                    Log.d("Array", "Answer1Array: " + answer1Array);

                    mediaPlayer = MediaPlayer.create(TitleSummary.this, R.raw.lessons);
                    mediaPlayer.setOnPreparedListener(MediaPlayer::start);
                    startActivity(intent);
                } else {
                    // Handle the case where the position is invalid
                    Toast.makeText(TitleSummary.this, "Invalid position", Toast.LENGTH_SHORT).show();
                    Log.d("error here", "error");

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
