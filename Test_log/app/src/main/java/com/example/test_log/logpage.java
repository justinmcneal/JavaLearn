package com.example.test_log;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class logpage extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    TextView textView;

    TextInputEditText editTextEmail, editTextPassword;
    Button buttonLogin;

    FirebaseAuth mAuth; //authentication

    ProgressBar progressBar; //design lng hihe
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){ //if may account na naka sign in si user mag aautomatic mag iistart ung next activity
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        } // lagyan ko paba toast to sa else pag di nag login??? T,T wag na
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) { //limited access, void is the return type, does not return anything.
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_logpage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets; //built in
        });

        mAuth = FirebaseAuth.getInstance(); //firebase my authentication you can check the document on firebase
        progressBar = findViewById(R.id.progressBar); //just the loading screen
        editTextEmail = findViewById(R.id.email); //call the id email
        editTextPassword = findViewById(R.id.password); //call the id password
        buttonLogin = findViewById(R.id.btn_signin); //call the id that when click will go to the other activity
        textView = findViewById(R.id.othersignup); //call the id that when click will go to the other activity on sign up
        textView.setOnClickListener(new View.OnClickListener() { //gives function
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(logpage.this,signpage.class);
                startActivity(intent);
                finish();

            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() { //gives function
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE); //loading object will be visible once it loads
                String email, password; //new variable to convert the textview into string
                email = String.valueOf(editTextEmail.getText()); //get the value and the text just to save in mauth
                password = String.valueOf(editTextPassword.getText()); //same goes

                if (TextUtils.isEmpty(email)){ //pag walang nilagay mag ttoast sya
                    Toast.makeText(logpage.this,"Enter Email", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE); //loading object will be visible once it loads
                    mediaPlayer = MediaPlayer.create(logpage.this, R.raw.error);
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {

                            mediaPlayer.start();
                        }
                    });
                    return;
                }

                if (TextUtils.isEmpty(password)){ //same goes
                    Toast.makeText(logpage.this,"Enter Password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE); //loading object will be visible once it loads
                    mediaPlayer = MediaPlayer.create(logpage.this, R.raw.error);
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {

                            mediaPlayer.start();
                        }
                    });
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password) //firebase docs reference
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() { //when complete, do something
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) { //copy lang sa firebase build
                                progressBar.setVisibility(View.GONE); //if successful, remove progress bar
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Login Successful",
                                            Toast.LENGTH_SHORT).show(); //mag ppopup to
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    mediaPlayer = MediaPlayer.create(logpage.this, R.raw.mismongsigninout);
                                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                        @Override
                                        public void onPrepared(MediaPlayer mp) {
                                            mediaPlayer.start();
                                        }
                                    });
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Toast.makeText(logpage.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show(); //magppop up to
                                    mediaPlayer = MediaPlayer.create(logpage.this, R.raw.error);
                                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                        @Override
                                        public void onPrepared(MediaPlayer mp) {
                                            mediaPlayer.start();
                                        }
                                    });
                                }
                            }
                        });



            }
        });
    }
}