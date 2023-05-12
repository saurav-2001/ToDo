package com.example.todoapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todoapp.databinding.ActivityInsertBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class InsertActivity extends AppCompatActivity {

    ActivityInsertBinding binding;
    private Calendar calendar;
    private Calendar selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInsertBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String type = getIntent().getStringExtra("type");

        setTitle("Add Mode");

        // Initialize the calendar object
        calendar = Calendar.getInstance();

        if (type.equals("update")) {
            setTitle("Update mode");
            binding.title.setText(getIntent().getStringExtra("title"));
            binding.description.setText(getIntent().getStringExtra("description"));
            binding.dateChooserButton.setText(getIntent().getStringExtra("date"));
            int id = getIntent().getIntExtra("id", 0);
            binding.newHeading.setText("Update ToDo");
            binding.add.setText("update");
        }

        binding.dateChooserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = binding.title.getText().toString().trim();
                String description = binding.description.getText().toString().trim();
                String selectedDate = binding.dateChooserButton.getText().toString().trim();

                if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description) || selectedDate.equals("Choose Date")) {
                    Toast.makeText(InsertActivity.this, "Please enter title, description, and select a date", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent();
                intent.putExtra("title", title);
                intent.putExtra("description", description);
                intent.putExtra("selectedDate", selectedDate);

                if (type.equals("update")) {
                    int id = getIntent().getIntExtra("id", 0);
                    intent.putExtra("id", id);
                }

                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(InsertActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Update the selectedDate variable with the chosen date
                        selectedDate = Calendar.getInstance();
                        selectedDate.set(Calendar.YEAR, year);
                        selectedDate.set(Calendar.MONTH, monthOfYear);
                        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDateText(); // Update the date text view
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void updateDateText() {
        if (selectedDate != null) {
            // Format the selected date as desired (e.g., "dd/MM/yyyy")
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String dateString = dateFormat.format(selectedDate.getTime());
            binding.dateChooserButton.setText(dateString);
        } else {
            // Set "Choose Date" when no date is selected
            binding.dateChooserButton.setText("Choose Date");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(InsertActivity.this, MainActivity.class));
    }
}

