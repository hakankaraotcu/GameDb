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

import com.hakankaraotcu.gamedb.Model.User;
import com.hakankaraotcu.gamedb.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GamePlayersAdapter extends ArrayAdapter<User> {
    private int[] ratings;
    private Context context;
    private CircularImageView avatar;
    private TextView username;
    private RatingBar ratingBar;
    private ArrayList<User> users;

    public GamePlayersAdapter(ArrayList<User> users, int[] ratings, Context context) {
        super(context, R.layout.game_players_item, users);
        this.users = users;
        this.ratings = ratings;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.game_players_item, null);

        if (view != null) {
            avatar = view.findViewById(R.id.game_players_item_avatar);
            username = view.findViewById(R.id.game_players_item_username);
            ratingBar = view.findViewById(R.id.game_players_item_rating);

            if(users.get(position).getAvatar().equals("default")){
                avatar.setImageResource(R.mipmap.ic_launcher);
            }
            else{
                Picasso.get().load(users.get(position).getAvatar()).resize(24,24).into(avatar);
            }
            username.setText(users.get(position).getUsername());
            ratingBar.setRating((float) ratings[position]);
        }
        return view;
    }
}
