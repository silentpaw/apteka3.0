package com.example.apteka30;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {
    private List<Medicine> medicineList;
    private Context context;

    public MedicineAdapter(Context context, List<Medicine> medicineList) {
        this.context = context;
        this.medicineList = medicineList;
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.medicine_list_item, parent, false);
        return new MedicineViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        Medicine medicine = medicineList.get(position);
        holder.nameTextView.setText(medicine.getName());
        holder.descriptionTextView.setText(medicine.getDescription());

        // Используем Picasso для загрузки изображения из пути
        Picasso.get()
                .load(medicine.getImagePath())
                .placeholder(R.drawable.placeholder) // Используйте ваш ресурс заполнителя
                .error(R.drawable.error) // Используйте ваш ресурс ошибки
                .into(holder.medicineImage);

        holder.detailButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, MedicineDetailActivity.class);
            intent.putExtra("medicine_id", medicine.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    public static class MedicineViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView descriptionTextView;
        public ImageView medicineImage;
        public Button detailButton;

        public MedicineViewHolder(View view) {
            super(view);
            nameTextView = view.findViewById(R.id.nameTextView);
            descriptionTextView = view.findViewById(R.id.descriptionTextView);
            medicineImage = view.findViewById(R.id.imageView);
            detailButton = view.findViewById(R.id.detailButton);
        }
    }
}
