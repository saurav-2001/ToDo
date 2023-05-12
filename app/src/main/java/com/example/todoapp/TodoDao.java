package com.example.todoapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TodoDao {
    @Insert
    public void insert(Todo todo);

    @Update
    public void update(Todo todo);

    @Delete
    public void delete(Todo todo);

    @Query("SELECT * FROM my_todo")
    public LiveData<List<Todo>> getData();

    @Query("UPDATE my_todo SET completed = :completed WHERE id = :todoId")
    void setCompleted(int todoId, boolean completed);

}
