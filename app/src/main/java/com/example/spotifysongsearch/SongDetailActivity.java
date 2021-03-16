package com.example.spotifysongsearch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.bumptech.glide.Glide;
import com.example.spotifysongsearch.data.SongData;

import java.util.Calendar;

public class SongDetailActivity extends AppCompatActivity {
    public static final String EXTRA_SONG_DATA = "SongDetailActivity.SongData";
//    public static final String EXTRA_FORECAST_CITY = "ForecastDetailActivity.ForecastCity";

    private SongData songData = null;
//    private ForecastCity forecastCity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_detail);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Intent intent = getIntent();

//        if (intent != null && intent.hasExtra(EXTRA_FORECAST_CITY)) {
//            this.forecastCity = (ForecastCity)intent.getSerializableExtra(EXTRA_FORECAST_CITY);
//            TextView forecastCityTV = findViewById(R.id.tv_forecast_city);
//            forecastCityTV.setText(this.forecastCity.getName());
//        }

        if (intent != null && intent.hasExtra(EXTRA_SONG_DATA)) {
            this.songData = (SongData)intent.getSerializableExtra(EXTRA_SONG_DATA);

//            /*
//             * Load forecast icon into ImageView using Glide: https://bumptech.github.io/glide/
//             */
//            ImageView forecastIconIV = findViewById(R.id.iv_forecast_icon);
//            Glide.with(this)
//                    .load(this.forecastData.getIconUrl())
//                    .into(forecastIconIV);

            TextView songNameTV = findViewById(R.id.tv_song_name);

            songNameTV.setText(getString(R.string.song_name));

            String genrePref = sharedPreferences.getString(
                    getString(R.string.pref_genre_key),
                    getString(R.string.pref_genre_default_value)
            );
//            TextView lowTempTV = findViewById(R.id.tv_low_temp);
//            lowTempTV.setText(getString(
//                    R.string.forecast_temp,
//                    forecastData.getLowTemp(),
//                    /* get correct temperature unit for unit preference setting */
//                    OpenWeatherUtils.getTemperatureDisplayForUnitsPref(unitsPref, this)
//            ));
//
//            TextView highTempTV = findViewById(R.id.tv_high_temp);
//            highTempTV.setText(getString(
//                    R.string.forecast_temp,
//                    forecastData.getHighTemp(),
//                    /* get correct temperature unit for unit preference setting */
//                    OpenWeatherUtils.getTemperatureDisplayForUnitsPref(unitsPref, this)
//            ));
//
//            TextView popTV = findViewById(R.id.tv_pop);
//            popTV.setText(getString(R.string.forecast_pop, forecastData.getPop()));
//
//            TextView cloudsTV = findViewById(R.id.tv_clouds);
//            cloudsTV.setText(getString(R.string.forecast_clouds, forecastData.getCloudCoverage()));
//
//            TextView windTV = findViewById(R.id.tv_wind);
//            windTV.setText(getString(
//                    R.string.forecast_wind,
//                    forecastData.getWindSpeed(),
//                    /* get correct wind speed unit for unit preference setting */
//                    OpenWeatherUtils.getWindSpeedDisplayForUnitsPref(unitsPref, this)
//            ));
//
//            ImageView windDirIV = findViewById(R.id.iv_wind_dir);
//            windDirIV.setRotation(forecastData.getWindDirDeg());
//
//            TextView forecastDescriptionTV = findViewById(R.id.tv_forecast_description);
//            forecastDescriptionTV.setText(forecastData.getShortDescription());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_song_detail, menu);
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
        if (this.songData != null) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String unitsPref = sharedPreferences.getString(
                    getString(R.string.pref_genre_key),
                    getString(R.string.pref_genre_default_value)
            );
            String shareText = getString(
                    R.string.share_song_text,
                    this.songData.getName(),
                    this.songData.getArtist()
//                    getString(R.string.app_name),
//                    this.forecastCity.getName(),
//                    getString(
//                            R.string.forecast_date_time,
//                            getString(R.string.forecast_date, date),
//                            getString(R.string.forecast_time, date)
//                    ),
//                    this.forecastData.getShortDescription(),
//                    getString(
//                            R.string.forecast_temp,
//                            forecastData.getHighTemp(),
//                            /* get correct temperature unit for unit preference setting */
//                            OpenWeatherUtils.getTemperatureDisplayForUnitsPref(unitsPref, this)
//                    ),
//                    getString(
//                            R.string.forecast_temp,
//                            forecastData.getLowTemp(),
//                            /* get correct temperature unit for unit preference setting */
//                            OpenWeatherUtils.getTemperatureDisplayForUnitsPref(unitsPref, this)
//                    ),
//                    getString(R.string.forecast_pop, this.forecastData.getPop())
            );

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            sendIntent.setType("text/plain");

            Intent chooserIntent = Intent.createChooser(sendIntent, null);
            startActivity(chooserIntent);
        }
    }
}
