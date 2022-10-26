package com.hakankaraotcu.gamedb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class GameAdapter extends ArrayAdapter<String> {
    private String[] games;
    private int[] pictures;
    private Context context;
    private ImageView gamePicture;
    private TextView gameName;

    public GameAdapter(String[] games, int[] pictures, Context context){
        super(context, R.layout.game_item, games);
        this.games = games;
        this.pictures = pictures;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = LayoutInflater.from(context).inflate(R.layout.game_item, null);

        if(v != null){
            gamePicture = v.findViewById(R.id.game_item_imageView);
            gameName = v.findViewById(R.id.deneme);

            gameName.setText(games[position]);
            gamePicture.setBackgroundResource(pictures[position]);
        }
        return v;
    }
}
