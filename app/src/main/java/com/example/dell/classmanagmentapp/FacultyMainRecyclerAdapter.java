package com.example.dell.classmanagmentapp;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;

public class FacultyMainRecyclerAdapter extends RecyclerView.Adapter<FacultyMainRecyclerAdapter.MyViewHolder> {

    ArrayList<ClassMasterClass> classList;

    public FacultyMainRecyclerAdapter(ArrayList<ClassMasterClass> classList) {
        this.classList = classList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.faculty_main_recycler_row,viewGroup,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        myViewHolder.className.setText(classList.get(i).className);
        myViewHolder.session.setText(classList.get(i).sessionStart + " - " + classList.get(i).sessionEnd);
        myViewHolder.roll.setText(classList.get(i).startRoll + " - " + classList.get(i).endRoll);
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return classList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView className, session, roll;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.tv_name_faculty_main_recycler);
            session = itemView.findViewById(R.id.tv_session_faculty_main_recycler);
            roll = itemView.findViewById(R.id.tv_student_roll_faculty_main_recycler);
        }
    }

}
