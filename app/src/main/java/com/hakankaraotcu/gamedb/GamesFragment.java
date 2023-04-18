package com.hakankaraotcu.gamedb;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

public class GamesFragment extends Fragment {
    private GameFragment gameFragment;
    private GridView mGridView;
    private GameAdapter adapter;
    private ArrayList<Games> games;

    private String[] names = {"","","","","","","","","","","","","","","","","","","",""};
    private int[] pictures = {R.drawable.spiderman, R.drawable.cuphead, R.drawable.monsterhunter, R.drawable.monkeyisland,
            R.drawable.batmanarkhamcity, R.drawable.bioshock, R.drawable.discoelysium, R.drawable.eldenring,
            R.drawable.gta4, R.drawable.halflife, R.drawable.halo, R.drawable.persona5,
            R.drawable.rdr, R.drawable.re4, R.drawable.darksiders2, R.drawable.metalgearsolid,
            R.drawable.skyrim, R.drawable.thelastofus, R.drawable.thephantompain, R.drawable.portal};


    public GamesFragment(ArrayList<Games> games){
        this.games = games;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_popular, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGridView = (GridView) view.findViewById(R.id.popular_gridView);

        adapter = new GameAdapter(games, getActivity());
        mGridView.setAdapter(adapter);

        gameFragment = new GameFragment();

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //from main activity
                //getParentFragmentManager().beginTransaction().replace(R.id.main_activity_RelativeLayout, gameFragment, null).addToBackStack(null).commit();
                //from popular activity
                getParentFragmentManager().beginTransaction().replace(R.id.user_popular_RelativeLayout, gameFragment, null).addToBackStack(null).commit();
            }
        });
    }
}