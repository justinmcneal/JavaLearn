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
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class signpage extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    TextView textView;

    TextInputEditText editTextEmail, editTextPassword, editTextName; //same goes
    Button buttonSign; //same goes

    FirebaseAuth mAuth; //same goes

    ProgressBar progressBar; //same goes

    FirebaseFirestore db = FirebaseFirestore.getInstance();

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) { //limited access, void is the return type, does not return anything.
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signpage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets; //built in
        });


        mAuth = FirebaseAuth.getInstance(); //same same lng
        progressBar = findViewById(R.id.progressBar);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
//        editTextName = findViewById(R.id.fullname); //to lang nadagdag
        buttonSign = findViewById(R.id.btn_signup);

        textView = findViewById(R.id.othersignin);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signpage.this,signpage.class);
                startActivity(intent);
                finish();
            }
        });

        buttonSign.setOnClickListener(new View.OnClickListener() { //when click do something
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE); //set the progress bar to visible
                String email, password; //create string variable
                email = String.valueOf(editTextEmail.getText()); //then call the created variable to get the value of edittextemail to be stored in firebase
                password = String.valueOf(editTextPassword.getText()); //same goes

                mediaPlayer = MediaPlayer.create(signpage.this, R.raw.mismongsigninout);
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer.start();
                    }
                });

                if (TextUtils.isEmpty(email)){ //same goes
                    Toast.makeText(signpage.this,"Enter Email", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(password)){ //same goes
                    Toast.makeText(signpage.this,"Enter Password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email,password) //user authentication firebase get the email ans password
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() { //copied on firebase then added toasts
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE); //remove the progress bar when done

                                if (task.isSuccessful()) {
                                    Toast.makeText(signpage.this, "Account created.", //same goes
                                            Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(signpage.this,logpage.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(signpage.this, "Authentication failed.", //samegoes
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}