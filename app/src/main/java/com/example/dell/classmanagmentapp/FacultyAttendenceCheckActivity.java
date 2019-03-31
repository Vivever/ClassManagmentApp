package com.example.dell.classmanagmentapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FacultyAttendenceCheckActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_attendence_check);
        setTitle(MainActivity.classList.get(FacultyMainRecyclerAdapter.FACULTY_LIST_INDEX).className);

        Button newAttendenceIntentButton = findViewById(R.id.bt_newAttendence);
        newAttendenceIntentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),FacultyRegisterAttendenceActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}
