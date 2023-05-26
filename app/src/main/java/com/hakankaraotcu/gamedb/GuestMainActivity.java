package com.hakankaraotcu.gamedb;

import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.hakankaraotcu.gamedb.Adapter.ViewPagerFragmentAdapter;
import com.hakankaraotcu.gamedb.General.AppGlobals;
import com.hakankaraotcu.gamedb.databinding.ActivityGuestMainBinding;

public class GuestMainActivity extends GuestDrawerBaseActivity {
    private ActivityGuestMainBinding activityGuestMainBinding;
    private ViewPager2 mViewPager2;
    private ViewPagerFragmentAdapter mViewPagerFragmentAdapter;
    private TabLayout mTablayout;

    private void init() {
        mViewPager2 = findViewById(R.id.guest_main_activity_viewPager2);
        mTablayout = findViewById(R.id.guest_main_activity_tabLayout);

        mViewPagerFragmentAdapter = new ViewPagerFragmentAdapter(this);

        mViewPager2.setAdapter(mViewPagerFragmentAdapter);

        new TabLayoutMediator(mTablayout, mViewPager2, ((tab, position) -> tab.setText(AppGlobals.mainTitles[position]))).attach();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityGuestMainBinding = ActivityGuestMainBinding.inflate(getLayoutInflater());
        setContentView(activityGuestMainBinding.getRoot());

        init();

        if (AppGlobals.mUser != null) {
            finish();
            startActivity(new Intent(GuestMainActivity.this, UserMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setSelectedMenu(R.id.nav_menu_popular);
    }
}