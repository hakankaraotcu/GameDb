package com.hakankaraotcu.gamedb;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hakankaraotcu.gamedb.Model.User;

import java.util.ArrayList;

public class GameReviewsFragment extends Fragment {
    private ListView listView;
    private GameReviewsAdapter adapter;
    private int[] images = {R.drawable.discoelysium, R.drawable.monsterhunter, R.drawable.halflife, R.drawable.cuphead, R.drawable.darksiders2};
    private String[] usernames = {"John", "Mike", "Kelly", "Ashe", "Jake"};
    private int[] ratings = {3, 2, 4, 5, 1};
    private String[] contents = {"Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book."};
    private ArrayList<Reviews> reviews;
    private FirebaseFirestore mFirestore;
    private DocumentReference userReference;
    private Query mQuery;
    private String gameId;

    public GameReviewsFragment(String gameId){
        this.gameId = gameId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_reviews, container, false);


        mFirestore = FirebaseFirestore.getInstance();

        reviews = new ArrayList<>();

        listView = view.findViewById(R.id.gameReviews_listView);

        mQuery = mFirestore.collection("Reviews");
        mQuery.whereEqualTo("gameID", gameId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                    Reviews review = documentSnapshot.toObject(Reviews.class);

                    assert review != null;
                    review.setGameID(documentSnapshot.getId());

                    reviews.add(review);
                }
                adapter = new GameReviewsAdapter(reviews, getContext());
                listView.setAdapter(adapter);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}