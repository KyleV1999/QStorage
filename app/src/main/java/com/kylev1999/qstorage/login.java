package com.kylev1999.qstorage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button signin;
    private Button register;
    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findallviews();
        handleLogin();

        if (mAuth.getCurrentUser() != null) { //Bypass login if signed in
            Log.d("QStore", "Not Signed in!");
            startActivity(new Intent(login.this, MainActivity.class));
            finish();
        }
    }

    private void handleLogin() {
        mAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(login.this, registration.class);
                startActivity(register);
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(String.valueOf(email.getText()), String.valueOf(password.getText()));
            }
        });

    }

    private void loginUser(String email, String password) {
        if (email.length()==0 || password.length()==0){
            Toast.makeText(getApplicationContext(), "Email and password cannot be empty",
                    Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("QStore", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            gotoMA();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("QStore", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_LONG).show();
                        }

                        // ...
                    }
                });
    }

    private void gotoMA() {
        Intent MAintent = new Intent(login.this, MainActivity.class);
        startActivity(MAintent);
        finish();
    }


    private void findallviews() {
        signin = findViewById(R.id.login_butt);
        register = findViewById (R.id.register_butt);
        email = findViewById(R.id.enter_email);
        password = findViewById(R.id.enter_password);
    }
 }
