package com.example.medilert;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.example.medilert.data.AppDatabase;
import com.example.medilert.data.Medicine;
import java.util.ArrayList;

public class InventoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView emptyStateText;
    private MedicineAdapter adapter;
    private AppDatabase db;
    private Medicine editingMedicine = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        setupToolbar();
        setupViews();
        setupDatabase();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setupViews() {
        recyclerView = findViewById(R.id.medicineRecyclerView);
        emptyStateText = findViewById(R.id.emptyStateText);
        FloatingActionButton addButton = findViewById(R.id.addMedicineButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MedicineAdapter(new ArrayList<>(), this::showMedicineDialog);
        recyclerView.setAdapter(adapter);

        addButton.setOnClickListener(v -> showAddMedicineDialog());
    }

    private void setupDatabase() {
        db = AppDatabase.getInstance(this);
        db.medicineDao().getAllMedicines().observe(this, medicines -> {
            if (medicines.isEmpty()) {
                emptyStateText.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                emptyStateText.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                adapter.setMedicines(medicines);
            }
        });
    }

    private void showAddMedicineDialog() {
        showMedicineDialog(null);
    }

    private void showMedicineDialog(Medicine medicine) {
        editingMedicine = medicine;
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_add_medicine, null);
        builder.setView(view);

        TextView titleText = view.findViewById(R.id.dialogTitle);
        TextInputEditText nameInput = view.findViewById(R.id.medicineNameInput);
        NumberPicker quantityPicker = view.findViewById(R.id.quantityPicker);
        Button cancelButton = view.findViewById(R.id.cancelButton);
        Button saveButton = view.findViewById(R.id.saveButton);

        // Setup NumberPicker
        quantityPicker.setMinValue(1);
        quantityPicker.setMaxValue(999);
        quantityPicker.setWrapSelectorWheel(false);

        // Set title and pre-fill fields if editing
        if (medicine != null) {
            titleText.setText(R.string.edit_medicine_title);
            nameInput.setText(medicine.name);
            quantityPicker.setValue(medicine.quantity);
        } else {
            titleText.setText(R.string.add_medicine_title);
            quantityPicker.setValue(1); // Default value for new entries
        }

        AlertDialog dialog = builder.create();
        dialog.show();

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        saveButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter medicine name", Toast.LENGTH_SHORT).show();
                return;
            }

            int quantity = quantityPicker.getValue();
            saveMedicine(name, quantity);
            dialog.dismiss();
        });
    }

    private void saveMedicine(String name, int quantity) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            Medicine medicine;
            if (editingMedicine != null) {
                medicine = editingMedicine;
                medicine.name = name;
                medicine.quantity = quantity;
                db.medicineDao().update(medicine);
            } else {
                medicine = new Medicine();
                medicine.name = name;
                medicine.quantity = quantity;
                db.medicineDao().insert(medicine);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
} 