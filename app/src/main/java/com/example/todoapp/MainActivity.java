package com.example.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class MainActivity extends AppCompatActivity implements RVAdapter.OnCheckedChangeListener, RVAdapter.OnItemClickListener {

    private ActivityMainBinding binding;
    private TodoViewModel todoViewModel;
    private RVAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        todoViewModel = new ViewModelProvider(this).get(TodoViewModel.class);

        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, InsertActivity.class);
                intent.putExtra("type", "addMode");
                startActivityForResult(intent, 1);
            }
        });

        adapter = new RVAdapter(todoViewModel, this, this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setHasFixedSize(true);
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

        Button deleteButton = findViewById(R.id.button_delete_completed);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todoViewModel.deleteCompleted();
                Toast.makeText(MainActivity.this, "Completed ToDos Deleted", Toast.LENGTH_SHORT).show();
            }
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_add_item) {
            // Handle Add Item option
            Intent intent = new Intent(MainActivity.this, InsertActivity.class);
            intent.putExtra("type", "addMode");
            startActivityForResult(intent, 1);
            return true;
        } else if (itemId == R.id.action_delete_all) {
            // Handle Delete All option
            todoViewModel.deleteAll();
            Toast.makeText(this, "All ToDos Deleted", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.action_logout) {
            // Handle Logout option
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Optional: Close the MainActivity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChange(Todo todo, boolean isChecked) {
        todoViewModel.setCompleted(todo.getId(), isChecked);
    }

    @Override
    public void onItemClick(Todo todo) {
        Intent intent = new Intent(MainActivity.this, InsertActivity.class);
        intent.putExtra("type", "update");
        intent.putExtra("title", todo.getTitle());
        intent.putExtra("description", todo.getDescription());
        intent.putExtra("date", todo.getDate());
        intent.putExtra("id", todo.getId());
        startActivityForResult(intent, 2);
    }
}