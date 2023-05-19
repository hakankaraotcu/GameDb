package com.hakankaraotcu.gamedb;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

public class ReviewFragment extends Fragment {
    private CircularImageView review_userImage;
    private ImageView review_gameImage;
    private TextView review_username, review_gameName, review_gameReleaseDate, review_date, review_content;
    private RatingBar review_gameRating;
    private Reviews review;

    public ReviewFragment(Reviews review){
        this.review = review;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review, container, false);

        review_userImage = view.findViewById(R.id.review_user_image);
        review_username = view.findViewById(R.id.review_username);
        review_gameRating = view.findViewById(R.id.review_game_rating);
        review_gameImage = view.findViewById(R.id.review_game_image);
        review_gameName = view.findViewById(R.id.review_game_name);
        review_gameReleaseDate = view.findViewById(R.id.review_game_releaseDate);
        review_date = view.findViewById(R.id.review_date_description);
        review_content = view.findViewById(R.id.review_content);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        review_username.setText(review.getUsername());
        review_gameRating.setRating(review.getGameRating());
        Glide.with(getView().getContext()).load(review.getGameImage()).into(review_gameImage);
        review_gameName.setText(review.getGameName());
        review_gameReleaseDate.setText(review.getGameReleaseDate());
        review_date.setText(review.getReviewDate());
        review_content.setText(review.getReviewContent());
    }
}