package com.example.apteka30;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {

    private Context context;
    private List<Medicine> medicineList;

    public MedicineAdapter(Context context, List<Medicine> medicineList) {
        this.context = context;
        this.medicineList = medicineList;
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.medicine_item, parent, false);
        return new MedicineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        Medicine medicine = medicineList.get(position);
        holder.nameTextView.setText(medicine.getName());
        holder.descriptionTextView.setText(medicine.getDescription());
        Picasso.get().load(medicine.getImagePath()).into(holder.imageView);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MedicineDetailActivity.class);
            intent.putExtra("medicine_id", medicine.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    static class MedicineViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView descriptionTextView;
        ImageView imageView;

        public MedicineViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
