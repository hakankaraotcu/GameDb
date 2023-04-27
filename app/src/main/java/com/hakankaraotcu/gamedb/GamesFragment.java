package com.hakankaraotcu.gamedb;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class GamesFragment extends Fragment {
    private GameFragment gameFragment;
    private GridView mGridView;
    private GameAdapter adapter;
    private FirebaseFirestore mFirestore;
    private Query mQuery;
    private ArrayList<Games> games;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_popular, container, false);

        mFirestore = FirebaseFirestore.getInstance();

        games = new ArrayList<>();

        mGridView = (GridView) view.findViewById(R.id.popular_gridView);

        mQuery = mFirestore.collection("Games");
        mQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                    Games game = documentSnapshot.toObject(Games.class);

                    assert game != null;
                    game.setId(documentSnapshot.getId());
                    games.add(game);
                }
                adapter = new GameAdapter(games, getActivity());
                mGridView.setAdapter(adapter);
                mGridView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gameFragment = new GameFragment();

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //from main activity
                //getParentFragmentManager().beginTransaction().replace(R.id.main_activity_RelativeLayout, gameFragment, null).addToBackStack(null).commit();

                //from popular activity
                Bundle args = new Bundle();
                args.putString("id", games.get(i).getId());
                gameFragment.setArguments(args);
                getParentFragmentManager().beginTransaction().replace(R.id.user_popular_RelativeLayout, gameFragment, null).addToBackStack(null).commit();
            }
        });
    }
}