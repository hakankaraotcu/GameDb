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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hakankaraotcu.gamedb.Adapter.ListAdapter;
import com.hakankaraotcu.gamedb.General.AppGlobals;
import com.hakankaraotcu.gamedb.Model.Game;
import com.hakankaraotcu.gamedb.Model.List;

import java.util.ArrayList;
import java.util.HashMap;

public class ListsFragment extends Fragment {
    private ListAdapter listAdapter;
    private RecyclerView recyclerView;
    private Query mQuery, mQuery2, mQuery3;
    private ArrayList<List> lists;
    private HashMap<String, ArrayList<Game>> gamesInList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lists, container, false);

        recyclerView = view.findViewById(R.id.lists_recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        lists = new ArrayList<>();
        gamesInList = new HashMap<>();

        mQuery = AppGlobals.db.collection("Lists");
        mQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                lists.clear();
                gamesInList.clear();
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                    List list = documentSnapshot.toObject(List.class);

                    assert list != null;
                    list.setId(documentSnapshot.getId());
                    lists.add(list);
                }
                for (List list : lists) {
                    mQuery2 = AppGlobals.db.collection("GamesInLists");
                    mQuery2.whereEqualTo("listID", list.getId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            ArrayList<Object> gamesIDs = new ArrayList<>();
                            ArrayList<Game> games = new ArrayList<>();
                            if (!queryDocumentSnapshots.isEmpty()) {
                                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                    gamesIDs.add(documentSnapshot.get("gameID"));
                                }
                                for (Object gameID : gamesIDs) {
                                    mQuery3 = AppGlobals.db.collection("Games");
                                    mQuery3.whereEqualTo("id", gameID).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                                Game game = documentSnapshot.toObject(Game.class);

                                                assert game != null;
                                                game.setId(documentSnapshot.getId());
                                                games.add(game);
                                            }
                                            gamesInList.put(list.getId(), games);
                                            if (lists.size() == gamesInList.size()) {
                                                listAdapter = new ListAdapter(lists, gamesInList, getContext());
                                                recyclerView.setAdapter(listAdapter);
                                                recyclerView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

                                                listAdapter.setOnItemClickListener(new ListAdapter.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(List list, int position) {
                                                        ListFragment listFragment = new ListFragment(list, gamesInList.get(list.getId()));
                                                        // for guest
                                                        if (getActivity().getLocalClassName().equals("GuestMainActivity")) {
                                                            getParentFragmentManager().beginTransaction().replace(R.id.guest_main_RelativeLayout, listFragment, null).addToBackStack(null).commit();
                                                        }
                                                        // for user
                                                        if (getActivity().getLocalClassName().equals("UserMainActivity")) {
                                                            getParentFragmentManager().beginTransaction().replace(R.id.user_main_RelativeLayout, listFragment, null).addToBackStack(null).commit();
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            } else {
                                gamesInList.put(list.getId(), games);
                            }
                            if (lists.size() == gamesInList.size()) {
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