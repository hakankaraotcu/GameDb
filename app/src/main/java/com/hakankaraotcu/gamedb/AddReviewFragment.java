package com.hakankaraotcu.gamedb;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hakankaraotcu.gamedb.Model.Game;
import com.hakankaraotcu.gamedb.Model.Review;
import com.hakankaraotcu.gamedb.Model.User;

public class AddReviewFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore mFirestore;
    private CollectionReference reviewsReference;
    private DocumentReference userReference, gameReference;
    private Game game;
    private TextView gameName, gameReleaseDate, reviewDate;
    private EditText reviewContent;
    private ImageView gameImage;
    private RatingBar gameRating;
    private Button confirmButton;
    private String txtReviewContent, txtReviewDate;

    public AddReviewFragment(Game game){
        this.game = game;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_review, container, false);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();

        gameName = view.findViewById(R.id.add_review_name);
        gameReleaseDate = view.findViewById(R.id.add_review_releaseDate);
        gameImage = view.findViewById(R.id.add_review_image);
        gameRating = view.findViewById(R.id.add_review_rating);
        reviewContent = view.findViewById(R.id.add_review_content);
        reviewDate = view.findViewById(R.id.add_review_date_description);
        confirmButton = view.findViewById(R.id.add_review_confirm);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gameName.setText(game.getName());
        gameReleaseDate.setText(game.getReleaseDate().substring(game.getReleaseDate().length() - 4));
        Glide.with(view.getContext()).load(game.getImage()).into(gameImage);
        gameRating.setRating(0);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createReview();
            }
        });

        gameRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ratingBar.setRating(v);
            }
        });
    }

    public void createReview(){
        txtReviewContent = reviewContent.getText().toString();
        txtReviewDate = reviewDate.getText().toString();

        if(txtReviewContent.isEmpty()){
            reviewContent.setError("List name is required");
            reviewContent.requestFocus();
            return;
        }
        if(txtReviewDate.isEmpty()){
            reviewDate.setError("List description is required");
            reviewDate.requestFocus();
            return;
        }

        userReference = mFirestore.collection("Users").document(mUser.getUid());
        userReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);

                assert user != null;
                user.setId(documentSnapshot.getId());

                Review review = new Review(txtReviewContent, txtReviewDate, game.getImage(), gameRating.getRating(), game.getId(), game.getName(), game.getReleaseDate(), user.getId(), user.getUsername());

                reviewsReference = mFirestore.collection("Reviews");
                reviewsReference.add(review).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){

                            gameReference = mFirestore.collection("Games").document(game.getId());
                            gameReference.update("numberOfReviews", FieldValue.increment(1));

                            userReference.update("reviewsCount", FieldValue.increment(1));

                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                    }
                });
            }
        });
    }
}