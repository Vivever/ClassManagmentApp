package com.example.dell.classmanagmentapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FacultyRegisterAttendenceActivity extends AppCompatActivity implements NewAttendanceAdapter.OnItemListener{

    private int startRoll, endRoll;
    private ArrayList<String> rollList;
    private NewAttendanceAdapter adapter;
    private RecyclerView recyclerView;
    private int x,j;
    public static ArrayList<String> presentStudentRollList;
    private ArrayList<AttendanceObjectClass> finalAttendanceList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_register_attendence);
        setTitle("Register Attendance");

        startRoll = Integer.parseInt(MainActivity.classList.get(FacultyMainRecyclerAdapter.FACULTY_LIST_INDEX).startRoll);
        endRoll = Integer.parseInt(MainActivity.classList.get(FacultyMainRecyclerAdapter.FACULTY_LIST_INDEX).endRoll);
        rollList = new ArrayList<>();
        finalAttendanceList = new ArrayList<>();
        presentStudentRollList = new ArrayList<>();
        presentStudentRollList.clear();
        finalAttendanceList.clear();
        for (int i = startRoll; i<=endRoll; i++){
            rollList.add(Integer.toString(i));
            presentStudentRollList.add("ABSENT");
        }
        adapter = new NewAttendanceAdapter(rollList,this);
        recyclerView = findViewById(R.id.recyclerview_new_attendance);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        Button done = findViewById(R.id.bt_attendance_done);


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date todayDate = Calendar.getInstance().getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String todayString = formatter.format(todayDate);
                for(j = startRoll-1; j<endRoll; j++){
                    finalAttendanceList.add(new AttendanceObjectClass(j+1,presentStudentRollList.get(j-(startRoll-1)),todayString));
                }
                new AlertDialog.Builder(FacultyRegisterAttendenceActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are You Sure?")
                        .setMessage("Once Attendance taken CANNOT be changed")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                uploadAttendanceData();
                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).setNegativeButton("No",null).show();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        if(presentStudentRollList.get(position).equals("ABSENT")){
            presentStudentRollList.set(position,"PRESENT");
        }else {
            presentStudentRollList.set(position,"ABSENT");
        }
        adapter.notifyDataSetChanged();
    }
    public void uploadAttendanceData(){
        DatabaseReference attendanceRef = MainActivity.mRefClass.child(MainActivity.selectedClassIdList.get(FacultyMainRecyclerAdapter.FACULTY_LIST_INDEX))
                .child("attendance");
        for(AttendanceObjectClass obj : finalAttendanceList) {
            attendanceRef.push().setValue(obj);
        }
    }
}
