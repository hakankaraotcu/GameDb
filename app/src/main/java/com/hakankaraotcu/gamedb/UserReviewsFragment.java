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
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hakankaraotcu.gamedb.Adapter.UserReviewAdapter;
import com.hakankaraotcu.gamedb.General.AppGlobals;
import com.hakankaraotcu.gamedb.Model.Review;
import com.hakankaraotcu.gamedb.Model.User;

import java.util.ArrayList;

public class UserReviewsFragment extends Fragment {
    private Query mQuery;
    private ArrayList<Review> reviews;
    private RecyclerView recyclerView;
    private UserReviewAdapter adapter;
    private User user;
    private String userID;
    private Button backButton;

    private TextView profile_username;

    public UserReviewsFragment(User user) {
        this.user = user;
    }

    public UserReviewsFragment(String userID) {
        this.userID = userID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_reviews, container, false);

        backButton = view.findViewById(R.id.user_reviews_backButton);
        profile_username = view.findViewById(R.id.user_reviews_username);

        reviews = new ArrayList<>();

        recyclerView = view.findViewById(R.id.user_reviews_recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        mQuery = AppGlobals.db.collection("Reviews");
        mQuery.whereEqualTo("userID", user.getId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                    Review review = documentSnapshot.toObject(Review.class);

                    assert review != null;
                    review.setId(documentSnapshot.getId());
                    reviews.add(review);
                }
                adapter = new UserReviewAdapter(reviews, getContext());
                recyclerView.setAdapter(adapter);

                adapter.setOnItemClickListener(new UserReviewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Review review, int position) {
                        ReviewFragment reviewFragment = new ReviewFragment(review);
                        getParentFragmentManager().beginTransaction().replace(R.id.user_main_RelativeLayout, reviewFragment, null).addToBackStack(null).commit();
                    }
                });
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });

        profile_username.setText(user.getUsername() + "'s Reviews");
    }
}