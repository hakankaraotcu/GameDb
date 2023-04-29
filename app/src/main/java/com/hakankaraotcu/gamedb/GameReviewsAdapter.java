package com.hakankaraotcu.gamedb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.hakankaraotcu.gamedb.Model.User;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class GameReviewsAdapter extends ArrayAdapter<Reviews> {
    private Context context;
    private CircularImageView image;
    private TextView username, content;
    private RatingBar ratingBar;
    private ArrayList<Reviews> reviews;

    public GameReviewsAdapter(ArrayList<Reviews> reviews, Context context) {
        super(context, R.layout.game_reviews_item, reviews);
        this.reviews = reviews;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.game_reviews_item, null);

        if (view != null) {
            image = view.findViewById(R.id.game_reviews_item_image);
            username = view.findViewById(R.id.game_reviews_item_userName);
            content = view.findViewById(R.id.game_reviews_item_content);
            ratingBar = view.findViewById(R.id.game_reviews_RatingBar);

            Glide.with(view.getContext()).load(reviews.get(position).getGameImage()).into(image);
            username.setText(reviews.get(position).getUsername());
            content.setText(reviews.get(position).getReviewContent());
            ratingBar.setRating(reviews.get(position).getGameRating());
        }
        return view;
    }
}