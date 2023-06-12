package com.hakankaraotcu.gamedb.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hakankaraotcu.gamedb.Model.User;
import com.hakankaraotcu.gamedb.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FollowingFollowersAdapter extends ArrayAdapter<User> {
    private int[] images;
    private String[] usernameList;
    private Context context;
    private CircularImageView image;
    private TextView username;
    private ArrayList<User> users;

    public FollowingFollowersAdapter(ArrayList<User> users, Context context) {
        super(context, R.layout.following_followers, users);
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.following_followers, null);

        if (view != null) {
            image = view.findViewById(R.id.following_followers_image);
            username = view.findViewById(R.id.following_followers_username);

            if (users.get(position).getAvatar().equals("default")) {
                image.setImageResource(com.taufiqrahman.reviewratings.R.drawable.ic_person);
            } else {
                Picasso.get().load(users.get(position).getAvatar()).resize(24,24).into(image);
            }

            username.setText(users.get(position).getUsername());
        }
        return view;
    }
}
