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

import com.mikhaellopez.circularimageview.CircularImageView;

public class ProfileFragment extends Fragment {

    private CircularImageView profile_image;
    private ListView listView;
    private ProfileListAdapter adapter;
    private String[] titles = {"Games", "Diary", "Lists", "Reviews", "To-Play List" , "Following", "Followers"};
    private int[] count = {0, 0, 0, 0, 0, 0, 0};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profile_image = view.findViewById(R.id.profile_circularImage);
        listView = view.findViewById(R.id.profile_listView);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new ProfileListAdapter(titles, count, getContext());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (titles[i]) {
                    case "Games":
                        UserGamesFragment userGamesFragment = new UserGamesFragment();
                        getParentFragmentManager().beginTransaction().replace(R.id.main_activity_drawerLayout, userGamesFragment).commit();
                        break;
                    case "Diary":
                        UserDiaryFragment userDiaryFragment = new UserDiaryFragment();
                        getParentFragmentManager().beginTransaction().replace(R.id.main_activity_drawerLayout, userDiaryFragment).commit();
                        break;
                    case "Lists":
                        UserListsFragment userListsFragment = new UserListsFragment();
                        getParentFragmentManager().beginTransaction().replace(R.id.main_activity_drawerLayout, userListsFragment).commit();
                        break;
                    case "Reviews":
                        UserReviewsFragment userReviewsFragment = new UserReviewsFragment();
                        getParentFragmentManager().beginTransaction().replace(R.id.main_activity_drawerLayout, userReviewsFragment).commit();
                        break;
                    case "To-Play List":
                        UserToPlayListFragment userToPlayListFragment = new UserToPlayListFragment();
                        getParentFragmentManager().beginTransaction().replace(R.id.main_activity_drawerLayout, userToPlayListFragment).commit();
                        break;
                    case "Following":
                        UserFollowingFragment userFollowingFragment = new UserFollowingFragment();
                        getParentFragmentManager().beginTransaction().replace(R.id.main_activity_drawerLayout, userFollowingFragment).commit();
                        break;
                    case "Followers":
                        UserFollowersFragment userFollowersFragment = new UserFollowersFragment();
                        getParentFragmentManager().beginTransaction().replace(R.id.main_activity_drawerLayout, userFollowersFragment).commit();
                        break;
                }
            }
        });
    }
}