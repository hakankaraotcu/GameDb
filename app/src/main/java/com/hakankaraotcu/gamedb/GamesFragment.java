package com.hakankaraotcu.gamedb;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hakankaraotcu.gamedb.Adapter.GameAdapter;
import com.hakankaraotcu.gamedb.General.AppGlobals;
import com.hakankaraotcu.gamedb.Model.Game;

import java.util.ArrayList;

public class GamesFragment extends Fragment {
    private GameFragment gameFragment;
    private GridView mGridView;
    private GameAdapter adapter;
    private Query mQuery;
    private ArrayList<Game> games;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_games, container, false);

        mGridView = view.findViewById(R.id.games_gridView);

        games = new ArrayList<>();

        mQuery = AppGlobals.db.collection("Games");
        mQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                    Game game = documentSnapshot.toObject(Game.class);

                    assert game != null;
                    game.setId(documentSnapshot.getId());
                    games.add(game);
                }
                adapter = new GameAdapter(games, getActivity());
                mGridView.setAdapter(adapter);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gameFragment = new GameFragment();

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle args = new Bundle();
                args.putString("id", games.get(i).getId());
                gameFragment.setArguments(args);
                // for guest
                if (getActivity().getLocalClassName().equals("GuestMainActivity")) {
                    getParentFragmentManager().beginTransaction().replace(R.id.guest_main_RelativeLayout, gameFragment, null).addToBackStack(null).commit();
                }
                // for user
                if (getActivity().getLocalClassName().equals("UserMainActivity")) {
                    getParentFragmentManager().beginTransaction().replace(R.id.user_main_RelativeLayout, gameFragment, null).addToBackStack(null).commit();
                }
            }
        });
    }
}