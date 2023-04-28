package com.hakankaraotcu.gamedb;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;

public class ListsFragment extends Fragment {
    private ListAdapter listAdapter;
    private RecyclerView recyclerView;
    private FirebaseFirestore mFirestore;
    private Query mQuery, mQuery2, mQuery3;
    private ArrayList<Lists> lists;
    private HashMap<String, ArrayList<Games>> gamesInList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lists, container, false);

        mFirestore = FirebaseFirestore.getInstance();

        recyclerView = view.findViewById(R.id.lists_recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        lists = new ArrayList<>();
        gamesInList = new HashMap<>();

        mQuery = mFirestore.collection("Lists");
        mQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                lists.clear();
                gamesInList.clear();
                for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                    Lists list = documentSnapshot.toObject(Lists.class);

                    assert list != null;
                    list.setId(documentSnapshot.getId());
                    lists.add(list);
                }
                for(Lists list : lists){
                    mQuery2 = mFirestore.collection("GamesInLists");
                    mQuery2.whereEqualTo("listID", list.getId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            ArrayList<Object> gamesIDs = new ArrayList<>();
                            ArrayList<Games> games = new ArrayList<>();
                            if(!queryDocumentSnapshots.isEmpty()){
                                for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                                    gamesIDs.add(documentSnapshot.get("gameID"));
                                }
                                for(Object gameID : gamesIDs){
                                    mQuery3 = mFirestore.collection("Games");
                                    mQuery3.whereEqualTo("id", gameID).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                                                Games game = documentSnapshot.toObject(Games.class);

                                                assert game != null;
                                                game.setId(documentSnapshot.getId());
                                                games.add(game);
                                            }
                                            gamesInList.put(list.getId(), games);
                                            if(lists.size() == gamesInList.size()){
                                                listAdapter = new ListAdapter(lists, gamesInList, getContext());
                                                recyclerView.setAdapter(listAdapter);
                                                recyclerView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
                                            }
                                        }
                                    });
                                }
                            }
                            else{
                                gamesInList.put(list.getId(), games);
                            }
                            if(lists.size() == gamesInList.size()){
                                listAdapter = new ListAdapter(lists, gamesInList, getContext());
                                recyclerView.setAdapter(listAdapter);
                                recyclerView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
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