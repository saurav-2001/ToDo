package com.example.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding; // Update the binding variable name

    private TodoViewModel todoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater()); // Inflate the layout using data binding
        setContentView(binding.getRoot());

        todoViewModel = new ViewModelProvider(this).get(TodoViewModel.class); // Simplify ViewModelProvider initialization

        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, InsertActivity.class);
                intent.putExtra("type", "addMode");
                startActivityForResult(intent, 1);
            }
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setHasFixedSize(true);
        RVAdapter adapter = new RVAdapter(todoViewModel, new RVAdapter.OnCheckedChangeListener() {
            @Override
            public void onCheckedChange(Todo todo, boolean isChecked) {
                todoViewModel.setCompleted(todo.getId(), isChecked);
            }
        });
        binding.recyclerView.setAdapter(adapter);

        todoViewModel.getData().observe(this, new Observer<List<Todo>>() {
            @Override
            public void onChanged(List<Todo> todos) {
                adapter.submitList(todos);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.RIGHT) {
                    todoViewModel.delete(adapter.getTodo(viewHolder.getAdapterPosition()));
                    Toast.makeText(MainActivity.this, "ToDo Deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainActivity.this, InsertActivity.class);
                    intent.putExtra("type", "update");
                    intent.putExtra("title", adapter.getTodo(viewHolder.getAdapterPosition()).getTitle());
                    intent.putExtra("description", adapter.getTodo(viewHolder.getAdapterPosition()).getDescription());
                    intent.putExtra("date", adapter.getTodo(viewHolder.getAdapterPosition()).getDate());
                    intent.putExtra("id", adapter.getTodo(viewHolder.getAdapterPosition()).getId());
                    startActivityForResult(intent, 2);
                }
            }
        }).attachToRecyclerView(binding.recyclerView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {
                String title = data.getStringExtra("title");
                String description = data.getStringExtra("description");
                String selectedDate = data.getStringExtra("selectedDate");
                Todo todo = new Todo(title, description, selectedDate);
                todo.setDate(selectedDate);
                todoViewModel.insert(todo);
                Toast.makeText(this, "New ToDo Added", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            if (data != null) {
                String title = data.getStringExtra("title");
                String description = data.getStringExtra("description");
                String selectedDate = data.getStringExtra("selectedDate");
                Todo todo = new Todo(title, description, selectedDate);
                todo.setId(data.getIntExtra("id", 0));
                todo.setDate(selectedDate);
                todoViewModel.update(todo);
                Toast.makeText(this, "ToDo Updated ", Toast.LENGTH_SHORT).show();
            }
        }
    }

}