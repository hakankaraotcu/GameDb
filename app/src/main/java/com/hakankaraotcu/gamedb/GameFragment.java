package com.hakankaraotcu.gamedb;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.taufiqrahman.reviewratings.BarLabels;
import com.taufiqrahman.reviewratings.RatingReviews;

import java.util.Random;

public class GameFragment extends Fragment {
    ExpandableTextView expTv;
    ImageButton activityButton, playButton, likeButton, toPlayButton;
    Button backButton;
    RatingBar averageRatingBar;
    TextView averageRatingText, trailerText, playDescription, likeDescription;

    private Boolean toPlayCheck = false;

    String text ="This isn't the Spider-Man you've known before, or seen in a movie. This is an experienced Peter Parker who is more masterful in fighting major crimes in New York City. At the same time he is struggling to balance his tumultuous personal life and career while the fate of nine million New Yorkers rests upon his shoulders.";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        RatingReviews ratingReviews = (RatingReviews) view.findViewById(R.id.ratingChart);
        averageRatingBar = (RatingBar) view.findViewById(R.id.averageRatingBar);
        averageRatingText = (TextView) view.findViewById(R.id.averageRatingText);
        trailerText = (TextView) view.findViewById(R.id.game_trailer);

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

        expTv = (ExpandableTextView) view.findViewById(R.id.expand_text_view);

        expTv.setText(text);

        backButton = view.findViewById(R.id.game_backButton);
        activityButton = view.findViewById(R.id.game_activityButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

        activityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.bottom_sheet, (LinearLayout) view.findViewById(R.id.bottomSheetContainer));
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
                playButton = bottomSheetView.findViewById(R.id.play_button);
                likeButton = bottomSheetView.findViewById(R.id.like_button);
                toPlayButton = bottomSheetView.findViewById(R.id.toPlay_button);
                playDescription = bottomSheetView.findViewById(R.id.play_description);
                likeDescription = bottomSheetView.findViewById(R.id.like_description);

                playButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(playDescription.getText().toString().equals("Play")){
                            playButton.setImageTintList(ColorStateList.valueOf(Color.argb(255, 21, 187, 50)));
                            playDescription.setText("Played");
                        }
                        else if(playDescription.getText().toString().equals("Played")){
                            playButton.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                            playDescription.setText("Play");
                        }
                    }
                });

                likeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(likeDescription.getText().toString().equals("Like")){
                            likeButton.setImageTintList(ColorStateList.valueOf(Color.argb(255, 255, 128, 0)));
                            likeDescription.setText("Liked");
                        }
                        else if(likeDescription.getText().toString().equals("Liked")){
                            likeButton.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                            likeDescription.setText("Like");
                        }
                    }
                });

                toPlayButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!toPlayCheck){
                            toPlayButton.setImageTintList(ColorStateList.valueOf(Color.argb(255, 64, 188, 244)));
                        }
                        else{
                            toPlayButton.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                        }
                        toPlayCheck = !toPlayCheck;
                    }
                });
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