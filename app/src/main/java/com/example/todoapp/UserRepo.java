package com.example.todoapp;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepo {
    private UserDao userDao;
    private LiveData<List<User>> userList;
    private ExecutorService executorService;

    public UserRepo(UserDao userDao) {
        this.userDao = userDao;
        userList = userDao.getAllUsers();
        executorService = Executors.newSingleThreadExecutor();
    }

    public UserRepo(Application application) {
        UserDatabase userDatabase = UserDatabase.getInstance(application);
        userDao = userDatabase.userDao();
        userList = userDao.getAllUsers();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insertUser(User user) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                userDao.insert(user);
            }
        });
    }

    public LiveData<List<User>> getAllUsers() {
        return userList;
    }

    public LiveData<User> getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }
}
