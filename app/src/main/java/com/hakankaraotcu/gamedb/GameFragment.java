package com.hakankaraotcu.gamedb;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.taufiqrahman.reviewratings.BarLabels;
import com.taufiqrahman.reviewratings.RatingReviews;

import java.util.Random;

public class GameFragment extends Fragment {
    ExpandableTextView expTv;
    Button backButton;
    RatingBar averageRatingBar;
    TextView averageRatingText;

    String text ="This isn't the Spider-Man you've known before, or seen in a movie. This is an experienced Peter Parker who is more masterful in fighting major crimes in New York City. At the same time he is struggling to balance his tumultuous personal life and career while the fate of nine million New Yorkers rests upon his shoulders.";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        RatingReviews ratingReviews = (RatingReviews) view.findViewById(R.id.ratingChart);
        averageRatingBar = (RatingBar) view.findViewById(R.id.averageRatingBar);
        averageRatingText = (TextView) view.findViewById(R.id.averageRatingText);

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
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        expTv = (ExpandableTextView) view.findViewById(R.id.expand_text_view);

        expTv.setText(text);

        backButton = view.findViewById(R.id.game_backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MainActivity.class));
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