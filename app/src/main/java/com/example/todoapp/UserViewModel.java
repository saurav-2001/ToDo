package com.example.todoapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class UserViewModel extends AndroidViewModel {
    private UserRepo userRepo;
    private LiveData<List<User>> allUsers;

    public UserViewModel(@NonNull Application application) {
        super(application);
        UserDatabase userDatabase = UserDatabase.getInstance(application);
        UserDao userDao = userDatabase.userDao();
        userRepo = new UserRepo(userDao);
        allUsers = userRepo.getAllUsers();
    }

    public void insertUser(User user) {
        userRepo.insertUser(user);
    }

    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }

    public LiveData<User> getUserByUsername(String username) {
        return userRepo.getUserByUsername(username);
    }
}
