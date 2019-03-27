package com.example.dell.classmanagmentapp;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LogInActivity extends AppCompatActivity {
    TextView createAccountTextView;
    EditText logInEmailEditText, logInPasswordEditText, signUpNameEditText, signUpEmailEditText, signUpPhoneEditText, signUpConfirmPasswordEditText,
             signUpJoinCodeEditText, logInJoinCode, signUpPasswordEditText;
    Button signUpButton, logInButton, signupPopupExitButton;
    RadioButton isFacultyRadioButton, isFacultySignUp;
    Dialog signupDialog;

    FirebaseDatabase mFirebaseDatabase;  //Create a instance of Firebase database
    DatabaseReference mDatabaseReference;   // It is used to Refers to a particular part of Database
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("users");
        mAuth = FirebaseAuth.getInstance();
        signupDialog = new Dialog(this);
        logInPasswordEditText = findViewById(R.id.et_password_logInActivity);
        logInEmailEditText = findViewById(R.id.et_email_logInActivity);
        logInJoinCode = findViewById(R.id.et_joinCode_logInActivity);
        isFacultyRadioButton = findViewById(R.id.radioButton_isFaculty_loginActivity);

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
        isFacultyRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                logInJoinCode.setVisibility(View.VISIBLE);
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
        signUpJoinCodeEditText = signupDialog.findViewById(R.id.et_joinCode_signUpPopup);
        signUpButton = signupDialog.findViewById(R.id.bt_signUp_signUpPopup);
        signupPopupExitButton = signupDialog.findViewById(R.id.bt_exit_signUpPopup);
        isFacultySignUp = signupDialog.findViewById(R.id.radioButton_faculty_signUpPopup);
        signupDialog.show();

        isFacultySignUp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                signUpJoinCodeEditText.setVisibility(View.VISIBLE);
            }
        });

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
                                                signUpJoinCodeEditText.getText().toString(),
                                                signUpPhoneEditText.getText().toString(),
                                                isFacultySignUp.isChecked());
                                        mDatabaseReference.push().setValue(newUserObj);
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
                            FirebaseUser user = mAuth.getCurrentUser();
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

}
