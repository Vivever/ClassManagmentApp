package com.example.dell.classmanagmentapp;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LogInActivity extends AppCompatActivity {
    TextView createAccountTextView;
    EditText logInEmailEditText, logInPasswordEditText, signUpNameEditText, signUpEmailEditText, signUpPhoneEditText, signUpConfirmPasswordEditText,
             signUpPasswordEditText;
    Button signUpButton, logInButton, signupPopupExitButton;
    RadioButton isFacultyRadioButton, isFacultySignUp;
    Dialog signupDialog;

    FirebaseDatabase mFirebaseDatabase;  //Create a instance of Firebase database
    public static FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        signupDialog = new Dialog(this);
        logInPasswordEditText = findViewById(R.id.et_password_logInActivity);
        logInEmailEditText = findViewById(R.id.et_email_logInActivity);

        createAccountTextView = findViewById(R.id.tv_createAccount_logInActivity);
        createAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpFunction();
            }
        });
        logInButton = findViewById(R.id.bt_logIn_logInActivity);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logInFunction();
            }
        });


    }


    public void signUpFunction(){
        signupDialog.setContentView(R.layout.sign_up_popup);
        signUpNameEditText = signupDialog.findViewById(R.id.et_name_signUpPopup);
        signUpEmailEditText = signupDialog.findViewById(R.id.et_email_signUpPopup);
        signUpPhoneEditText = signupDialog.findViewById(R.id.et_phone_signUpPopup);
        signUpConfirmPasswordEditText = signupDialog.findViewById(R.id.et_password2_signUpPopup);
        signUpPasswordEditText = signupDialog.findViewById(R.id.et_password_signUpPopup);
        signUpButton = signupDialog.findViewById(R.id.bt_signUp_signUpPopup);
        signupPopupExitButton = signupDialog.findViewById(R.id.bt_exit_signUpPopup);
        isFacultySignUp = signupDialog.findViewById(R.id.radioButton_faculty_signUpPopup);
        signupDialog.show();



        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (signUpPasswordEditText.getText().toString().equals(signUpConfirmPasswordEditText.getText().toString())) {
                    mAuth.createUserWithEmailAndPassword(signUpEmailEditText.getText().toString(), signUpPasswordEditText.getText().toString())
                            .addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        UserObjectClass newUserObj = new UserObjectClass(mAuth.getUid(), signUpNameEditText.getText().toString(),
                                                signUpEmailEditText.getText().toString(),
                                                signUpPhoneEditText.getText().toString(),
                                                isFacultySignUp.isChecked());
                                        MainActivity.mRefUsers.push().setValue(newUserObj);
                                        signupDialog.dismiss();
                                    } else {
                                        // If sign in fails, display a message to the user.

                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(LogInActivity.this, "Password Not Match", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signupPopupExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupDialog.dismiss();
            }
        });
    }

    public void logInFunction(){
        String email = logInEmailEditText.getText().toString();
        String password = logInPasswordEditText.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LogInActivity.this,"Try Again",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public static void getCurrentUser(){
        final FirebaseUser user = mAuth.getCurrentUser();
        MainActivity.mRefUsers.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                UserObjectClass userObject = dataSnapshot.getValue(UserObjectClass.class);
                if(userObject.getUid().equals(user.getUid())){
                    MainActivity.FACULTY = userObject.isFaculty();
                    MainActivity.CURRENT_USER = userObject;
                }
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
    }

}
