package com.hakankaraotcu.gamedb;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hakankaraotcu.gamedb.Model.User;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private CircularImageView profile_image;
    private ListView listView;
    private ProfileListAdapter adapter;
    private final String[] titles = {"Games", "Diary", "Lists", "Reviews", "To-Play List", "Likes", "Following", "Followers"};
    private ArrayList<Integer> count;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore mFirestore;
    private Query mQuery;
    private User user;

    public ProfileFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();

        count = new ArrayList<>();

        listView = view.findViewById(R.id.profile_listView);

        mQuery = mFirestore.collection("Users");
        mQuery.whereEqualTo("id", mUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
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
                adapter = new ProfileListAdapter(titles, count, getContext());
                listView.setAdapter(adapter);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profile_image = view.findViewById(R.id.profile_circularImage);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (titles[i]) {
                    case "Games":
                        UserGamesFragment userGamesFragment = new UserGamesFragment();
                        getParentFragmentManager().beginTransaction().replace(R.id.user_popular_RelativeLayout, userGamesFragment, null).addToBackStack(null).commit();
                        break;
                    case "Diary":
                        UserDiaryFragment userDiaryFragment = new UserDiaryFragment();
                        getParentFragmentManager().beginTransaction().replace(R.id.user_popular_RelativeLayout, userDiaryFragment, null).addToBackStack(null).commit();
                        break;
                    case "Lists":
                        UserListsFragment userListsFragment = new UserListsFragment();
                        getParentFragmentManager().beginTransaction().replace(R.id.user_popular_RelativeLayout, userListsFragment, "userListsFragment").addToBackStack(null).commit();
                        break;
                    case "Reviews":
                        UserReviewsFragment userReviewsFragment = new UserReviewsFragment();
                        getParentFragmentManager().beginTransaction().replace(R.id.user_popular_RelativeLayout, userReviewsFragment, null).addToBackStack(null).commit();
                        break;
                    case "To-Play List":
                        UserToPlayListFragment userToPlayListFragment = new UserToPlayListFragment();
                        getParentFragmentManager().beginTransaction().replace(R.id.user_popular_RelativeLayout, userToPlayListFragment, null).addToBackStack(null).commit();
                        break;
                    case "Likes":
                        UserLikesFragment userLikesFragment = new UserLikesFragment();
                        getParentFragmentManager().beginTransaction().replace(R.id.user_popular_RelativeLayout, userLikesFragment, null).addToBackStack(null).commit();
                        break;
                    case "Following":
                        UserFollowingFragment userFollowingFragment = new UserFollowingFragment();
                        getParentFragmentManager().beginTransaction().replace(R.id.user_popular_RelativeLayout, userFollowingFragment, null).addToBackStack(null).commit();
                        break;
                    case "Followers":
                        UserFollowersFragment userFollowersFragment = new UserFollowersFragment();
                        getParentFragmentManager().beginTransaction().replace(R.id.user_popular_RelativeLayout, userFollowersFragment, null).addToBackStack(null).commit();
                        break;
                }
            }
        });
    }
}