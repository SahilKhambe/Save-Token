package com.example.savetoken;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.installations.FirebaseInstallations;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    public static final String NODE_USERS = "users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        FirebaseInstallations.getInstance().getId().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {

                if(task.isSuccessful()){

                    String token = task.getResult();
                    saveToken(token);

                }else {

                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // means user is not logged in
        if (mAuth.getCurrentUser() == null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void saveToken(String token){

        String email = mAuth.getCurrentUser().getEmail();
        User user = new User(email, token);

        DatabaseReference dbUsers = FirebaseDatabase.getInstance().getReference(NODE_USERS);

        dbUsers.child(mAuth.getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    Toast.makeText(ProfileActivity.this, "Token Saved", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}