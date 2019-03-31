package com.example.dell.classmanagmentapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.FirebaseError;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.Math.round;

public class FacultyAttendenceCheckActivity extends AppCompatActivity {

    private ArrayList<String> rollList;
    private int startRoll, endRoll;
    private ArrayList<Integer> percentageList;
    ArrayList<AttendanceObjectClass> totalAttendanceGetList;
    String tempdate = "";
    int days = 0;
    int count = 0;
    int temp;
    DatabaseReference attendanceRef, attendancePercentlistRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_attendence_check);
        setTitle(MainActivity.classList.get(FacultyMainRecyclerAdapter.FACULTY_LIST_INDEX).className);
        attendanceRef = MainActivity.mRefClass.child(MainActivity.selectedClassIdList.get(FacultyMainRecyclerAdapter.FACULTY_LIST_INDEX))
                .child("attendance");
        attendancePercentlistRef = MainActivity.mRefClass.child(MainActivity.selectedClassIdList.get(FacultyMainRecyclerAdapter.FACULTY_LIST_INDEX))
                .child("attendance_detailed_percentage");
        Button newAttendenceIntentButton = findViewById(R.id.bt_newAttendence);
        newAttendenceIntentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),FacultyRegisterAttendenceActivity.class);
                startActivity(intent);
                finish();
            }
        });
        totalAttendanceGetList = new ArrayList<>();

        startRoll = Integer.parseInt(MainActivity.classList.get(FacultyMainRecyclerAdapter.FACULTY_LIST_INDEX).startRoll);
        endRoll = Integer.parseInt(MainActivity.classList.get(FacultyMainRecyclerAdapter.FACULTY_LIST_INDEX).endRoll);
        rollList = new ArrayList<>();
        percentageList = new ArrayList<>();
        for (int i = startRoll; i<=endRoll; i++){
            rollList.add(Integer.toString(i));
            percentageList.add(0);
        }
        getAttendanceData();
    }

    public void getAttendanceData() {
        attendanceRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                AttendanceObjectClass tempObj = dataSnapshot.getValue(AttendanceObjectClass.class);
                totalAttendanceGetList.add(tempObj);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        attendanceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                refresh();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void refresh(){
        for(int k = 0; k< totalAttendanceGetList.size(); k++){
            if(!totalAttendanceGetList.get(k).date.equals(tempdate)){
                days++;
                count=0;
                tempdate = totalAttendanceGetList.get(k).date;
                temp = percentageList.get(count);
                if(totalAttendanceGetList.get(k).status.equals("PRESENT")){
                    temp++;
                    percentageList.set(count,temp);
                }
                count++;
            }else{
                temp = percentageList.get(count);
                if(totalAttendanceGetList.get(k).status.equals("PRESENT")){
                    temp++;
                    percentageList.set(count,temp);
                }
                count++;
            }
        }
        ArrayList<Double> percentageFloatList = new ArrayList<>();
        for(int k = 0; k< percentageList.size(); k++){
            percentageFloatList.add(Double.valueOf(percentageList.get(k)));
            percentageFloatList.set(k,Math.round(((percentageFloatList.get(k)/days)*100)* 100.0) / 100.0);
        }
        FacultyAttendanceCheckAdapter adapter = new FacultyAttendanceCheckAdapter(rollList,percentageFloatList);
        RecyclerView recyclerView = findViewById(R.id.recyclerview_faculty_attendance_check);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        for (int i = 0; i<rollList.size();i++){
            attendancePercentlistRef.child(rollList.get(i)).setValue(percentageFloatList.get(i));
        }
    }
}
