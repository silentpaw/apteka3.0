package com.example.apteka30;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailRegister;
    private EditText passwordRegister;
    private Button btnRegister;
    private TextView loginTxt;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailRegister = findViewById(R.id.email_register);
        passwordRegister = findViewById(R.id.password_register);
        btnRegister = findViewById(R.id.btn_register);
        loginTxt = findViewById(R.id.register_txt);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        loginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String email = emailRegister.getText().toString();
        String password = passwordRegister.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(RegisterActivity.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(RegisterActivity.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            saveUserToFirestore(user);
                            startActivity(new Intent(RegisterActivity.this, UserProfileActivity.class));
                            Toast.makeText(RegisterActivity.this, "Registration successful.", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveUserToFirestore(FirebaseUser user) {
        String userId = user.getUid();
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("email", user.getEmail());

        db.collection("users").document(userId)
                .set(userMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "User data saved.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}