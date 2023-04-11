package com.hakankaraotcu.gamedb;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;
import com.hakankaraotcu.gamedb.Model.User;

import java.util.ArrayList;

public class CreateListFragment extends Fragment {

    private EditText editListName, editListDescription;
    private String txtListName, txtListDescription;
    private Button confirmButton;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore mFirestore;
    private Lists list;

    public CreateListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();

        editListName = view.findViewById(R.id.create_list_listName);
        editListDescription = view.findViewById(R.id.create_list_description);
        confirmButton = view.findViewById(R.id.create_list_confirm);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createList();
            }
        });
    }

    private void createList(){
        txtListName = editListName.getText().toString();
        txtListDescription = editListDescription.getText().toString();

        if(txtListName.isEmpty()){
            editListName.setError("List name is required");
            editListName.requestFocus();
            return;
        }
        if(txtListDescription.isEmpty()){
            editListDescription.setError("List description is required");
            editListDescription.requestFocus();
            return;
        }

        list = new Lists(txtListName, txtListDescription);
        mFirestore.collection("Users").document(mUser.getUid()).collection("Lists").add(list);

        ProfileFragment profileFragment = new ProfileFragment();
        getParentFragmentManager().beginTransaction().replace(R.id.user_popular_RelativeLayout, profileFragment, null).addToBackStack(null).commit();

    }
}