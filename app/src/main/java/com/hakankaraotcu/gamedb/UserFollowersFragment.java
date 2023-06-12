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

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class UserFollowersFragment extends Fragment {
    private ListView listView;
    private FollowingFollowersAdapter adapter;
    private int[] images = {R.drawable.discoelysium, R.drawable.monsterhunter, R.drawable.halflife, R.drawable.skyrim, R.drawable.darksiders2, R.drawable.cuphead, R.drawable.halo, R.drawable.bioshock};
    private String[] usernames = {"John", "Mike", "Kelly", "Harry", "Jake", "Ashe", "Kobe", "Castle"};
    private User user;
    private String userID;
    private Button backButton;
    private TextView profile_username;
    private Query mQuery, mQuery2;
    private ArrayList<Object> userIDs;
    private ArrayList<User> followers;
    private ProfileFragment profileFragment;

    public UserFollowersFragment(User user) {
        this.user = user;
    }

    public UserFollowersFragment(String userID) {
        this.userID = userID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_followers, container, false);

        backButton = view.findViewById(R.id.user_followers_backButton);
        listView = view.findViewById(R.id.user_followers_listView);
        profile_username = view.findViewById(R.id.user_followers_username);

        userIDs = new ArrayList<>();
        followers = new ArrayList<>();

        mQuery = AppGlobals.db.collection("Users").document(user.getId()).collection("Followers");
        mQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    userIDs.add(documentSnapshot.getId());
                }
                mQuery2 = AppGlobals.db.collection("Users");
                for(Object followerUserID : userIDs){
                    mQuery2.whereEqualTo("id", followerUserID).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                User user = documentSnapshot.toObject(User.class);

                                assert user != null;
                                followers.add(user);
                            }
                            adapter = new FollowingFollowersAdapter(followers, getContext());
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

        profile_username.setText(user.getUsername() + "'s Followers");
    }
}