package com.hakankaraotcu.gamedb;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hakankaraotcu.gamedb.Model.User;

import java.util.ArrayList;

public class GamePlayersFragment extends Fragment {
    private ListView listView;
    private GamePlayersAdapter adapter;
    private int[] images = {R.drawable.discoelysium, R.drawable.monsterhunter, R.drawable.halflife, R.drawable.cuphead, R.drawable.darksiders2};
    private int[] ratings = {3, 2, 4, 5, 1};
    private ArrayList<User> users;
    private ArrayList<Object> usersIDs;
    private FirebaseFirestore mFirestore;
    private DocumentReference userReference;
    private Query mQuery;
    private String gameId;

    public GamePlayersFragment(String gameId){
        this.gameId = gameId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_players, container, false);

        mFirestore = FirebaseFirestore.getInstance();

        users = new ArrayList<>();
        usersIDs = new ArrayList<>();

        listView = view.findViewById(R.id.gamePlayers_listView);

        mQuery = mFirestore.collection("PlayedGames");
        mQuery.whereEqualTo("gameID", gameId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                    usersIDs.add(documentSnapshot.get("userID"));
                }
                for(Object userID : usersIDs){
                    userReference = mFirestore.collection("Users").document(userID.toString());
                    userReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            User user = documentSnapshot.toObject(User.class);

                            assert user != null;
                            user.setId(user.getId());
                            users.add(user);

                            if(usersIDs.size() == users.size()){
                                adapter = new GamePlayersAdapter(users, images, ratings, getContext());
                                listView.setAdapter(adapter);
                            }
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
    }
}