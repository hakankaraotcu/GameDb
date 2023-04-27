package com.hakankaraotcu.gamedb;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class GameListsFragment extends Fragment {
    private ListAdapter listAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Lists> lists;
    private HashMap<String, ArrayList<Games>> gamesInList = new HashMap<>();
    private FirebaseFirestore mFirestore;
    private Query mQuery, mQuery2;
    private String id;

    public GameListsFragment(String id){
        this.id = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_lists, container, false);

        mFirestore = FirebaseFirestore.getInstance();

        lists = new ArrayList<>();

        recyclerView = view.findViewById(R.id.gameLists_recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        mQuery = mFirestore.collection("Games").document(id).collection("Lists");
        mQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    return;
                }
                if(value != null){
                    lists.clear();

                    for(DocumentSnapshot documentSnapshot : value.getDocuments()) {
                        Lists list = documentSnapshot.toObject(Lists.class);

                        assert list != null;
                        list.setId(documentSnapshot.getId());
                        lists.add(list);
                    }

                    for(Lists list : lists){
                        mQuery2 = mFirestore.collection("Lists").document(list.getId()).collection("Games");
                        mQuery2.addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if(error != null){
                                    return;
                                }
                                if(value != null){
                                    ArrayList<Games> games = new ArrayList<>();

                                    for(DocumentSnapshot documentSnapshot : value.getDocuments()){
                                        Games game = documentSnapshot.toObject(Games.class);

                                        assert game != null;
                                        game.setId((documentSnapshot.getId()));
                                        games.add(game);
                                    }
                                    gamesInList.put(list.getId(), games);
                                    if(lists.size() == gamesInList.size()){
                                        listAdapter = new ListAdapter(lists, gamesInList, getContext());
                                        recyclerView.setAdapter(listAdapter);
                                        recyclerView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
                                    }
                                }
                            }
                        });
                    }
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