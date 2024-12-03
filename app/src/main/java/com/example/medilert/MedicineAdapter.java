package com.example.medilert;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.medilert.data.Medicine;
import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {
    private List<Medicine> medicines;
    private final OnMedicineClickListener listener;

    public interface OnMedicineClickListener {
        void onMedicineClick(Medicine medicine);
    }

    public MedicineAdapter(List<Medicine> medicines, OnMedicineClickListener listener) {
        this.medicines = medicines;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_medicine, parent, false);
        return new MedicineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        Medicine medicine = medicines.get(position);
        holder.bind(medicine, listener);
    }

    @Override
    public int getItemCount() {
        return medicines.size();
    }

    public void setMedicines(List<Medicine> medicines) {
        this.medicines = medicines;
        notifyDataSetChanged();
    }

    static class MedicineViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameText;
        private final TextView quantityText;

        MedicineViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.medicineName);
            quantityText = itemView.findViewById(R.id.medicineQuantity);
        }

        void bind(Medicine medicine, OnMedicineClickListener listener) {
            nameText.setText(medicine.name);
            quantityText.setText("Quantity: " + medicine.quantity);
            itemView.setOnClickListener(v -> listener.onMedicineClick(medicine));
        }
    }
} 