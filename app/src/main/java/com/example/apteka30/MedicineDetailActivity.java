package com.example.apteka30;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;

public class MedicineDetailActivity extends AppCompatActivity {

    private TextView nameTextView, descriptionTextView;
    private ImageView imageView;
    private MedicineDatabaseHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_detail);

        nameTextView = findViewById(R.id.nameTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        imageView = findViewById(R.id.imageView);

        dbHelper = new MedicineDatabaseHelper(this);

        Intent intent = getIntent();
        int medicineId = intent.getIntExtra("medicine_id", -1);

        if (medicineId != -1) {
            Medicine medicine = dbHelper.getMedicineById(medicineId);
            if (medicine != null) {
                nameTextView.setText(medicine.getName());
                descriptionTextView.setText(medicine.getDescription());
                Picasso.get().load(medicine.getImagePath()).into(imageView);

                // Сохранение информации о просмотренном лекарстве
                dbHelper.addRecentlyViewedMedicine(medicineId);
            }
        }
    }
}
