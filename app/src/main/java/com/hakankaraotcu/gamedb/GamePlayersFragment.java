package com.hakankaraotcu.gamedb;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class GamePlayersFragment extends Fragment {
    private ListView listView;
    private GamePlayersAdapter adapter;
    private int[] images = {R.drawable.discoelysium, R.drawable.monsterhunter, R.drawable.halflife, R.drawable.cuphead, R.drawable.darksiders2};
    private String[] usernames = {"John", "Mike", "Kelly", "Ashe", "Jake"};
    private int[] ratings = {3, 2, 4, 5, 1};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_players, container, false);
        listView = view.findViewById(R.id.gamePlayers_listView);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new GamePlayersAdapter(usernames, images, ratings, getContext());
        listView.setAdapter(adapter);
    }
}