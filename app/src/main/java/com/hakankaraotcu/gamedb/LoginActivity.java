package com.hakankaraotcu.gamedb;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hakankaraotcu.gamedb.databinding.ActivityLoginBinding;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends GuestDrawerBaseActivity {

    private ActivityLoginBinding activityLoginBinding;

    private ProgressDialog mProgress;
    private EditText editEmail, editPassword;
    private String txtEmail, txtPassword;
    private Button backButton, joinButton, goButton;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore mFirestore;
    private DocumentReference mReference;

    private HashMap<String, Object> mData;
    private ConstraintLayout mConstraint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(activityLoginBinding.getRoot());

        editEmail = findViewById(R.id.login_email);
        editPassword = findViewById(R.id.login_password);

        mConstraint = findViewById(R.id.login_constraint);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();

        backButton = findViewById(R.id.backButton);
        joinButton = findViewById(R.id.joinBtn);
        goButton = findViewById(R.id.loginBtn);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, GuestMainActivity.class));
            }
        });

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignInUser();
            }
        });
    }

    private void SignInUser(){
        txtEmail = editEmail.getText().toString();
        txtPassword = editPassword.getText().toString();

        if(txtEmail.isEmpty()){
            editEmail.setError("Email is required");
            editEmail.requestFocus();
            return;
        }
        if(txtPassword.isEmpty()){
            editPassword.setError("Password is required");
            editPassword.requestFocus();
            return;
        }

        mProgress = new ProgressDialog(LoginActivity.this);
        mProgress.setTitle("Sign in...");
        mProgress.show();

        mAuth.signInWithEmailAndPassword(txtEmail, txtPassword).addOnSuccessListener(LoginActivity.this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                progressSet();
                mUser = mAuth.getCurrentUser();

                assert mUser != null;
                getUserData(mUser.getUid());
            }
        }).addOnFailureListener(LoginActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressSet();
                Snackbar.make(mConstraint, e.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserData(String uid){
        mReference = mFirestore.collection("Users").document(uid);
        mReference.get().addOnSuccessListener(LoginActivity.this, new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    progressSet();
                    mData = (HashMap)documentSnapshot.getData();

                    for(Map.Entry data: mData.entrySet()){
                        System.out.println(data.getKey() + " = " + data.getValue());
                    }
                    Intent intent = new Intent(LoginActivity.this, UserMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        }).addOnFailureListener(LoginActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressSet();
                Snackbar.make(mConstraint, e.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void progressSet(){
        if(mProgress.isShowing()){
            mProgress.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setSelectedMenu(R.id.nav_menu_signIn);
    }
}