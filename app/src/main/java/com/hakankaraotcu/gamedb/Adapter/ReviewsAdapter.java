package com.hakankaraotcu.gamedb.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hakankaraotcu.gamedb.Model.Review;
import com.hakankaraotcu.gamedb.R;

import java.util.ArrayList;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsHolder> {
    private ArrayList<Review> reviews;
    private Context context;
    private OnItemClickListener listener;

    public ReviewsAdapter(ArrayList<Review> reviews, Context context) {
        this.reviews = reviews;
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
        Review review = reviews.get(position);
        holder.setData(review);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    class ReviewsHolder extends RecyclerView.ViewHolder{
        TextView gameName, gameDate, reviewContent, username;
        ImageView reviewGameImage;

        public ReviewsHolder(@NonNull View itemView){
            super(itemView);
            gameName = (TextView) itemView.findViewById(R.id.reviews_item_textViewGameName);
            gameDate = (TextView) itemView.findViewById(R.id.reviews_item_textViewGameDate);
            reviewContent = (TextView) itemView.findViewById(R.id.reviews_item_textViewContent);
            reviewGameImage = (ImageView) itemView.findViewById(R.id.reviews_item_GameImageViewImage);
            username = (TextView) itemView.findViewById(R.id.reviews_item_textViewUsername);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if(listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(reviews.get(position), position);
                    }
                }
            });
        }

        public void setData(Review review){
            this.gameName.setText(review.getGameName());
            this.gameDate.setText(review.getGameReleaseDate().substring(review.getGameReleaseDate().length() - 4));
            this.reviewContent.setText(review.getReviewContent());
            this.username.setText(review.getUsername());
            Glide.with(itemView.getContext()).load(review.getGameImage()).into(reviewGameImage);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(Review review, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
