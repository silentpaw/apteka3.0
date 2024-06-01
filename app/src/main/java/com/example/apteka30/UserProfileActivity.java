package com.example.apteka30;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class UserProfileActivity extends AppCompatActivity {

    private TextView userEmailTextView;
    private RecyclerView recentlyViewedRecyclerView;
    private MedicineAdapter adapter;
    private List<Medicine> recentlyViewedList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        userEmailTextView = findViewById(R.id.userEmailTextView);
        recentlyViewedRecyclerView = findViewById(R.id.recentlyViewedRecyclerView);
        recentlyViewedRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Получение электронной почты текущего пользователя
        String userEmail = getCurrentUserEmail();

        // Установка текста электронной почты пользователя
        userEmailTextView.setText(userEmail);

        // Получение последних просмотренных лекарств
        loadRecentlyViewedMedicines();

        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(UserProfileActivity.this, LoginActivity.class));
            finish();
        });
    }

    private String getCurrentUserEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return (user != null) ? user.getEmail() : "Неизвестный пользователь";
    }

    private void loadRecentlyViewedMedicines() {
        MedicineDatabaseHelper dbHelper = new MedicineDatabaseHelper(this);
        recentlyViewedList = dbHelper.getRecentlyViewedMedicines();

        // Создание и установка адаптера для RecyclerView
        adapter = new MedicineAdapter(this, recentlyViewedList); // Передаем контекст
        recentlyViewedRecyclerView.setAdapter(adapter);
    }
}
