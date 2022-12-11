package com.hakankaraotcu.gamedb;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

public class UserGamesFragment extends Fragment {
    private GameFragment gameFragment;

    private GridView mGridView;
    private String[] games = {"","","","","","","","","","","","","","","","","","","",""};
    private int[] pictures = {R.drawable.spiderman, R.drawable.cuphead, R.drawable.monsterhunter, R.drawable.monkeyisland,
            R.drawable.batmanarkhamcity, R.drawable.bioshock, R.drawable.discoelysium, R.drawable.eldenring,
            R.drawable.gta4, R.drawable.halflife, R.drawable.halo, R.drawable.persona5,
            R.drawable.rdr, R.drawable.re4, R.drawable.darksiders2, R.drawable.metalgearsolid,
            R.drawable.skyrim, R.drawable.thelastofus, R.drawable.thephantompain, R.drawable.portal};
    private GameAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_games, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGridView = (GridView) view.findViewById(R.id.userGames_gridView);

        adapter = new GameAdapter(games, pictures, getActivity());
        mGridView.setAdapter(adapter);

        gameFragment = new GameFragment();

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                getParentFragmentManager().beginTransaction().replace(R.id.user_popular_drawerLayout, gameFragment).commit();
            }
        });
    }
}