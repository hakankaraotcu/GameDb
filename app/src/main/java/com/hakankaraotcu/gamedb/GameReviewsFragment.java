package com.hakankaraotcu.gamedb;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hakankaraotcu.gamedb.Adapter.GameReviewsAdapter;
import com.hakankaraotcu.gamedb.General.AppGlobals;
import com.hakankaraotcu.gamedb.Model.Game;
import com.hakankaraotcu.gamedb.Model.Review;

import java.util.ArrayList;

public class GameReviewsFragment extends Fragment {
    private ListView listView;
    private GameReviewsAdapter adapter;
    private ArrayList<Review> reviews;
    private Query mQuery;
    private Game selectedGame;
    private TextView gameName;
    private Button backButton;

    public GameReviewsFragment(Game selectedGame) {
        this.selectedGame = selectedGame;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_reviews, container, false);

        gameName = view.findViewById(R.id.game_reviews_gameName);
        backButton = view.findViewById(R.id.game_reviews_backButton);

        reviews = new ArrayList<>();

        listView = view.findViewById(R.id.game_reviews_listView);

        mQuery = AppGlobals.db.collection("Reviews");
        mQuery.whereEqualTo("gameID", selectedGame.getId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                    Review review = documentSnapshot.toObject(Review.class);

                    assert review != null;
                    review.setId(documentSnapshot.getId());

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

        gameName.setText("Reviews of " + selectedGame.getName());

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ReviewFragment reviewFragment = new ReviewFragment(reviews.get(i));
                // for guest
                if (getActivity().getLocalClassName().equals("GuestMainActivity")) {
                    getParentFragmentManager().beginTransaction().replace(R.id.guest_main_RelativeLayout, reviewFragment, null).addToBackStack(null).commit();
                }
                // for user
                if (getActivity().getLocalClassName().equals("UserMainActivity")) {
                    getParentFragmentManager().beginTransaction().replace(R.id.user_main_RelativeLayout, reviewFragment, null).addToBackStack(null).commit();
                }
            }
        });
    }
}