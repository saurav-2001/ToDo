package com.example.todoapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

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

}
