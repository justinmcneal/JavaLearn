package com.example.test_log;

import android.content.Intent;
import android.graphics.text.LineBreaker;
import android.icu.text.CaseMap;
import android.os.Build;
import android.os.Bundle;
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

import org.w3c.dom.Text;

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tvSummary.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD); //jinustify ang text
        }

//        textView = findViewById(R.id.user_details);
//        user = auth.getCurrentUser();
//
//        if(user == null){
//            Intent intent = new Intent(getApplicationContext(), logpage.class);
//            startActivity(intent);
//            finish();
//        } else {
//            textView.setText(user.getEmail());
//        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), logpage.class);
                startActivity(intent);
                finish();
            }
        });

        startActivity = findViewById(R.id.startActivity);
        downloadPDF = findViewById(R.id.downloadPDF);


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
//                Intent intent = new Intent(TitleSummary.this, QuizAssessment.class);
//                startActivity(intent);
//            }
//        }); idk pa eh


    }
}