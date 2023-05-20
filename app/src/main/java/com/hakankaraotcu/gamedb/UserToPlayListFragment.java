package com.hakankaraotcu.gamedb;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hakankaraotcu.gamedb.Adapter.GameAdapter;
import com.hakankaraotcu.gamedb.Model.Game;
import com.hakankaraotcu.gamedb.Model.User;

import java.util.ArrayList;

public class UserToPlayListFragment extends Fragment {
    private GameFragment gameFragment;
    private FirebaseFirestore mFirestore;
    private Query mQuery, mQuery2;
    private ArrayList<Game> games;
    private ArrayList<Object> gamesIDs;
    private User user;
    private String userID;

    private TextView profile_username;

    private GridView mGridView;
    private GameAdapter adapter;

    public UserToPlayListFragment(User user) {
        this.user = user;
    }

    public UserToPlayListFragment(String userID) {
        this.userID = userID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_to_play_list, container, false);

        mFirestore = FirebaseFirestore.getInstance();

        profile_username = view.findViewById(R.id.user_toPlayList_username);

        games = new ArrayList<>();
        gamesIDs = new ArrayList<>();

        mGridView = (GridView) view.findViewById(R.id.userToPlayList_gridView);

        mQuery = mFirestore.collection("ToPlayGames");
        mQuery.whereEqualTo("userID", user.getId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                    gamesIDs.add(documentSnapshot.get("gameID"));
                }
                for(Object gameID : gamesIDs){
                    mQuery2 = mFirestore.collection("Games");
                    mQuery2.whereEqualTo("id", gameID).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                                Game game = documentSnapshot.toObject(Game.class);

                                assert game != null;
                                games.add(game);
                            }
                            adapter = new GameAdapter(games, getActivity());
                            mGridView.setAdapter(adapter);
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

        profile_username.setText(user.getUsername() + "'s to-Playlist");

        gameFragment = new GameFragment();

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle args = new Bundle();
                args.putString("id", games.get(i).getId());
                gameFragment.setArguments(args);
                getParentFragmentManager().beginTransaction().replace(R.id.user_popular_RelativeLayout, gameFragment, null).addToBackStack(null).commit();
            }
        });
    }
}