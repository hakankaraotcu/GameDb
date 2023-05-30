package com.hakankaraotcu.gamedb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.navigation.NavigationView;

public class GuestDrawerBaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawer;
    private NavigationView mNav;

    @Override
    public void setContentView(View view) {
        mDrawer = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_guest_drawer_base, null);
        FrameLayout container = mDrawer.findViewById(R.id.activity_container);
        container.addView(view);
        super.setContentView(mDrawer);

        Toolbar mToolbar = mDrawer.findViewById(R.id.guest_main_activity_toolBar);
        setSupportActionBar(mToolbar);

        mNav = findViewById(R.id.guest_main_activity_navigationView);
        mNav.getMenu().findItem(R.id.nav_menu_popular).setChecked(true);
        mNav.setItemIconTintList(null);
        mNav.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle mToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.nav_open, R.string.nav_close);
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_menu_popular:
                if (!item.isChecked()) {
                    startActivity(new Intent(this, GuestMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
                mDrawer.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_menu_search:
                mDrawer.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_menu_signIn:
                if (!item.isChecked()) {
                    startActivity(new Intent(this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
                mDrawer.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_menu_createAccount:
                if (!item.isChecked()) {
                    startActivity(new Intent(this, RegisterActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
                mDrawer.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_menu_openTour:
                mDrawer.closeDrawer(GravityCompat.START);
                return true;
            default:
                return false;
        }
    }

    protected void setSelectedMenu(int title) {
        mNav.getMenu().findItem(title).setChecked(true);
    }
}