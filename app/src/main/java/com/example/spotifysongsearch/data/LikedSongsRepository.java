package com.example.spotifysongsearch.data;


import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class LikedSongsRepository {
    private LikedSongsDAO dao;

    public LikedSongsRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        this.dao = db.likedSongsDAO();
    }

    public void insertLikedSong(LikedSongs song) {
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insert(song);
            }
        });
    }

    public LiveData<List<LikedSongs>> getAllLikedSongs(){
        return this.dao.getAllSongs();
    }
}
