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
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hakankaraotcu.gamedb.Adapter.ListAdapter;
import com.hakankaraotcu.gamedb.Model.Game;
import com.hakankaraotcu.gamedb.Model.List;

import java.util.ArrayList;
import java.util.HashMap;

public class GameListsFragment extends Fragment {
    private ListAdapter listAdapter;
    private RecyclerView recyclerView;
    private ArrayList<List> lists;
    private ArrayList<Object> listsIDs;
    private HashMap<String, ArrayList<Game>> gamesInList = new HashMap<>();
    private FirebaseFirestore mFirestore;
    private DocumentReference listReference, gameReference;
    private Query mQuery;
    private Game selectedGame;

    private TextView gameName;

    public GameListsFragment(Game selectedGame){
        this.selectedGame = selectedGame;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_lists, container, false);

        mFirestore = FirebaseFirestore.getInstance();

        gameName = view.findViewById(R.id.game_lists_gameName);

        lists = new ArrayList<>();
        listsIDs = new ArrayList<>();

        recyclerView = view.findViewById(R.id.gameLists_recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        mQuery = mFirestore.collection("GamesInLists");
        mQuery.whereEqualTo("gameID", selectedGame.getId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                lists.clear();
                listsIDs.clear();
                for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                    listsIDs.add(documentSnapshot.get("listID"));
                }
                for(Object listID : listsIDs){
                    listReference = mFirestore.collection("Lists").document(listID.toString());
                    listReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            ArrayList<Game> games = new ArrayList<>();
                            ArrayList<Object> gameIDs = new ArrayList<>();
                            List list = documentSnapshot.toObject(List.class);

                            assert list != null;;
                            list.setId(listID.toString());
                            lists.add(list);

                            mQuery.whereEqualTo("listID", listID.toString()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for(DocumentSnapshot documentSnapshot1 : queryDocumentSnapshots.getDocuments()){
                                        gameIDs.add(documentSnapshot1.get("gameID"));
                                    }
                                    for(Object gameID : gameIDs){
                                        gameReference = mFirestore.collection("Games").document(gameID.toString());
                                        gameReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                Game game = documentSnapshot.toObject(Game.class);

                                                assert game != null;
                                                game.setId(gameID.toString());
                                                games.add(game);
                                                gamesInList.put(list.getId(), games);
                                                if(lists.size() == gamesInList.size()){
                                                    listAdapter = new ListAdapter(lists, gamesInList, getContext());
                                                    recyclerView.setAdapter(listAdapter);
                                                    recyclerView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

                                                    listAdapter.setOnItemClickListener(new ListAdapter.OnItemClickListener() {
                                                        @Override
                                                        public void onItemClick(List list, int position) {
                                                            ListFragment listFragment = new ListFragment(list, gamesInList.get(list.getId()));
                                                            // for guest
                                                            if(getActivity().getLocalClassName().equals("GuestMainActivity")){
                                                                getParentFragmentManager().beginTransaction().replace(R.id.guest_main_RelativeLayout, listFragment, null).addToBackStack(null).commit();
                                                            }
                                                            // for user
                                                            if(getActivity().getLocalClassName().equals("UserMainActivity")){
                                                                getParentFragmentManager().beginTransaction().replace(R.id.user_popular_RelativeLayout, listFragment, null).addToBackStack(null).commit();
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    }
                                }
                            });
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

        gameName.setText("Lists with " + selectedGame.getName());
    }
}