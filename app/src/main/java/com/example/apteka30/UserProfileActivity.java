package com.example.apteka30;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class UserProfileActivity extends AppCompatActivity {

    private TextView userEmailTextView;
    private Button logoutButton;
    private RecyclerView favoritesRecyclerView;
    private MedicineAdapter adapter;
    private MedicineDatabaseHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        userEmailTextView = findViewById(R.id.userEmailTextView);
        logoutButton = findViewById(R.id.logoutButton);
        favoritesRecyclerView = findViewById(R.id.favoritesRecyclerView);

        // Настройка RecyclerView
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new MedicineDatabaseHelper(this);

        // Получаем текущего пользователя Firebase
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Если пользователь авторизован, отображаем его email
            String userEmail = user.getEmail();
            userEmailTextView.setText(userEmail);

            // Получаем и отображаем избранные лекарства
            List<Medicine> favoriteMedicines = dbHelper.getFavoriteMedicines();
            if (favoriteMedicines.isEmpty()) {
                Toast.makeText(this, "No favorite medicines", Toast.LENGTH_SHORT).show();
            } else {
                adapter = new MedicineAdapter(this, favoriteMedicines);
                favoritesRecyclerView.setAdapter(adapter);
            }
        } else {
            // Если пользователь не авторизован, можете выполнить действия по вашему усмотрению
            userEmailTextView.setText("User is not logged in");
        }

        // Устанавливаем обработчик для кнопки выхода
        logoutButton.setOnClickListener(v -> logout());
    }

    // Метод для выхода пользователя
    private void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(UserProfileActivity.this, LoginActivity.class));
        finish();
    }
}
