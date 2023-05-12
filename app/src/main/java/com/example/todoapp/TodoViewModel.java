package com.example.todoapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TodoViewModel extends AndroidViewModel {

    private TodoRepo todoRepo;
    private LiveData<List<Todo>> todoList;

    public TodoViewModel(@NonNull Application application) {
        super(application);
        todoRepo = new TodoRepo(application);
        todoList = todoRepo.getData();
    }

    public void insert(Todo todo) {
        todoRepo.insertData(todo);
    }

    public void update(Todo todo) {
        todoRepo.updateData(todo);
    }

    public void delete(Todo todo) {
        todoRepo.deleteData(todo);
    }

    public void setCompleted(int todoId, boolean completed) {
        todoRepo.setCompleted(todoId, completed);
    }

    public LiveData<List<Todo>> getData() {
        return todoList;
    }

    public void deleteAll() {
        todoRepo.deleteAll();
    }

    public void deleteCompleted() {
        // Execute the deletion operation in a background thread
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                todoRepo.deleteCompleted();
            }
        });
    }

}
