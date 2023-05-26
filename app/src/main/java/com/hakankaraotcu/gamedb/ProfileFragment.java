package com.hakankaraotcu.gamedb;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hakankaraotcu.gamedb.Adapter.ProfileListAdapter;
import com.hakankaraotcu.gamedb.General.AppGlobals;
import com.hakankaraotcu.gamedb.Model.User;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {
    private TextView profile_username;
    private CircularImageView profile_avatar;
    private ListView listView;
    private ProfileListAdapter adapter;
    private ArrayList<Integer> count;
    private Query mQuery;
    private User user;
    private String userID;

    public ProfileFragment(String userID) {
        this.userID = userID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        count = new ArrayList<>();

        listView = view.findViewById(R.id.profile_listView);

        profile_avatar = view.findViewById(R.id.profile_avatar);
        profile_username = view.findViewById(R.id.profile_username);

        mQuery = AppGlobals.db.collection("Users");
        mQuery.whereEqualTo("id", userID).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                    user = documentSnapshot.toObject(User.class);
                    count.add(user.getPlayedCount());
                    count.add(user.getDiaryCount());
                    count.add(user.getListsCount());
                    count.add(user.getReviewsCount());
                    count.add(user.getToPlayCount());
                    count.add(user.getLikedCount());
                    count.add(user.getFollowingCount());
                    count.add(user.getFollowersCount());
                }
                profile_username.setText(user.getUsername());
                if (user.getAvatar().equals("default")) {
                    profile_avatar.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Picasso.get().load(user.getAvatar()).resize(120, 120).into(profile_avatar);
                }
                adapter = new ProfileListAdapter(AppGlobals.profileTitles, count, getContext());
                listView.setAdapter(adapter);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
                            getParentFragmentManager().beginTransaction().replace(R.id.guest_main_RelativeLayout, userListsFragment, null).addToBackStack(null).commit();
                        }
                        // for user
                        if (getActivity().getLocalClassName().equals("UserMainActivity")) {
                            getParentFragmentManager().beginTransaction().replace(R.id.user_main_RelativeLayout, userListsFragment, null).addToBackStack(null).commit();
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