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

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spotifysongsearch.data.LoadingStatus;
import com.example.spotifysongsearch.data.Results;
import com.example.spotifysongsearch.data.SongData;
import com.spotify.android.appremote.api.SpotifyAppRemote;

public class MainActivity extends AppCompatActivity
        implements ResultsAdapter.OnSongItemClickListener,
        SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String CLIENT_ID = BuildConfig.CLIENT_ID;
    private SpotifyAppRemote mSpotifyAppRemote;

    private ResultsAdapter resultsAdapter;
    private ResultsViewModel resultsViewModel;

    private DrawerLayout drawerLayout;

    private SharedPreferences sharedPreferences;

    private RecyclerView forecastListRV;
    private ProgressBar loadingIndicatorPB;
    private TextView errorMessageTV;
    private Toast errorToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.loadingIndicatorPB = findViewById(R.id.pb_loading_indicator);
        this.errorMessageTV = findViewById(R.id.tv_error_message);

        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        this.forecastListRV = findViewById(R.id.rv_songs_list);
        this.forecastListRV.setLayoutManager(new LinearLayoutManager(this));
        this.forecastListRV.setHasFixedSize(true);

        this.resultsAdapter = new ResultsAdapter(this);
        this.forecastListRV.setAdapter(this.resultsAdapter);

        this.resultsViewModel = new ViewModelProvider(this).get(ResultsViewModel.class);
        this.loadResults();

        this.drawerLayout = findViewById(R.id.drawer_layout);
//        this.navCityListRV = findViewById(R.id.rv_nav_drawer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

//        this.favoriteLocationsViewModel.getAllLikedSongs().observe(
//                this,
//                new Observer<List<LikedSongs>>() {
//                    @Override
//                    public void onChanged(List<LikedSongs> locationsList) {
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
    }



    @Override
    public void onSongItemClick(SongData songData) {
        Intent intent = new Intent(this, SongDetailActivity.class);
        intent.putExtra(SongDetailActivity.EXTRA_SONG_DATA, songData);
//        intent.putExtra(SongDetailActivity.EXTRA_SONG_CITY, this.forecastCity);
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

//    @Override
//    public void onFavoriteLocationItemClick(LikedSongs favoriteLocationData) {
//        Log.d(TAG, "onFavoriteLocationItemClick: Location name - " + favoriteLocationData.getName());
//        this.drawerLayout.closeDrawers();
//        SharedPreferences.Editor editor = this.sharedPreferences.edit();
//        editor.putString(getString(R.string.pref_artist_key), favoriteLocationData.getName());
//        editor.apply();
//        loadForecast();
//    }
}