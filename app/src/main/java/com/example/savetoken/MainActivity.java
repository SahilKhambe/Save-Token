package com.example.savetoken;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class MainActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;
    Button button;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        button = findViewById(R.id.button_signup);
        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                createUser();
                
            }
        });
    }
    private void createUser() {

        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        if(email.isEmpty()){

            editTextEmail.setError("Email Required");
            editTextEmail.requestFocus();
            return;

        }

        if(password.isEmpty()){

            editTextPassword.setError("Password Required");
            editTextPassword.requestFocus();
            return;

        }

        if(password.length() < 6){

            editTextPassword.setError("Password Should Be 6 Digits");
            editTextPassword.requestFocus();

        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    startProfileActivity();

                }else {

                    if(task.getException() instanceof FirebaseAuthUserCollisionException){

                        userLogin(email, password);

                    }else {

                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }

    private void userLogin(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    startProfileActivity();

                }else {

                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // means user is already logged in
        if (mAuth.getCurrentUser() != null) {
            startProfileActivity();

        }
    }

    private void startProfileActivity() {

        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
}