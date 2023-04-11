package com.hakankaraotcu.gamedb;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private CircularImageView profile_image;
    private ListView listView;
    private ProfileListAdapter adapter;
    private String[] titles = {"Games", "Diary", "Lists", "Reviews", "To-Play List" , "Following", "Followers"};
    private int[] count = {0, 0, 0, 0, 0, 0, 0};
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore db;
    private CollectionReference listsReference;
    private ArrayList<Lists> lists = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        listsReference = db.collection("Users").document(mUser.getUid()).collection("Lists");

        listsReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    return;
                }
                int index = 0;
                for(QueryDocumentSnapshot documentSnapshot : value){
                    Lists list = documentSnapshot.toObject(Lists.class);
                    list.setId(documentSnapshot.getId());
                    if(index > lists.size()){
                        lists.add(list);
                    }
                    index++;
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profile_image = view.findViewById(R.id.profile_circularImage);
        listView = view.findViewById(R.id.profile_listView);

        adapter = new ProfileListAdapter(titles, count, getContext());
        listView.setAdapter(adapter);

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
                        UserListsFragment userListsFragment = new UserListsFragment(lists);
                        getParentFragmentManager().beginTransaction().replace(R.id.user_popular_RelativeLayout, userListsFragment, null).addToBackStack(null).commit();
                        break;
                    case "Reviews":
                        UserReviewsFragment userReviewsFragment = new UserReviewsFragment();
                        getParentFragmentManager().beginTransaction().replace(R.id.user_popular_RelativeLayout, userReviewsFragment, null).addToBackStack(null).commit();
                        break;
                    case "To-Play List":
                        UserToPlayListFragment userToPlayListFragment = new UserToPlayListFragment();
                        getParentFragmentManager().beginTransaction().replace(R.id.user_popular_RelativeLayout, userToPlayListFragment, null).addToBackStack(null).commit();
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