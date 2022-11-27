package com.hakankaraotcu.gamedb;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.ms.square.android.expandabletextview.ExpandableTextView;

public class GameFragment extends Fragment {
    ExpandableTextView expTv;
    Button backButton;

    String text ="This isn't the Spider-Man you've known before, or seen in a movie. This is an experienced Peter Parker who is more masterful in fighting major crimes in New York City. At the same time he is struggling to balance his tumultuous personal life and career while the fate of nine million New Yorkers rests upon his shoulders.";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
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
}