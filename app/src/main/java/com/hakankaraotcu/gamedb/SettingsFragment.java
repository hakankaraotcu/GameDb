package com.hakankaraotcu.gamedb;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.hakankaraotcu.gamedb.General.AppGlobals;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class SettingsFragment extends Fragment {
    private EditText editUsername, editEmail, editWebsite, editBio;
    private TextView changePassword, avatarChange;
    private CircularImageView editAvatar;
    private DocumentReference userReference;
    private Button cancelButton, confirmButton;
    private ChangePasswordFragment changePasswordFragment;
    private Uri selectedImageUri;
    private StorageTask uploadTask;
    private StorageReference storageReference;
    private Bitmap selectedImageBitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        changePasswordFragment = new ChangePasswordFragment();

        editUsername = view.findViewById(R.id.settings_username);
        editEmail = view.findViewById(R.id.settings_email);
        changePassword = view.findViewById(R.id.settings_changePassword);
        editWebsite = view.findViewById(R.id.settings_website);
        editBio = view.findViewById(R.id.settings_bio);
        editAvatar = view.findViewById(R.id.settings_avatar);
        avatarChange = view.findViewById(R.id.settings_avatar_change);
        cancelButton = view.findViewById(R.id.settings_cancel);
        confirmButton = view.findViewById(R.id.settings_confirm);

        userReference = AppGlobals.db.collection("Users").document(AppGlobals.mUser.getUid());

        if (AppGlobals.currentUser != null) {
            editUsername.setText(AppGlobals.currentUser.getUsername());
            editEmail.setText(AppGlobals.currentUser.getEmail());
            editBio.setText(AppGlobals.currentUser.getBio());
            editWebsite.setText(AppGlobals.currentUser.getWebsite());

            if (AppGlobals.currentUser.getAvatar().equals("default")) {
                editAvatar.setImageResource(R.mipmap.ic_launcher);
            } else {
                Picasso.get().load(AppGlobals.currentUser.getAvatar()).resize(156, 156).into(editAvatar);
            }
        }

        editAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });

        avatarChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction().replace(R.id.user_main_RelativeLayout, changePasswordFragment, null).addToBackStack(null).commit();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile(editUsername.getText().toString(), editEmail.getText().toString(), editWebsite.getText().toString(), editBio.getText().toString());
            }
        });
    }

    private void uploadImage(){
        if(selectedImageUri != null){
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + selectedImageBitmap);

            uploadTask = fileReference.putFile(selectedImageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String myUrl = downloadUri.toString();

                        userReference.update("avatar", ""+myUrl);
                        AppGlobals.currentUser.setAvatar(""+myUrl);
                        getParentFragmentManager().popBackStack();
                    }
                    else{
                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                        getParentFragmentManager().popBackStack();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().popBackStack();
                }
            });
        }
        else{
            Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().popBackStack();
        }
    }

    private void updateProfile(String username, String email, String website, String bio){
        userReference.update("email", email, "website", website, "bio", bio);
        AppGlobals.currentUser.setUsername(username);
        AppGlobals.currentUser.setEmail(email);
        AppGlobals.currentUser.setWebsite(website);
        AppGlobals.currentUser.setBio(bio);
        uploadImage();
    }

    private void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        launchSomeActivity.launch(i);
    }

    ActivityResultLauncher<Intent> launchSomeActivity
            = registerForActivityResult(
            new ActivityResultContracts
                    .StartActivityForResult(),
            result -> {
                if (result.getResultCode()
                        == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    // do your operation from here....
                    if (data != null
                            && data.getData() != null) {
                        selectedImageUri = data.getData();
                        selectedImageBitmap = null;
                        try {
                            selectedImageBitmap
                                    = MediaStore.Images.Media.getBitmap(
                                    getActivity().getContentResolver(),
                                    selectedImageUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        editAvatar.setImageBitmap(
                                selectedImageBitmap);
                    }
                }
            });
}