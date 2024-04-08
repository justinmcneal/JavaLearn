package com.example.test_log;

import android.content.Intent;
import android.graphics.text.LineBreaker;
import android.icu.text.CaseMap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class TitleSummary extends AppCompatActivity {

    FirebaseAuth auth;
    Button button;
    TextView tvTitle;
    TextView startActivity;
    TextView downloadPDF;
    TextView tvSummary;
//    FirebaseUser user;

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

        Intent intent = getIntent(); //get the intent earlier
        String title = intent.getStringExtra("title"); //get the extra kanina
        String summary = intent.getStringExtra("summary"); //ganun lng din

        tvTitle = findViewById(R.id.tv_title);
        tvSummary = findViewById(R.id.tv_summary);
        tvTitle.setText(title);
        tvSummary.setText(summary);
        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout);
        startActivity = findViewById(R.id.startActivity);
        downloadPDF = findViewById(R.id.downloadPDF);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tvSummary.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD); //jinustify ang text
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), logpage.class);
                startActivity(intent);
                finish();
            }
        });

        startActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TitleSummary.this, QuizAssessment.class);
                startActivity(intent);
            }
        });

//        downloadPDF.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String directoryName; //waaaa
//                String difficulty = intent.getStringExtra("difficulty");
//                String fileAssets;
//
//                if (Objects.equals(difficulty, "easy")) {
//                    directoryName = "easypdf"; //waaaa
//                    fileAssets = String.valueOf(getAssets());
//                        if ( fileAssets!= null) {
//                            try {
//                                String[] fileList = getAssets().list(directoryName);
//                                if (fileList != null && fileList.length > 0) {
//                                    for (String filename: fileList) {
//                                        String filePath = directoryName + "/" + filename;
//
//                                        File outputFile = new File(getExternalFilesDir(null), filename);
//
//
//                                    }
//
//                                }
//                            }





//
//            }
//
//
//
//
//
//
//
//                // Get a list of all files in the selected directory
//                try {
//                    String[] fileList = getAssets().list(directoryName);
//                    if (fileList != null && fileList.length > 0) {
//                        for (String fileName : fileList) {
//                            // Construct the full path to each PDF file
//                            String filePath = directoryName + "/" + fileName;
//
//                            // Copy the PDF file from assets to external storage
//                            File outputFile = new File(getExternalFilesDir(null), fileName);
//                            try (InputStream inputStream = getAssets().open(filePath);
//                                 OutputStream outputStream = new FileOutputStream(outputFile)) {
//
//                                byte[] buffer = new byte[4 * 1024];
//                                int read;
//                                while ((read = inputStream.read(buffer)) != -1) {
//                                    outputStream.write(buffer, 0, read);
//                                }
//
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//        });
//
//
    }
}