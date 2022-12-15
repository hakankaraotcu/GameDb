package com.hakankaraotcu.gamedb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class UserPopularActivity extends AppCompatActivity {

    private ViewPager2 mViewPager2;
    private ViewPagerFragmentAdapter mViewPagerFragmentAdapter;
    private TabLayout mTablayout;

    private String[] titles = new String[] {"GAMES", "REVIEWS", "LISTS", "NEWS"};

    private DrawerLayout mDrawer;
    private NavigationView mNav;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mToggle;

    /*
    private SearchFragment searchFragment;
    */
    private ProfileFragment profileFragment;
    private UserToPlayListFragment usertoPlayListFragment;
    private UserListsFragment userListsFragment;
    /*
    private UserDiaryFragment userDiaryFragment;
    */
    private UserReviewsFragment userReviewsFragment;
    /*
    private ActivityFragment activityFragment;
    private SettingsFragment settingsFragment;
    */
    private void init(){
        mViewPager2 = findViewById(R.id.user_popular_viewPager2);
        mTablayout = findViewById(R.id.user_popular_tabLayout);

        mViewPagerFragmentAdapter = new ViewPagerFragmentAdapter(this);

        mViewPager2.setAdapter(mViewPagerFragmentAdapter);

        new TabLayoutMediator(mTablayout, mViewPager2,((tab, position) -> tab.setText(titles[position]))).attach();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_popular);
        init();

        mDrawer = (DrawerLayout) findViewById(R.id.user_popular_drawerLayout);
        mNav = (NavigationView) findViewById(R.id.user_popular_navigationView);
        mToolbar = (Toolbar) findViewById(R.id.user_popular_toolBar);

        /*
        searchFragment = new SearchFragment();
        */
        profileFragment = new ProfileFragment();
        usertoPlayListFragment = new UserToPlayListFragment();
        userListsFragment = new UserListsFragment();
        /*
        userDiaryFragment = new UserDiaryFragment();
        */
        userReviewsFragment = new UserReviewsFragment();
        /*
        activityFragment = new ActivityFragment();
        settingsFragment = new SettingsFragment();
        */

        setSupportActionBar(mToolbar);
        mNav.getMenu().findItem(R.id.nav_menu_popular).setChecked(true);
        mNav.setItemIconTintList(null);

        mToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.nav_open, R.string.nav_close);
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();

        mNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_menu_popular:
                        mDrawer.closeDrawer(GravityCompat.START);
                        return true;
                    /*
                    case R.id.nav_menu_search:
                        setFragment(searchFragment);
                        mDrawer.closeDrawer(GravityCompat.START);
                        return true;*/
                    case R.id.nav_menu_profile:
                        setFragment(profileFragment);
                        mDrawer.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_menu_toPlayList:
                        setFragment(usertoPlayListFragment);
                        mDrawer.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_menu_lists:
                        setFragment(userListsFragment);
                        mDrawer.closeDrawer(GravityCompat.START);
                        return true;
                    /*
                    case R.id.nav_menu_diary:
                        setFragment(userDiaryFragment);
                        mDrawer.closeDrawer(GravityCompat.START);
                        return true;
                    */
                    case R.id.nav_menu_reviews:
                        setFragment(userReviewsFragment);
                        mDrawer.closeDrawer(GravityCompat.START);
                        return true;
                    /*
                    case R.id.nav_menu_activity:
                        setFragment(activityFragment);
                        mDrawer.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_menu_settings:
                        setFragment(settingsFragment);
                        mDrawer.closeDrawer(GravityCompat.START);
                        return true;
                     */
                    default:
                        return false;
                }
            }
        });
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.user_popular_RelativeLayout, fragment, null);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}