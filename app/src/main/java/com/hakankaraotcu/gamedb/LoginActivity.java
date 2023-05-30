package com.hakankaraotcu.gamedb;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.hakankaraotcu.gamedb.General.AppGlobals;
import com.hakankaraotcu.gamedb.databinding.ActivityLoginBinding;

public class LoginActivity extends GuestDrawerBaseActivity {
    private ActivityLoginBinding activityLoginBinding;
    private ProgressDialog mProgress;
    private EditText editEmail, editPassword;
    private String txtEmail, txtPassword;
    private Button backButton, joinButton, goButton;
    private ConstraintLayout mConstraint;

    private void init() {
        editEmail = findViewById(R.id.login_email);
        editPassword = findViewById(R.id.login_password);

        mConstraint = findViewById(R.id.login_constraint);

        backButton = findViewById(R.id.login_backButton);
        joinButton = findViewById(R.id.joinBtn);
        goButton = findViewById(R.id.loginBtn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(activityLoginBinding.getRoot());

        init();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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

    private void SignInUser() {
        txtEmail = editEmail.getText().toString();
        txtPassword = editPassword.getText().toString();

        if (txtEmail.isEmpty()) {
            editEmail.setError("Email is required");
            editEmail.requestFocus();
            return;
        }
        if (txtPassword.isEmpty()) {
            editPassword.setError("Password is required");
            editPassword.requestFocus();
            return;
        }

        mProgress = new ProgressDialog(LoginActivity.this);
        mProgress.setTitle("Sign in...");
        mProgress.show();

        AppGlobals.mAuth.signInWithEmailAndPassword(txtEmail, txtPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressSet();
                    Intent intent = new Intent(LoginActivity.this, UserMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    finish();
                    AppGlobals.mUser = AppGlobals.mAuth.getCurrentUser();
                    startActivity(intent);
                } else {
                    progressSet();
                    Snackbar.make(mConstraint, task.getException().getMessage(), Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void progressSet() {
        if (mProgress.isShowing()) {
            mProgress.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setSelectedMenu(R.id.nav_menu_signIn);
    }
}