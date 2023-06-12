package com.hakankaraotcu.gamedb;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hakankaraotcu.gamedb.Adapter.ProfileListAdapter;
import com.hakankaraotcu.gamedb.General.AppGlobals;
import com.hakankaraotcu.gamedb.Model.User;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {
    private TextView profile_username, profile_bio;
    private MaterialButton profile_follow;
    private CircularImageView profile_avatar;
    private ListView listView;
    private ProfileListAdapter adapter;
    private ArrayList<Integer> count;
    private CollectionReference followingReference, followersReference;
    private DocumentReference userReference;
    private User user;
    private String userID;
    private Button backButton;

    public ProfileFragment(String userID) {
        this.userID = userID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        backButton = view.findViewById(R.id.profile_backButton);

        count = new ArrayList<>();

        listView = view.findViewById(R.id.profile_listView);

        profile_avatar = view.findViewById(R.id.profile_avatar);
        profile_username = view.findViewById(R.id.profile_username);
        profile_bio = view.findViewById(R.id.profile_bio);
        profile_follow = view.findViewById(R.id.profile_follow);

        followingReference = AppGlobals.db.collection("Users").document(AppGlobals.currentUser.getId()).collection("Following");
        followersReference = AppGlobals.db.collection("Users").document(userID).collection("Followers");

        userReference = AppGlobals.db.collection("Users").document(userID);
        userReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
                count.add(user.getPlayedCount());
                count.add(user.getDiaryCount());
                count.add(user.getListsCount());
                count.add(user.getReviewsCount());
                count.add(user.getToPlayCount());
                count.add(user.getLikedCount());
                count.add(user.getFollowingCount());
                count.add(user.getFollowersCount());

                System.out.println(count);
                profile_username.setText(user.getUsername());
                profile_bio.setText(user.getBio());

                if(user.getBio().equals("")){
                    profile_bio.setVisibility(View.GONE);
                }
                else{
                    profile_bio.setVisibility(View.VISIBLE);
                }

                if (user.getAvatar().equals("default")) {
                    profile_avatar.setImageResource(com.taufiqrahman.reviewratings.R.drawable.ic_person);
                } else {
                    Picasso.get().load(user.getAvatar()).resize(120, 120).into(profile_avatar);
                }

                System.out.println("work");
                adapter = new ProfileListAdapter(AppGlobals.profileTitles, count, getContext());
                listView.setAdapter(adapter);
            }
        });
        if(userID.equals(AppGlobals.mAuth.getUid())){
            profile_follow.setVisibility(View.GONE);
        }
        else{
            profile_follow.setVisibility(View.VISIBLE);
        }
        followingReference.document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    profile_follow.setText("Following");
                    profile_follow.setBackgroundTintList(ColorStateList.valueOf(Color.argb(255, 21, 187, 50)));
                }
                else{
                    profile_follow.setText("Follow");
                    profile_follow.setBackgroundTintList(ColorStateList.valueOf(Color.argb(255, 68, 85, 102)));
                }
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profile_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(profile_follow.getText().toString().equals("Follow")){
                    Map<String, Object> followingData = new HashMap<>();
                    followingData.put("followingData", userID);
                    Map<String, Object> followerData = new HashMap<>();

                    followerData.put("followerData", AppGlobals.currentUser.getId());
                    followingReference.document(userID).set(followingData);
                    followingReference.getParent().update("followingCount", FieldValue.increment(1));
                    followersReference.document(AppGlobals.currentUser.getId()).set(followerData);
                    followersReference.getParent().update("followersCount", FieldValue.increment(1));
                    profile_follow.setText("Following");
                    profile_follow.setBackgroundTintList(ColorStateList.valueOf(Color.argb(255, 21, 187, 50)));
                }
                else if(profile_follow.getText().toString().equals("Following")){
                    followingReference.getParent().update("followingCount", FieldValue.increment(-1));
                    followingReference.document(userID).delete();
                    followersReference.getParent().update("followersCount", FieldValue.increment(-1));
                    followersReference.document(AppGlobals.currentUser.getId()).delete();
                    profile_follow.setText("Follow");
                    profile_follow.setBackgroundTintList(ColorStateList.valueOf(Color.argb(255, 68, 85, 102)));
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (AppGlobals.profileTitles[i]) {
                    case "Games":
                        UserGamesFragment userGamesFragment = new UserGamesFragment(user);
                        // for guest
                        if (getActivity().getLocalClassName().equals("GuestMainActivity")) {
                            getParentFragmentManager().beginTransaction().replace(R.id.guest_main_RelativeLayout, userGamesFragment, null).addToBackStack(null).commit();
                        }
                        // for user
                        if (getActivity().getLocalClassName().equals("UserMainActivity")) {
                            getParentFragmentManager().beginTransaction().replace(R.id.user_main_RelativeLayout, userGamesFragment, null).addToBackStack(null).commit();
                        }
                        break;
                    case "Diary":
                        UserDiaryFragment userDiaryFragment = new UserDiaryFragment(user);
                        // for guest
                        if (getActivity().getLocalClassName().equals("GuestMainActivity")) {
                            getParentFragmentManager().beginTransaction().replace(R.id.guest_main_RelativeLayout, userDiaryFragment, null).addToBackStack(null).commit();
                        }
                        // for user
                        if (getActivity().getLocalClassName().equals("UserMainActivity")) {
                            getParentFragmentManager().beginTransaction().replace(R.id.user_main_RelativeLayout, userDiaryFragment, null).addToBackStack(null).commit();
                        }
                        break;
                    case "Lists":
                        UserListsFragment userListsFragment = new UserListsFragment(user);
                        // for guest
                        if (getActivity().getLocalClassName().equals("GuestMainActivity")) {
                            getParentFragmentManager().beginTransaction().replace(R.id.guest_main_RelativeLayout, userListsFragment, "userListsFragment").addToBackStack(null).commit();
                        }
                        // for user
                        if (getActivity().getLocalClassName().equals("UserMainActivity")) {
                            getParentFragmentManager().beginTransaction().replace(R.id.user_main_RelativeLayout, userListsFragment, "userListsFragment").addToBackStack(null).commit();
                        }
                        break;
                    case "Reviews":
                        UserReviewsFragment userReviewsFragment = new UserReviewsFragment(user);
                        // for guest
                        if (getActivity().getLocalClassName().equals("GuestMainActivity")) {
                            getParentFragmentManager().beginTransaction().replace(R.id.guest_main_RelativeLayout, userReviewsFragment, null).addToBackStack(null).commit();
                        }
                        // for user
                        if (getActivity().getLocalClassName().equals("UserMainActivity")) {
                            getParentFragmentManager().beginTransaction().replace(R.id.user_main_RelativeLayout, userReviewsFragment, null).addToBackStack(null).commit();
                        }
                        break;
                    case "To-Play List":
                        UserToPlayListFragment userToPlayListFragment = new UserToPlayListFragment(user);
                        // for guest
                        if (getActivity().getLocalClassName().equals("GuestMainActivity")) {
                            getParentFragmentManager().beginTransaction().replace(R.id.guest_main_RelativeLayout, userToPlayListFragment, null).addToBackStack(null).commit();
                        }
                        // for user
                        if (getActivity().getLocalClassName().equals("UserMainActivity")) {
                            getParentFragmentManager().beginTransaction().replace(R.id.user_main_RelativeLayout, userToPlayListFragment, null).addToBackStack(null).commit();
                        }
                        break;
                    case "Likes":
                        UserLikesFragment userLikesFragment = new UserLikesFragment(user);
                        // for guest
                        if (getActivity().getLocalClassName().equals("GuestMainActivity")) {
                            getParentFragmentManager().beginTransaction().replace(R.id.guest_main_RelativeLayout, userLikesFragment, null).addToBackStack(null).commit();
                        }
                        // for user
                        if (getActivity().getLocalClassName().equals("UserMainActivity")) {
                            getParentFragmentManager().beginTransaction().replace(R.id.user_main_RelativeLayout, userLikesFragment, null).addToBackStack(null).commit();
                        }
                        break;
                    case "Following":
                        UserFollowingFragment userFollowingFragment = new UserFollowingFragment(user);
                        // for guest
                        if (getActivity().getLocalClassName().equals("GuestMainActivity")) {
                            getParentFragmentManager().beginTransaction().replace(R.id.guest_main_RelativeLayout, userFollowingFragment, null).addToBackStack(null).commit();
                        }
                        // for user
                        if (getActivity().getLocalClassName().equals("UserMainActivity")) {
                            getParentFragmentManager().beginTransaction().replace(R.id.user_main_RelativeLayout, userFollowingFragment, null).addToBackStack(null).commit();
                        }
                        break;
                    case "Followers":
                        UserFollowersFragment userFollowersFragment = new UserFollowersFragment(user);
                        // for guest
                        if (getActivity().getLocalClassName().equals("GuestMainActivity")) {
                            getParentFragmentManager().beginTransaction().replace(R.id.guest_main_RelativeLayout, userFollowersFragment, null).addToBackStack(null).commit();
                        }
                        // for user
                        if (getActivity().getLocalClassName().equals("UserMainActivity")) {
                            getParentFragmentManager().beginTransaction().replace(R.id.user_main_RelativeLayout, userFollowersFragment, null).addToBackStack(null).commit();
                        }
                        break;
                }
            }
        });
    }
}