package com.example.apteka30;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfileActivity extends AppCompatActivity {

    private TextView userEmailTextView;
    private Button logoutButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        userEmailTextView = findViewById(R.id.userEmailTextView);
        logoutButton = findViewById(R.id.logoutButton);

        // Получаем текущего пользователя Firebase
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Если пользователь авторизован, отображаем его email
            String userEmail = user.getEmail();
            userEmailTextView.setText(userEmail);
        } else {
            // Если пользователь не авторизован, можете выполнить действия по вашему усмотрению
            userEmailTextView.setText("User is not logged in");
        }

        // Устанавливаем обработчик для кнопки выхода
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    // Метод для выхода пользователя
    private void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(UserProfileActivity.this, LoginActivity.class));
        finish();
    }
}

