package com.example.todolistapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.todolistapp.data.ProjectData;

public class ProjectDetailActivity extends AppCompatActivity {
    public static final String EXTRA_PROJECT_DATA = "ProjectDetailActivity.ProjectData";

    private ProjectData projectData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(EXTRA_PROJECT_DATA)) {
            this.projectData = (ProjectData) intent.getSerializableExtra(EXTRA_PROJECT_DATA);

            TextView projectNameTV = findViewById(R.id.tv_project_name);

            projectNameTV.setText(getString(R.string.project_name));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_project_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                shareForecastText();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This method uses an implicit intent to launch the Android Sharesheet to allow the user to
     * share the current forecast.
     */
    private void shareForecastText() {
        if (this.projectData != null) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

            String shareText = getString(
                    R.string.share_project_text,
                    this.projectData.getName()
            );

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            sendIntent.setType("text/plain");

            Intent chooserIntent = Intent.createChooser(sendIntent, null);
            startActivity(chooserIntent);
        }
    }
}
