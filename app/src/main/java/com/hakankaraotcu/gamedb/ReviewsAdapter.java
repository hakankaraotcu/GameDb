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

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsHolder> {

    private ArrayList<Reviews> reviewsList;
    private Context context;

    public ReviewsAdapter(ArrayList<Reviews> reviewsList, Context context) {
        this.reviewsList = reviewsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ReviewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.reviews_item, parent, false);
        return new ReviewsHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsHolder holder, int position) {
        Reviews reviews = reviewsList.get(position);
        holder.setData(reviews);
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    class ReviewsHolder extends RecyclerView.ViewHolder{

        TextView gameName,gameDate, reviewContent;
        ImageView reviewGameImage;

        public ReviewsHolder(@NonNull View itemView){
            super(itemView);
            gameName = (TextView) itemView.findViewById(R.id.reviews_item_textViewGameName);
            gameDate = (TextView) itemView.findViewById(R.id.reviews_item_textViewGameDate);
            reviewContent = (TextView) itemView.findViewById(R.id.reviews_item_textViewContent);
            reviewGameImage = (ImageView) itemView.findViewById(R.id.reviews_item_GameImageViewImage);
        }

        public void setData(Reviews reviews){
            this.gameName.setText(reviews.getGameName());
            this.gameDate.setText(reviews.getGameDate());
            this.reviewContent.setText(reviews.getReviewContent());
            this.reviewGameImage.setBackgroundResource(reviews.getReviewGameImage());
        }
    }
}
