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

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class GameAdapter extends BaseAdapter {
    private String[] games;
    private int[] pictures;
    private Context context;
    private ImageView gamePicture;
    private List<Games> gamess;

    public GameAdapter(String[] games, int[] pictures, Context context){
        this.games = games;
        this.pictures = pictures;
        this.context = context;
    }

    public GameAdapter(List<Games> games, Context context){
        this.gamess = games;
        this.context = context;
    }

    @Override
    public int getCount() {
        return gamess.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.game_item, null);

        if(v != null){
            gamePicture = v.findViewById(R.id.game_item_imageView);
            Glide.with(v.getContext()).load(gamess.get(position).getImg()).into(gamePicture);
            //gamePicture.setBackgroundResource(pictures[position]);
        }
        return v;
    }
}
