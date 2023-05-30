package com.hakankaraotcu.gamedb;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.hakankaraotcu.gamedb.General.AppGlobals;
import com.hakankaraotcu.gamedb.Model.Review;
import com.hakankaraotcu.gamedb.Model.User;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class ReviewFragment extends Fragment {
    private CircularImageView review_userImage;
    private ImageView review_gameImage;
    private TextView review_username, review_gameName, review_gameReleaseDate, review_date, review_content;
    private RatingBar review_gameRating;
    private Review review;
    private DocumentReference documentReference;
    private User user;
    private Button backButton;

    public ReviewFragment(Review review) {
        this.review = review;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review, container, false);

        backButton = view.findViewById(R.id.review_backButton);
        review_userImage = view.findViewById(R.id.review_userImage);
        review_username = view.findViewById(R.id.review_username);
        review_gameRating = view.findViewById(R.id.review_gameRating);
        review_gameImage = view.findViewById(R.id.review_gameImage);
        review_gameName = view.findViewById(R.id.review_gameName);
        review_gameReleaseDate = view.findViewById(R.id.review_gameReleaseDate);
        review_date = view.findViewById(R.id.review_dateDescription);
        review_content = view.findViewById(R.id.review_content);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });

        documentReference = AppGlobals.db.collection("Users").document(review.getUserID());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    user = documentSnapshot.toObject(User.class);

                    assert user != null;
                    review_username.setText(user.getUsername());
                    review_gameRating.setRating(review.getGameRating());
                    Glide.with(getView().getContext()).load(review.getGameImage()).into(review_gameImage);
                    review_gameName.setText(review.getGameName());
                    review_gameReleaseDate.setText(review.getGameReleaseDate());
                    review_date.setText(review.getReviewDate());
                    review_content.setText(review.getReviewContent());

                    if (user.getAvatar().equals("default")) {
                        review_userImage.setImageResource(R.mipmap.ic_launcher);
                    } else {
                        Picasso.get().load(user.getAvatar()).resize(24, 24).into(review_userImage);
                    }
                }
            }
        });

        review_gameImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameFragment gameFragment = new GameFragment();

                Bundle args = new Bundle();
                args.putString("id", review.getGameID());
                gameFragment.setArguments(args);
                // for guest
                if (getActivity().getLocalClassName().equals("GuestMainActivity")) {
                    getParentFragmentManager().beginTransaction().replace(R.id.guest_main_RelativeLayout, gameFragment, null).addToBackStack(null).commit();
                }
                // for user
                if (getActivity().getLocalClassName().equals("UserMainActivity")) {
                    getParentFragmentManager().beginTransaction().replace(R.id.user_main_RelativeLayout, gameFragment, null).addToBackStack(null).commit();
                }
            }
        });

        review_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileFragment profileFragment = new ProfileFragment(review.getUserID());
                // for guest
                if (getActivity().getLocalClassName().equals("GuestMainActivity")) {
                    getParentFragmentManager().beginTransaction().replace(R.id.guest_main_RelativeLayout, profileFragment, null).addToBackStack(null).commit();
                }
                // for user
                if (getActivity().getLocalClassName().equals("UserMainActivity")) {
                    getParentFragmentManager().beginTransaction().replace(R.id.user_main_RelativeLayout, profileFragment, null).addToBackStack(null).commit();
                }
            }
        });

        review_userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileFragment profileFragment = new ProfileFragment(review.getUserID());
                // for guest
                if (getActivity().getLocalClassName().equals("GuestMainActivity")) {
                    getParentFragmentManager().beginTransaction().replace(R.id.guest_main_RelativeLayout, profileFragment, null).addToBackStack(null).commit();
                }
                // for user
                if (getActivity().getLocalClassName().equals("UserMainActivity")) {
                    getParentFragmentManager().beginTransaction().replace(R.id.user_main_RelativeLayout, profileFragment, null).addToBackStack(null).commit();
                }
            }
        });
    }
}