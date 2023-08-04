package com.codify92.reminderappmaterialdesign.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.codify92.reminderappmaterialdesign.Others.TodoModelClass;
import com.codify92.reminderappmaterialdesign.R;

import java.util.ArrayList;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private ArrayList<TodoModelClass> todoArrayList;
    private Context context;

    private OnItemClickListener mListener;
    private OnItemLongClickListener mLongListener;


    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener longListener){
        mLongListener = longListener;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mReminderTitle;
        TextView mReminderDetails;
        TextView mReminderDate;

        CardView mCustomCardView;



        public ViewHolder(@NonNull View itemView,OnItemClickListener listener,OnItemLongClickListener longClickListener) {
            super(itemView);
            mReminderTitle = itemView.findViewById(R.id.todoText);
            mReminderDetails = itemView.findViewById(R.id.subtext);
            mReminderDate = itemView.findViewById(R.id.timeAndDate);
            mCustomCardView = itemView.findViewById(R.id.custom_card);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (longClickListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            longClickListener.onItemLongClick(position);
                        }
                    }
                    return true;
                }
            });
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
        return new ViewHolder(view,mListener,mLongListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (todoArrayList.get(position).getText().equals("")){
            holder.mReminderTitle.setVisibility(View.GONE);
        } else {
            holder.mReminderTitle.setText(todoArrayList.get(position).getText());
        }
        if (todoArrayList.get(position).getSubtext().equals("")){
            holder.mReminderDetails.setVisibility(View.GONE);
        } else {
            holder.mReminderDetails.setText(todoArrayList.get(position).getSubtext());
        }
        if (todoArrayList.get(position).getDate().equals("")){
            holder.mReminderDate.setVisibility(View.GONE);
        } else {
            holder.mReminderDate.setText(todoArrayList.get(position).getDate());
        }

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
