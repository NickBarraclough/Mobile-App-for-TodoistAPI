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

    public void insertLikedSong(LikedSong song) {
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insert(song);
            }
        });
    }

    public void deleteLikedSong(LikedSong song) {
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.delete(song);
            }
        });
    }

    public LiveData<List<LikedSong>> getAllLikedSongs(){
        return this.dao.getAllSongs();
    }

    public LiveData<LikedSong> getLikedSongByName(String name) {
        return this.dao.getSongByName(name);
    }
}
