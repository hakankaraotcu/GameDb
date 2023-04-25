package com.hakankaraotcu.gamedb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private ArrayList<Games> games;
    private Context context;
    private OnItemClickListener listener;

    public SearchAdapter(ArrayList<Games> games, Context context) {
        this.games = games;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.search_item, parent, false);
        return new SearchAdapter.SearchViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchViewHolder holder, int position) {
        Games game = games.get(position);
        holder.setData(game);
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    class SearchViewHolder extends RecyclerView.ViewHolder{
        ImageView gameImage;
        TextView gameName;

        public SearchViewHolder(@NonNull View itemView){
            super(itemView);
            gameImage = itemView.findViewById(R.id.search_item_GameImageViewImage);
            gameName = itemView.findViewById(R.id.search_item_textViewGameName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if(listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(games.get(position), position);
                    }
                }
            });
        }

        public void setData(Games game){
            Glide.with(itemView.getContext()).load(game.getImage()).into(gameImage);
            this.gameName.setText(game.getName());
        }
    }

    public interface OnItemClickListener{
        void onItemClick(Games game, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
