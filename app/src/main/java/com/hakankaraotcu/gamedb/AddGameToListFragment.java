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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AddGameToListFragment extends Fragment {

    private RecyclerView recyclerView;
    private SearchView searchView;
    private FirebaseFirestore mFirestore;
    private Query mQuery;
    private ArrayList<Games> games;
    private ArrayList<Games> allGames;
    private SearchAdapter searchAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_game_to_list, container, false);

        mFirestore = FirebaseFirestore.getInstance();

        games = new ArrayList<>();

        recyclerView = view.findViewById(R.id.add_game_search_recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        searchView = view.findViewById(R.id.add_game_searchView);

        mQuery = mFirestore.collection("Games");

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        mQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    return;
                }
                if(value != null){
                    games.clear();
                }

                for(DocumentSnapshot documentSnapshot : value.getDocuments()){
                    Games game = documentSnapshot.toObject(Games.class);

                    assert game != null;
                    if(game.getName().toLowerCase().contains(query.toLowerCase())){
                        game.setId(documentSnapshot.getId());
                        games.add(game);
                    }

                    searchAdapter = new SearchAdapter(games, getContext());
                    searchAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(searchAdapter);
                    recyclerView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

                    searchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Games game, int position) {
                            Fragment fragment = getParentFragmentManager().findFragmentByTag("createListFragment");
                            CreateListFragment createListFragment = (CreateListFragment) fragment;
                            boolean flag = true;
                            for(Games addedGame : createListFragment.getGames()){
                                if(addedGame.getName().equals(game.getName())){
                                    flag = false;
                                    break;
                                }
                            }
                            if(flag){
                                createListFragment.setGames(game);
                            }
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                    });
                }
            }
        });
    }
}