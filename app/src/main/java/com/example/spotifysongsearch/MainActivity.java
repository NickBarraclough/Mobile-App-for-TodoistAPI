package com.example.spotifysongsearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spotifysongsearch.data.LikedSong;
import com.example.spotifysongsearch.data.LoadingStatus;
import com.example.spotifysongsearch.data.Results;
import com.example.spotifysongsearch.data.SongData;
import com.google.android.material.navigation.NavigationView;
//import com.spotify.android.appremote.api.SpotifyAppRemote;

public class MainActivity extends AppCompatActivity
        implements ResultsAdapter.OnSongItemClickListener,
        SharedPreferences.OnSharedPreferenceChangeListener, NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String CLIENT_ID = BuildConfig.CLIENT_ID;
//    private SpotifyAppRemote mSpotifyAppRemote;

    private RecyclerView forecastListRV;
    private EditText searchBoxET;
    private ResultsAdapter resultsAdapter;
    private ResultsViewModel resultsViewModel;

    private DrawerLayout drawerLayout;

    private SharedPreferences sharedPreferences;

    private ProgressBar loadingIndicatorPB;
    private TextView errorMessageTV;
    private Toast errorToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.forecastListRV = findViewById(R.id.rv_songs_list);
        this.forecastListRV.setLayoutManager(new LinearLayoutManager(this));
        this.forecastListRV.setHasFixedSize(true);
        this.searchBoxET = findViewById(R.id.et_search_box);

        this.loadingIndicatorPB = findViewById(R.id.pb_loading_indicator);
        this.errorMessageTV = findViewById(R.id.tv_error_message);

        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.sharedPreferences.registerOnSharedPreferenceChangeListener(this);


        this.resultsAdapter = new ResultsAdapter(this);
        this.forecastListRV.setAdapter(this.resultsAdapter);

        this.resultsViewModel = new ViewModelProvider(this).get(ResultsViewModel.class);
        this.loadResults();

        this.drawerLayout = findViewById(R.id.drawer_layout);
//        this.navCityListRV = findViewById(R.id.rv_nav_drawer);

        NavigationView navigationView = findViewById(R.id.nv_nav_drawer);
        navigationView.setNavigationItemSelectedListener(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

//        this.favoriteLocationsViewModel.getAllLikedSongs().observe(
//                this,
//                new Observer<List<LikedSong>>() {
//                    @Override
//                    public void onChanged(List<LikedSong> locationsList) {
//                        favoriteLocationsAdapter.addLocation(locationsList);
//                    }
//                }
//        );

        /*
         * Update UI to reflect newly fetched forecast data.
         */
        this.resultsViewModel.getResults().observe(
                this,
                new Observer<Results>() {
                    @Override
                    public void onChanged(Results results) {
                        resultsAdapter.updateSongsData(results);
                        if (results != null) {
                            ActionBar actionBar = getSupportActionBar();
                            actionBar.setTitle("Spotify Search");
                        }
                    }
                }
        );

        /*
         * Update UI to reflect changes in loading status.
         */
        this.resultsViewModel.getLoadingStatus().observe(
                this,
                new Observer<LoadingStatus>() {
                    @Override
                    public void onChanged(LoadingStatus loadingStatus) {
                        if (loadingStatus == LoadingStatus.LOADING) {
                            loadingIndicatorPB.setVisibility(View.VISIBLE);
                        } else if (loadingStatus == LoadingStatus.SUCCESS) {
                            loadingIndicatorPB.setVisibility(View.INVISIBLE);
                            forecastListRV.setVisibility(View.VISIBLE);
                            errorMessageTV.setVisibility(View.INVISIBLE);
                        } else {
                            loadingIndicatorPB.setVisibility(View.INVISIBLE);
                            forecastListRV.setVisibility(View.INVISIBLE);
                            errorMessageTV.setVisibility(View.VISIBLE);
                            errorMessageTV.setText(getString(R.string.loading_error, "ヽ(。_°)ノ"));
                        }
                    }
                }
        );

        Button searchButton = (Button)findViewById(R.id.btn_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = searchBoxET.getText().toString();
                if (!TextUtils.isEmpty(searchQuery)) {
                    String artist = sharedPreferences.getString(
                            getString(R.string.pref_artist_key),
                            ""
                    );
                    String genre = sharedPreferences.getString(
                            getString(R.string.pref_genre_key),
                            getString(R.string.pref_genre_default_value)
                    );
                    resultsViewModel.loadResults(artist, genre, CLIENT_ID);
                }
            }
        });
    }



    @Override
    public void onSongItemClick(SongData songData) {
        Intent intent = new Intent(this, SongDetailActivity.class);
        intent.putExtra(SongDetailActivity.EXTRA_SONG_DATA, songData);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case android.R.id.home:
                this.drawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        this.loadResults();
//        if (TextUtils.equals(key, "pref_artist")) {
//            Log.d(TAG, "Preference changed! key: " + key);
//            Genre newGenre =
//                    new Genre(sharedPreferences.getString(
//                            getString(R.string.pref_genre_key),
//                            "alternative")
//                    );
//            this.favoriteLocationsViewModel.insertFavoriteGenre(newGenre);
//        }
    }

    /**
     * Triggers a new results to be fetched based on current preference values.
     */
    private void loadResults() {
        this.resultsViewModel.loadResults(
                this.sharedPreferences.getString(
                        getString(R.string.pref_artist_key),
                        ""
                ),
                this.sharedPreferences.getString(
                        getString(R.string.pref_genre_key),
                        getString(R.string.pref_genre_default_value)
                ),
                CLIENT_ID
        );
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        this.drawerLayout.closeDrawers();
        switch (item.getItemId()) {
            case R.id.nav_search:
                return true;
            case R.id.nav_liked_songs:
                Intent likedSongsIntent = new Intent(this, LikedSong.class);
                startActivity(likedSongsIntent);
                return true;
            case R.id.nav_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            default:
                return false;
        }
    }
}
