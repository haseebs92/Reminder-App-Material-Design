package com.codify92.reminderappmaterialdesign.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
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

        int number = todoArrayList.get(position).getChosenColor();
        if (number == 1){
            holder.mCustomCardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.card_blue));
        } else if (number == 2) {
            holder.mCustomCardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.card_green));
        }else if (number == 3) {
            holder.mCustomCardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.card_light_pink));
        }else if (number == 4) {
            holder.mCustomCardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.card_orange));
        }else if (number == 5) {
            holder.mCustomCardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.card_light_purple));
        }else if (number == 6) {
            holder.mCustomCardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.card_dark_purple));
        }

    }

    @Override
    public int getItemCount() {
        return todoArrayList.size();
    }

}
