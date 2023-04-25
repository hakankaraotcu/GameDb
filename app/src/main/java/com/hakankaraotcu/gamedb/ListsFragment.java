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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class ListsFragment extends Fragment {
    private ListAdapter listAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Lists> lists;
    private FirebaseFirestore mFirestore;
    private CollectionReference gamesReferences;
    private HashMap<String, ArrayList<Games>> gamesInList = new HashMap<>();
    private int index = 0;

    public ListsFragment(ArrayList<Lists> lists){
        this.lists = lists;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lists, container, false);
        recyclerView = view.findViewById(R.id.lists_recyclerView);
        mFirestore = FirebaseFirestore.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        for(Lists list : lists){
            gamesReferences = mFirestore.collection("Lists").document(list.getId()).collection("Games");
            gamesReferences.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        ArrayList<Games> selectedGames = new ArrayList<>();
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            Games game = queryDocumentSnapshot.toObject(Games.class);
                            selectedGames.add(game);
                        }
                        gamesInList.put(list.getId(), selectedGames);
                    }
                    setIndex(1);

                    if(index == lists.size()){
                        listAdapter = new ListAdapter(lists, gamesInList, getContext());

                        recyclerView.setHasFixedSize(true);
                        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(manager);
                        recyclerView.setAdapter(listAdapter);
                        recyclerView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
                        index = 0;
                    }
                }
            });
        }
    }

    public void setIndex(int count){
        index++;
    }
}