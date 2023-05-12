package com.example.todoapp;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;



public class TodoRepo {

    private TodoDao todoDao;
    private LiveData<List<Todo>> todoList;

    public TodoRepo(Application application){
        TodoDatabase todoDatabase = TodoDatabase.getInstance(application);
        todoDao = todoDatabase.todoDao();
        todoList = todoDao.getData();
    }

    public void insertData(Todo todo){
        new InsertTask(todoDao).execute(todo);
    }

    public void updateData(Todo todo){
        new UpdateTask(todoDao).execute(todo);
    }

    public void deleteData(Todo todo){
        new DeleteTask(todoDao).execute(todo);
    }

    public void setCompleted(int todoId, boolean completed) {
        new SetCompletedTask(todoDao).execute(todoId, completed);
    }

    public LiveData<List<Todo>> getData(){
        return todoList;
    }

    private static class InsertTask extends AsyncTask<Todo, Void, Void>{
        private TodoDao todoDao;

        public InsertTask(TodoDao todoDao) {
            this.todoDao = todoDao;
        }

        @Override
        protected Void doInBackground(Todo... todos) {
            todoDao.insert(todos[0]);
            return null;
        }
    }

    private static class UpdateTask extends AsyncTask<Todo, Void, Void>{
        private TodoDao todoDao;

        public UpdateTask(TodoDao todoDao) {
            this.todoDao = todoDao;
        }

        @Override
        protected Void doInBackground(Todo... todos) {
            todoDao.update(todos[0]);
            return null;
        }
    }

    private static class DeleteTask extends AsyncTask<Todo, Void, Void>{
        private TodoDao todoDao;

        public DeleteTask(TodoDao todoDao) {
            this.todoDao = todoDao;
        }

        @Override
        protected Void doInBackground(Todo... todos) {
            todoDao.delete(todos[0]);
            return null;
        }
    }

    private static class SetCompletedTask extends AsyncTask<Object, Void, Void>{
        private TodoDao todoDao;

        public SetCompletedTask(TodoDao todoDao) {
            this.todoDao = todoDao;
        }

        @Override
        protected Void doInBackground(Object... params) {
            int todoId = (int) params[0];
            boolean completed = (boolean) params[1];
            todoDao.setCompleted(todoId, completed);
            return null;
        }
    }
}

