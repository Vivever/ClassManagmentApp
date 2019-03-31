package com.example.dell.classmanagmentapp;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class FacultyMainRecyclerAdapter extends RecyclerView.Adapter<FacultyMainRecyclerAdapter.MyViewHolder> {

    public static int FACULTY_LIST_INDEX;

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
                Intent intent = new Intent(view.getContext(),FacultyAttendenceCheckActivity.class);
                view.getContext().startActivity(intent);
                FACULTY_LIST_INDEX = i;
            }
        });
        myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final Dialog dialog = new Dialog(view.getContext());
                dialog.setContentView(R.layout.main_faculty_recycler_longclickmenu_popup);
                Button button = dialog.findViewById(R.id.bt_faculty_end_session);
                dialog.show();
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

    public void endSession(){
        DatabaseReference fromPath; final DatabaseReference toPath;
        fromPath = MainActivity.mRefClass.child(MainActivity.selectedClassIdList.get(FacultyMainRecyclerAdapter.FACULTY_LIST_INDEX));
        toPath = MainActivity.mRefOldRecord;
        moveRecord(fromPath,toPath);
    }

    private void moveRecord(DatabaseReference fromPath, final DatabaseReference toPath) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.push().setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
                            Log.d(TAG, "Success!");
                        } else {
                            Log.d(TAG, "Copy failed!");
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        fromPath.addListenerForSingleValueEvent(valueEventListener);
        fromPath.removeValue();
    }

}
