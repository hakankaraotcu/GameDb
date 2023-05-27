package com.hakankaraotcu.gamedb.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

public class GameReviewsAdapter extends ArrayAdapter<Review> {
    private Context context;
    private CircularImageView avatar;
    private TextView username, content;
    private RatingBar ratingBar;
    private ArrayList<Review> reviews;
    private DocumentReference documentReference;
    private User user;

    public GameReviewsAdapter(ArrayList<Review> reviews, Context context) {
        super(context, R.layout.game_reviews_item, reviews);
        this.reviews = reviews;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.game_reviews_item, null);

        if (view != null) {
            avatar = view.findViewById(R.id.game_reviews_item_avatar);
            username = view.findViewById(R.id.game_reviews_item_username);
            content = view.findViewById(R.id.game_reviews_item_content);
            ratingBar = view.findViewById(R.id.game_reviews_ratingBar);

            documentReference = AppGlobals.db.collection("Users").document(reviews.get(position).getUserID());
            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        user = documentSnapshot.toObject(User.class);

                        assert user != null;
                        if (user.getAvatar().equals("default")) {
                            avatar.setImageResource(R.mipmap.ic_launcher);
                        } else {
                            Picasso.get().load(user.getAvatar()).resize(24, 24).into(avatar);
                        }
                        username.setText(user.getUsername());
                        content.setText(reviews.get(position).getReviewContent());
                        ratingBar.setRating(reviews.get(position).getGameRating());
                    }
                }
            });
        }
        return view;
    }
}