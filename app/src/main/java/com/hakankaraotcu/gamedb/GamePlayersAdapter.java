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

import com.hakankaraotcu.gamedb.Model.User;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class GamePlayersAdapter extends ArrayAdapter<User> {
    private int[] images;
    private int[] ratings;
    private Context context;
    private CircularImageView image;
    private TextView username;
    private RatingBar ratingBar;
    private ArrayList<User> users;

    public GamePlayersAdapter(ArrayList<User> users, int[] images, int[] ratings, Context context) {
        super(context, R.layout.game_players_item, users);
        this.users = users;
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
            username.setText(users.get(position).getUsername());
            ratingBar.setRating((float) ratings[position]);
        }
        return view;
    }
}
