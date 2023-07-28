package com.codify92.reminderappmaterialdesign.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.codify92.reminderappmaterialdesign.Others.TodoModelClass;
import com.codify92.reminderappmaterialdesign.R;

import java.util.ArrayList;
import java.util.Random;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private ArrayList<TodoModelClass> todoArrayList;
    private Context context;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mReminderTitle;
        TextView mReminderDetails;
        TextView mReminderDate;

        CardView mCustomCardView;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mReminderTitle = itemView.findViewById(R.id.todoText);
            mReminderDetails = itemView.findViewById(R.id.subtext);
            mReminderDate = itemView.findViewById(R.id.timeAndDate);
            mCustomCardView = itemView.findViewById(R.id.custom_card);
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

        holder.mReminderTitle.setText(todoArrayList.get(position).getText());
        holder.mReminderDetails.setText(todoArrayList.get(position).getSubtext());
        holder.mReminderDate.setText(todoArrayList.get(position).getDate());
        Random random = new Random();
        int number = random.nextInt(7);

//        holder.mCustomCardView.setBackgroundColor(Integer.parseInt("#010f01"));



    }

    @Override
    public int getItemCount() {
        return todoArrayList.size();
    }

}
