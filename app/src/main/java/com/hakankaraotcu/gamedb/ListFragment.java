package com.hakankaraotcu.gamedb;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;

public class ListFragment extends Fragment {
    private GameFragment gameFragment;
    private CircularImageView list_userImage;
    private TextView list_username, list_name;
    private ExpandableTextView list_description;
    private Lists list;
    private GridView mGridView;
    private GameAdapter adapter;
    private ArrayList<Games> games;

    public ListFragment(Lists list, ArrayList<Games> games){
        this.list = list;
        this.games = games;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        mGridView = (GridView) view.findViewById(R.id.list_gridView);
        list_userImage = view.findViewById(R.id.list_user_image);
        list_username = view.findViewById(R.id.list_username);
        list_name = view.findViewById(R.id.list_name);
        list_description = view.findViewById(R.id.expand_text_view);

        adapter = new GameAdapter(games, getActivity());
        mGridView.setAdapter(adapter);
        mGridView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list_username.setText(list.getUsername());
        //Glide.with(getView().getContext()).load(list.getUserImage()).into(list_userImage);
        list_name.setText(list.getName());
        list_description.setText(list.getDescription());

        gameFragment = new GameFragment();

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //from main activity
                //getParentFragmentManager().beginTransaction().replace(R.id.main_activity_RelativeLayout, gameFragment, null).addToBackStack(null).commit();

                //from popular activity
                Bundle args = new Bundle();
                args.putString("id", games.get(i).getId());
                gameFragment.setArguments(args);
                getParentFragmentManager().beginTransaction().replace(R.id.user_popular_RelativeLayout, gameFragment, null).addToBackStack(null).commit();
            }
        });
    }
}