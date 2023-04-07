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

public class GamePlayersAdapter extends ArrayAdapter<String> {
    private int[] images;
    private String[] usernameList;
    private int[] ratings;
    private Context context;
    private CircularImageView image;
    private TextView username;
    RatingBar ratingBar;

    public GamePlayersAdapter(String[] usernameList, int[] images, int[] ratings, Context context) {
        super(context, R.layout.game_players_item, usernameList);
        this.usernameList = usernameList;
        this.images = images;
        this.ratings = ratings;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.game_players_item, null);

        if (view != null) {
            image = view.findViewById(R.id.player_image);
            username = view.findViewById(R.id.player_username);
            ratingBar = view.findViewById(R.id.player_rating);

            image.setImageResource(images[position]);
            username.setText(usernameList[position]);
            ratingBar.setRating((float) ratings[position]);
        }
        return view;
    }
}
