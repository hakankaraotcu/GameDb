package com.hakankaraotcu.gamedb.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.hakankaraotcu.gamedb.General.AppGlobals;
import com.hakankaraotcu.gamedb.GamesFragment;
import com.hakankaraotcu.gamedb.ListsFragment;
import com.hakankaraotcu.gamedb.NewsFragment;
import com.hakankaraotcu.gamedb.ReviewsFragment;

public class ViewPagerFragmentAdapter extends FragmentStateAdapter {

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
                return new NewsFragment();
        }
        return new GamesFragment();
    }

    @Override
    public int getItemCount() {
        return AppGlobals.mainTitles.length;
    }
}
