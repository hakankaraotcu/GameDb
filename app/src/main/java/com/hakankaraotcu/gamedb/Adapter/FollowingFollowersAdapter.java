package com.hakankaraotcu.gamedb.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hakankaraotcu.gamedb.R;
import com.mikhaellopez.circularimageview.CircularImageView;

public class FollowingFollowersAdapter extends ArrayAdapter<String> {
    private int[] images;
    private String[] usernameList;
    private Context context;
    private CircularImageView image;
    private TextView username;

    public FollowingFollowersAdapter(String[] usernameList, int[] images, Context context) {
        super(context, R.layout.following_followers, usernameList);
        this.usernameList = usernameList;
        this.images = images;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.following_followers, null);

        if (view != null) {
            image = view.findViewById(R.id.follower_image);
            username = view.findViewById(R.id.follower_username);

            image.setImageResource(images[position]);
            username.setText(usernameList[position]);
        }
        return view;
    }
}
