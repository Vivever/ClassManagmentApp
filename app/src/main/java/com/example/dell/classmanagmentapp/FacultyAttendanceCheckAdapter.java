package com.example.dell.classmanagmentapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class FacultyAttendanceCheckAdapter extends RecyclerView.Adapter<FacultyAttendanceCheckAdapter.MyViewHolder> {

    ArrayList<String> rollList;
    ArrayList<Double> attendanceList;

    public FacultyAttendanceCheckAdapter(ArrayList<String> rollList, ArrayList<Double> attendanceList) {
        this.rollList = rollList;
        this.attendanceList = attendanceList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.new_attendance_row,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.textView.setText(rollList.get(i)+"  -----  "+attendanceList.get(i));
    }

    @Override
    public int getItemCount() {
        return rollList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_new_attendance);
        }
    }

}
