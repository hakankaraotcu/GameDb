package com.hakankaraotcu.gamedb;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hakankaraotcu.gamedb.Adapter.SearchAdapter;
import com.hakankaraotcu.gamedb.General.AppGlobals;
import com.hakankaraotcu.gamedb.Model.Game;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    private RecyclerView recyclerView;
    private SearchView searchView;
    private Query mQuery;
    private ArrayList<Game> games;
    private SearchAdapter searchAdapter;
    private GameFragment gameFragment;
    private Button backButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        backButton = view.findViewById(R.id.search_backButton);

        games = new ArrayList<>();

        gameFragment = new GameFragment();

        recyclerView = view.findViewById(R.id.search_recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        searchView = view.findViewById(R.id.search_searchView);

        mQuery = AppGlobals.db.collection("Games");

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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query.trim())) {
                    searchGames(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText.trim())) {
                    searchGames(newText);
                }
                return false;
            }
        });
    }

    private void searchGames(String query) {
        mQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }
                if (value != null) {
                    games.clear();
                }

                for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                    Game game = documentSnapshot.toObject(Game.class);

                    assert game != null;
                    if (game.getName().toLowerCase().contains(query.toLowerCase())) {
                        game.setId(documentSnapshot.getId());
                        games.add(game);
                    }

                    searchAdapter = new SearchAdapter(games, getContext());
                    searchAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(searchAdapter);
                    recyclerView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

                    searchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Game game, int position) {
                            Bundle args = new Bundle();
                            args.putString("id", game.getId());
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
        });
    }
}