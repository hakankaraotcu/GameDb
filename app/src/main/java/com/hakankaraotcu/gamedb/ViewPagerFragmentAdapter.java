package com.hakankaraotcu.gamedb;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class ViewPagerFragmentAdapter extends FragmentStateAdapter {

    private String[] titles = new String[] {"POPULAR", "REVIEWS", "LISTS", "NEWS"};
    private ArrayList<Games> games = new ArrayList<>();
    private ArrayList<Lists> lists = new ArrayList<>();

    public ViewPagerFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public ViewPagerFragmentAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<Games> games, ArrayList<Lists> lists) {
        super(fragmentActivity);
        this.games = games;
        this.lists = lists;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0:
                return new GamesFragment(games);
            case 1:
                return new ReviewsFragment();
            case 2:
                return new ListsFragment(lists);
            case 3:
                return new JournalFragment();
        }
        return new GamesFragment(games);
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}
