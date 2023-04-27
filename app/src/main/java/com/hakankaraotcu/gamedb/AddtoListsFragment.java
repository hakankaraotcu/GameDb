package com.hakankaraotcu.gamedb;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AddtoListsFragment extends Fragment {
    private ListView listView;
    private AddToListsAdapter adapter;
    private Games game;
    private Button confirmBtn;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore mFirestore;
    private Query mQuery;
    private DocumentReference gameReference, gamesInListReference, listReference;
    private String listID = null;
    private int position;
    private ArrayList<Lists> lists;

    public AddtoListsFragment(Games game){
        this.game = game;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addto_lists, container, false);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();

        lists = new ArrayList<>();

        listView = view.findViewById(R.id.add_to_lists_listView);

        mQuery = mFirestore.collection("Lists");
        mQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    return;
                }
                if(value != null){
                    lists.clear();

                    for(DocumentSnapshot documentSnapshot : value.getDocuments()){
                        Lists list = documentSnapshot.toObject(Lists.class);

                        assert list != null;
                        if(list.getUser().getId().equals(mUser.getUid())){
                            list.setId(documentSnapshot.getId());
                            lists.add(list);
                        }
                    }
                    adapter = new AddToListsAdapter(lists, getContext());
                    listView.setAdapter(adapter);
                }
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        confirmBtn = view.findViewById(R.id.add_to_lists_confirm);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listID = lists.get(i).getId();
                position = i;
                gamesInListReference = mFirestore.collection("Lists").document(listID).collection("Games").document(game.getId());
                gameReference = mFirestore.collection("Games").document(game.getId());
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listID != null){
                    game.addNumberOfLists();
                    gamesInListReference.set(game);

                    gameReference.update("numberOfLists", game.getNumberOfLists());

                    lists.get(position).addNumberOfGames();
                    gameReference.collection("Lists").document(listID).set(lists.get(position));

                    listReference = mFirestore.collection("Lists").document(listID);
                    listReference.update("numberOfGames", lists.get(position).getNumberOfGames());
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });
    }
}