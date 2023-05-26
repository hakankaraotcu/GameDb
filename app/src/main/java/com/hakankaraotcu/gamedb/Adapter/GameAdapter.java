package com.hakankaraotcu.gamedb.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.hakankaraotcu.gamedb.Model.Game;
import com.hakankaraotcu.gamedb.R;

import java.util.List;

public class GameAdapter extends BaseAdapter {
    private Context context;
    private ImageView gamePicture;
    private List<Game> games;

    public GameAdapter(List<Game> games, Context context){
        this.games = games;
        this.context = context;
    }

    @Override
    public int getCount() {
        return games.size();
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
            gamePicture = v.findViewById(R.id.game_item_image);
            Glide.with(v.getContext()).load(games.get(position).getImage()).into(gamePicture);
        }
        return v;
    }
}
