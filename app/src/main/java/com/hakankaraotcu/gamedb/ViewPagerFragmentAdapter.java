package com.hakankaraotcu.gamedb;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerFragmentAdapter extends FragmentStateAdapter {

    private String[] titles = new String[] {"POPULAR", "REVIEWS", "NEWS", "JOURNAL"};

    public ViewPagerFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0:
                return new GamesFragment();
            case 1:
                return new ReviewsFragment();
            case 2:
                return new ListsFragment();
            case 3:
                return new JournalFragment();
        }
        return new GamesFragment();
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}
