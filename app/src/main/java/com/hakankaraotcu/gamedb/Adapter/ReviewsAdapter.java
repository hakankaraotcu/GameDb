package com.hakankaraotcu.gamedb.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.hakankaraotcu.gamedb.General.AppGlobals;
import com.hakankaraotcu.gamedb.Model.Review;
import com.hakankaraotcu.gamedb.Model.User;
import com.hakankaraotcu.gamedb.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsHolder> {
    private ArrayList<Review> reviews;
    private Context context;
    private OnItemClickListener listener;
    private DocumentReference documentReference;
    private User user;

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
        documentReference = AppGlobals.db.collection("Users").document(review.getUserID());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    user = documentSnapshot.toObject(User.class);

                    assert user != null;
                    holder.setData(review);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    class ReviewsHolder extends RecyclerView.ViewHolder {
        TextView gameName, gameReleaseDate, reviewContent, username;
        ImageView reviewGameImage;
        CircularImageView userImage;
        RatingBar ratingBar;

        public ReviewsHolder(@NonNull View itemView) {
            super(itemView);
            gameName = itemView.findViewById(R.id.reviews_item_gameName);
            gameReleaseDate = itemView.findViewById(R.id.reviews_item_gameReleaseDate);
            reviewContent = itemView.findViewById(R.id.reviews_item_content);
            ratingBar = itemView.findViewById(R.id.reviews_item_ratingBar);
            reviewGameImage = itemView.findViewById(R.id.reviews_item_gameImage);
            username = itemView.findViewById(R.id.reviews_item_username);
            userImage = itemView.findViewById(R.id.reviews_item_userImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(reviews.get(position), position);
                    }
                }
            });
        }

        public void setData(Review review) {
            this.gameName.setText(review.getGameName());
            this.gameReleaseDate.setText(review.getGameReleaseDate().substring(review.getGameReleaseDate().length() - 4));
            this.ratingBar.setRating(review.getGameRating());
            this.reviewContent.setText(review.getReviewContent());
            this.username.setText(user.getUsername());
            Glide.with(itemView.getContext()).load(review.getGameImage()).into(reviewGameImage);

            if (user.getAvatar().equals("default")) {
                userImage.setImageResource(R.mipmap.ic_launcher);
            } else {
                Picasso.get().load(user.getAvatar()).resize(24, 24).into(userImage);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Review review, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
