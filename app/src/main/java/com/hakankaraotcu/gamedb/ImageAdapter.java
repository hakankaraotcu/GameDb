package com.hakankaraotcu.gamedb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private ArrayList<Games> games;
    private Context context;

    public ImageAdapter(ArrayList<Games> games, Context context) {
        this.games = games;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_game_item, parent, false);
        return new ImageAdapter.ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Games game = games.get(position);
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
            gameImage = itemView.findViewById(R.id.list_game_item_imageView);
        }

        public void setData(Games game){
            this.gameImage.setImageResource(game.getImage());
        }
    }
}
