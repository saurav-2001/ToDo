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
    private OnItemClickListener itemClickListener;

    public RVAdapter(TodoViewModel todoViewModel, OnCheckedChangeListener listener, OnItemClickListener itemClickListener) {
        super(CALLBACK);
        this.todoViewModel = todoViewModel;
        this.listener = listener;
        this.itemClickListener = itemClickListener;
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

    public class ViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
        EachRvBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = EachRvBinding.bind(itemView);
            binding.checkBox.setOnCheckedChangeListener(this);
            itemView.setOnClickListener(this);
        }

        public void bind(Todo todo) {
            binding.title.setText(todo.getTitle());
            binding.description.setText(todo.getDescription());
            binding.date.setText(todo.getDate());

            binding.checkBox.setChecked(todo.isCompleted());

            if (todo.isCompleted()) {
                binding.checkBox.setVisibility(View.VISIBLE);
            } else {
                binding.checkBox.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            Todo todo = getItem(getAdapterPosition());

            if (listener != null) {
                listener.onCheckedChange(todo, isChecked);
            }
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && itemClickListener != null) {
                Todo todo = getItem(position);
                itemClickListener.onItemClick(todo);
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

    public interface OnItemClickListener {
        void onItemClick(Todo todo);
    }
}