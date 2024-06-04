package com.example.apteka30;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import android.view.View;
import android.widget.Button;

import android.widget.Toast;


public class MedicineDetailActivity extends AppCompatActivity {
    private MedicineDatabaseHelper dbHelper;
    private ImageView imageView;
    private TextView nameTextView;
    private TextView descriptionTextView;
    private Button addToFavoritesButton;
    private int medicineId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_detail);

        imageView = findViewById(R.id.imageView);
        nameTextView = findViewById(R.id.nameTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        addToFavoritesButton = findViewById(R.id.addToFavoritesButton);

        dbHelper = new MedicineDatabaseHelper(this);

        Intent intent = getIntent();
        medicineId = intent.getIntExtra("medicine_id", -1);

        if (medicineId != -1) {
            Medicine medicine = dbHelper.getMedicineById(medicineId);
            if (medicine != null) {
                nameTextView.setText(medicine.getName());
                descriptionTextView.setText(medicine.getDescription());
                Picasso.get()
                        .load(medicine.getImagePath())
                        .placeholder(R.drawable.placeholder) // Замените на ваш ресурс заполнителя
                        .error(R.drawable.error) // Замените на ваш ресурс ошибки
                        .into(imageView);
            }
        }

        addToFavoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.addMedicineToFavorites(medicineId);
                Toast.makeText(MedicineDetailActivity.this, "Added to favorites", Toast.LENGTH_SHORT).show();
            }
        });
    }
}



