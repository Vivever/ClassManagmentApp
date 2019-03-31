package com.example.dell.classmanagmentapp;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class NewAttendanceAdapter extends RecyclerView.Adapter<NewAttendanceAdapter.MyViewHolder> {

    ArrayList<String> rollList;
    OnItemListener onItemListener;
    public NewAttendanceAdapter(ArrayList<String> rollList, OnItemListener onItemListener) {
        this.rollList = rollList;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.new_attendance_row,viewGroup,false);
        return new MyViewHolder(view,onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i){
        myViewHolder.textView.setText(rollList.get(i));
        if(FacultyRegisterAttendenceActivity.presentStudentRollList.get(i).equals("PRESENT")){
            myViewHolder.textView.setBackgroundColor(Color.parseColor("#f48f42"));
        }else {
            myViewHolder.textView.setBackgroundColor(Color.parseColor("#80000000"));
        }
    }

    @Override
    public int getItemCount() {
        return rollList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;
        OnItemListener onItemListener;

        public MyViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_new_attendance);
            this.onItemListener= onItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemListener.onItemClick(getAdapterPosition());
        }
    }

    public interface OnItemListener{
        void onItemClick(int position);
    }

}
