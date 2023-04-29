package com.hakankaraotcu.gamedb;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.taufiqrahman.reviewratings.BarLabels;
import com.taufiqrahman.reviewratings.RatingReviews;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GameFragment extends Fragment {
    private ImageButton activityButton, playButton, likeButton, toPlayButton;
    private Button backButton;
    private RatingBar averageRatingBar;
    private TextView averageRatingText, trailerText, playDescription, likeDescription, addReview, addToLists;
    private TextView gameName, gameDeveloper, gameReleaseDate, gameMetacritic, gameGenres, gamePlayersCount, gameReviewsCount, gameListsCount;
    private ExpandableTextView gameContent;
    private ImageView gameImage;
    private CardView gamePlayersCardView, gameReviewsCardView, gameListsCardView;
    private ListView listView;
    private GameActivityAdapter adapter;
    private String[] titles = {"Review", "Add to lists"};
    private int[] images = {R.drawable.ic_add_circle, R.drawable.ic_addtolist};
    private String id;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore mFirestore;
    private CollectionReference playedGamesReference, likedGamesReference, toPlayGamesReference;
    private DocumentReference userReference, gameReference;
    private Games game;

    private Boolean toPlayCheck = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        RatingReviews ratingReviews = (RatingReviews) view.findViewById(R.id.ratingChart);
        averageRatingBar = (RatingBar) view.findViewById(R.id.averageRatingBar);
        averageRatingText = (TextView) view.findViewById(R.id.averageRatingText);
        trailerText = (TextView) view.findViewById(R.id.game_trailer);

        gameName = view.findViewById(R.id.game_name);
        gameDeveloper = view.findViewById(R.id.game_developer);
        gameReleaseDate = view.findViewById(R.id.game_releaseDate);
        gameMetacritic = view.findViewById(R.id.game_metacritic_description);
        gameGenres = view.findViewById(R.id.game_genres);
        gameImage = view.findViewById(R.id.game_image);
        gameContent = (ExpandableTextView) view.findViewById(R.id.expand_text_view);
        gamePlayersCount = view.findViewById(R.id.game_players_count);
        gameReviewsCount = view.findViewById(R.id.game_reviews_count);
        gameListsCount = view.findViewById(R.id.game_lists_count);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();

        id = getArguments().getString("id");

        fetchData();

        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int colors[] = new int[]{
                Color.parseColor("#0e9d58"),
                Color.parseColor("#bfd047"),
                Color.parseColor("#ffc105"),
                Color.parseColor("#ef7e14"),
                Color.parseColor("#d36259")};

        int raters[] = new int[]{
                new Random().nextInt(100),
                new Random().nextInt(100),
                new Random().nextInt(100),
                new Random().nextInt(100),
                new Random().nextInt(100)
        };

        double avg = Math.round(avgValue(raters) * 10) / 10.0;

        averageRatingText.setText(Double.toString(avg));
        averageRatingBar.setRating((float) avg);

        ratingReviews.createRatingBars(maxValue(raters), BarLabels.STYPE1, colors, raters);

        trailerText.setMovementMethod(LinkMovementMethod.getInstance());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        backButton = view.findViewById(R.id.game_backButton);
        activityButton = view.findViewById(R.id.game_activityButton);
        gamePlayersCardView = view.findViewById(R.id.game_players);
        gameReviewsCardView = view.findViewById(R.id.game_reviews);
        gameListsCardView = view.findViewById(R.id.game_lists);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

        gamePlayersCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GamePlayersFragment gamePlayersFragment = new GamePlayersFragment(id);
                getParentFragmentManager().beginTransaction().replace(R.id.user_popular_RelativeLayout, gamePlayersFragment, null).addToBackStack(null).commit();
            }
        });

        gameReviewsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameReviewsFragment gameReviewsFragment = new GameReviewsFragment(id);
                getParentFragmentManager().beginTransaction().replace(R.id.user_popular_RelativeLayout, gameReviewsFragment, null).addToBackStack(null).commit();
            }
        });

        gameListsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameListsFragment gameListsFragment = new GameListsFragment(id);
                getParentFragmentManager().beginTransaction().replace(R.id.user_popular_RelativeLayout, gameListsFragment, null).addToBackStack(null).commit();
            }
        });

        activityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.bottom_sheet, (LinearLayout) view.findViewById(R.id.bottomSheetContainer));
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();

                listView = bottomSheetView.findViewById(R.id.game_activity_listView);
                playButton = bottomSheetView.findViewById(R.id.play_button);
                likeButton = bottomSheetView.findViewById(R.id.like_button);
                toPlayButton = bottomSheetView.findViewById(R.id.toPlay_button);
                playDescription = bottomSheetView.findViewById(R.id.play_description);
                likeDescription = bottomSheetView.findViewById(R.id.like_description);

                playedGamesReference = mFirestore.collection("PlayedGames");
                playedGamesReference.whereEqualTo("gameID", id).whereEqualTo("userID", mUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.getDocuments().size() > 0){
                            playButton.setImageTintList(ColorStateList.valueOf(Color.argb(255, 21, 187, 50)));
                            playDescription.setText("Played");
                        }
                        else{
                            playButton.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                            playDescription.setText("Play");
                        }
                    }
                });

                likedGamesReference = mFirestore.collection("LikedGames");
                likedGamesReference.whereEqualTo("gameID", id).whereEqualTo("userID", mUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.getDocuments().size() > 0){
                            likeButton.setImageTintList(ColorStateList.valueOf(Color.argb(255, 255, 128, 0)));
                            likeDescription.setText("Liked");
                        }
                        else{
                            likeButton.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                            likeDescription.setText("Like");
                        }
                    }
                });

                toPlayGamesReference = mFirestore.collection("ToPlayGames");
                toPlayGamesReference.whereEqualTo("gameID", id).whereEqualTo("userID", mUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.getDocuments().size() > 0){
                            toPlayButton.setImageTintList(ColorStateList.valueOf(Color.argb(255, 64, 188, 244)));
                            toPlayCheck = true;
                        }
                        else{
                            toPlayButton.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                            toPlayCheck = false;
                        }
                    }
                });

                adapter = new GameActivityAdapter(titles, images, getContext());
                listView.setAdapter(adapter);

                playButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(playDescription.getText().toString().equals("Play")){
                            Map<String, Object> data = new HashMap<>();
                            data.put("gameID", id);
                            data.put("userID", mUser.getUid());
                            playedGamesReference.document(id + "_" + mUser.getUid()).set(data);

                            userReference = mFirestore.collection("Users").document(mUser.getUid());
                            userReference.update("playedCount", FieldValue.increment(1));

                            gameReference = mFirestore.collection("Games").document(id);
                            gameReference.update("numberOfPlayers", FieldValue.increment(1));

                            playButton.setImageTintList(ColorStateList.valueOf(Color.argb(255, 21, 187, 50)));
                            playDescription.setText("Played");
                        }
                        else if(playDescription.getText().toString().equals("Played")){
                            playedGamesReference.document(id + "_" + mUser.getUid()).delete();

                            userReference = mFirestore.collection("Users").document(mUser.getUid());
                            userReference.update("playedCount", FieldValue.increment(-1));

                            gameReference = mFirestore.collection("Games").document(id);
                            gameReference.update("numberOfPlayers", FieldValue.increment(-1));

                            playButton.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                            playDescription.setText("Play");
                        }
                    }
                });

                likeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(likeDescription.getText().toString().equals("Like")){
                            Map<String, Object> data = new HashMap<>();
                            data.put("gameID", id);
                            data.put("userID", mUser.getUid());
                            likedGamesReference.document(id + "_" + mUser.getUid()).set(data);

                            userReference = mFirestore.collection("Users").document(mUser.getUid());
                            userReference.update("likedCount", FieldValue.increment(1));

                            likeButton.setImageTintList(ColorStateList.valueOf(Color.argb(255, 255, 128, 0)));
                            likeDescription.setText("Liked");
                        }
                        else if(likeDescription.getText().toString().equals("Liked")){
                            likedGamesReference.document(id + "_" + mUser.getUid()).delete();

                            userReference = mFirestore.collection("Users").document(mUser.getUid());
                            userReference.update("likedCount", FieldValue.increment(-1));

                            likeButton.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                            likeDescription.setText("Like");
                        }
                    }
                });

                toPlayButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!toPlayCheck){
                            Map<String, Object> data = new HashMap<>();
                            data.put("gameID", id);
                            data.put("userID", mUser.getUid());

                            toPlayGamesReference.document(id + "_" + mUser.getUid()).set(data);

                            userReference = mFirestore.collection("Users").document(mUser.getUid());
                            userReference.update("toPlayCount", FieldValue.increment(1));
                            toPlayButton.setImageTintList(ColorStateList.valueOf(Color.argb(255, 64, 188, 244)));
                        }
                        else{
                            toPlayGamesReference.document(id + "_" + mUser.getUid()).delete();

                            userReference = mFirestore.collection("Users").document(mUser.getUid());
                            userReference.update("toPlayCount", FieldValue.increment(-1));
                            toPlayButton.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                        }
                        toPlayCheck = !toPlayCheck;
                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        switch (titles[i]) {
                            case "Review":
                                AddReviewFragment addReviewFragment = new AddReviewFragment(game);
                                getParentFragmentManager().beginTransaction().replace(R.id.user_popular_RelativeLayout, addReviewFragment, null).addToBackStack(null).commit();
                                bottomSheetDialog.hide();
                                break;
                            case "Add to lists":
                                AddtoListsFragment addtoListsFragment = new AddtoListsFragment(game.getId());
                                getParentFragmentManager().beginTransaction().replace(R.id.user_popular_RelativeLayout, addtoListsFragment, null).addToBackStack(null).commit();
                                bottomSheetDialog.hide();
                                break;
                        }
                    }
                });
            }
        });
    }

    private void fetchData(){
        DocumentReference documentReference = mFirestore.collection("Games").document(id);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    game = documentSnapshot.toObject(Games.class);

                    assert game != null;
                    game.setId(id);
                    gameName.setText(game.getName());
                    gameMetacritic.setText(String.valueOf(game.getMetacritic()));
                    gameReleaseDate.setText(game.getReleaseDate());
                    gameContent.setText(game.getContent());
                    gamePlayersCount.setText(String.valueOf(game.getNumberOfPlayers()));
                    gameReviewsCount.setText(String.valueOf(game.getNumberOfReviews()));
                    gameListsCount.setText(String.valueOf(game.getNumberOfLists()));
                    Glide.with(getView().getContext()).load(game.getImage()).into(gameImage);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Failed to fetch data", Toast.LENGTH_LONG).show();
            }
        });
    }

    private int maxValue(int[] raters){
        int min = raters[0];
        int max = raters[0];
        for(int i = 0; i < raters.length; i++){
            if(raters[i] < min){
                min = raters[i];
            }
            if(raters[i] > max){
                max = raters[i];
            }
        }
        return max;
    }

    private double avgValue(int[] raters){
        float avg;
        float sum = 0;
        for(int i = 0; i < raters.length; i++){
            sum += raters[i];
        }
        avg = ((raters[0] * 5) + (raters[1] * 4) + (raters[2] * 3) + (raters[3] * 2) + (raters[4] * 1)) / sum;
        return avg;
    }
}