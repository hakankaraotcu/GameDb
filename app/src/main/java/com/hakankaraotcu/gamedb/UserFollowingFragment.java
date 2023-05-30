package com.hakankaraotcu.gamedb;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hakankaraotcu.gamedb.Adapter.FollowingFollowersAdapter;
import com.hakankaraotcu.gamedb.Model.User;

public class UserFollowingFragment extends Fragment {
    private ListView listView;
    private FollowingFollowersAdapter adapter;
    private int[] images = {R.drawable.discoelysium, R.drawable.monsterhunter, R.drawable.halflife, R.drawable.cuphead, R.drawable.darksiders2};
    private String[] usernames = {"John", "Mike", "Kelly", "Ashe", "Jake"};
    private User user;
    private String userID;
    private Button backButton;

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

        adapter = new FollowingFollowersAdapter(usernames, images, getContext());
        listView.setAdapter(adapter);

        profile_username.setText("Followed By " + user.getUsername());
    }
}