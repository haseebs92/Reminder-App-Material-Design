package com.codify92.reminderappmaterialdesign.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.codify92.reminderappmaterialdesign.Others.TodoModelClass;
import com.codify92.reminderappmaterialdesign.R;

import java.util.ArrayList;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private ArrayList<TodoModelClass> todoArrayList;
    private Context context;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTodoText;
        CheckBox mCheckBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTodoText = itemView.findViewById(R.id.todoText);
            mCheckBox = itemView.findViewById(R.id.todoCheckBox);
        }
    }

    public TodoAdapter(Context context, ArrayList<TodoModelClass> todoArrayList) {
        this.context = context;
        this.todoArrayList = todoArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.mTodoText.setText(todoArrayList.get(position).getText());
        holder.mCheckBox.setChecked(todoArrayList.get(position).isCompleted());

    }

    @Override
    public int getItemCount() {
        return todoArrayList.size();
    }

}
