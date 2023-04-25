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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class UserListsFragment extends Fragment {
    private UserListAdapter userListAdapter;
    private RecyclerView recyclerView;
    private Button createButton;
    private ArrayList<Lists> lists;
    private ArrayList<Games> games;
    private FirebaseFirestore mFirestore;
    private CollectionReference gamesReferences;
    private HashMap<String, ArrayList<Games>> gamesInList = new HashMap<>();
    private int index = 0;

    public UserListsFragment(ArrayList<Lists> lists, ArrayList<Games> games) {
        this.lists = lists;
        this.games = games;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_lists, container, false);
        recyclerView = view.findViewById(R.id.userLists_recyclerView);
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
                        userListAdapter = new UserListAdapter(lists, gamesInList, getContext());

                        recyclerView.setHasFixedSize(true);
                        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(manager);
                        recyclerView.setAdapter(userListAdapter);
                        recyclerView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
                        index = 0;
                    }
                }
            });
        }

        createButton = view.findViewById(R.id.user_lists_createButton);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateListFragment createListFragment = new CreateListFragment(lists, games);
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

    public void setIndex(int count){
        index++;
    }
}