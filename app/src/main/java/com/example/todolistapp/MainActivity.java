package com.example.todolistapp;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todolistapp.data.CompletedProject;
import com.example.todolistapp.data.LoadingStatus;
import com.example.todolistapp.data.ProjectData;
import com.example.todolistapp.data.Results;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity
        implements ResultsAdapter.OnProjectItemClickListener,
        SharedPreferences.OnSharedPreferenceChangeListener, NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String CLIENT_ID = BuildConfig.CLIENT_ID;

    private RecyclerView projectListRV;
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

        this.projectListRV = findViewById(R.id.rv_project_list);
        this.projectListRV.setLayoutManager(new LinearLayoutManager(this));
        this.projectListRV.setHasFixedSize(true);
        this.searchBoxET = findViewById(R.id.et_search_box);

        this.loadingIndicatorPB = findViewById(R.id.pb_loading_indicator);
        this.errorMessageTV = findViewById(R.id.tv_error_message);

        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.sharedPreferences.registerOnSharedPreferenceChangeListener(this);


        this.resultsAdapter = new ResultsAdapter(this);
        this.projectListRV.setAdapter(this.resultsAdapter);

        this.resultsViewModel = new ViewModelProvider(this).get(ResultsViewModel.class);

        this.drawerLayout = findViewById(R.id.drawer_layout);

        this.loadResults();

        NavigationView navigationView = findViewById(R.id.nv_nav_drawer);
        navigationView.setNavigationItemSelectedListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        this.resultsViewModel.getResults().observe(
                this,
                new Observer<Results>() {
                    @Override
                    public void onChanged(Results results) {
                        resultsAdapter.updateProjectsData(results);
                        if (results != null) {
                            ActionBar actionBar = getSupportActionBar();
                            actionBar.setTitle("Todoist Projects");
                        }
                    }
                }
        );

        this.resultsViewModel.getLoadingStatus().observe(
                this,
                new Observer<LoadingStatus>() {
                    @Override
                    public void onChanged(LoadingStatus loadingStatus) {
                        if (loadingStatus == LoadingStatus.LOADING) {
                            loadingIndicatorPB.setVisibility(View.VISIBLE);
                        } else if (loadingStatus == LoadingStatus.SUCCESS) {
                            loadingIndicatorPB.setVisibility(View.INVISIBLE);
                            projectListRV.setVisibility(View.VISIBLE);
                            errorMessageTV.setVisibility(View.INVISIBLE);
                        } else {
                            loadingIndicatorPB.setVisibility(View.INVISIBLE);
                            projectListRV.setVisibility(View.INVISIBLE);
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
                    String str_show = sharedPreferences.getString(
                            getString(R.string.pref_show_incomplete_key),
                            getString(R.string.pref_show_incomplete_default_value)
                    );
                    Boolean show = false;
                    if (str_show == "true") {
                        show = true;
                    }
                    resultsViewModel.loadResults(show);
                }
            }
        });
    }



    @Override
    public void onProjectItemClick(ProjectData projectData) {
        Intent intent = new Intent(this, ProjectDetailActivity.class);
        intent.putExtra(ProjectDetailActivity.EXTRA_PROJECT_DATA, projectData);
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
        if (TextUtils.equals(key, "pref_show_incomplete")) {
            Log.d(TAG, "Preference changed! key: " + key);
//            Genre newGenre =
//                    new Genre(sharedPreferences.getString(
//                            getString(R.string.pref_genre_key),
//                            "alternative")
//                    );
//            this.favoriteLocationsViewModel.insertFavoriteGenre(newGenre);
        }
    }

    /**
     * Triggers a new results to be fetched based on current preference values.
     */
    private void loadResults() {
//        String str_show = this.sharedPreferences.getString(
//                getString(R.string.pref_show_incomplete_key),
//                getString(R.string.pref_show_incomplete_default_value)
//        );
        Boolean show = false;
//        if (str_show == "true") {
//            show = true;
//        }
        this.resultsViewModel.loadResults(show);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        this.drawerLayout.closeDrawers();
        switch (item.getItemId()) {
            case R.id.nav_search:
                return true;
            case R.id.nav_completed_projects:
                Intent completedProjectsIntent = new Intent(this, CompletedProject.class);
                startActivity(completedProjectsIntent);
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
