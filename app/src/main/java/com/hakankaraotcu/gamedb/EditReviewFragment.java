package com.hakankaraotcu.gamedb;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.google.firebase.firestore.DocumentReference;
import com.hakankaraotcu.gamedb.General.AppGlobals;
import com.hakankaraotcu.gamedb.Model.Review;

public class EditReviewFragment extends Fragment {
    private Review review;
    private DocumentReference reviewReference;
    private TextView gameName, gameReleaseDate, reviewDate;
    private EditText reviewContent;
    private ImageView gameImage;
    private RatingBar gameRating;
    private Button cancelButton, confirmButton;
    private String txtReviewContent, txtReviewDate;

    public EditReviewFragment(Review review) {
        this.review = review;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_review, container, false);

        gameName = view.findViewById(R.id.edit_review_gameName);
        gameReleaseDate = view.findViewById(R.id.edit_review_gameReleaseDate);
        gameImage = view.findViewById(R.id.edit_review_gameImage);
        gameRating = view.findViewById(R.id.edit_review_rating);
        reviewContent = view.findViewById(R.id.edit_review_content);
        reviewDate = view.findViewById(R.id.edit_review_dateDescription);
        cancelButton = view.findViewById(R.id.edit_review_cancel);
        confirmButton = view.findViewById(R.id.edit_review_confirm);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gameName.setText(review.getGameName());
        gameReleaseDate.setText(review.getGameReleaseDate().substring(review.getGameReleaseDate().length() - 4));
        Glide.with(view.getContext()).load(review.getGameImage()).into(gameImage);
        gameRating.setRating(review.getGameRating());
        reviewContent.setText(review.getReviewContent());
        reviewDate.setText(review.getReviewDate());

        reviewReference = AppGlobals.db.collection("Reviews").document(review.getId());

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Discard Changes");
                builder.setMessage("Are you sure? Changes will be lost.");
                builder.setNegativeButton("CANCEL", null);
                builder.setPositiveButton("DISCARD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getParentFragmentManager().popBackStack();
                    }
                });
                builder.show();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editReview();
            }
        });

        gameRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ratingBar.setRating(v);
            }
        });
    }

    private void editReview() {
        txtReviewContent = reviewContent.getText().toString();
        txtReviewDate = reviewDate.getText().toString();

        if (txtReviewContent.isEmpty()) {
            reviewContent.setError("Review content is required");
            reviewContent.requestFocus();
            return;
        }
        if (txtReviewDate.isEmpty()) {
            reviewDate.setError("Review date is required");
            reviewDate.requestFocus();
            return;
        }

        reviewReference.update("reviewContent", txtReviewContent, "reviewDate", txtReviewDate, "gameRating", gameRating.getRating());
        getParentFragmentManager().popBackStack();
    }
}