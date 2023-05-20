package com.hakankaraotcu.gamedb;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hakankaraotcu.gamedb.Model.User;
import com.hakankaraotcu.gamedb.databinding.ActivityRegisterBinding;

public class RegisterActivity extends GuestDrawerBaseActivity {

    ActivityRegisterBinding activityRegisterBinding;

    private User user;
    private EditText editEmail, editUsername, editPassword;
    private CheckBox checkAge, checkPrivacy;
    private String txtEmail, txtUsername, txtPassword;
    private Button backButton, registerButton;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore db;
    private ConstraintLayout mConstraint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityRegisterBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(activityRegisterBinding.getRoot());

        editEmail = findViewById(R.id.register_email);
        editUsername = findViewById(R.id.register_username);
        editPassword = findViewById(R.id.register_password);
        checkAge = findViewById(R.id.register_ageCheckBox);
        checkPrivacy = findViewById(R.id.register_privacyCheckBox);

        mConstraint = findViewById(R.id.register_constraint);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        backButton = findViewById(R.id.backBtn);
        registerButton = findViewById(R.id.registerBtn);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, GuestMainActivity.class));
            }
        });
    }

    private void registerUser(){
        txtEmail = editEmail.getText().toString();
        txtUsername = editUsername.getText().toString();
        txtPassword = editPassword.getText().toString();

        if(txtEmail.isEmpty()){
            editEmail.setError("Email is required");
            editEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(txtEmail).matches()){
            editEmail.setError("Please provide a valid email");
            editEmail.requestFocus();
            return;
        }
        if(txtUsername.isEmpty()){
            editUsername.setError("Username is required");
            editUsername.requestFocus();
            return;
        }
        if(txtPassword.isEmpty()){
            editPassword.setError("Password is required");
            editPassword.requestFocus();
            return;
        }
        if(txtPassword.length() < 6){
            editPassword.setError("Please enter at least 6 characters");
            editPassword.requestFocus();
            return;
        }
        if(!checkAge.isChecked()){
            checkAge.setError("You must accepts the age");
            checkAge.requestFocus();
            return;
        }
        if(!checkPrivacy.isChecked()){
            checkPrivacy.setError("You must accepts the privacy");
            checkPrivacy.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(txtEmail, txtPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    mUser = mAuth.getCurrentUser();

                    if(mUser != null){
                        user = new User(mUser.getUid(), txtUsername, txtEmail, txtPassword);

                        db.collection("Users").document(mUser.getUid()).set(user).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Intent intent = new Intent(RegisterActivity.this, UserMainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    Snackbar.make(mConstraint, task.getException().getMessage(), Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
                else{
                    Snackbar.make(mConstraint, task.getException().getMessage(), Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setSelectedMenu(R.id.nav_menu_createAccount);
    }
}