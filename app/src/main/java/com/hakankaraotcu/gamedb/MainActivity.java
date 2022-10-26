package com.hakankaraotcu.gamedb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawer;
    private NavigationView mNav;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mToggle;
    private GridView mGridView;
    private String[] games = {"","","","","","","","","","","","","","","","","","","",""};
    private int[] pictures = {R.drawable.spiderman, R.drawable.cuphead, R.drawable.monsterhunter, R.drawable.monkeyisland,
            R.drawable.batmanarkhamcity, R.drawable.bioshock, R.drawable.discoelysium, R.drawable.eldenring,
            R.drawable.gta4, R.drawable.halflife, R.drawable.halo, R.drawable.persona5,
            R.drawable.rdr, R.drawable.re4, R.drawable.darksiders2, R.drawable.metalgearsolid,
            R.drawable.skyrim, R.drawable.thelastofus, R.drawable.thephantompain, R.drawable.portal};
    private GameAdapter adapter;

    private SignInFragment signInFragment;
    private CreateAccountFragment createAccountFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawer = (DrawerLayout) findViewById(R.id.main_activity_drawerLayout);
        mNav = (NavigationView) findViewById(R.id.main_activity_navigationView);
        mToolbar = (Toolbar) findViewById(R.id.main_activity_toolBar);
        mGridView = (GridView) findViewById(R.id.main_activity_gridView);

        signInFragment = new SignInFragment();
        createAccountFragment = new CreateAccountFragment();

        adapter = new GameAdapter(games, pictures, this);
        mGridView.setAdapter(adapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

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
                    case R.id.nav_menu_search:
                        mDrawer.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_menu_signIn:
                        setFragment(signInFragment);
                        mDrawer.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_menu_createAccount:
                        setFragment(createAccountFragment);
                        mDrawer.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_menu_openTour:
                        mDrawer.closeDrawer(GravityCompat.START);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_activity_drawerLayout, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if(mDrawer.isDrawerOpen(GravityCompat.START)){
            mDrawer.closeDrawer(GravityCompat.START);
        }
    }
}