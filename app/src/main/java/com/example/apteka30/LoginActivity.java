package com.example.apteka30;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

// LoginActivity.java
public class LoginActivity extends AppCompatActivity {

    private EditText email_login;
    private EditText password_login;
    private Button btn_login;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_login = findViewById(R.id.email_login);
        password_login = findViewById(R.id.password_login);
        btn_login = findViewById(R.id.btn_login);

        mAuth = FirebaseAuth.getInstance();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_login.getText().toString();
                String password = password_login.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish(); // Optional: Close the LoginActivity so the user cannot go back to it
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}

