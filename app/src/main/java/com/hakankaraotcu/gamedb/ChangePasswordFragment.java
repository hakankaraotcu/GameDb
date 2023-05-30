package com.hakankaraotcu.gamedb;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.hakankaraotcu.gamedb.General.AppGlobals;

public class ChangePasswordFragment extends Fragment {
    private EditText currentPassword, newPassword, confirmPassword;
    private TextView changePassword;
    private String txtCurrentPassword, txtNewPassword, txtConfirmPassword;
    private Button cancelButton;
    private DocumentReference userReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentPassword = view.findViewById(R.id.change_password_currentPassword);
        newPassword = view.findViewById(R.id.change_password_newPassword);
        confirmPassword = view.findViewById(R.id.change_password_confirmPassword);
        changePassword = view.findViewById(R.id.change_password_changePassword);
        cancelButton = view.findViewById(R.id.change_password_cancel);

        userReference = AppGlobals.db.collection("Users").document(AppGlobals.mUser.getUid());

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePassword();
            }
        });
    }

    private void updatePassword() {
        txtCurrentPassword = currentPassword.getText().toString();
        txtNewPassword = newPassword.getText().toString();
        txtConfirmPassword = confirmPassword.getText().toString();

        if (txtCurrentPassword.isEmpty()) {
            currentPassword.setError("This field is required");
            currentPassword.requestFocus();
            return;
        }
        if (txtNewPassword.isEmpty()) {
            newPassword.setError("This field is required");
            newPassword.requestFocus();
            return;
        }
        if (txtConfirmPassword.isEmpty()) {
            confirmPassword.setError("This field is required");
            confirmPassword.requestFocus();
            return;
        }
        if (txtNewPassword.length() < 6) {
            newPassword.setError("Please enter at least 6 characters");
            newPassword.requestFocus();
            return;
        }
        if (!txtNewPassword.equals(txtConfirmPassword)) {
            newPassword.setError("Your new password and confirm password do not match");
            newPassword.requestFocus();
            return;
        }
        if (!txtCurrentPassword.equals(AppGlobals.currentUser.getPassword())) {
            currentPassword.setError("Current password is incorrect");
            currentPassword.requestFocus();
            return;
        } else {
            userReference.update("password", txtNewPassword);
            AppGlobals.currentUser.setPassword(txtNewPassword);
            getParentFragmentManager().popBackStack();
        }
    }
}