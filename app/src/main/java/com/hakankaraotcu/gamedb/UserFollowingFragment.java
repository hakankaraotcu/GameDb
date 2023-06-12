package com.hakankaraotcu.gamedb;

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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hakankaraotcu.gamedb.Adapter.FollowingFollowersAdapter;
import com.hakankaraotcu.gamedb.General.AppGlobals;
import com.hakankaraotcu.gamedb.Model.User;

import java.util.ArrayList;

public class UserFollowingFragment extends Fragment {
    private ListView listView;
    private FollowingFollowersAdapter adapter;
    private int[] images = {R.drawable.discoelysium, R.drawable.monsterhunter, R.drawable.halflife, R.drawable.cuphead, R.drawable.darksiders2};
    private String[] usernames = {"John", "Mike", "Kelly", "Ashe", "Jake"};
    private User user;
    private String userID;
    private Button backButton;
    private Query mQuery, mQuery2;
    private ArrayList<Object> userIDs;
    private ArrayList<User> following;
    private ProfileFragment profileFragment;

    private TextView profile_username;

    public UserFollowingFragment(User user) {
        this.user = user;
    }

    public UserFollowingFragment(String userID) {
        this.userID = userID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_following, container, false);

        backButton = view.findViewById(R.id.user_following_backButton);
        listView = view.findViewById(R.id.user_following_listView);
        profile_username = view.findViewById(R.id.user_following_username);

        userIDs = new ArrayList<>();
        following = new ArrayList<>();

        mQuery = AppGlobals.db.collection("Users").document(user.getId()).collection("Following");
        mQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    userIDs.add(documentSnapshot.getId());
                }
                mQuery2 = AppGlobals.db.collection("Users");
                for(Object followingUserID : userIDs){
                    mQuery2.whereEqualTo("id", followingUserID).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                User user = documentSnapshot.toObject(User.class);

                                assert user != null;
                                following.add(user);
                            }
                            adapter = new FollowingFollowersAdapter(following, getContext());
                            listView.setAdapter(adapter);
                        }
                    });
                }
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                profileFragment = new ProfileFragment(userIDs.get(i).toString());
                // for guest
                if (getActivity().getLocalClassName().equals("GuestMainActivity")) {
                    getParentFragmentManager().beginTransaction().replace(R.id.guest_main_RelativeLayout, profileFragment, null).addToBackStack(null).commit();
                }
                // for user
                if (getActivity().getLocalClassName().equals("UserMainActivity")) {
                    getParentFragmentManager().beginTransaction().replace(R.id.user_main_RelativeLayout, profileFragment, null).addToBackStack(null).commit();
                }
            }
        });

        profile_username.setText("Followed By " + user.getUsername());
    }
}