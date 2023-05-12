package com.example.todoapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.databinding.EachRvBinding;

public class RVAdapter extends ListAdapter<Todo, RVAdapter.ViewHolder> {

    private TodoViewModel todoViewModel;
    private OnCheckedChangeListener listener;

    public RVAdapter(TodoViewModel todoViewModel, OnCheckedChangeListener listener) {
        super(CALLBACK);
        this.todoViewModel = todoViewModel;
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<Todo> CALLBACK = new DiffUtil.ItemCallback<Todo>() {
        @Override
        public boolean areItemsTheSame(@NonNull Todo oldItem, @NonNull Todo newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Todo oldItem, @NonNull Todo newItem) {
            return oldItem.getTitle().equals(newItem.getTitle())
                    && oldItem.getDescription().equals(newItem.getDescription())
                    && oldItem.getDate().equals(newItem.getDate());
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_rv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Todo todo = getItem(position);
        holder.bind(todo);
    }

    public Todo getTodo(int position) {
        return getItem(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {
        EachRvBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = EachRvBinding.bind(itemView);
            binding.checkBox.setOnCheckedChangeListener(this);
        }

        public void bind(Todo todo) {
            binding.title.setText(todo.getTitle());
            binding.description.setText(todo.getDescription());
            binding.date.setText(todo.getDate());

            // Set the checked state of the checkbox based on the completion status of the Todo item
            binding.checkBox.setChecked(todo.isCompleted());

            // Hide or show the checkbox based on the completion status of the Todo item
            if (todo.isCompleted()) {
                binding.checkBox.setVisibility(View.VISIBLE);
            } else {
                binding.checkBox.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            Todo todo = getItem(getAdapterPosition());

            // Update the completion status of the corresponding Todo item
            if (listener != null) {
                listener.onCheckedChange(todo, isChecked);
            }
        }
    }

    public Todo getTodoById(int id) {
        for (Todo todo : getCurrentList()) {
            if (todo.getId() == id) {
                return todo;
            }
        }
        return null;
    }

    public interface OnCheckedChangeListener {
        void onCheckedChange(Todo todo, boolean isChecked);
    }
}
