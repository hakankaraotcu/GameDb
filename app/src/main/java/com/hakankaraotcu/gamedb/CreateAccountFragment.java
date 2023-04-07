package com.hakankaraotcu.gamedb;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class CreateAccountFragment extends Fragment {

    private EditText editEmail, editUsername, editPassword;
    private CheckBox checkAge, checkPrivacy;
    private String txtEmail, txtUsername, txtPassword;
    private Button backButton, registerButton;
    private DatabaseReference mReference;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore mFirestore;
    private HashMap<String, Object> mData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_account, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editEmail = view.findViewById(R.id.register_email);
        editUsername = view.findViewById(R.id.register_username);
        editPassword = view.findViewById(R.id.register_password);
        checkAge = view.findViewById(R.id.register_ageCheckBox);
        checkPrivacy = view.findViewById(R.id.register_privacyCheckBox);

        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();
        mFirestore = FirebaseFirestore.getInstance();

        backButton = view.findViewById(R.id.backBtn);
        registerButton = view.findViewById(R.id.registerBtn);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MainActivity.class));
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

                    mData = new HashMap<>();
                    mData.put("id", mUser.getUid());
                    mData.put("username", txtUsername);
                    mData.put("email", txtEmail);
                    mData.put("password", txtPassword);

                    mFirestore.collection("Users").document(mUser.getUid()).set(mData).addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getActivity(), "Kayıt işlemi başarılı", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}