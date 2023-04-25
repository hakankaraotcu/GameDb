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

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AddGameToListFragment extends Fragment {

    private RecyclerView recyclerView;
    private SearchView searchView;
    private FirebaseFirestore mFirestore;
    private CollectionReference gamesReference;
    private ArrayList<Games> games = new ArrayList<>();
    private ArrayList<Games> allGames;
    private SearchAdapter searchAdapter;

    public AddGameToListFragment(ArrayList<Games> allGames){
        this.allGames = allGames;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_game_to_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.add_game_search_recyclerView);
        searchView = view.findViewById(R.id.add_game_searchView);
        mFirestore = FirebaseFirestore.getInstance();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!TextUtils.isEmpty(query.trim())){
                    searchGames(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!TextUtils.isEmpty(newText.trim())){
                    searchGames(newText);
                }
                return false;
            }
        });
    }

    private void searchGames(String query) {
        games.clear();
        for(Games game : allGames){
            if(game.getName().toLowerCase().contains(query.toLowerCase())){
                games.add(game);
            }
            searchAdapter = new SearchAdapter(games, getContext());
            searchAdapter.notifyDataSetChanged();
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(searchAdapter);
            recyclerView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

            searchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Games game, int position) {
                    Fragment fragment = getParentFragmentManager().findFragmentByTag("createListFragment");
                    CreateListFragment createListFragment = (CreateListFragment) fragment;
                    createListFragment.setGames(game);
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });
        }
        /*
        gamesReference = mFirestore.collection("Games");

        gamesReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                games.clear();
                if(error != null){
                    return;
                }
                for(QueryDocumentSnapshot documentSnapshot : value){
                    System.out.println("working");
                    Games game = documentSnapshot.toObject(Games.class);
                    if(game.getName().toLowerCase().contains(query.toLowerCase())){
                        games.add(game);
                    }

                    searchAdapter = new SearchAdapter(games, getContext());
                    searchAdapter.notifyDataSetChanged();
                    recyclerView.setHasFixedSize(true);
                    LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(manager);
                    recyclerView.setAdapter(searchAdapter);
                    recyclerView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

                    searchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Games game, int position) {
                            Fragment fragment = getParentFragmentManager().findFragmentByTag("createListFragment");
                            CreateListFragment createListFragment = (CreateListFragment) fragment;
                            createListFragment.setGames(game);
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                    });
                }
            }
        });

         */
    }
}