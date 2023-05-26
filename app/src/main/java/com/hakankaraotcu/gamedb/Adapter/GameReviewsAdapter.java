package com.hakankaraotcu.gamedb.Adapter;

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
import com.hakankaraotcu.gamedb.Model.Review;
import com.hakankaraotcu.gamedb.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GameReviewsAdapter extends ArrayAdapter<Review> {
    private Context context;
    private CircularImageView avatar;
    private TextView username, content;
    private RatingBar ratingBar;
    private ArrayList<Review> reviews;

    public GameReviewsAdapter(ArrayList<Review> reviews, Context context) {
        super(context, R.layout.game_reviews_item, reviews);
        this.reviews = reviews;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.game_reviews_item, null);

        if (view != null) {
            avatar = view.findViewById(R.id.game_reviews_item_avatar);
            username = view.findViewById(R.id.game_reviews_item_username);
            content = view.findViewById(R.id.game_reviews_item_content);
            ratingBar = view.findViewById(R.id.game_reviews_ratingBar);

            if (reviews.get(position).getUserAvatar().equals("default")) {
                avatar.setImageResource(R.mipmap.ic_launcher);
            } else {
                Picasso.get().load(reviews.get(position).getUserAvatar()).resize(24, 24).into(avatar);
            }
            username.setText(reviews.get(position).getUsername());
            content.setText(reviews.get(position).getReviewContent());
            ratingBar.setRating(reviews.get(position).getGameRating());
        }
        return view;
    }
}