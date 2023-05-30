package com.hakankaraotcu.gamedb;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.hakankaraotcu.gamedb.General.AppGlobals;
import com.hakankaraotcu.gamedb.Model.Game;
import com.hakankaraotcu.gamedb.Model.Review;

public class AddReviewFragment extends Fragment {
    private CollectionReference reviewsReference;
    private DocumentReference userReference, gameReference;
    private Game game;
    private TextView gameName, gameReleaseDate, reviewDate;
    private EditText reviewContent;
    private ImageView gameImage;
    private RatingBar gameRating;
    private Button cancelButton, confirmButton;
    private String txtReviewContent, txtReviewDate;

    public AddReviewFragment(Game game) {
        this.game = game;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_review, container, false);

        gameName = view.findViewById(R.id.add_review_gameName);
        gameReleaseDate = view.findViewById(R.id.add_review_gameReleaseDate);
        gameImage = view.findViewById(R.id.add_review_gameImage);
        gameRating = view.findViewById(R.id.add_review_rating);
        reviewContent = view.findViewById(R.id.add_review_content);
        reviewDate = view.findViewById(R.id.add_review_dateDescription);
        cancelButton = view.findViewById(R.id.add_review_cancel);
        confirmButton = view.findViewById(R.id.add_review_confirm);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gameName.setText(game.getName());
        gameReleaseDate.setText(game.getReleaseDate().substring(game.getReleaseDate().length() - 4));
        Glide.with(view.getContext()).load(game.getImage()).into(gameImage);
        gameRating.setRating(0);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createReview();
            }
        });

        gameRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ratingBar.setRating(v);
            }
        });
    }

    public void createReview() {
        txtReviewContent = reviewContent.getText().toString();
        txtReviewDate = reviewDate.getText().toString();

        if (txtReviewContent.isEmpty()) {
            reviewContent.setError("List name is required");
            reviewContent.requestFocus();
            return;
        }
        if (txtReviewDate.isEmpty()) {
            reviewDate.setError("List description is required");
            reviewDate.requestFocus();
            return;
        }

        Review review = new Review(txtReviewContent, txtReviewDate, game.getImage(), gameRating.getRating(), game.getId(), game.getName(), game.getReleaseDate(), AppGlobals.currentUser.getId());

        reviewsReference = AppGlobals.db.collection("Reviews");
        reviewsReference.add(review).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {

                    gameReference = AppGlobals.db.collection("Games").document(game.getId());
                    gameReference.update("numberOfReviews", FieldValue.increment(1));

                    userReference = AppGlobals.db.collection("Users").document(AppGlobals.currentUser.getId());
                    userReference.update("reviewsCount", FieldValue.increment(1));

                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });
    }
}