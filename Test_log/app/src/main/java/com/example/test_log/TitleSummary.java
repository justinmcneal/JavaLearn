package com.example.test_log;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.text.LineBreaker;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

public class TitleSummary extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private FirebaseAuth auth;
    private Button button;
    private TextView tvTitle;
    private TextView btnStartActivity; // Renamed from startActivity
    private TextView downloadPDF;
    private TextView tvSummary;

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

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String summary = intent.getStringExtra("summary");

        tvTitle = findViewById(R.id.tv_title);
        tvSummary = findViewById(R.id.tv_summary);
        tvTitle.setText(title);
        tvSummary.setText(summary);
        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout);
        btnStartActivity = findViewById(R.id.startActivity); // Renamed from startActivity
        downloadPDF = findViewById(R.id.downloadPDF);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tvSummary.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        }

        button.setOnClickListener(new View.OnClickListener() { //do something
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut(); //kukunin ung data tas isisignout sa app
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

        btnStartActivity.setOnClickListener(v -> {
            Intent quizIntent = new Intent(TitleSummary.this, QuizAssessment.class);
            startActivity(quizIntent);
        });

        downloadPDF.setOnClickListener(v -> {
            String difficulty = intent.getStringExtra("difficulty");
            String directoryName;

            if (Objects.equals(difficulty, "easy")) {
                directoryName = "easypdf";
            } else if (Objects.equals(difficulty, "medium")) {
                directoryName = "mediumpdf";
            } else if (Objects.equals(difficulty, "hard")) {
                directoryName = "hardpdf";
            } else {
                Toast.makeText(TitleSummary.this, "Invalid difficulty level", Toast.LENGTH_SHORT).show();
                return;
            }

            AssetManager assetManager = getAssets();
            try {
                String[] fileList = assetManager.list(directoryName);
                if (fileList != null && fileList.length > 0) {
                    for (String filename : fileList) {
                        try (InputStream in = assetManager.open(directoryName + "/" + filename);
                             OutputStream out = new FileOutputStream(new File(getExternalFilesDir(null), filename))) {

                            byte[] buffer = new byte[1024];
                            int read;
                            while ((read = in.read(buffer)) != -1) {
                                out.write(buffer, 0, read);
                            }

                            Toast.makeText(TitleSummary.this, "PDF files downloaded successfully", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Log.e("PDFDownload", "Error downloading PDF files", e);
                        }
                    }
                } else {
                    Log.e("PDFDownload", "No files found in directory: " + directoryName);
                }
            } catch (IOException e) {
                Log.e("PDFDownload", "Error listing files in directory", e);
            }
        });
    }
}
