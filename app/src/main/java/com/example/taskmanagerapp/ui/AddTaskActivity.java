package com.example.taskmanagerapp.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taskmanagerapp.R;
import com.example.taskmanagerapp.database.TaskDatabaseHelper;
import com.example.taskmanagerapp.model.Task;

import java.util.Calendar;

public class AddTaskActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextDescription, editTextDueDate;
    private Button buttonSave, buttonDelete;
    private TaskDatabaseHelper dbHelper;
    private int taskId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextDueDate = findViewById(R.id.editTextDueDate);
        buttonSave = findViewById(R.id.buttonSave);
        buttonDelete = findViewById(R.id.buttonDelete);
        dbHelper = new TaskDatabaseHelper(this);

        // DatePicker
        editTextDueDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year1, month1, dayOfMonth) -> {
                        String date = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                        editTextDueDate.setText(date);
                    }, year, month, day);
            datePickerDialog.show();
        });

        // Si viene desde edición
        if (getIntent().hasExtra("id")) {
            taskId = getIntent().getIntExtra("id", -1);
            editTextTitle.setText(getIntent().getStringExtra("title"));
            editTextDescription.setText(getIntent().getStringExtra("description"));
            editTextDueDate.setText(getIntent().getStringExtra("dueDate"));
            buttonDelete.setVisibility(View.VISIBLE);
        } else {
            buttonDelete.setVisibility(View.GONE);
        }

        buttonSave.setOnClickListener(v -> {
            String title = editTextTitle.getText().toString().trim();
            String description = editTextDescription.getText().toString().trim();
            String dueDate = editTextDueDate.getText().toString().trim();

            if (title.isEmpty()) {
                Toast.makeText(this, "El título es obligatorio", Toast.LENGTH_SHORT).show();
                return;
            }

            if (taskId != -1) {
                Task task = new Task(taskId, title, description, dueDate, 0);
                dbHelper.updateTask(task);
                Toast.makeText(this, "Tarea actualizada", Toast.LENGTH_SHORT).show();
            } else {
                Task task = new Task(0, title, description, dueDate, 0);
                dbHelper.addTask(task);
                Toast.makeText(this, "Tarea guardada", Toast.LENGTH_SHORT).show();
            }
            finish();
        });

        buttonDelete.setOnClickListener(v -> {
            if (taskId != -1) {
                dbHelper.deleteTask(taskId);
                Toast.makeText(this, "Tarea eliminada", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
