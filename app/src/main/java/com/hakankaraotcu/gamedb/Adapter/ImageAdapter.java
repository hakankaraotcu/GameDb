package com.hakankaraotcu.gamedb.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hakankaraotcu.gamedb.Model.Game;
import com.hakankaraotcu.gamedb.R;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private ArrayList<Game> games;
    private Context context;

    public ImageAdapter(ArrayList<Game> games, Context context) {
        this.games = games;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_game_item, parent, false);
        return new ImageAdapter.ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ImageViewHolder holder, int position) {
        Game game = games.get(position);
        holder.setData(game);
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder{
        ImageView gameImage;

        public ImageViewHolder(@NonNull View itemView){
            super(itemView);
            gameImage = itemView.findViewById(R.id.list_game_item_image);
        }

        public void setData(Game game){
            Glide.with(itemView.getContext()).load(game.getImage()).into(gameImage);
        }
    }
}
