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
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class UserListsFragment extends Fragment {
    private UserListAdapter userListAdapter;
    private RecyclerView recyclerView;
    private Button createButton;
    private ArrayList<Lists> lists;
    private HashMap<String, ArrayList<Games>> gamesInList;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore mFirestore;
    private Query mQuery, mQuery2, mQuery3;
    private CollectionReference gamesReferences;
    private DocumentReference gameReference;
    private int index = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_lists, container, false);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();

        lists = new ArrayList<>();
        gamesInList = new HashMap<>();

        recyclerView = view.findViewById(R.id.userLists_recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        mQuery = mFirestore.collection("Lists");
        mQuery.whereEqualTo("userID", mUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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
                            ArrayList<Games> games = new ArrayList<>();
                            ArrayList<Object> gamesIDs = new ArrayList<>();
                            if(!queryDocumentSnapshots.isEmpty()){
                                for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                                    gamesIDs.add(documentSnapshot.get("gameID"));
                                }
                                for(Object gameID : gamesIDs){
                                    gameReference = mFirestore.collection("Games").document(gameID.toString());
                                    gameReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            Games game = documentSnapshot.toObject(Games.class);

                                            assert game != null;
                                            game.setId(documentSnapshot.getId());
                                            games.add(game);
                                            gamesInList.put(list.getId(), games);
                                            if(lists.size() == gamesInList.size()){
                                                userListAdapter = new UserListAdapter(lists, gamesInList, getContext());
                                                recyclerView.setAdapter(userListAdapter);
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
                                userListAdapter = new UserListAdapter(lists, gamesInList, getContext());
                                recyclerView.setAdapter(userListAdapter);
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

        createButton = view.findViewById(R.id.user_lists_createButton);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateListFragment createListFragment = new CreateListFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.user_popular_RelativeLayout, createListFragment, "createListFragment").addToBackStack(null).commit();
            }
        });
    }

    public void setLists(ArrayList<Lists> lists){
        this.lists = lists;
    }

    public ArrayList<Lists> getLists(){
        return lists;
    }
}