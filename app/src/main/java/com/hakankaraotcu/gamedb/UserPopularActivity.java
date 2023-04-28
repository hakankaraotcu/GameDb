package com.hakankaraotcu.gamedb;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hakankaraotcu.gamedb.Model.User;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class UserPopularActivity extends AppCompatActivity {

    private ViewPager2 mViewPager2;
    private ViewPagerFragmentAdapter mViewPagerFragmentAdapter;
    private TabLayout mTablayout;

    private final String[] titles = new String[] {"GAMES", "REVIEWS", "LISTS", "NEWS"};
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> releaseDates = new ArrayList<>();
    private ArrayList<String> contents = new ArrayList<>();
    private ArrayList<String> images = new ArrayList<>();
    private ArrayList<Integer> metacritics = new ArrayList<>();

    private DrawerLayout mDrawer;
    private NavigationView mNav;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mToggle;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore mFirestore;
    private Query mQuery;
    private ArrayList<Games> games;
    private Games game;

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
    */
    private SettingsFragment settingsFragment;

    private void init(){
        mViewPager2 = findViewById(R.id.user_popular_viewPager2);
        mTablayout = findViewById(R.id.user_popular_tabLayout);

        mViewPagerFragmentAdapter = new ViewPagerFragmentAdapter(this, games);

        mViewPager2.setAdapter(mViewPagerFragmentAdapter);

        new TabLayoutMediator(mTablayout, mViewPager2,((tab, position) -> tab.setText(titles[position]))).attach();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_popular);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();

        games = new ArrayList<>();

        //webscrape ile bilgileri metacritic sitesinden almak için alttaki iki kodu çalıştır.
        //Description_webscrape dw = new Description_webscrape();
        //dw.execute();

        mQuery = mFirestore.collection("Games");
        mQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                    Games game = documentSnapshot.toObject(Games.class);

                    assert game != null;
                    game.setId(documentSnapshot.getId());
                    games.add(game);
                }
                init();
            }
        });

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
        */
        settingsFragment = new SettingsFragment();

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
                        setFragment(profileFragment, "profileFragment");
                        mDrawer.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_menu_toPlayList:
                        setFragment(usertoPlayListFragment, "usertoPlayListFragment");
                        mDrawer.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_menu_lists:
                        setFragment(userListsFragment, "userListsFragment");
                        mDrawer.closeDrawer(GravityCompat.START);
                        return true;
                    /*
                    case R.id.nav_menu_diary:
                        setFragment(userDiaryFragment);
                        mDrawer.closeDrawer(GravityCompat.START);
                        return true;
                    */
                    case R.id.nav_menu_reviews:
                        setFragment(userReviewsFragment, "userReviewsFragment");
                        mDrawer.closeDrawer(GravityCompat.START);
                        return true;
                    /*
                    case R.id.nav_menu_activity:
                        setFragment(activityFragment);
                        mDrawer.closeDrawer(GravityCompat.START);
                        return true;
                     */
                    case R.id.nav_menu_settings:
                        setFragment(settingsFragment, "settingsFragment");
                        mDrawer.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_menu_signOut:
                        mAuth.signOut();
                        finish();
                        startActivity(new Intent(UserPopularActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void setFragment(Fragment fragment, String tag){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.user_popular_RelativeLayout, fragment, tag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private class Description_webscrape extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected void onCancelled(){
            super.onCancelled();
        }

        @Override
        protected Void doInBackground(Void... voids){
            try {
                Document document = Jsoup.connect("https://www.metacritic.com/browse/games/score/metascore/all/pc/filtered").get();
                Elements elementsSummary = document.select("td.clamp-summary-wrap");
                Elements elementsImage = document.select("td.clamp-image-wrap");

                for(Element element : elementsSummary){
                    String name = element.select("a.title").select("h3").text();
                    String releaseDate = element.select("div.clamp-details").select("span").eq(2).text();
                    String content = element.select("div.summary").text();
                    String metacritic = element.select("a.metascore_anchor").eq(0).text();
                    names.add(name);
                    releaseDates.add(releaseDate);
                    contents.add(content);
                    metacritics.add(Integer.parseInt(metacritic));
                }

                for(Element element : elementsImage){
                    String imageUrl = element.select("a").select("img").attr("src");
                    images.add(imageUrl);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid){
            for(int i = 0; i < 10; i++){
                game = new Games(names.get(i), releaseDates.get(i), contents.get(i), metacritics.get(i), images.get(i), 0, 0, 0);
                mFirestore.collection("Games").add(game);
            }
        }
    }
}