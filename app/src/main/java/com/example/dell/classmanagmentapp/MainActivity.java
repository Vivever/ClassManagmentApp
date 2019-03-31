package com.example.dell.classmanagmentapp;

import android.app.Application;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static boolean FACULTY = true ;
    public static UserObjectClass CURRENT_USER;
    public static boolean LOGGED_IN = false;
    public static FirebaseDatabase mFirebaseDatabase;  //Create a instance of Firebase database
    public static DatabaseReference mRefClass;   // It is used to Refers to a particular part of Database
    public static DatabaseReference mRefUsers;   // It is used to Refers to a particular part of Database
    public static DatabaseReference mRefOldRecord;

    public FirebaseUser currentUser;
    public static ArrayList<ClassMasterClass> classList;
    public static ArrayList<String> selectedClassIdList;
    public RecyclerView recyclerView;
    FacultyMainRecyclerAdapter adapter;

    private FirebaseAuth.AuthStateListener authStateListener; // It is used to listen to authentication state change i.e sign in or sign out


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRefClass = mFirebaseDatabase.getReference("classes");
        mRefUsers = mFirebaseDatabase.getReference("users");
        mRefOldRecord = mFirebaseDatabase.getReference("old_class_records");
        LogInActivity.mAuth = FirebaseAuth.getInstance();
        currentUser = LogInActivity.mAuth.getCurrentUser();
        classList = new ArrayList<>();
        classList.clear();
        recyclerView = findViewById(R.id.recycler_view_faculty_classes);
        selectedClassIdList = new ArrayList<>();

        //<editor-fold desc=" DEFAULT RUN CODE ">
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(currentUser == null){
            Intent loginIntent = new Intent(getApplicationContext(),LogInActivity.class);
            startActivity(loginIntent);
        }else {
            LogInActivity.getCurrentUser();
            LOGGED_IN = true;
        }
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                // here we do something depending on whether user signed in or signed out
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser == null) {
                    //Signed Out
                    Intent intent = new Intent(getApplicationContext(),LogInActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    LogInActivity.getCurrentUser();
                    LOGGED_IN = true;
                }
            }
        };
        //</editor-fold>

        if (FACULTY && LOGGED_IN){
            mRefClass.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    ClassMasterClass classObject = dataSnapshot.getValue(ClassMasterClass.class);
                    if (classObject.facultyUID.equals(LogInActivity.mAuth.getUid())){
                        classList.add(classObject);
                        selectedClassIdList.add(dataSnapshot.getKey());
                        adapter.notifyDataSetChanged();
                    }
                }
                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }
                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    finish();
                    startActivity(getIntent());
                }
                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            adapter = new FacultyMainRecyclerAdapter(classList);
            loadFacultyMainRecycler();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {
                LogInActivity.mAuth.signOut();
            return true;
        }
        if(id == R.id.action_add_class){
                addNewClass();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id==R.id.nav_profile){
            startActivity(new Intent(this,UserProfile.class));
        }
        else if (id == R.id.nav_gallery) {
        }
        else if (id == R.id.nav_slideshow) {
        }
        else if (id == R.id.nav_share) {
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void onResume() {
        //We Connect Auth listener when activity resume
        super.onResume();
        LogInActivity.mAuth.addAuthStateListener(authStateListener);
    }

    protected void onPause() {
        //We disconnect Auth listener when activity pause
        super.onPause();
        LogInActivity.mAuth.removeAuthStateListener(authStateListener);
    }

    public void addNewClass(){
        if(MainActivity.FACULTY) {
            final Dialog addClassPopup = new Dialog(this);
            addClassPopup.setContentView(R.layout.add_new_class_faculty_layout);
            final EditText className, joinCode, sessionStart, sessionEnd, startRoll, endRoll;
            Button addClass = addClassPopup.findViewById(R.id.bt_add_new_class_popup);
            className = addClassPopup.findViewById(R.id.et_name_add_new_class_popup);
            joinCode = addClassPopup.findViewById(R.id.et_code_add_new_class_popup);
            sessionStart = addClassPopup.findViewById(R.id.et_session_start_add_new_class_popup);
            sessionEnd = addClassPopup.findViewById(R.id.et_session_end_add_new_class_popup);
            startRoll = addClassPopup.findViewById(R.id.et_roll_start_add_new_class_popup);
            endRoll = addClassPopup.findViewById(R.id.et_roll_end_add_new_class_popup);
            addClassPopup.show();
            sessionDatePick(sessionStart);
            sessionDatePick(sessionEnd);
            addClass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClassMasterClass classMasterObject = new ClassMasterClass(LogInActivity.mAuth.getUid(), className.getText().toString(),joinCode.getText().toString(),
                            sessionStart.getText().toString(), sessionEnd.getText().toString(), startRoll.getText().toString(), endRoll.getText().toString());
                    mRefClass.push().setValue(classMasterObject);
                    addClassPopup.dismiss();
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    finish();
                    startActivity(getIntent());
                }
            });
        }
        else{
            //Student add here
        }
    }

    private void sessionDatePick(@NotNull final EditText editText){
        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                editText.setText(sdf.format(myCalendar.getTime()));
            }

        };

        editText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(MainActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private void loadFacultyMainRecycler(){

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
    }

}
