package com.hakankaraotcu.gamedb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextUsername, editTextPassword;
    private CheckBox checkBoxAge, checkBoxPrivacy;

    private String editEmail, editUsername, editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // EditTexts
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextUsername = (EditText) findViewById(R.id.username);
        editTextPassword = (EditText) findViewById(R.id.password);

        //  CheckBoxs
        checkBoxAge =  (CheckBox) findViewById(R.id.ageCheckBox);
        checkBoxPrivacy = (CheckBox) findViewById(R.id.privacyCheckBox);
    }

    public void btnRegister(View v){
        editEmail = editTextEmail.getText().toString();
        editUsername = editTextUsername.getText().toString();
        editPassword = editTextPassword.getText().toString();

        if(!TextUtils.isEmpty(editEmail) && !TextUtils.isEmpty(editUsername) && !TextUtils.isEmpty(editPassword)){
            if(checkBoxAge.isChecked() && checkBoxPrivacy.isChecked()){

            }
        }
    }
}