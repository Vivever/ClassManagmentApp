package com.example.dell.classmanagmentapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class StudentMainRecyclerAdapter extends RecyclerView.Adapter<StudentMainRecyclerAdapter.MyViewHolder>{

    ArrayList<String> percentageList;
    ArrayList<ClassMasterClass> classList;
    int selectedRow;


    public StudentMainRecyclerAdapter(ArrayList<String> percentageList, ArrayList<ClassMasterClass> classList) {
        this.percentageList = percentageList;
        this.classList = classList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.student_main_recycler_row,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        myViewHolder.className.setText(classList.get(i).className);
        myViewHolder.facultyname.setText(classList.get(i).facultyName);
        myViewHolder.session.setText(classList.get(i).sessionStart+ " - " +classList.get(i).sessionEnd);
        myViewHolder.attendance.setText(percentageList.get(i));

        myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final Dialog dialog = new Dialog(view.getContext());
                dialog.setContentView(R.layout.main_faculty_recycler_longclickmenu_popup);
                Button button = dialog.findViewById(R.id.bt_faculty_end_session);
                dialog.show();
                selectedRow = i;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AlertDialog.Builder(view.getContext())
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Are You Sure?")
                                .setMessage("Once Session End, it CANNOT be reversed")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        endSession();
                                    }
                                }).setNegativeButton("No",null).show();
                        dialog.dismiss();
                    }
                });
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return percentageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView className,facultyname,session,attendance;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.tv_name_student_main_recycler_row);
            facultyname = itemView.findViewById(R.id.tv_facultyname_student_main_recycler_row);
            session = itemView.findViewById(R.id.tv_session_student_main_recycler_row);
            attendance = itemView.findViewById(R.id.tv_attendance_student_main_recycler_row);
        }
    }

    public void endSession(){
        DatabaseReference fromPath; final DatabaseReference toPath;
        fromPath = MainActivity.mRefStudentClass.child(MainActivity.selectedClassIdList.get(selectedRow));
        fromPath.removeValue();
    }
}
