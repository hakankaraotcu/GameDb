package com.hakankaraotcu.gamedb;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hakankaraotcu.gamedb.Adapter.GamePlayersAdapter;
import com.hakankaraotcu.gamedb.General.AppGlobals;
import com.hakankaraotcu.gamedb.Model.Game;
import com.hakankaraotcu.gamedb.Model.User;

import java.util.ArrayList;

public class GamePlayersFragment extends Fragment {
    private ListView listView;
    private GamePlayersAdapter adapter;
    private int[] ratings = {3, 2, 4, 5, 1};
    private ArrayList<User> users;
    private ArrayList<Object> usersIDs;
    private DocumentReference userReference;
    private Query mQuery;
    private Game selectedGame;
    private TextView gameName;

    public GamePlayersFragment(Game selectedGame) {
        this.selectedGame = selectedGame;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_players, container, false);

        gameName = view.findViewById(R.id.game_players_gameName);

        users = new ArrayList<>();
        usersIDs = new ArrayList<>();

        listView = view.findViewById(R.id.game_players_listView);

        mQuery = AppGlobals.db.collection("PlayedGames");
        mQuery.whereEqualTo("gameID", selectedGame.getId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                    usersIDs.add(documentSnapshot.get("userID"));
                }
                for (Object userID : usersIDs) {
                    userReference = AppGlobals.db.collection("Users").document(userID.toString());
                    userReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            User user = documentSnapshot.toObject(User.class);

                            assert user != null;
                            user.setId(user.getId());
                            users.add(user);

                            if (usersIDs.size() == users.size()) {
                                adapter = new GamePlayersAdapter(users, ratings, getContext());
                                listView.setAdapter(adapter);

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        ProfileFragment profileFragment = new ProfileFragment(user.getId());
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

        gameName.setText("Players of " + selectedGame.getName());
    }
}