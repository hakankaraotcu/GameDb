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

import com.mikhaellopez.circularimageview.CircularImageView;

public class GameReviewsAdapter extends ArrayAdapter<String> {
    private int[] images;
    private String[] usernameList;
    private int[] ratings;
    private String[] contents;
    private Context context;
    private CircularImageView image;
    private TextView username, content;
    private RatingBar ratingBar;

    public GameReviewsAdapter(String[] usernameList, String[] contents, int[] images, int[] ratings, Context context) {
        super(context, R.layout.game_reviews_item, usernameList);
        this.usernameList = usernameList;
        this.contents = contents;
        this.images = images;
        this.ratings = ratings;
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

            image.setImageResource(images[position]);
            username.setText(usernameList[position]);
            content.setText(contents[position]);
            ratingBar.setRating((float) ratings[position]);
        }
        return view;
    }
}