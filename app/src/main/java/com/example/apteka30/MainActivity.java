package com.example.apteka30;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Medicine> medicineList;
    private MedicineAdapter adapter;
    private MedicineDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new MedicineDatabaseHelper(this);

        dbHelper.clearAllMedicines();

        // Проверяем, есть ли лекарства в базе данных
        if (dbHelper.getAllMedicines().isEmpty()) {
            // Если база данных пуста, добавляем лекарства
            addDefaultMedicines();
        }

        prepareMedicineData();

    }

    private void addDefaultMedicines() {
        // Пример добавления данных
        dbHelper.addMedicine(new Medicine("Aspirin", "Pain reliever", "file:///android_asset/data/aspirin.jpg"));
        dbHelper.addMedicine(new Medicine("Ibuprofen", "Anti-inflammatory", "file:///android_asset/data/ibuprofen.jpg"));
        dbHelper.addMedicine(new Medicine("Paracetamol", "Anti-inflammatory", "file:///android_asset/data/paracetamol.jpg"));
        dbHelper.addMedicine(new Medicine("Tantum-verde", "Spray", "file:///android_asset/data/verede-forte.jpg"));
        // Добавьте другие лекарства здесь по вашему выбору

    }

    private void prepareMedicineData() {
        medicineList = dbHelper.getAllMedicines();
        adapter = new MedicineAdapter(this, medicineList);
        recyclerView.setAdapter(adapter);
    }
}
